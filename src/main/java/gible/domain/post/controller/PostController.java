package gible.domain.post.controller;

import gible.domain.post.api.PostApi;
import gible.domain.post.dto.PostReq;
import gible.domain.post.service.PostService;
import gible.domain.security.common.SecurityUserDetails;
import gible.domain.user.service.UserService;
import gible.global.common.response.SuccessRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostController implements PostApi {

    private final UserService userService;
    private final PostService postService;

    /* 게시글 업로드 */
    @Override
    @PostMapping("/upload")
    public ResponseEntity<?> savePost(@Valid @RequestBody PostReq postReq,
                                      @AuthenticationPrincipal SecurityUserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.savePost(postReq, userDetails.getId()));
    }

    /* 게시글에 필요한 정보 불러오기 */
    @Override
    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal SecurityUserDetails userDetails) {

        return ResponseEntity.ok().body(userService.getUserInfo(userDetails.getId()));
    }

    /* 게시글 목록 조회 + 검색 조회 */
    @Override
    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "search", required = false) String search) {
        if (search == null)
            return ResponseEntity.ok().body(postService.getAllPosts(pageable));

        return ResponseEntity.ok().body(postService.getPostsByKeyword(search, pageable));
    }

    /* 특정 게시글 조회 */
    @Override
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @PathVariable UUID postId
    ) {

        return ResponseEntity.ok().body(postService.getPost(postId, userDetails.getId()));
    }

    /* 게시글 수정 */
    @Override
    @PutMapping("/upload/{postId}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody PostReq postReq,
                                        @PathVariable UUID postId) {

        postService.updatePost(postReq, postId);
        return ResponseEntity.ok(SuccessRes.from("게시글 수정 완료."));
    }

    /* 게시글 삭제 */
    @Override
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable UUID postId) {

        postService.deletePost(postId);
        return ResponseEntity.ok(SuccessRes.from("게시글 삭제 완료."));
    }
}


