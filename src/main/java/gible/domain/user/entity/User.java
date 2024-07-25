package gible.domain.user.entity;

import gible.domain.donation.entity.Donation;
import gible.domain.participate.entity.Participate;
import gible.domain.post.entity.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
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
    @Email
    private String email;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "donate_count")
    @ColumnDefault("0")
    private int donateCount;

    @Column(name = "email_agree")
    private boolean emailAgree;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
    private List<Donation> donationsSent = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE)
    private List<Donation> donationsReceived = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Participate> participates = new ArrayList<>();

    @Builder
    public User(String name, String email, String nickname, String phoneNumber, boolean emailAgree, Role role) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.emailAgree = emailAgree;
        this.role = role;
    }
}
