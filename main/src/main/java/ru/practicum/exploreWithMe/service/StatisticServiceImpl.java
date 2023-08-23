package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.client.StatClient;
import ru.practicum.exploreWithMe.dto.HitDto;
import ru.practicum.exploreWithMe.dto.StatsDto;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.util.UriBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatClient statClient;
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void setStatistic(List<Event> events) {
        if (events.isEmpty()) {
            return;
        }
        List<String> uris = events.stream().map(UriBuilder::toUri).collect(Collectors.toList());
        LocalDateTime now = LocalDateTime.now();
        List<StatsDto> statisticsDto = statClient.getStatistic(
                LocalDateTime.now().minusYears(2).format(FORMATTER), now.format(FORMATTER), uris, true);
        if (statisticsDto.isEmpty()) {
            events.forEach(o -> o.setViews(0));
        }
        for (StatsDto statisticDto : statisticsDto) {
            Integer id = Integer.parseInt(String.valueOf(statisticDto.getUri().charAt(statisticDto.getUri().length() - 1)));
            for (Event event : events) {
                if (Objects.equals(event.getId(), id)) {
                    event.setViews(Math.toIntExact(statisticDto.getHits()));
                }
            }
        }
    }

    @Override
    public void setStatistic(Event event) {
        String uris = UriBuilder.toUri(event);
        LocalDateTime now = LocalDateTime.now();
        List<StatsDto> statisticsDto = statClient.getStatistic(
                LocalDateTime.now().minusYears(2).format(FORMATTER), now.format(FORMATTER), Collections.singletonList(uris), true);
        if (statisticsDto.isEmpty()) {
            event.setViews(0);
        } else {
            event.setViews(Math.toIntExact(statisticsDto.get(0).getHits()));
        }
    }

    @Override
    public void addHit(String uri, HttpServletRequest httpServletRequest) {
        LocalDateTime now = LocalDateTime.now();
        HitDto hitDto = new HitDto("ewm-service", uri, httpServletRequest.getRemoteAddr(), now.format(FORMATTER));
        statClient.addHit(hitDto);
    }
}
