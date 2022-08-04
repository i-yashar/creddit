package bg.softuni.creddit.model.dto;

import bg.softuni.creddit.model.validation.UniqueUsername;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserProfileEditDTO {
    @Size(min = 2, max = 20)
    @NotBlank
    @UniqueUsername(message = "Username already taken")
    private String firstName;
    private String lastName;

    @Size(min = 3, max = 25, message = "Username should be between 3 and 25 symbols")
    private String username;
    private String profilePicUrl;
    private String about;

    public UserProfileEditDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
