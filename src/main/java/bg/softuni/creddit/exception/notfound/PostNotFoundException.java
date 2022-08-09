package bg.softuni.creddit.exception.notfound;


public class PostNotFoundException extends ObjectNotFoundException {
    public PostNotFoundException(String message) {
        super(message);
    }
}
