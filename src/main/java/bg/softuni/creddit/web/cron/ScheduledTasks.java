package bg.softuni.creddit.web.cron;

import bg.softuni.creddit.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    public static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
    private final CommentService commentService;

    public ScheduledTasks(CommentService commentService) {
        this.commentService = commentService;
    }

    @Scheduled(fixedRate = 300000, initialDelay = 10000)
    public void commentCheck() {
        String result = this.commentService.cleanUpBadComments();
        LOGGER.info("{}", result);
    }
}
