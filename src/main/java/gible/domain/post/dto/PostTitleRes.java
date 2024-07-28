package gible.domain.post.dto;

import gible.domain.post.entity.Post;

import java.util.UUID;

public record PostTitleRes(
        UUID postId,
        String title
) {
    public static PostTitleRes fromEntity(Post post) {
        return new PostTitleRes(post.getId(), post.getTitle());
    }
}
