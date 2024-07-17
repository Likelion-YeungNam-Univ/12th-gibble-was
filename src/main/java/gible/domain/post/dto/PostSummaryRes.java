package gible.domain.post.dto;

import gible.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostSummaryRes(
        UUID postId,
        String title,
        LocalDateTime createdAt,
        String writer
) {
    public static PostSummaryRes fromEntity(Post post) {
        return new PostSummaryRes(
                post.getId(), post.getTitle(), post.getCreatedAt(), post.getWriter().getNickname());
    }
}
