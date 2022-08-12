package bg.softuni.creddit.model.dto;

import javax.validation.constraints.NotBlank;

public class AddCommentDTO {
    @NotBlank(message = "Comment can't be blank.")
    private String comment;

    public AddCommentDTO() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
