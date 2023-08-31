package ru.practicum.exploreWithMe.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDto {
    private List<Integer> events;
    private Boolean pinned = false;
    @NotBlank
    @Size(max = 50)
    private String title;
}
