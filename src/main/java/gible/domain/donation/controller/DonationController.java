package gible.domain.donation.controller;

import gible.domain.donation.dto.DonationPostInfoRes;
import gible.domain.donation.dto.DonationReq;
import gible.domain.donation.dto.DonationSenderInfoRes;
import gible.domain.donation.service.DonationService;
import gible.global.util.api.ApiUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class DonationController {

    private final DonationService donationService;

    /* 기부하기 */
    @PostMapping("/post/{postId}/donate")
    public ResponseEntity<?> donate(@Valid @RequestBody DonationReq donationReq,
                                    @PathVariable UUID postId,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        donationService.donate(donationReq, userDetails.getUsername(), postId);
        return ResponseEntity.ok(ApiUtil.from("기부 성공."));
    }

    /* 게시글에 대한 기부자 목록 불러오기 */
    @GetMapping("/post/{postId}/donators")
    public List<DonationSenderInfoRes> getDonorsForPost(@PathVariable UUID postId) {

        return donationService.getDonorsForPost(postId);
    }

    /* 기부한 게시글에 대한 정보 불러오기 */
    @GetMapping("/donation/my-donation")
    public List<DonationPostInfoRes> getPostDonationDetails(
            @AuthenticationPrincipal UserDetails userDetails) {

        return donationService.getPostDonationDetails(userDetails.getUsername());
    }

    /* 기부해준 사람들의 목록 불러오기 */
    @GetMapping("/donation/received-donation")
    public List<DonationSenderInfoRes> getDonorsList(
            @AuthenticationPrincipal UserDetails userDetails) {

        return donationService.getDonorsList(userDetails.getUsername());
    }
}
