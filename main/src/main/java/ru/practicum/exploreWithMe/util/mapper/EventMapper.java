package ru.practicum.exploreWithMe.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.exploreWithMe.dto.event.EventDto;
import ru.practicum.exploreWithMe.dto.event.EventForAddDto;
import ru.practicum.exploreWithMe.dto.event.EventFullDto;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.State;
import ru.practicum.exploreWithMe.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {
    public Event toEvent(EventForAddDto eventForAddDto, Category category, User user) {
        return Event.builder()
                .annotation(eventForAddDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(eventForAddDto.getDescription())
                .eventDate(eventForAddDto.getEventDate())
                .initiator(user)
                .location(eventForAddDto.getLocation())
                .paid(eventForAddDto.isPaid())
                .participantLimit(eventForAddDto.getParticipantLimit())
                .requestModeration(eventForAddDto.isRequestModeration())
                .state(State.PENDING)
                .title(eventForAddDto.getTitle())
                .build();
    }

    public EventDto toEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .eventDate(event.getEventDate())
                .initiator(event.getInitiator())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .confirmedRequests(event.getConfirmedRequests())
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(event.getInitiator())
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .confirmedRequests(event.getConfirmedRequests())
                .build();
    }
}
