import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
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

    @RequestMapping("/list")
    @ResponseBody
    public DTResponse<Row> list(@RequestParam(defaultValue = "0") int draw, @RequestParam(name="search[value]", defaultValue = "") String search)
    {
        ArrayList<Row> data = new ArrayList<>();
        data.add(new Row("Anna", "Andersson"));
        data.add(new Row("Bertil", "Bengtsson"));
        data.add(new Row("Carl", "Carlberg"));
        data.add(new Row("David", "Dahlman"));

        DTResponse<Row> json = new DTResponse<>();
        json.draw = draw;
        for (Row r : data)
        {
            if (search == "" || r.a.contains(search) || r.b.contains(search))
            {
                json.data.add(r);
            }
        }

        json.recordsTotal = data.size();
        json.recordsFiltered = json.data.size();
        return json;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(new Class[]{Index.class}, args);
    }
}
