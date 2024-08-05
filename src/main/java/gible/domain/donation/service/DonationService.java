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

@RequiredArgsConstructor
@Service
public class DonationService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final DonationRepository donationRepository;

    /* 기부하기 */
    @Transactional
    public void donate(DonationReq donationReq, UUID userId, UUID postId) {

        User foundUser = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorType.USER_NOT_FOUND));

        Post foundPost = postRepository.findById(postId).orElseThrow(() ->
                new CustomException(ErrorType.POST_NOT_FOUND));

        if(foundPost.getWriter().equals(foundUser)){
            throw new CustomException(ErrorType.INVALID_SELF_DONATE);
        }
        Donation donation = DonationReq.toEntity(donationReq, foundUser, foundPost);

        if(foundPost.getWantedCard() - foundPost.getDonatedCard()< donationReq.donateCount()){
            throw new CustomException(ErrorType.INVALID_DONATE_COUNT);
        }
        foundPost.updateDonatedCard(donationReq.donateCount());     // 게시글에 기부한 개수 업데이트.

        donationRepository.save(donation);
    }

    /* 게시글에 대한 기부자 목록 불러오기 */
    @Transactional(readOnly = true)
    public List<DonationSenderInfoRes> getDonorsForPost(UUID postId) {

        List<Donation> donations = donationRepository.findByPost_Id(postId);

        return donations.stream().map(DonationSenderInfoRes::fromEntity).toList();
    }

    /* 기부한 게시글에 대한 정보 불러오기 */
    @Transactional(readOnly = true)
    public List<DonationPostInfoRes> getPostDonationDetails(UUID userId) {

        List<Donation> donations = donationRepository.findBySender_Id(userId);

        return donations.stream().map(DonationPostInfoRes::fromEntity).toList();
    }

    /* 기부해준 사람들의 목록 불러오기 */
    @Transactional(readOnly = true)
    public List<DonationSenderInfoRes> getDonorsList(UUID userId) {

        List<Donation> donations = donationRepository.findByReceiver_Id(userId);

        return donations.stream().map(DonationSenderInfoRes::fromEntity).toList();
    }
}
