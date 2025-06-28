package com.budgetin.controller;

import com.budgetin.model.Transaction;
import com.budgetin.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() throws IOException {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) throws IOException {
        Transaction saved = transactionService.addTransaction(transaction);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) throws IOException {
        boolean deleted = transactionService.deleteTransaction(id);
        if (deleted) {
            return ResponseEntity.ok("Deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Transaction not found");
        }
    }
}
