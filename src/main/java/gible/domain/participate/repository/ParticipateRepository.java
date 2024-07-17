package gible.domain.participate.repository;

import gible.domain.participate.entity.Participate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParticipateRepository extends JpaRepository<Participate, UUID> {

    List<Participate> findByUser_Email(String email);
}
