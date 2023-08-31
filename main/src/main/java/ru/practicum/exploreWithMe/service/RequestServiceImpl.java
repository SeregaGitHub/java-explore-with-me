package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.request.ParticipationRequestDto;
import ru.practicum.exploreWithMe.exception.ConflictException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.ParticipationRequest;
import ru.practicum.exploreWithMe.model.State;
import ru.practicum.exploreWithMe.model.Status;
import ru.practicum.exploreWithMe.storage.EventRepository;
import ru.practicum.exploreWithMe.storage.ParticipationRequestRepository;
import ru.practicum.exploreWithMe.storage.UserRepository;
import ru.practicum.exploreWithMe.util.mapper.ParticipationRequestMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<ParticipationRequestDto> getRequests(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with Id=" + userId + " was not found");
        }
        return participationRequestRepository.findByRequesterId(userId).stream()
                                                     .map(ParticipationRequestMapper::toParticipationRequestDto)
                                                     .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto addRequest(Integer userId, Integer eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with Id=" + userId + " was not found");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with Id=" + eventId + " was not found"));
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Initiator cant send request to his own event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event with Id=" + eventId + " was not found");
        }
        if (event.getParticipantLimit() > 0 &&
                participationRequestRepository.getConfirmedRequestsByEventId(eventId) >= event.getParticipantLimit()) {
            throw new ConflictException("Have not free places");
        }
        ParticipationRequest participationRequest = participationRequestRepository.save(
                ParticipationRequestMapper.toParticipationRequest(userRepository.findById(userId).orElseThrow(
                        () -> new NotFoundException("User with Id=" + userId + " was not found")), event));
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequest);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Integer userId, Integer requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with Id=" + userId + " was not found");
        }
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Request with Id=" + requestId + " was not found"));
        participationRequest.setStatus(Status.CANCELED);
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }
}
