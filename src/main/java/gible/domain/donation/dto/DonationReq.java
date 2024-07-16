package gible.domain.donation.dto;

import gible.domain.donation.entity.Donation;
import gible.domain.post.entity.Post;
import gible.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;

public record DonationReq(
        @NotNull(message = "기부 개수는 필수 작성 항목입니다.")
        int donateCount
) {
    public static Donation toEntity(DonationReq donationReq, User sender, Post post) {
        return Donation.builder()
                .donateCount(donationReq.donateCount())
                .sender(sender)
                .receiver(post.getWriter())
                .post(post)
                .build();
    }
}
