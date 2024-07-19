package gible.domain.post.repository;

import gible.domain.post.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    Page<Post> findByTitleContaining(String search, Pageable pageable);

    @Query("SELECT p.writer.id FROM Post p WHERE p.id = :postId")
    UUID findWriterIdByPostId(@Param("postId") UUID postId);
}
