package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.event.EventForUpdateDto;
import ru.practicum.exploreWithMe.dto.event.EventFullDto;
import ru.practicum.exploreWithMe.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Validated
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getFullEvents(@RequestParam(value = "users", required = false) List<Integer> users,
                                            @RequestParam(value = "states", required = false) List<String> states,
                                            @RequestParam(value = "categories", required = false) List<Integer> categories,
                                            @RequestParam(value = "rangeStart", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam(value = "rangeEnd", required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @RequestParam(value = "from", defaultValue = "0") Integer from,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return eventService.getFullEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer eventId, @Valid @RequestBody EventForUpdateDto eventForUpdateDto) {
        return eventService.updateEvent(eventForUpdateDto, eventId);
    }
}
