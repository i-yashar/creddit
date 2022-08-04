package bg.softuni.creddit.config;

import bg.softuni.creddit.model.entity.Comment;
import bg.softuni.creddit.model.entity.Community;
import bg.softuni.creddit.model.entity.Post;
import bg.softuni.creddit.model.view.CommentDisplayView;
import bg.softuni.creddit.model.view.CommunityView;
import bg.softuni.creddit.model.view.PostDisplayView;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(Post.class, PostDisplayView.class).addMappings(mapper -> {
            mapper.map(src -> src.getOwner().getUsername(),
                    PostDisplayView::setOwner);
            mapper.map(src -> src.getCommunity().getName(),
                    PostDisplayView::setCommunity);
        });

        modelMapper.typeMap(Comment.class, CommentDisplayView.class).addMappings(mapper -> {
            mapper.map(src -> src.getOwner().getUsername(),
                    CommentDisplayView::setOwner);
        });

        modelMapper.typeMap(Community.class, CommunityView.class).addMappings(mapper -> {
            mapper.map(src -> src.getCreatedBy().getUsername(),
                    CommunityView::setOwner);
        });

        return modelMapper;
    }
}
