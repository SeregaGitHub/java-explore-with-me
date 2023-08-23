package ru.practicum.exploreWithMe.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.exploreWithMe.dto.event.EventFullDto;

import java.util.Set;

@Data
@Builder
public class CompilationDto {
    private Set<EventFullDto> events;
    private Integer id;
    private Boolean pinned;
    private String title;
}
