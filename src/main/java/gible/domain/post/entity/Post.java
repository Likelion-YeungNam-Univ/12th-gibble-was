package gible.domain.post.entity;

import gible.domain.donation.entity.Donation;
import gible.domain.post.dto.PostReq;
import gible.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String address;

    @NotNull
    @Column(name = "wanted_card")
    private int wantedCard;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "donated_card")
    private int donatedCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @OneToMany(mappedBy = "post")
    private List<Donation> donations = new ArrayList<>();

    @Builder
    public Post(String title, String content, String address, int wantedCard, User writer) {
        this.title = title;
        this.content = content;
        this.address = address;
        this.wantedCard = wantedCard;
        this.writer = writer;
    }

    /* 게시글 업데이트 */
    public void updatePost(PostReq postReq) {
        this.title = postReq.title();
        this.content = postReq.content();
        this.address = postReq.address();
        this.wantedCard = postReq.wantedCard();
    }

    /* 작성자 할당 */
    public void addWriter(User writer) {
        this.writer = writer;
        this.writer.getPosts().add(this);
    }

    /* 게시글에 기부한 개수 업데이트 */
    public void updateDonatedCard(int donatedCard) {
        this.donatedCard += donatedCard;
    }
}
