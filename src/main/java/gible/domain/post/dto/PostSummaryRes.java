package gible.domain.post.dto;

import gible.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostSummaryRes(
        String title,
        LocalDateTime createdAt,
        String writer
) {
    public static PostSummaryRes fromEntity(Post post) {
        return new PostSummaryRes(post.getTitle(), post.getCreatedAt(), post.getWriter().getNickname());
    }
}
