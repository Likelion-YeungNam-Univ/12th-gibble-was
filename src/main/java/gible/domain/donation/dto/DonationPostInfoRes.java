package gible.domain.donation.dto;

import gible.domain.donation.entity.Donation;

import java.util.UUID;

public record DonationPostInfoRes(
        UUID postId,
        String title,
        String nickname,
        int donateCount
) {
    public static DonationPostInfoRes fromEntity(Donation donation) {
        return new DonationPostInfoRes(donation.getPost().getId(), donation.getPost().getTitle(),
                donation.getReceiver().getNickname(), donation.getDonateCount());
    }
}
