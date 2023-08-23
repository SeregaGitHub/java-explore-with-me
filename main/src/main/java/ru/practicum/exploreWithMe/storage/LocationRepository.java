package ru.practicum.exploreWithMe.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

}
