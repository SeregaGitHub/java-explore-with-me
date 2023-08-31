package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.compilation.CompilationDto;
import ru.practicum.exploreWithMe.dto.compilation.NewCompilationDto;
import ru.practicum.exploreWithMe.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exploreWithMe.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@Validated
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCompilation(@PathVariable Integer compId) {
        compilationService.removeCompilation(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable Integer compId, @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
