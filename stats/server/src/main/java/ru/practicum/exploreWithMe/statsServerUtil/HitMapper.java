package ru.practicum.exploreWithMe.statsServerUtil;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.HitDto;
import ru.practicum.exploreWithMe.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class HitMapper {
    private final ModelMapper modelMapper;
    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public HitMapper() {
        this.modelMapper = new ModelMapper();
        Configuration configuration = modelMapper.getConfiguration();
        configuration.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        modelMapper.typeMap(Hit.class, HitDto.class).addMappings(m -> m.skip(HitDto::setTimestamp));
    }

    public Hit toHit(HitDto hitDto) {
        Hit hit = modelMapper.map(hitDto, Hit.class);
        LocalDateTime localDateTime = LocalDateTime.parse(hitDto.getTimestamp(),
                DateTimeFormatter.ofPattern(DATE_FORMAT));
        hit.setRequestTime(localDateTime);
        return hit;
    }

    public HitDto toHitDto(Hit hit) {
        HitDto hitDto = modelMapper.map(hit, HitDto.class);
        String ldt = hit.getRequestTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        hitDto.setTimestamp(ldt);
        return hitDto;
    }
}
