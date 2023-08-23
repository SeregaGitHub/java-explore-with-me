package ru.practicum.exploreWithMe.util;

import lombok.experimental.UtilityClass;
import ru.practicum.exploreWithMe.exception.BadRequestException;
import ru.practicum.exploreWithMe.model.State;
import ru.practicum.exploreWithMe.model.Status;

@UtilityClass
public class EnumsUtil {
    public State makeState(String stateString) {
        State state;
        try {
            state = State.valueOf(stateString);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Unknown state: " + stateString);
        }
        return state;
    }

    public Status makeStatus(String statusString) {
        Status status;
        try {
            status = Status.valueOf(statusString);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Unknown status: " + statusString);
        }
        return status;
    }
}
