package gible.domain.post.dto;

import java.util.UUID;

public record PostUploadRes(UUID postId) {
    public static PostUploadRes from(UUID postId){
        return new PostUploadRes(postId);
    }
}
