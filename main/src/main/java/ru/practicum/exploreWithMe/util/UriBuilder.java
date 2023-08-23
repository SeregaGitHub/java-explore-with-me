package ru.practicum.exploreWithMe.util;

import lombok.experimental.UtilityClass;
import ru.practicum.exploreWithMe.model.Event;

@UtilityClass
public class UriBuilder {
    public String toUri(Event event) {
        return "/events/" + event.getId();
    }
}
