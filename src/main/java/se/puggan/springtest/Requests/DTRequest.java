package se.puggan.springtest.Requests;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.context.annotation.RequestScope;
import se.puggan.springtest.Helpers.Unpaged;
import se.puggan.springtest.Json.DTResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestScope
@Component
public class DTRequest extends UserRequest
{
    private int draw;
    private int start;
    private int length;
    private int sortColumn;
    private String sortOrder;
    private String[] columns;

    public DTRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            String session_id
    ) {
        super(request, response, session_id);
        this.draw = param("draw", 0);
        this.start = param("start", 0);
        this.length = param("length", 0);
        this.sortColumn = param("order[0][column]", 0);
        this.sortOrder = param("order[0][dir]", "");
    }

    @Override
    public void validate(Errors errors)
    {
        super.validate(errors);
        if(draw < 0) errors.rejectValue("draw", "draw most be positive");
        if(start < 0) errors.rejectValue("start", "start most be positive");
        if(length < 0) errors.rejectValue("length", "length most be positive");
        if(sortColumn < 0) errors.rejectValue("sortColumn", "sortColumn most be positive");
    }

    public Pageable getPagable() {
        if (columns != null && columns.length > 0)
        {
            return getPagable(columns);
        }

        if (length > 0)
        {
            return PageRequest.of(start / length, length, Sort.unsorted());
        }

        return new Unpaged();
    }

    public Pageable getPagable(String[] columns) {
        Sort sort;
        Sort.Direction direction = Sort.Direction.ASC;
        if(sortOrder.equals("desc")) {
            direction = Sort.Direction.DESC;
        }

        if (columns.length < 1)
        {
            sort = Sort.unsorted();
        }
        else if (columns.length <= sortColumn)
        {
            sort = Sort.by(direction, columns[0]);
        }
        else
        {
            sort = Sort.by(direction, columns[sortColumn]);
        }

        if (length > 0)
        {
            return PageRequest.of(start / length, length, sort);
        }

        return new Unpaged(sort);
    }

    public <T> DTResponse<T> response()
    {
        DTResponse<T> r = new DTResponse<>();
        r.draw = draw;
        r.ok = !guest();
        return r;
    }

    public <T> DTResponse<T> response(long total)
    {
        DTResponse<T> r = new DTResponse<>();
        r.draw = draw;
        r.recordsTotal = total;
        r.ok = !guest();
        return r;
    }

    public String globalSearch() {
        return param("search[value]", "");
    }

    public String columnSearch(int columnIndex) {
        return param("columns[" + columnIndex + "][search][value]", "");
    }
}
