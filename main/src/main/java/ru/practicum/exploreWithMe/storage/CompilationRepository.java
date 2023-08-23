package ru.practicum.exploreWithMe.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.model.Compilation;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    List<Compilation> findAllByPinned(boolean pinned, Pageable pageable);
}
