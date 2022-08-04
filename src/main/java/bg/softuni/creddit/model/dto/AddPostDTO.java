package bg.softuni.creddit.model.dto;

import bg.softuni.creddit.model.validation.CommunityFormat;
import bg.softuni.creddit.model.validation.CommunityPresent;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddPostDTO {
    @Size(min = 10, max = 100)
    @NotBlank
    private String title;

    @Size(min = 10)
    @NotBlank
    private String description;

    @Size(min = 2, max = 20, message = "Community name must be between 2 and 20 symbols.")
    @CommunityFormat(message = "Community name must begin with a '_'")
    @CommunityPresent(message = "Community doesn't exist")
    private String community;

    public AddPostDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }
}
