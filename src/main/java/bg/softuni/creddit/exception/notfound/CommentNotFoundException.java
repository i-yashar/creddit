package bg.softuni.creddit.exception.notfound;

public class CommentNotFoundException extends ObjectNotFoundException{
    public CommentNotFoundException(String message) {
        super(message);
    }
}
