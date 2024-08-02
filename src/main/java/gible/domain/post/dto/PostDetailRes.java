package gible.domain.post.dto;

import gible.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostDetailRes(
        UUID postId,
        String title,
        String content,
        String address,
        String name,
        int wantedCard,
        int donatedCard,
        String phoneNumber,
        String writer,
        UUID writerId,
        String email,
        LocalDateTime createdAt
) {
    public static PostDetailRes fromEntity(Post post) {
        return new PostDetailRes(
                post.getId(), post.getTitle(), post.getContent(), post.getAddress(),
                post.getWriter().getName(), post.getWantedCard(), post.getDonatedCard(),
                post.getWriter().getPhoneNumber(), post.getWriter().getNickname(),
                post.getWriter().getId(), post.getWriter().getEmail(), post.getCreatedAt()
                );
    }
}
