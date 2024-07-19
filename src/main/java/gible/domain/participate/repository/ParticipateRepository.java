package gible.domain.participate.repository;

import gible.domain.participate.entity.Participate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParticipateRepository extends JpaRepository<Participate, UUID> {
    List<Participate> findByUser_Id(UUID userId);
}
