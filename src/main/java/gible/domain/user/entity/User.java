package gible.domain.user.entity;

import gible.domain.donation.entity.Donation;
import gible.domain.participate.entity.Participate;
import gible.domain.post.entity.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "donate_count")
    private int donateCount;

    private int point;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE)
    private List<Post> posts;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
    private List<Donation> donationsSent;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE)
    private List<Donation> donationsReceived;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Participate> participates;

    @Builder
    public User(String name, String email, String nickname, String phoneNumber, Role role) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
