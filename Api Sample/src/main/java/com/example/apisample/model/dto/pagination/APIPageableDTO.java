package com.example.apisample.model.dto.pagination;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@Data
public class APIPageableDTO implements Serializable {
    int pageNumber;
    int pageSize;
    int offset;
    int numberOfElements;
    long totalElements;
    int totalPages;
    boolean sorted;
    boolean first;
    boolean last;
    boolean empty;

    public <T> APIPageableDTO(Page<T> page) {
        Pageable pageable = page.getPageable();
        setPageNumber(pageable.getPageNumber());
        setPageSize(pageable.getPageSize());
        setTotalElements(page.getTotalElements());
        setTotalPages(page.getTotalPages());
        setNumberOfElements(page.getNumberOfElements());
        setSorted(page.getSort().isSorted());
        setFirst(page.isFirst());
        setLast(page.isLast());
        setEmpty(page.isEmpty());
    }


}
