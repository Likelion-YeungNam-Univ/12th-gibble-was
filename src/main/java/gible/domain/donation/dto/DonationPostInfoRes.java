package gible.domain.donation.dto;

import gible.domain.donation.entity.Donation;

public record DonationPostInfoRes(
        String title,
        String nickname
) {
    public static DonationPostInfoRes fromEntity(Donation donation) {
        return new DonationPostInfoRes(donation.getPost().getTitle(), donation.getReceiver().getNickname());
    }
}
