package ru.practicum.exploreWithMe.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "annotation", length = 2048, nullable = false)
    String annotation;
    @OneToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
    @Column(name = "created_on", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    @Column(name = "description", length = 4096, nullable = false)
    String description;
    @Column(name = "event_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    @OneToOne
    @JoinColumn(name = "location_id")
    Location location;
    Boolean paid;
    Integer participantLimit;
    @Column(name = "published_on")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    State state;
    @Column(name = "event_title", nullable = false)
    String title;
    @Transient
    Integer views;
    @Transient
    Integer confirmedRequests;
}
