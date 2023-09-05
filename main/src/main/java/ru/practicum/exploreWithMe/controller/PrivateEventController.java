package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.comment.CommentDto;
import ru.practicum.exploreWithMe.dto.comment.NewCommentDto;
import ru.practicum.exploreWithMe.dto.comment.UpdateCommentDto;
import ru.practicum.exploreWithMe.dto.event.EventDto;
import ru.practicum.exploreWithMe.dto.event.EventForAddDto;
import ru.practicum.exploreWithMe.dto.event.EventForUserUpdateDto;
import ru.practicum.exploreWithMe.dto.event.EventFullDto;
import ru.practicum.exploreWithMe.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.exploreWithMe.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.exploreWithMe.dto.request.ParticipationRequestDto;
import ru.practicum.exploreWithMe.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Validated
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getEventsByUser(@PathVariable Integer userId,
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return eventService.getEventsByUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Integer userId, @Valid @RequestBody EventForAddDto eventForAddDto) {
        return eventService.addEvent(userId, eventForAddDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getFullEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventService.getFullEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer userId, @PathVariable Integer eventId,
                                    @Valid @RequestBody EventForUserUpdateDto eventForUserUpdateDto) {
        return eventService.updateEventByUser(userId, eventId, eventForUserUpdateDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventService.getRequestByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsByUser(@PathVariable Integer userId, @PathVariable Integer eventId,
                                                         @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return eventService.updateRequestsByUser(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@Valid @RequestBody NewCommentDto newCommentDto,
                                 @PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventService.addComment(eventId, userId, newCommentDto);
    }

    @PatchMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@Valid @RequestBody UpdateCommentDto updateCommentDto,
                                    @PathVariable Integer userId, @PathVariable Integer commentId) {
        return eventService.updateCommentByOwner(updateCommentDto, userId, commentId);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCommentByOwner(@PathVariable Integer userId, @PathVariable Integer commentId) {
        eventService.removeCommentByOwner(commentId, userId);
    }
}
