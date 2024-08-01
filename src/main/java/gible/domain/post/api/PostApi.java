package gible.domain.post.api;

import gible.domain.post.dto.PostReq;
import gible.domain.security.common.SecurityUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "[게시글 API]", description = "게시글 관련 API")
public interface PostApi {

    ResponseEntity<?> savePost(@Valid @RequestBody PostReq postReq,
                               @AuthenticationPrincipal SecurityUserDetails userDetails);

    ResponseEntity<?> getAllPosts(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
            @RequestParam(name = "search", required = false) String search);

    ResponseEntity<?> getPost(@PathVariable UUID postId);

    ResponseEntity<?> updatePost(@Valid @RequestBody PostReq postReq,
                                 @PathVariable UUID postId);

    ResponseEntity<?> deletePost(@PathVariable UUID postId);
}
