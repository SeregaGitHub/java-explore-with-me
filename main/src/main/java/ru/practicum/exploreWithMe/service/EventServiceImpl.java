package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.comment.CommentDto;
import ru.practicum.exploreWithMe.dto.comment.NewCommentDto;
import ru.practicum.exploreWithMe.dto.comment.UpdateCommentDto;
import ru.practicum.exploreWithMe.dto.event.*;
import ru.practicum.exploreWithMe.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.exploreWithMe.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.exploreWithMe.dto.request.ParticipationRequestDto;
import ru.practicum.exploreWithMe.exception.BadRequestException;
import ru.practicum.exploreWithMe.exception.ConflictException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.*;
import ru.practicum.exploreWithMe.storage.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.exploreWithMe.util.EnumsUtil;
import ru.practicum.exploreWithMe.util.UriBuilder;
import ru.practicum.exploreWithMe.util.mapper.CommentMapper;
import ru.practicum.exploreWithMe.util.mapper.EventMapper;
import ru.practicum.exploreWithMe.util.mapper.ParticipationRequestMapper;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final StatisticService statisticService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ParticipationRequestDto> getRequestByUser(Integer userId, Integer eventId) {
        List<ParticipationRequest> list = participationRequestRepository.getRequestByUserIdAndEventId(userId, eventId);
        if (list == null) {
            throw new NotFoundException("Request not found");
        }
        return list.stream().map(ParticipationRequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getFullEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<State> statesResult;
        if (states != null) {
            statesResult = states.stream().map(EnumsUtil::makeState).collect(Collectors.toList());
        } else {
            statesResult = null;
        }
        List<Event> events = eventRepository.getEventsByUserId(users, statesResult, categories, rangeStart, rangeEnd, pageable);
        statisticService.setStatistic(events);
        events.forEach(o -> o.setConfirmedRequests(participationRequestRepository.getConfirmedRequestsByEventId(o.getId())));
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(EventForUpdateDto eventForUpdateDto, Integer eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with Id=" + eventId + " was not found"));

        if (eventForUpdateDto.getStateAction() != null && eventForUpdateDto.getStateAction().equals("PUBLISH_EVENT")) {
            if (event.getState() != State.PENDING) {
                throw new ConflictException("Unable to publish event with Id=" + eventId);
            }
            LocalDateTime published = LocalDateTime.now();
            event.setPublishedOn(published);
            event.setState(State.PUBLISHED);
        }

        if (eventForUpdateDto.getStateAction() != null && eventForUpdateDto.getStateAction().equals("REJECT_EVENT")) {
            if (event.getState() == State.PUBLISHED && event.getPublishedOn().isBefore(LocalDateTime.now())) {
                throw new ConflictException("Unable to publish event with Id=" + eventId);
            }
            event.setState(State.CANCELED);
        }

        if (eventForUpdateDto.getTitle() != null) {
            event.setTitle(eventForUpdateDto.getTitle());
        }
        if (eventForUpdateDto.getAnnotation() != null) {
            event.setAnnotation(eventForUpdateDto.getAnnotation());
        }
        if (eventForUpdateDto.getRequestModeration() != null) {
            event.setRequestModeration(eventForUpdateDto.getRequestModeration());
        }
        if (eventForUpdateDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventForUpdateDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with Id=" + eventForUpdateDto.getCategory() + " was not found")));
        }
        if (eventForUpdateDto.getDescription() != null) {
            event.setDescription(eventForUpdateDto.getDescription());
        }
        if (eventForUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventForUpdateDto.getParticipantLimit());
        }
        if (eventForUpdateDto.getEventDate() != null) {
            if (eventForUpdateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("The date is incorrect");
            }
            event.setEventDate(eventForUpdateDto.getEventDate());
        }
        if (eventForUpdateDto.getPaid() != null) {
            event.setPaid(eventForUpdateDto.getPaid());
        }
        event.setConfirmedRequests(participationRequestRepository.getConfirmedRequestsByEventId(event.getId()));
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public List<EventDto> getAllEvents(String text, List<Integer> categories, Boolean paid,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort,
                                       Integer from, Integer size, HttpServletRequest httpRequest) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd) || rangeEnd.isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Начало и окончание события должны быть в правильном хронологическом порядке.");
            }
        }
        List<Event> events;
        if (onlyAvailable) {
            events = eventRepository.getAvailableEvents(text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, PageRequest.of(from, size));
        } else {
            events = eventRepository.getAllEvents(text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, PageRequest.of(from, size));
        }
        statisticService.setStatistic(events);
        if (sort == null || !sort.equals("VIEWS")) {
            events.sort(Comparator.comparing(Event::getEventDate));
        } else {
            events.sort(Comparator.comparing(Event::getViews));
        }
        int resultFrom = from.equals(0) ? 0 : from - 1;
        events = events.stream().skip(resultFrom).limit(size).collect(Collectors.toList());
        statisticService.addHit("/events", httpRequest);
        for (Event event : events) {
            event.setConfirmedRequests(participationRequestRepository.getConfirmedRequestsByEventId(event.getId()));
        }
        return events.stream().map(EventMapper::toEventDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(Integer id, HttpServletRequest httpServletRequest) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Event with Id=" + id + " was not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Event with Id=" + id + " was not found");
        }
        statisticService.setStatistic(event);
        statisticService.addHit(UriBuilder.toUri(event), httpServletRequest);
        event.setConfirmedRequests(participationRequestRepository.getConfirmedRequestsByEventId(event.getId()));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventDto> getEventsByUser(Integer userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User was not found");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        statisticService.setStatistic(events);
        events.forEach(o -> o.setConfirmedRequests(participationRequestRepository.getConfirmedRequestsByEventId(o.getId())));
        return events.stream().map(EventMapper::toEventDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto addEvent(Integer userId, EventForAddDto eventForAddDto) {
        if (eventForAddDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("The date and time for which the event is scheduled cannot be earlier " +
                                            "than two hours from the current moment");
        }
        Event event = EventMapper.toEvent(eventForAddDto, categoryRepository.findById(eventForAddDto.getCategory()).get(),
                userRepository.findById(userId).get());
        locationRepository.save(event.getLocation());
        Event createdEvent = eventRepository.save(event);
        event.setViews(0);
        event.setConfirmedRequests(0);
        return EventMapper.toEventFullDto(createdEvent);
    }

    @Override
    public EventFullDto getFullEvent(Integer userId, Integer eventId) {
        if (!eventRepository.existsById(eventId) || !eventRepository.findById(eventId).get().getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with Id=" + eventId + " was not found");
        }
        Event event = eventRepository.findById(eventId).get();
        statisticService.setStatistic(event);
        event.setConfirmedRequests(participationRequestRepository.getConfirmedRequestsByEventId(event.getId()));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(Integer userId, Integer eventId, EventForUserUpdateDto eventForUserUpdateDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with Id=" + eventId + " was not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with Id=" + eventId + " was not found");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("You can not change event if his state is PUBLISHED");
        }
        if (eventForUserUpdateDto.getEventDate() != null) {
            if (eventForUserUpdateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("The date and time for which the event is scheduled cannot be earlier " +
                        "than two hours from the current moment");
            }
        }
        if (eventForUserUpdateDto.getStateAction() != null && eventForUserUpdateDto.getStateAction().equals("SEND_TO_REVIEW")) {
            event.setState(State.PENDING);
        }
        if (eventForUserUpdateDto.getStateAction() != null && eventForUserUpdateDto.getStateAction().equals("CANCEL_REVIEW")) {
            event.setState(State.CANCELED);
        }
        if (eventForUserUpdateDto.getTitle() != null) {
            event.setTitle(eventForUserUpdateDto.getTitle());
        }
        if (eventForUserUpdateDto.getAnnotation() != null) {
            event.setAnnotation(eventForUserUpdateDto.getAnnotation());
        }
        if (eventForUserUpdateDto.getRequestModeration() != null) {
            event.setRequestModeration(eventForUserUpdateDto.getRequestModeration());
        }
        if (eventForUserUpdateDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventForUserUpdateDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with Id=" + eventForUserUpdateDto.getCategory() + " was not found")));
        }
        if (eventForUserUpdateDto.getDescription() != null && !eventForUserUpdateDto.getDescription().isBlank()) {
            event.setDescription(eventForUserUpdateDto.getDescription());
        }
        if (eventForUserUpdateDto.getLocation() != null) {
            event.setLocation(eventForUserUpdateDto.getLocation());
        }
        if (eventForUserUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventForUserUpdateDto.getParticipantLimit());
        }
        if (eventForUserUpdateDto.getPaid() != null) {
            event.setPaid(eventForUserUpdateDto.getPaid());
        }
        eventRepository.save(event);
        event.setConfirmedRequests(participationRequestRepository.getConfirmedRequestsByEventId(event.getId()));

        return EventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestsByUser(Integer userId, Integer eventId,
                                                               EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        if (!eventRepository.existsById(eventId) || !eventRepository.findById(eventId).get().getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with Id=" + eventId + " was not found");
        }
        List<ParticipationRequest> prs = participationRequestRepository.getRequestsByRequestIds(eventRequestStatusUpdateRequest.getRequestIds());
        Event event = eventRepository.findById(eventId).get();
        int freePlaces = event.getParticipantLimit() - participationRequestRepository.getConfirmedRequestsByEventId(eventId);
        if (freePlaces <= 0) {
            throw new ConflictException("Have not free places");
        }
        for (ParticipationRequest pr : prs) {
            if (!pr.getStatus().equals(Status.PENDING)) {
                throw new ConflictException("Request must have status PENDING");
            }
        }

        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();

        if (eventRequestStatusUpdateRequest.getStatus().equals("CONFIRMED")) {
            for (int i = 0; i < prs.size(); i++) {
                if (i >= freePlaces) {
                    prs.get(i).setStatus(Status.REJECTED);
                    rejectedRequests.add(prs.get(i));
                } else {
                    prs.get(i).setStatus(Status.CONFIRMED);
                    confirmedRequests.add(prs.get(i));
                }
            }
            confirmedRequests.forEach(participationRequestRepository::save);
            rejectedRequests.forEach(participationRequestRepository::save);

            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(confirmedRequests.stream()
                            .map(ParticipationRequestMapper::toParticipationRequestDto)
                            .collect(Collectors.toList()))
                    .rejectedRequests(rejectedRequests.stream()
                            .map(ParticipationRequestMapper::toParticipationRequestDto)
                            .collect(Collectors.toList()))
                    .build();
        } else {
            prs.forEach(e -> e.setStatus(Status.REJECTED));
            prs.forEach(participationRequestRepository::save);
            rejectedRequests.addAll(prs);

            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(new ArrayList<>())
                    .rejectedRequests(rejectedRequests.stream()
                            .map(ParticipationRequestMapper::toParticipationRequestDto)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Override
    @Transactional
    public CommentDto addComment(Integer eventId, Integer userId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User was not found."));
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event was not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("You can not comment on an unpublished event");
        }
        Comment comment = commentRepository.save(CommentMapper.toComment(newCommentDto, user, event));
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public CommentDto updateCommentByOwner(UpdateCommentDto updateCommentDto, Integer userId, Integer commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment was not found"));
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new NotFoundException("Only author can update the comment");
        }
        comment.setText(updateCommentDto.getText());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void removeCommentByOwner(Integer commentId, Integer userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment with Id=" + commentId + " was not found"));
        if (Objects.equals(comment.getAuthor().getId(), userId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new BadRequestException("You can not delete this comment");
        }
    }

    @Override
    public CommentDto getComment(Integer commentId) {
        return CommentMapper.toCommentDto(commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment was not found")));
    }

    @Override
    @Transactional
    public List<CommentDto> getAllUserComments(Integer userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with Id=" + userId + " was not found"));
        return commentRepository.findAllByAuthor(user, PageRequest.of(from / size, size))
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeCommentByAdmin(Integer commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new NotFoundException("Comment with Id=" + commentId + " was not found");
        }
    }

    @Override
    @Transactional
    public List<CommentDto> getAllEventComments(Integer eventId, Integer from, Integer size) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with Id=" + eventId + " was not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event state is not - PUBLISHED");
        }
        return commentRepository.findAllByEvent(event, PageRequest.of(from / size, size))
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}