package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.DTO.TransactionDTO;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.TransactionServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionServices transactionService;

    @PostMapping("/create")
    public ResponseEntity <TransactionDTO> addTransaction(@RequestBody TransactionDTO dto){
        return new ResponseEntity<>(transactionService.addTransaction(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id) {
        TransactionDTO dto = transactionService.getTransactionById(id);
        return ResponseEntity.ok(dto);
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

}
