package gible.domain.post.controller;

import gible.domain.post.dto.PostDetailRes;
import gible.domain.post.dto.PostReq;
import gible.domain.post.dto.PostSummaryRes;
import gible.domain.post.service.PostService;
import gible.global.util.api.SuccessRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/post")
@RestController
public class PostController {

    private final PostService postService;

    /* 게시글 업로드 */
    @PostMapping("/upload")
    public ResponseEntity<?> savePost(@Valid @RequestBody PostReq postReq,
                                      @AuthenticationPrincipal UserDetails userDetails) {

        postService.savePost(postReq, userDetails.getUsername());
        return ResponseEntity.created(null).body(SuccessRes.from("게시글 업로드 완료."));
    }

    /* 게시글 목록 조회 + 검색 조회 */
    @GetMapping
    public Page<PostSummaryRes> getAllPosts(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "search", required = false) String search) {

        if (search == null)
            return postService.getAllPosts(pageable);

        return postService.getPostsByKeyword(search, pageable);
    }

    /* 특정 게시글 조회 */
    @GetMapping("/{postId}")
    public PostDetailRes getPost(@PathVariable UUID postId) {

        return postService.getPost(postId);
    }

    /* 게시글 수정 */
    @PutMapping("/upload/{postId}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody PostReq postReq,
                                        @PathVariable UUID postId) {

        postService.updatePost(postReq, postId);
        return ResponseEntity.ok(SuccessRes.from("게시글 수정 완료."));
    }

    /* 게시글 삭제 */
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable UUID postId) {

        postService.deletePost(postId);
        return ResponseEntity.ok(SuccessRes.from("게시글 삭제 완료."));
    }
}


