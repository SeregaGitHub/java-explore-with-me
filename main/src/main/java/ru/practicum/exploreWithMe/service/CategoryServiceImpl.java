package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.category.CategoryDto;
import ru.practicum.exploreWithMe.exception.ConflictException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.storage.CategoryRepository;
import ru.practicum.exploreWithMe.storage.EventRepository;
import ru.practicum.exploreWithMe.util.mapper.CategoryMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public Category addCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        log.info("Category with name " + categoryDto.getName() + " was added");
        return categoryRepository.save(category);
    }

    @Override
    public void removeCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category with Id=" + id + " was not found");
        }
        if (eventRepository.countEventsWithCategory(id) > 0) {
            throw new ConflictException("The category is not empty");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Category updateCategory(Integer id, CategoryDto categoryDto) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category with Id=" + id + " was not found");
        }
        Category category = CategoryMapper.toCategory(categoryDto);
        category.setId(id);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories(Integer from, Integer size) {
        int actualFrom = from == 0 ? 0 : from -1;
        return categoryRepository.findAll().stream().skip(actualFrom).limit(size).collect(Collectors.toList());
    }

    @Override
    public Category getCategory(Integer id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category with Id=" + id + " was not found"));
    }
}
