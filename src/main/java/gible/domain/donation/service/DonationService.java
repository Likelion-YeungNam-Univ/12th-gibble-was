package gible.domain.donation.service;

import gible.domain.donation.dto.DonationPostInfoRes;
import gible.domain.donation.dto.DonationReq;
import gible.domain.donation.dto.DonationSenderInfoRes;
import gible.domain.donation.entity.Donation;
import gible.domain.donation.repository.DonationRepository;
import gible.domain.post.entity.Post;
import gible.domain.post.repository.PostRepository;
import gible.domain.user.entity.User;
import gible.domain.user.repository.UserRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DonationService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final DonationRepository donationRepository;

    /* 기부하기 */
    @Transactional
    public void donate(DonationReq donationReq, String email, UUID postId) {

        User foundUser = userRepository.findByEmail(email).orElseThrow(() ->
                new CustomException(ErrorType.USER_NOT_FOUND));

        Post foundPost = postRepository.findById(postId).orElseThrow(() ->
                new CustomException(ErrorType.POST_NOT_FOUND));

        Donation donation = DonationReq.toEntity(donationReq, foundUser, foundPost);
        foundPost.updateDonatedCare(donationReq.donateCount());     // 게시글에 기부한 개수 업데이트.

        donationRepository.save(donation);
    }

    /* 게시글에 대한 기부자 목록 불러오기 */
    @Transactional(readOnly = true)
    public List<DonationSenderInfoRes> getDonorsForPost(UUID postId) {

        List<Donation> donations = donationRepository.findByPost_Id(postId);

        return donations.stream().map(DonationSenderInfoRes::fromEntity).collect(Collectors.toList());
    }

    /* 기부한 게시글에 대한 정보 불러오기 */
    @Transactional(readOnly = true)
    public List<DonationPostInfoRes> getPostDonationDetails(String email) {

        List<Donation> donations = donationRepository.findBySender_Email(email);

        return donations.stream().map(DonationPostInfoRes::fromEntity).collect(Collectors.toList());
    }

    /* 기부해준 사람들의 목록 불러오기 */
    @Transactional(readOnly = true)
    public List<DonationSenderInfoRes> getDonorsList(String email) {

        List<Donation> donations = donationRepository.findByReceiver_Email(email);

        return donations.stream().map(DonationSenderInfoRes::fromEntity).collect(Collectors.toList());
    }
}
