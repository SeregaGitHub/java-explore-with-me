package ru.practicum.exploreWithMe.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.exploreWithMe.model.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class ParticipationRequestDto {
    private Integer id;
    private Integer requester;
    private Integer event;
    private Status status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}
