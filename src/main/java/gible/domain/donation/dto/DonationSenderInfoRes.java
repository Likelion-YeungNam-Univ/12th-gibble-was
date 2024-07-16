package gible.domain.donation.dto;

import gible.domain.donation.entity.Donation;

public record DonationSenderInfoRes(
        String nickname,
        int donateCount
) {
    public static DonationSenderInfoRes fromEntity(Donation donation) {
        return new DonationSenderInfoRes(donation.getSender().getNickname(), donation.getDonateCount());
    }
}
