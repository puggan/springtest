package se.puggan.springtest.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.annotation.RequestScope;
import se.puggan.springtest.Helpers.SelfValidator;
import se.puggan.springtest.Repositories.NameRepository;
import se.puggan.springtest.Repositories.UserAuthRepository;
import se.puggan.springtest.Repositories.UserRepository;
import se.puggan.springtest.Requests.DTRequest;
import se.puggan.springtest.Requests.GuestRequest;
import se.puggan.springtest.Requests.UserRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestScope
@EnableAutoConfiguration
public class Base implements Validator
{
    @Autowired
    UserRepository usersquery;

    @Autowired
    UserAuthRepository authquery;

    @Autowired
    NameRepository namequery;

    @ModelAttribute
    private GuestRequest guestRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            @CookieValue String session_id
    )
    {
        return new GuestRequest(request, response, session_id);
    }

    @ModelAttribute
    private UserRequest userRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            @CookieValue String session_id
            )
    {
        return new UserRequest(request, response, session_id);
    }

    @ModelAttribute
    private DTRequest dtRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            @CookieValue String session_id
    )
    {
        return new DTRequest(request, response, session_id);
    }

    //<editor-fold desc="Validation wrapper">
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(this);
    }


    public boolean supports(Class c) {
        return SelfValidator.class.isAssignableFrom(c);
    }

    @Override
    public void validate(Object o, Errors errors)
    {
        ((SelfValidator) o).validate(errors);
    }
    //</editor-fold>
}
