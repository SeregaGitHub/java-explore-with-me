package ru.practicum.exploreWithMe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "compilation")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Compilation {
    @Id
    @Column(name = "compilation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "compilation_pinned", nullable = false)
    private Boolean pinned;
    @Column(name = "compilation_title", length = 512, nullable = false)
    private String title;
    @ManyToMany
    @JoinTable(name = "event_compilation",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    private Set<Event> events;
}
