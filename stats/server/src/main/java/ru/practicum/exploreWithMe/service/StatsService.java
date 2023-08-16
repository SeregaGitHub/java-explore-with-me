package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.HitDto;
import ru.practicum.exploreWithMe.dto.StatsDto;

import java.util.List;

public interface StatsService {

    HitDto addHit(HitDto hitDto);

    List<StatsDto> getStats(String start, String end, List<String> uris, boolean unique);
}
