package bg.softuni.creddit.service;

import bg.softuni.creddit.model.entity.Post;
import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.model.entity.Vote;
import bg.softuni.creddit.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote findVoteByUserAndPost(User user, Post post) {
        Vote vote = this.voteRepository.findVoteByUsernameAndPostId(user.getUsername(), post.getId());

        return vote != null
                ? vote
                : createNewVote(user, post);
    }

    public void updateVote(Vote vote) {
        this.voteRepository.save(vote);
    }

    private Vote createNewVote(User user, Post post) {
        Vote vote = new Vote(user, post, 0);
        this.voteRepository.save(vote);

        return this.voteRepository.findVoteByUsernameAndPostId(user.getUsername(), post.getId());
    }

    public void deleteAllVotesOnPost(Long postId) {
        this.voteRepository.findAll().stream()
                .filter(v -> Objects.equals(v.getPost().getId(), postId))
                .forEach(this.voteRepository::delete);
    }
}
