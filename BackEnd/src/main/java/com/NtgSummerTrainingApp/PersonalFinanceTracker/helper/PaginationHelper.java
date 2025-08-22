package com.NtgSummerTrainingApp.PersonalFinanceTracker.helper;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationHelper {
    public static Pageable getPageable(PaginationRequest paginationReq){
        Sort sort = paginationReq.getDirection().equalsIgnoreCase("asc")?
                Sort.by(paginationReq.getSortBy()).ascending() : Sort.by(paginationReq.getSortBy()).descending();
        return PageRequest.of(paginationReq.getPageNumber(),paginationReq.getPageSize(),sort);
    }
}
