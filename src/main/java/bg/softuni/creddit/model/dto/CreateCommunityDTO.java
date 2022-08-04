package bg.softuni.creddit.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateCommunityDTO {

    @Size(min = 4, max = 20)
    private String name;

    @NotBlank
    private String description;
    private String communityPhotoUrl;

    public CreateCommunityDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommunityPhotoUrl() {
        return communityPhotoUrl;
    }

    public void setCommunityPhotoUrl(String communityPhotoUrl) {
        this.communityPhotoUrl = communityPhotoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
