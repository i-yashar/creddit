package bg.softuni.creddit.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommunityFormatValidator implements ConstraintValidator<CommunityFormat, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.startsWith("_") && value.trim().length() > 1;
    }
}
