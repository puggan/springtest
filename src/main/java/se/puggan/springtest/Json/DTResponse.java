package se.puggan.springtest.Json;

import java.util.ArrayList;

public class DTResponse<T>
{
    public boolean ok = true;
    public int draw;
    public int recordsTotal;
    public int recordsFiltered;
    public Iterable<T> data;

    public DTResponse() {
        data = new ArrayList<>();
    }
}
