package gible.domain.event.entity;

import gible.domain.event.dto.EventReq;
import gible.domain.participate.entity.Participate;
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
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private List<Participate> participates = new ArrayList<>();

    @Builder
    public Event(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    /* 이벤트 업데이트 */
    public void updateEvent(EventReq eventReq) {
        this.title = eventReq.title();
        this.content = eventReq.content();
        this.imageUrl = eventReq.imageUrl();
    }
}
