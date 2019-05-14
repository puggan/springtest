package se.puggan.springtest.Json;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class DTResponse<T>
{
    public boolean ok = true;
    public int draw;
    public long recordsTotal;
    public long recordsFiltered;
    public Iterable<T> data;

    public DTResponse() {
        data = new ArrayList<>();
    }

    public void setList(List<T> list)
    {
        data = list;
        recordsFiltered = list.size();
    }

    public void setPage(Page<T> page)
    {
        data = page.getContent();
        recordsFiltered = page.getTotalElements();
    }
}
