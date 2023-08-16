package ru.practicum.exploreWithMe.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.dto.StatsDto;
import ru.practicum.exploreWithMe.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Integer> {
    @Query(value = "select new ru.practicum.exploreWithMe.dto." +
                   "StatsDto(h.app, h.uri, COUNT(DISTINCT h.ip) as i) " +
                   "from Hit as h " +
                   "where h.requestTime between ?1 and ?2 " +
                  // "and (h.uri in (?3) or ?4 = true) " +
                   "and h.uri in (?3) " +
                   "group by h.app, h.uri " +
                   "order by COUNT(DISTINCT h.ip) desc")
    List<StatsDto> getUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean emptyList);

    @Query(value = "select new ru.practicum.exploreWithMe.dto." +
                   "StatsDto(h.app, h.uri, COUNT(h.ip) as i) " +
                   "from Hit as h " +
                   "where h.requestTime between ?1 and ?2 " +
                   // "and (h.uri in (?3) or ?4 = true) " +
                   //"and (?4 = true or h.uri in (?3)) " +
                   "and h.uri in (?3) " +
                   "group by h.app, h.uri "+
                   "order by COUNT(h.ip) desc")
    List<StatsDto> getNotUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean emptyList);

    @Query(value = "select new ru.practicum.exploreWithMe.dto." +
            "StatsDto(h.app, h.uri, COUNT(h.ip) as i) " +
            "from Hit as h " +
            "where h.requestTime between ?1 and ?2 " +
            // "and (h.uri in (?3) or ?4 = true) " +
            //"and (?4 = true or h.uri in (?3)) " +
            //"and h.uri in (?3) " +
            "group by h.app, h.uri "+
            "order by COUNT(h.ip) desc")
    List<StatsDto> getNotUniqueStatsEmptyList(LocalDateTime parse, LocalDateTime parse1, List<String> actualList, boolean emptyList);
}
