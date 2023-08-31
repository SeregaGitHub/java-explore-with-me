package ru.practicum.exploreWithMe.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
