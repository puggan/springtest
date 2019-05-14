package se.puggan.springtest.Requests;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestScope
@Component
public class UserRequest extends GuestRequest
{
    public UserRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            String session_id
    ) {
        super(request, response, session_id);
    }

    @Override
    public void validate(Errors errors)
    {
        if(guest()) {
            errors.reject("Unauthorized user");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized user");
        }
    }
}
