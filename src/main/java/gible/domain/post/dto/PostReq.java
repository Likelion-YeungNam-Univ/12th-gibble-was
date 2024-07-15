package gible.domain.post.dto;

import gible.domain.post.entity.Post;
import gible.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostReq(
        @NotBlank(message = "제목은 필수 작성 항목입니다.")
        String title,
        @NotBlank(message = "내용은 필수 작성 항목입니다.")
        String content,
        @NotBlank(message = "주소는 필수 작성 항목입니다.")
        String address,
        @NotBlank(message = "이름은 필수 작성 항목입니다.")
        String name,
        @NotNull(message = "필요한 개수는 필수 작성 항목입니다.")
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
