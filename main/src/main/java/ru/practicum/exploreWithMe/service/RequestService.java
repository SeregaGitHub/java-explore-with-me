package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequests(Integer userId);

    ParticipationRequestDto addRequest(Integer userId, Integer eventId);

    ParticipationRequestDto cancelRequest(Integer userId, Integer requestId);
}
