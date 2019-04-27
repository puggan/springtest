import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@EnableAutoConfiguration
public class Index
{
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
