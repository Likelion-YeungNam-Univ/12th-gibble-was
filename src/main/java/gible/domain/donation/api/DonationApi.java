package gible.domain.donation.api;

import gible.domain.donation.dto.DonationReq;
import gible.domain.security.common.SecurityUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "[기부 API]", description = "기부 관련 API")
public interface DonationApi {

    ResponseEntity<?> donate(@Valid @RequestBody DonationReq donationReq,
                             @PathVariable UUID postId,
                             @AuthenticationPrincipal SecurityUserDetails userDetails);

    ResponseEntity<?> getDonorsForPost(@PathVariable UUID postId);

    ResponseEntity<?> getPostDonationDetails(
            @AuthenticationPrincipal SecurityUserDetails userDetails);

    ResponseEntity<?> getDonorsList(
            @AuthenticationPrincipal SecurityUserDetails userDetails);
}
