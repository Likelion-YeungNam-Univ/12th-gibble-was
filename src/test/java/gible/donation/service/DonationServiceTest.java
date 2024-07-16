package gible.donation.service;

import gible.domain.donation.dto.DonationPostInfoRes;
import gible.domain.donation.dto.DonationReq;
import gible.domain.donation.dto.DonationSenderInfoRes;
import gible.domain.donation.entity.Donation;
import gible.domain.donation.repository.DonationRepository;
import gible.domain.donation.service.DonationService;
import gible.domain.post.entity.Post;
import gible.domain.post.repository.PostRepository;
import gible.domain.user.entity.User;
import gible.domain.user.repository.UserRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DonationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private DonationRepository donationRepository;

    @InjectMocks
    private DonationService donationService;

    private UUID userId;
    private UUID postId;

    private Post post1;

    private User user1;
    private User user2;

    private Donation donation1;
    private Donation donation2;

    private DonationReq donationReq;

    @BeforeEach
    void setUp() {
        this.userId = UUID.randomUUID();
        this.postId = UUID.randomUUID();
        this.donationReq = new DonationReq(2);

        createUser();
        createPost();
        createDonation();
    }

    private void createPost() {
        this.post1 = Post.builder()
                .title("제목1")
                .writer(user1)
                .build();

    }

    private void createUser() {
        this.user1 = User.builder()
                .nickname("작성자1")
                .build();

        this.user2 = User.builder()
                .nickname("작성자2")
                .build();
    }

    private void createDonation() {
        this.donation1 = Donation.builder()
                .donateCount(2)
                .post(post1)
                .sender(user1)
                .receiver(user2)
                .build();

        this.donation2 = Donation.builder()
                .donateCount(1)
                .post(post1)
                .sender(user2)
                .receiver(user1)
                .build();
    }

    @Test
    @DisplayName("기부하기 테스트")
    void donateTest() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user1));
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post1));

        Donation donation = DonationReq.toEntity(donationReq, user1, post1);
        when(donationRepository.save(any(Donation.class))).thenReturn(donation);

        // when
        donationService.donate(donationReq, userId, postId);

        // then
        assertEquals(2, post1.getDonatedCare());
        verify(userRepository, times(1)).findById(userId);
        verify(postRepository, times(1)).findById(postId);
        verify(donationRepository, times(1)).save(any(Donation.class));
    }

    @Test
    @DisplayName("기부하기 실패 테스트 - 사용자 없음")
    void donateFailedByUserNotFoundTest () {
        // given
        when(userRepository.findById(userId)).thenThrow(new CustomException(ErrorType.USER_NOT_FOUND));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            donationService.donate(donationReq, userId, postId);
        });

        // then
        assertEquals(ErrorType.USER_NOT_FOUND, exception.getErrortype());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("기부하기 실패 테스트 - 게시글 없음")
    void donateFailedByPostNotFoundTest() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user1));
        when(postRepository.findById(postId)).thenThrow(new CustomException(ErrorType.POST_NOT_FOUND));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            donationService.donate(donationReq, userId, postId);
        });

        // then
        assertEquals(ErrorType.POST_NOT_FOUND, exception.getErrortype());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시글에 대한 기부자 목록 불러오기 테스트")
    void getDonorsForPostTest() {
        // given
        List<Donation> donations = List.of(
                donation1, donation2
        );

        when(donationRepository.findByPost_Id(postId)).thenReturn(donations);

        // when
        List<DonationSenderInfoRes> donationSenderInfoList
                = donationService.getDonorsForPost(postId);

        // then
        assertNotNull(donationSenderInfoList);
        assertEquals(2, donationSenderInfoList.size());
        assertEquals("작성자1", donationSenderInfoList.get(0).nickname());
        assertEquals(2, donationSenderInfoList.get(0).donateCount());
        assertEquals("작성자2", donationSenderInfoList.get(1).nickname());
        assertEquals(1, donationSenderInfoList.get(1).donateCount());
    }

    @Test
    @DisplayName("기부한 게시글에 대한 정보 불러오기")
    void getPostDonationDetails() {
        // given
        List<Donation> donations = List.of(
                donation1
        );

        when(donationRepository.findBySender_Id(userId)).thenReturn(donations);

        // when
        List<DonationPostInfoRes> donationPostInfoList =
                donationService.getPostDonationDetails(userId);

        // then
        assertNotNull(donationPostInfoList);
        assertEquals(1, donationPostInfoList.size());
        assertEquals("제목1", donationPostInfoList.get(0).title());
        assertEquals("작성자2", donationPostInfoList.get(0).nickname());
    }

    @Test
    @DisplayName("기부해준 사람들의 목록 불러오기")
    void getDonorsListTest() {
        // given
        List<Donation> donations = List.of(
                donation2
        );

        when(donationRepository.findByReceiver_Id(userId)).thenReturn(donations);

        // when
        List<DonationSenderInfoRes> donationSenderInfoList
                = donationService.getDonorsList(userId);

        // then
        assertNotNull(donationSenderInfoList);
        assertEquals(1, donationSenderInfoList.size());
        assertEquals("작성자2", donationSenderInfoList.get(0).nickname());
        assertEquals(1, donationSenderInfoList.get(0).donateCount());
    }
}
