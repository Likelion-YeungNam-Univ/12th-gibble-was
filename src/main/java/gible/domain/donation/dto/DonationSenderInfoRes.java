package gible.domain.donation.dto;

import gible.domain.donation.entity.Donation;

import java.util.UUID;

public record DonationSenderInfoRes(
        UUID userId,
        String nickname,
        int donateCount
) {
    public static DonationSenderInfoRes fromEntity(Donation donation) {
        return new DonationSenderInfoRes(donation.getSender().getId(), donation.getSender().getNickname(), donation.getDonateCount());
    }

    public static DonationSenderInfoRes of(UUID userId, String nickname, int donateCount) {
        return new DonationSenderInfoRes(userId, nickname, donateCount);
    }
}
