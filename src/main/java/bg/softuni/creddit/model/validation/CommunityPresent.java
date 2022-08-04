package bg.softuni.creddit.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = CommunityPresentValidator.class)
public @interface CommunityPresent {
    String message() default "Invalid community.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
