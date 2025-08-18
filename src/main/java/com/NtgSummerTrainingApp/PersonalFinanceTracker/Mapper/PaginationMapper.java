package com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationDto;
import org.springframework.data.domain.Page;

public class PaginationMapper {
    public static <T> PaginationDto<T> toPaginatedDto(Page<T> TPage){
        PaginationDto<T> dto = new PaginationDto<>();
        dto.setData(TPage.getContent());
        dto.setCurrentPage(TPage.getNumber());
        dto.setPageSize(TPage.getSize());
        dto.setTotalPages(TPage.getTotalPages());
        dto.setTotalElements(TPage.getTotalElements());
        return dto;
    }
}
