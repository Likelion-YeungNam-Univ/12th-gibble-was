package gible.domain.post.repository;

import gible.domain.post.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    Page<Post> findByTitleContaining(String search, Pageable pageable);
    List<Post> findByWriter_Id(UUID userId);

    @Query("SELECT p.writer.id FROM Post p WHERE p.id = :postId")
    UUID findWriterIdByPostId(@Param("postId") UUID postId);

    @Modifying
    @Query("UPDATE Post p SET p.isDonationPermitted = :isDonationPermitted WHERE p.id = :id")
    void updateDonationPermittedById(UUID postId, boolean isDonationPermitted);
}
