package ru.practicum.exploreWithMe.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.User;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAuthor(User user, Pageable pageable);

    List<Comment> findAllByEvent(Event event, Pageable pageable);
}
