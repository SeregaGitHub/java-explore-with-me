package ru.practicum.exploreWithMe.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.exploreWithMe.dto.compilation.CompilationDto;
import ru.practicum.exploreWithMe.dto.compilation.NewCompilationDto;
import ru.practicum.exploreWithMe.dto.event.EventFullDto;
import ru.practicum.exploreWithMe.model.Compilation;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        Set<EventFullDto> set;
        if (compilation.getEvents() == null) {
            set = new HashSet<>();
        } else {
            if (compilation.getEvents().isEmpty()) {
                set = new HashSet<>();
            } else {
                set = compilation.getEvents().stream().map(EventMapper::toEventFullDto).collect(Collectors.toSet());
            }
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(set)
                .build();
    }
}
