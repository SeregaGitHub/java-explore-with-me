package ru.practicum.exploreWithMe.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.exploreWithMe.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventForAddDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull
    Integer category;
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    LocalDateTime eventDate;
    @NotNull
    Location location;
    @NotNull
    boolean paid = false;
    @PositiveOrZero
    Integer participantLimit = 0;
    boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
}
