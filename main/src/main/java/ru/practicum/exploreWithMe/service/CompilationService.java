package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.compilation.CompilationDto;
import ru.practicum.exploreWithMe.dto.compilation.NewCompilationDto;
import ru.practicum.exploreWithMe.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Integer compId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void removeCompilation(Integer compId);

    CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest updateCompilationRequest);
}
