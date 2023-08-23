package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.compilation.CompilationDto;
import ru.practicum.exploreWithMe.dto.compilation.NewCompilationDto;
import ru.practicum.exploreWithMe.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.Compilation;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.storage.CompilationRepository;
import ru.practicum.exploreWithMe.storage.EventRepository;
import ru.practicum.exploreWithMe.storage.ParticipationRequestRepository;
import ru.practicum.exploreWithMe.util.mapper.CompilationMapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final StatisticService statisticService;

    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
        for (Compilation compilation : compilations) {
            if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
                statisticService.setStatistic(new ArrayList<>(compilation.getEvents()));
                compilation.getEvents().forEach(
                        e -> e.setConfirmedRequests(participationRequestRepository.getConfirmedRequestsByEventId(e.getId())));
            }
        }
        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    public CompilationDto getCompilation(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with Id=" + compId + " was not found"));

        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            statisticService.setStatistic(new ArrayList<>(compilation.getEvents()));
            compilation.getEvents().forEach(o -> o.setConfirmedRequests(
                    participationRequestRepository.getConfirmedRequestsByEventId(o.getId())));
        }
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            if (events.isEmpty() || events.size() != newCompilationDto.getEvents().size()) {
                throw new NotFoundException("Event was not found");
            }
            compilation.setEvents(new HashSet<>(events));
        }
        Compilation savedCompilation = compilationRepository.save(compilation);
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            statisticService.setStatistic(new ArrayList<>(savedCompilation.getEvents()));
            savedCompilation.getEvents().forEach(
                    e -> e.setConfirmedRequests(participationRequestRepository.getConfirmedRequestsByEventId(e.getId())));
        }
        log.info("Compilation with Id=" + savedCompilation.getId() + " was added");
        return CompilationMapper.toCompilationDto(savedCompilation);
    }

    @Override
    public void removeCompilation(Integer compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with Id=" + compId + " was not found");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with Id=" + compId + " was not found"));

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null && !updateCompilationRequest.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(updateCompilationRequest.getEvents().stream()
                    .map(e -> eventRepository.findById(e).orElseThrow(
                            () -> new NotFoundException("Event with Id=" + e + "not found"))).collect(Collectors.toSet()));
        }

        Compilation updatedCompilation = compilationRepository.save(compilation);
        if (!updatedCompilation.getEvents().isEmpty() || updatedCompilation.getEvents() != null) {
            statisticService.setStatistic(new ArrayList<>(updatedCompilation.getEvents()));
            updatedCompilation.getEvents().forEach(
                    e -> e.setConfirmedRequests(participationRequestRepository.getConfirmedRequestsByEventId(e.getId())));
        }
        return CompilationMapper.toCompilationDto(updatedCompilation);
    }
}
