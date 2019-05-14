package se.puggan.springtest.Helpers;

import org.springframework.validation.Errors;

public interface SelfValidator
{
    void validate(Errors errors);
}
