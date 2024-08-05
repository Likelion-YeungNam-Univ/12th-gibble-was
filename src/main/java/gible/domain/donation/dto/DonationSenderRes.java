package gible.domain.donation.dto;

import java.util.UUID;

public record DonationSenderRes(
        UUID userId,
        String name,
        int donateCount
) {

    public static DonationSenderRes of(UUID userId, String nickname, int donateCount) {
        return new DonationSenderRes(userId, nickname, donateCount);
    }
}
