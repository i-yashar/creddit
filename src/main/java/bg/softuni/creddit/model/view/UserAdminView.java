package bg.softuni.creddit.model.view;

import java.util.List;

public class UserAdminView {
    private String username;
    private List<String> roles;

    public UserAdminView() {
    }

    public UserAdminView(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
