package gible.global.aop;

import gible.domain.post.repository.PostRepository;
import gible.domain.security.common.SecurityUserDetails;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Aspect
@Component
public class AuthenticatedUserAop {

    private final PostRepository postRepository;

    @Around("@annotation(gible.global.aop.annotation.AuthenticatedUser)")
    public Object authenticatedUser(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

        SecurityUserDetails principalDetails =
                (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID expectedUserId = principalDetails.getId();

        for (Object arg : args) {
            if (arg instanceof UUID) {
                UUID postId = (UUID) arg;
                verifyAuthentication(expectedUserId, postId);
                break;
            }
        }

        return joinPoint.proceed();
    }

    /* 게시글에 대한 권한 확인 메서드 */
    private void verifyAuthentication(UUID expectedUserId, UUID postId) {

        UUID actualUserId = postRepository.findWriterIdByPostId(postId);
        if (actualUserId == null) {
            throw new CustomException(ErrorType.POST_NOT_FOUND);
        }

        if (!expectedUserId.equals(actualUserId)) {
            throw new CustomException(ErrorType.UNAUTHORIZED);
        }
    }
}