package gible.domain.event.repository;

import gible.domain.event.entity.Event;
import gible.domain.event.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    Page<Event> findByRegion(Region region);
}
