package gible.domain.post.dto;

import gible.domain.post.entity.Post;

import java.util.UUID;

public record PostDetailRes(
        UUID postId,
        String title,
        String content,
        String address,
        String name,
        int wantedCard,
        int donatedCare,
        String phoneNumber,
        String writer
) {
    public static PostDetailRes fromEntity(Post post) {
        return new PostDetailRes(
                post.getId(), post.getTitle(), post.getContent(), post.getAddress(),
                post.getWriter().getName(), post.getWantedCard(), post.getDonatedCare(),
                post.getWriter().getPhoneNumber(), post.getWriter().getNickname());
    }
}
