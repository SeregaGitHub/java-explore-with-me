package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.category.CategoryDto;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@Validated
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable Integer id) {
        categoryService.removeCategory(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Category updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(id, categoryDto);
    }
}
