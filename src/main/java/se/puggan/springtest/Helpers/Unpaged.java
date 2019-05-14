package se.puggan.springtest.Helpers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Unpaged implements Pageable
{
    private Sort sort;

    public Unpaged() {
        sort = Sort.unsorted();
    }
    public Unpaged(Sort sort) {
        this.sort = sort;
    }

    public boolean isPaged() {
        return false;
    }

    public Pageable previousOrFirst() {
        return this;
    }

    public Pageable next() {
        return this;
    }

    public boolean hasPrevious() {
        return false;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public int getPageSize() {
        throw new UnsupportedOperationException();
    }

    public int getPageNumber() {
        throw new UnsupportedOperationException();
    }

    public long getOffset() {
        throw new UnsupportedOperationException();
    }

    public Pageable first() {
        return this;
    }
}
