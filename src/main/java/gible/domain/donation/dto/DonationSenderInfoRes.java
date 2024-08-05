package gible.domain.donation.dto;

import gible.domain.donation.entity.Donation;

public record DonationSenderInfoRes(
        String title,
        String name,
        int donateCount
) {
    public static DonationSenderInfoRes fromEntity(Donation donation) {
        return new DonationSenderInfoRes(donation.getPost().getTitle(),
                donation.getSender().getName(), donation.getDonateCount());
    }
}
