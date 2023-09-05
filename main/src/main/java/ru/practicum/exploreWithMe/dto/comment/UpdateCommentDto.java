package ru.practicum.exploreWithMe.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentDto {
    @NotBlank(message = "Comment must have some text")
    private String text;
}
