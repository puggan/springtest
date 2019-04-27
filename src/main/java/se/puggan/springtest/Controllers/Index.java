package se.puggan.springtest.Controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.puggan.springtest.Json.DTResponse;
import se.puggan.springtest.Models.Row;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Controller
@EnableAutoConfiguration
public class Index
{
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
        if (!username.equals("foo") || !password.equals("bar"))
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
    public DTResponse<Row> list(
        @RequestParam(defaultValue = "0") int draw,
        @RequestParam(name="search[value]", defaultValue = "") String search,
        HttpServletResponse response,
        HttpServletRequest request
    )
    {
        DTResponse<Row> json = new DTResponse<>();

        //<editor-fold desc="Auth?">
        if (guest(request))
        {
            response.setStatus(403);
            json.ok = false;
            return json;
        }
        //</editor-fold>

        //<editor-fold desc="Exemple Data">
        ArrayList<Row> data = new ArrayList<>();
        data.add(new Row("Anna", "Andersson"));
        data.add(new Row("Bertil", "Bengtsson"));
        data.add(new Row("Carl", "Carlberg"));
        data.add(new Row("David", "Dahlman"));
        //</editor-fold>

        json.draw = draw;

        //<editor-fold desc="Filter">
        for (Row r : data)
        {
            if (search.equals("") || r.a.contains(search) || r.b.contains(search))
            {
                json.data.add(r);
            }
        }
        //</editor-fold>

        //<editor-fold desc="Count">
        json.recordsTotal = data.size();
        json.recordsFiltered = json.data.size();
        //</editor-fold>

        return json;
    }
}
