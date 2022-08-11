package bg.softuni.creddit.aop;

import bg.softuni.creddit.service.CommentService;
import bg.softuni.creddit.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CreditDistributionAspect {
    private final CommentService commentService;

    public CreditDistributionAspect(CommentService commentService) {
        this.commentService = commentService;
    }

    @Pointcut("execution(* bg.softuni.creddit.service.CommentService.upVoteComment(..)) || execution(* bg.softuni.creddit.service.CommentService.downVoteComment(..))")
    void onUserVote() {

    }

    @After(value = "onUserVote() && args(username, commentId)", argNames = "joinPoint, username,commentId")
    public void giveCredits(JoinPoint joinPoint, String username, Long commentId) {
        this.commentService.distributeCredits(username, commentId);
    }
}
