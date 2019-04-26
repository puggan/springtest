import java.util.ArrayList;
import java.util.List;

public class DTResponse<T>
{
    public int draw;
    public int recordsTotal;
    public int recordsFiltered;
    public List<T> data;

    public DTResponse() {
        data = new ArrayList<T>();
    }
}
