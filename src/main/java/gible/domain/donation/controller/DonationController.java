package gible.domain.donation.controller;

import gible.domain.donation.api.DonationApi;
import gible.domain.donation.dto.DonationReq;
import gible.domain.donation.service.DonationService;
import gible.domain.security.common.SecurityUserDetails;
import gible.global.common.response.SuccessRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class DonationController implements DonationApi {

    private final DonationService donationService;

    /* 기부하기 */
    @Override
    @PostMapping("/post/{postId}/donate")
    public ResponseEntity<?> donate(@Valid @RequestBody DonationReq donationReq,
                                    @PathVariable UUID postId,
                                    @AuthenticationPrincipal SecurityUserDetails userDetails) {

        donationService.donate(donationReq, userDetails.getId(), postId);
        return ResponseEntity.ok(SuccessRes.from("기부 성공."));
    }

    /* 게시글에 대한 기부자 목록 불러오기 */
    @Override
    @GetMapping("/post/{postId}/donators")
    public ResponseEntity<?> getDonorsForPost(@PathVariable UUID postId) {

        return ResponseEntity.ok().body(donationService.getDonorsForPost(postId));
    }

    /* 기부한 게시글에 대한 정보 불러오기 */
    @Override
    @GetMapping("/donation/my-donation")
    public ResponseEntity<?> getPostDonationDetails(
            @AuthenticationPrincipal SecurityUserDetails userDetails) {

        return ResponseEntity.ok().body(donationService.getPostDonationDetails(userDetails.getId()));
    }

    /* 기부해준 사람들의 목록 불러오기 */
    @Override
    @GetMapping("/donation/received-donation")
    public ResponseEntity<?> getDonorsList(
            @AuthenticationPrincipal SecurityUserDetails userDetails) {

        return ResponseEntity.ok().body(donationService.getDonorsList(userDetails.getId()));
    }
}
