package se.puggan.springtest.Controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import se.puggan.springtest.Json.DTResponse;
import se.puggan.springtest.Models.Name;
import se.puggan.springtest.Models.User;
import se.puggan.springtest.Models.UserAuth;
import se.puggan.springtest.Requests.DTRequest;
import se.puggan.springtest.Requests.GuestRequest;
import se.puggan.springtest.Requests.UserRequest;

import javax.servlet.http.Cookie;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestScope
@EnableAutoConfiguration
public class Index extends Base
{
    @RequestMapping("/logout")
    @ResponseBody
    public String logout(
            @ModelAttribute @Valid GuestRequest guestRequest
    )
    {
        guestRequest.addCookie(new Cookie("session_id", ""));
        return guestRequest.redirect("/", "{\"ok\": true}");
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(
        @RequestParam(defaultValue = "") String username,
        @RequestParam(defaultValue = "") String password,
        @ModelAttribute @Valid GuestRequest guestRequest
    )
    {
        Optional<User> ou = usersquery.byUsername(username);
        boolean ok = false;
        if(ou.isPresent()) {
            User user = ou.get();
            Optional<UserAuth> auth = authquery.byType(user, UserAuth.PASSWORD);
            ok = auth.isPresent() && auth.get().verify(password);
        }
        if (!ok)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Login Failed");
        }

        guestRequest.addCookie(new Cookie("session_id", "qwerty12345"));
        return guestRequest.redirect("/", "{\"ok\": true}");
    }

    @RequestMapping("/list")
    @ResponseBody
    public DTResponse<Name> list(
            @ModelAttribute @Valid DTRequest dtRequest
    )
    {
        DTResponse<Name> json = dtRequest.response(namequery.count());
        json.setPage(namequery.maxSearch(dtRequest.getPagable(new String[]{"firstname", "lastname"}),
                dtRequest.globalSearch().replace(" ", "%"),
                dtRequest.columnSearch(0).replace(" ", "%"),
                dtRequest.columnSearch(1).replace(" ", "%")
        ));
        return json;
    }

    @RequestMapping("/name/{id}")
    //public String name(
    public ModelAndView name(
        @PathVariable("id") Integer id,
        @ModelAttribute @Valid UserRequest userRequest
    ) {
        ModelAndView view = new ModelAndView("name");
        Optional<Name> maybeName = namequery.findById(id);
        view.addObject("id", id);
        if (maybeName.isPresent())
        {
            view.addObject("Name", maybeName.get());
        }
        else
        {
            view.addObject("Name", new Name());
        }
        return view;
    }
}
