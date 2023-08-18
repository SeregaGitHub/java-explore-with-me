package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.HitDto;
import ru.practicum.exploreWithMe.dto.StatsDto;
import ru.practicum.exploreWithMe.model.Hit;
import ru.practicum.exploreWithMe.statsServerUtil.HitMapper;
import ru.practicum.exploreWithMe.storage.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public HitDto addHit(HitDto hitDto) {
        Hit hit = statsRepository.save(hitMapper.toHit(hitDto));
        log.info("Hit with Id={} was created", hit.getId());
        return hitMapper.toHitDto(hit);
    }

    @Override
    public List<StatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        List<String> actualList;
        boolean emptyList = false;
        if (uris == null) {
            actualList = Collections.emptyList();
            emptyList = true;
        } else {
            actualList = uris;
        }

        log.info("Stats from {} to {} was viewed", start, end);

        if (unique && !emptyList) {
            return statsRepository.getUniqueStats(LocalDateTime.parse(start, DateTimeFormatter.ofPattern(DATE_FORMAT)),
                    LocalDateTime.parse(end, DateTimeFormatter.ofPattern(DATE_FORMAT)), actualList);
        } else if (unique && emptyList) {
            return statsRepository.getUniqueStatsEmptyList(LocalDateTime.parse(start, DateTimeFormatter.ofPattern(DATE_FORMAT)),
                    LocalDateTime.parse(end, DateTimeFormatter.ofPattern(DATE_FORMAT)), actualList);
        } else if (!unique && !emptyList) {
            return statsRepository.getNotUniqueStats(LocalDateTime.parse(start, DateTimeFormatter.ofPattern(DATE_FORMAT)),
                    LocalDateTime.parse(end, DateTimeFormatter.ofPattern(DATE_FORMAT)), actualList);
        } else {
            return statsRepository.getNotUniqueStatsEmptyList(LocalDateTime.parse(start, DateTimeFormatter.ofPattern(DATE_FORMAT)),
                    LocalDateTime.parse(end, DateTimeFormatter.ofPattern(DATE_FORMAT)), actualList);
        }
    }
}
