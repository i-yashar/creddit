package bg.softuni.creddit.model.validation;

import bg.softuni.creddit.repository.CommunityRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommunityPresentValidator implements ConstraintValidator<CommunityPresent, String> {
    private final CommunityRepository communityRepository;

    public CommunityPresentValidator(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.communityRepository
                .findByName(value)
                .isPresent();
    }
}
