package ru.practicum.exploreWithMe.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.State;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("select e from Event e " +
            "where ((:text is null or upper(e.annotation) like upper(concat('%', :text, '%'))) " +
            "or (:text is null or upper(e.description) like upper(concat('%', :text, '%')))) " +
            "and (:state is null or e.state = :state) " +
            "and (:categories is null or e.category.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (cast(:rangeStart as java.time.LocalDateTime) is null or e.eventDate >= :rangeStart) " +
            "and (cast(:rangeEnd as java.time.LocalDateTime) is null or e.eventDate <= :rangeEnd) ")
    List<Event> getAllEvents(@Param("text") String text,
                             @Param("state") State state,
                             @Param("categories") List<Integer> categories,
                             @Param("paid") Boolean paid,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             @Param("rangeEnd") LocalDateTime rangeEnd,
                             Pageable pageable);

    @Query("select e from Event e " +
            "where (:users is null or e.initiator.id in :users) " +
            "and (:states is null or e.state in :states) " +
            "and (:categories is null or e.category.id in :categories) " +
            "and (cast(:rangeStart as java.time.LocalDateTime) is null or e.eventDate >= :rangeStart) " +
            "and (cast(:rangeEnd as java.time.LocalDateTime) is null or e.eventDate <= :rangeEnd) ")
    List<Event> getEventsByUserId(@Param("users") List<Integer> usersIds,
                                  @Param("states") List<State> states,
                                  @Param("categories") List<Integer> categories,
                                  @Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd,
                                  Pageable pageable);

    List<Event> findByInitiatorId(Integer initiatorId, Pageable pageable);

    @Query("select count(e) from Event e " +
            "where (e.category.id = :categoryId)")
    Integer countEventsWithCategory(@Param("categoryId") Integer categoryId);

    @Query("select e from Event e " +
            "where ((:text is null or upper(e.annotation) like upper(concat('%', :text, '%'))) " +
            "or (:text is null or upper(e.description) like upper(concat('%', :text, '%')))) " +
            "and (:state is null or e.state = :state) " +
            "and (:categories is null or e.category.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (cast(:rangeStart as java.time.LocalDateTime) is null or e.eventDate >= :rangeStart) " +
            "and (cast(:rangeEnd as java.time.LocalDateTime) is null or e.eventDate <= :rangeEnd) " +
            "AND (SELECT COUNT(pr) FROM ParticipationRequest pr WHERE pr.event.id = e.id " +
            "AND pr.status = 'APPROVE') < e.participantLimit")
    List<Event> getAvailableEvents(@Param("text") String text,
                                   @Param("state") State state,
                                   @Param("categories") List<Integer> categories,
                                   @Param("paid") Boolean paid,
                                   @Param("rangeStart") LocalDateTime rangeStart,
                                   @Param("rangeEnd") LocalDateTime rangeEnd,
                                   Pageable pageable);
}