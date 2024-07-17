package gible.domain.donation.repository;

import gible.domain.donation.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DonationRepository extends JpaRepository<Donation, UUID> {

    List<Donation> findByPost_Id(UUID postId);
    List<Donation> findBySender_Email(String email);
    List<Donation> findByReceiver_Email(String email);
}
