package gible.domain.post.dto;

import gible.domain.post.entity.Post;
import gible.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;

public record PostReq(
        @NotBlank
        String title,
        @NotBlank
        String content,
        @NotBlank
        String address,
        @NotBlank
        String name,
        @NotBlank
        int wantedCard
) {
    public Post toEntity(PostReq postReq, User writer) {
        return Post.builder()
                .title(postReq.title())
                .content(postReq.content())
                .address(postReq.address())
                .name(postReq.name())
                .wantedCard(postReq.wantedCard())
                .writer(writer)
                .build();
    }
}
