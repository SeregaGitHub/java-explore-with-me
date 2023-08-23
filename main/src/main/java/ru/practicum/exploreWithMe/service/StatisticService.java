package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface StatisticService {
    void setStatistic(List<Event> events);

    void setStatistic(Event event);

    void addHit(String uri, HttpServletRequest httpServletRequest);
}
