package gible.domain.post.dto;

import gible.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record PostDetailRes(
        UUID postId,
        String title,
        String content,
        String address,
        int wantedCard,
        int donatedCard,
        String writer,
        String email,
        LocalDateTime createdAt,
        boolean isPermitted,
        Map<String, Integer> donationInfo
) {
    public static PostDetailRes fromEntitywithNickname(Post post, Map<String, Integer> donationInfo, boolean isPermitted) {
        return new PostDetailRes(
                post.getId(), post.getTitle(), post.getContent(), post.getAddress(),
                post.getWantedCard(), post.getDonatedCard(), post.getWriter().getName(),
                post.getWriter().getEmail(), post.getCreatedAt(), isPermitted, donationInfo);
    }

    public static PostDetailRes fromEntitywithName(Post post, Map<String, Integer> donationInfo, boolean isPermitted) {
        return new PostDetailRes(
                post.getId(), post.getTitle(), post.getContent(), post.getAddress(),
                post.getWantedCard(), post.getDonatedCard(), post.getWriter().getName(),
                post.getWriter().getEmail(), post.getCreatedAt(), isPermitted, donationInfo);
    }
}
