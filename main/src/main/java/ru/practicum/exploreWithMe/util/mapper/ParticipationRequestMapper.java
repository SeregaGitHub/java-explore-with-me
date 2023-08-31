package ru.practicum.exploreWithMe.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.exploreWithMe.dto.request.ParticipationRequestDto;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.ParticipationRequest;
import ru.practicum.exploreWithMe.model.Status;
import ru.practicum.exploreWithMe.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class ParticipationRequestMapper {
    public ParticipationRequest toParticipationRequest(User user, Event event) {
        Status status;
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            status = Status.CONFIRMED;
        } else {
            status = Status.PENDING;
        }
        return ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();
    }

    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .requester(participationRequest.getRequester().getId())
                .event(participationRequest.getEvent().getId())
                .status(participationRequest.getStatus())
                .created(participationRequest.getCreated())
                .build();
    }
}
