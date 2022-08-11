package bg.softuni.creddit.web.exceptionhandler;

import bg.softuni.creddit.exception.illegalaction.IllegalActionException;
import bg.softuni.creddit.exception.notfound.*;
import bg.softuni.creddit.exception.servererror.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class CredditExceptionHandler {
    @ExceptionHandler({CommentNotFoundException.class,
            CommunityNotFoundException.class,
            PostNotFoundException.class,
            UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleMissingObjects(ObjectNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("not-found");

        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }

    @ExceptionHandler({IllegalActionException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleIllegalActionTaken(IllegalActionException e) {
        ModelAndView modelAndView = new ModelAndView("action-forbidden");

        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }

    @ExceptionHandler({InternalServerErrorException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleServerError(InternalServerErrorException e) {
        ModelAndView modelAndView = new ModelAndView("internal-error");

        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }
}
