package ru.practicum.exploreWithMe.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.model.ParticipationRequest;

import java.util.List;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {
    List<ParticipationRequest> findByRequesterId(Integer requesterId);

    @Query("select count(pr) from ParticipationRequest pr " +
            "where (pr.event.id = :id) " +
            "and (pr.status = 'CONFIRMED') ")
    Integer getConfirmedRequestsByEventId(@Param("id") Integer id);

    @Query("select pr from ParticipationRequest pr " +
            "where (pr.event.id = :eventId) " +
            "and (pr.event.initiator.id = :userId) ")
    List<ParticipationRequest> getRequestByUserIdAndEventId(@Param("userId") Integer userId,
                                                            @Param("eventId") Integer eventId);

    @Query("select pr from ParticipationRequest pr " +
            "where (pr.id in :requestIds) ")
    List<ParticipationRequest> getRequestsByRequestIds(@Param("requestIds") List<Integer> requestIds);

    @Query("select count(pr) from ParticipationRequest pr " +
            "where (pr.event.id = :id) ")
    Integer getRequestsByEventId(@Param("id") Integer id);
}
