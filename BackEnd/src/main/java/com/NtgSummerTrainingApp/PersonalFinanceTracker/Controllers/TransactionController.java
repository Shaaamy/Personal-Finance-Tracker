package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;


import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.TransactionService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationRequest;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.TransactionDTO;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity <TransactionDTO> addTransaction(@RequestBody TransactionDTO dto){
        return new ResponseEntity<>(transactionService.addTransaction(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id) {
        TransactionDTO dto = transactionService.getTransactionById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public ResponseEntity<PaginationDto<TransactionDTO>> getAllTransactions(@ModelAttribute PaginationRequest paginationReq){
        return new ResponseEntity<>(transactionService.getAllTransactions(paginationReq) , HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsForUser(@PathVariable Long userId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByUser(userId);
        return ResponseEntity.ok(transactions); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK); // 204 No Content
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/filter")
    public ResponseEntity<List<TransactionDTO>> filterTransactions(
            @RequestParam Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) CategoryTypeEnum type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<TransactionDTO> transactions =
                transactionService.filterTransactions(userId, categoryId, type, startDate, endDate);

        return ResponseEntity.ok(transactions);
    }


}
