package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest {
    private int pageNumber = 0;
    private int pageSize = 5;
    private String sortBy = "id";
    private String direction = "asc";
}