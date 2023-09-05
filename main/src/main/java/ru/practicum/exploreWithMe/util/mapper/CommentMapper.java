package ru.practicum.exploreWithMe.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.exploreWithMe.dto.comment.CommentDto;
import ru.practicum.exploreWithMe.dto.comment.NewCommentDto;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.User;

@UtilityClass
public class CommentMapper {
    public Comment toComment(NewCommentDto newCommentDto, User author, Event event) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .author(author)
                .event(event)
                .created(newCommentDto.getCreated())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
