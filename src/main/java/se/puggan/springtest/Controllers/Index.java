package se.puggan.springtest.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import se.puggan.springtest.Json.DTResponse;
import se.puggan.springtest.Models.Name;
import se.puggan.springtest.Models.User;
import se.puggan.springtest.Models.UserAuth;
import se.puggan.springtest.Repositories.NameRepository;
import se.puggan.springtest.Repositories.UserAuthRepository;
import se.puggan.springtest.Repositories.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
@EnableAutoConfiguration
public class Index
{
    @Autowired
    private UserRepository usersquery;

    @Autowired
    private UserAuthRepository authquery;

    @Autowired
    private NameRepository namequery;

    private boolean guest(
        HttpServletRequest request
    ) {
        if (request == null)
        {
            return true;
        }

        for(Cookie c : request.getCookies())
        {
            if(c.getName().equals("session_id")) {
                return !c.getValue().equals("qwerty12345");
            }
        }

        return true;
    }

    private boolean isAjax(
        HttpServletRequest request
    ) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    private String redirect(
        String url,
        String content,
        HttpServletResponse response,
        HttpServletRequest request
    ) {
        if(!isAjax(request)) {
            response.setStatus(302);
            response.addHeader("Location", url);
        }
        return content;
    }

    @RequestMapping("/logout")
    @ResponseBody
    public String logout(
        HttpServletResponse response,
        HttpServletRequest request
    )
    {
        response.addCookie(new Cookie("session_id", ""));
        return redirect("/", "{\"ok\": true}", response, request);
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(
        @RequestParam(defaultValue = "") String username,
        @RequestParam(defaultValue = "") String password,
        HttpServletResponse response,
        HttpServletRequest request
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
            response.setStatus(403);
            if (isAjax(request))
            {
                return "{\"ok\": false}";
            }
            return "403 Permission Denied";
        }

        response.addCookie(new Cookie("session_id", "qwerty12345"));
        return redirect("/", "{\"ok\": true}", response, request);
    }

    @RequestMapping("/list")
    @ResponseBody
    public DTResponse<Name> list(
        @RequestParam(defaultValue = "0") int draw,
        @RequestParam(defaultValue = "0") int start,
        @RequestParam(defaultValue = "0") int length,
        @RequestParam(name="search[value]", defaultValue = "") String search,
        @RequestParam(name="columns[0][search][value]", defaultValue = "") String firstname,
        @RequestParam(name="columns[1][search][value]", defaultValue = "") String lastname,
        HttpServletResponse response,
        HttpServletRequest request
    )
    {
        DTResponse<Name> json = new DTResponse<>();

        //<editor-fold desc="Auth?">
        if (guest(request))
        {
            response.setStatus(403);
            json.ok = false;
            return json;
        }
        //</editor-fold>

        json.draw = draw;
        json.recordsTotal = (int)namequery.count();
        if (length > 0)
        {
            Page<Name> namePage = namequery.maxSearch(
                PageRequest.of(start / length, length),
                "%" + search.replace(" ", "%") + "%",
                "%" + firstname.replace(" ", "%") + "%",
                "%" + lastname.replace(" ", "%") + "%"
            );
            json.recordsFiltered = (int) namePage.getTotalElements();
            json.data = namePage.getContent();
        }
        else
        {
            List<Name> names = namequery.maxSearch(
                    "%" + search.replace(" ", "%") + "%",
                    "%" + firstname.replace(" ", "%") + "%",
                    "%" + lastname.replace(" ", "%") + "%"
            );
            json.recordsFiltered = names.size();
            json.data = names;
        }
        return json;
    }


    @RequestMapping("/name/{id}")
    //public String name(
    public ModelAndView name(
        @PathVariable("id") Integer id
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
