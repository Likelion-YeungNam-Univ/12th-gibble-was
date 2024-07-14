package gible.domain.post.entity;

import gible.domain.donation.entity.Donation;
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
    private String name;

    @NotNull
    @Column(name = "wanted_card")
    private int wantedCard;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "donated_care")
    private int donatedCare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @OneToMany(mappedBy = "post")
    private List<Donation> donations;

    @Builder
    public Post(String title, String content, String address, String name, int wantedCard, User writer) {
        this.title = title;
        this.content = content;
        this.address = address;
        this.name = name;
        this.wantedCard = wantedCard;
        this.writer = writer;
    }
}
