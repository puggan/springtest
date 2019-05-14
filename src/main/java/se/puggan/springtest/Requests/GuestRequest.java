package se.puggan.springtest.Requests;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.context.annotation.RequestScope;
import se.puggan.springtest.Helpers.SelfValidator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestScope
@Component
public class GuestRequest implements SelfValidator
{
    private HttpServletResponse response;
    private HttpServletRequest request;
    String session_id;

    public GuestRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            String session_id
    ) {
        this.request = request;
        this.response = response;
        this.session_id = session_id;
    }

    public void addCookie(Cookie cookie) {
        response.addCookie(cookie);
    }

    public String param(String name, String default_value) {
        String parameter = request.getParameter(name);
        if(parameter == null) return default_value;
        return parameter;
    }

    public int param(String name, int default_value) {
        String parameter = request.getParameter(name);
        if(parameter == null) return default_value;
        return Integer.valueOf(parameter);
    }

    public boolean guest()
    {
        return !session_id.equals("qwerty12345");
    }

    public boolean isAjax()
    {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public <T> T redirect(String url, T content)
    {
        if (!isAjax())
        {
            response.setStatus(302);
            response.addHeader("Location", url);
        }
        return content;
    }

    @Override
    public void validate(Errors errors)
    {
    }
}
