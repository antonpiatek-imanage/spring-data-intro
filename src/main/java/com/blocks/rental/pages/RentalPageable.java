package com.blocks.rental.pages;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class RentalPageable implements Pageable {

    private int pageNumber;
    private int pageSize;
    private Sort sort;

    public RentalPageable(int pageNumber, int pageSize, String sort, boolean ascending){
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = Sort.by(sort);
        if (ascending){
            this.sort = this.sort.ascending();
        }
        else{
            this.sort = this.sort.descending();
        }
    }



    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getOffset() {
        return 0;
    }


    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }


}
