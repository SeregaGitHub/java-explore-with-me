package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.event.EventDto;
import ru.practicum.exploreWithMe.dto.event.EventFullDto;
import ru.practicum.exploreWithMe.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@Validated
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> getAllEvents(@RequestParam(required = false) String text,
                                       @RequestParam(required = false) List<Integer> categories,
                                       @RequestParam(required = false) Boolean paid,
                                       @RequestParam(required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                       @RequestParam(required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                       @RequestParam(required = false) boolean onlyAvailable,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       HttpServletRequest httpRequest) {
        return eventService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, httpRequest);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        return eventService.getEvent(id, httpServletRequest);
    }
}
