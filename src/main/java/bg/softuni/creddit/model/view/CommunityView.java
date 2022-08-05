package bg.softuni.creddit.model.view;

import java.time.LocalDate;

public class CommunityView {
    private String name;
    private String description;
    private String owner;
    private LocalDate createdOn;
    private String communityPhotoUrl;
    private boolean hasCurrentUserJoined = false;

    public CommunityView() {
    }

    public boolean isHasCurrentUserJoined() {
        return hasCurrentUserJoined;
    }

    public void setHasCurrentUserJoined(boolean hasCurrentUserJoined) {
        this.hasCurrentUserJoined = hasCurrentUserJoined;
    }

    public String getCommunityPhotoUrl() {
        return communityPhotoUrl;
    }

    public void setCommunityPhotoUrl(String communityPhotoUrl) {
        this.communityPhotoUrl = communityPhotoUrl;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }
}
