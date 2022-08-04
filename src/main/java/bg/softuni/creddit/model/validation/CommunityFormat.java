package bg.softuni.creddit.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = CommunityFormatValidator.class)
public @interface CommunityFormat {
    String message() default "Invalid community name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
