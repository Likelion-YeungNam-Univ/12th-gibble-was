package gible.domain.post.dto;

import gible.domain.post.entity.Post;

public record PostDetailRes(
        String title,
        String content,
        String address,
        String name,
        int wantedCard,
        int donatedCare,
        String writer
) {
    public static PostDetailRes fromEntity(Post post) {
        return new PostDetailRes(
                post.getTitle(), post.getContent(), post.getAddress(),
                post.getName(), post.getWantedCard(), post.getDonatedCare(),
                post.getWriter().getName());
    }
}
