package gible.domain.post.service;

import gible.domain.post.dto.PostDetailRes;
import gible.domain.post.dto.PostReq;
import gible.domain.post.dto.PostSummaryRes;
import gible.domain.post.entity.Post;
import gible.domain.post.repository.PostRepository;
import gible.domain.user.entity.User;
import gible.domain.user.repository.UserRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import gible.global.aop.annotation.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /* 게시글 생성 */
    @Transactional
    public void savePost(PostReq postReq, UUID userId) {

        User foundUser = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorType.USER_NOT_FOUND));

        Post post = postReq.toEntity(postReq);
        post.addWriter(foundUser);
        postRepository.save(post);
    }

    /* 전체 게시글 불러오기 */
    @Transactional(readOnly = true)
    public Page<PostSummaryRes> getAllPosts(Pageable pageable) {

        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(PostSummaryRes::fromEntity);
    }

    /* 특정 게시글 불러오기 */
    @Transactional(readOnly = true)
    public PostDetailRes getPost(UUID postId) {

        Post foundPost = postRepository.findById(postId).orElseThrow(() ->
                new CustomException(ErrorType.POST_NOT_FOUND));
        return PostDetailRes.fromEntity(foundPost);
    }

    /* 검색한 단어에 대한 게시글 불러오기 */
    @Transactional(readOnly = true)
    public Page<PostSummaryRes> getPostsByKeyword(String search, Pageable pageable) {

        Page<Post> searchPosts = postRepository.findByTitleContaining(search, pageable);
        return searchPosts.map(PostSummaryRes::fromEntity);
    }

    /* 게시글 수정 */
    @AuthenticatedUser
    @Transactional
    public void updatePost(PostReq postReq, UUID postId) {

        Post foundPost = postRepository.findById(postId).orElseThrow(() ->
                new CustomException(ErrorType.POST_NOT_FOUND));

        foundPost.updatePost(postReq);
    }

    /* 게시글 삭제 */
    @AuthenticatedUser
    @Transactional
    public void deletePost(UUID postId) {

        postRepository.deleteById(postId);
    }
}
