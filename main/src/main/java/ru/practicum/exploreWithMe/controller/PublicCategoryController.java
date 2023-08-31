package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@Validated
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Category getCategory(@PathVariable Integer id) {
        return categoryService.getCategory(id);
    }
}
