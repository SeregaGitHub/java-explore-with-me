package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.comment.CommentDto;
import ru.practicum.exploreWithMe.dto.comment.NewCommentDto;
import ru.practicum.exploreWithMe.dto.comment.UpdateCommentDto;
import ru.practicum.exploreWithMe.dto.event.*;
import ru.practicum.exploreWithMe.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.exploreWithMe.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.exploreWithMe.dto.request.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventFullDto> getFullEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEvent(EventForUpdateDto eventForUpdateDto, Integer eventId);

    List<EventDto> getAllEvents(String text, List<Integer> categories, Boolean paid,
                                LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort,
                                Integer from, Integer size, HttpServletRequest httpRequest);

    EventFullDto getEvent(Integer id, HttpServletRequest httpServletRequest);

    List<EventDto> getEventsByUser(Integer userId, Integer from, Integer size);

    EventFullDto addEvent(Integer userId, EventForAddDto eventForAddDto);

    EventFullDto getFullEvent(Integer userId, Integer eventId);

    EventFullDto updateEventByUser(Integer userId, Integer eventId, EventForUserUpdateDto eventForUserUpdateDto);

    List<ParticipationRequestDto> getRequestByUser(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult updateRequestsByUser(Integer userId, Integer eventId,
                                                        EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    CommentDto addComment(Integer eventId, Integer userId, NewCommentDto newCommentDto);

    CommentDto updateCommentByOwner(UpdateCommentDto comment, Integer userId, Integer commentId);

    void removeCommentByOwner(Integer commentId, Integer userId);

    List<CommentDto> getAllEventComments(Integer eventId, Integer from, Integer size);

    CommentDto getComment(Integer commentId);

    List<CommentDto> getAllUserComments(Integer userId, Integer from, Integer size);

    void removeCommentByAdmin(Integer commentId);
}
