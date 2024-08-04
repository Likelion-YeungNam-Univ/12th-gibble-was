package gible.domain.post.dto;

import gible.domain.donation.dto.DonationSenderInfoRes;
import gible.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
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
        List<DonationSenderInfoRes> donationInfo
) {
    public static PostDetailRes fromEntity(Post post, List<DonationSenderInfoRes> donationInfo, boolean isPermitted) {
        return new PostDetailRes(
                post.getId(), post.getTitle(), post.getContent(), post.getAddress(),
                post.getWantedCard(), post.getDonatedCard(), post.getWriter().getName(),
                post.getWriter().getEmail(), post.getCreatedAt(), isPermitted, donationInfo);
    }
}
