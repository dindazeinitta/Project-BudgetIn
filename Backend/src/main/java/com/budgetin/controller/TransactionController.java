package com.budgetin.controller;

import com.budgetin.model.Transaction;
import com.budgetin.service.TransactionService;
import com.budgetin.web.dto.CreateTransactionDto;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(Principal principal) {
        // Mengambil transaksi hanya untuk pengguna yang sedang login
        List<Transaction> transactions = transactionService.getAllTransactionsForUser(principal.getName());
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id, Principal principal) {
        Transaction transaction = transactionService.getTransactionById(id, principal.getName());
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@Valid @RequestBody CreateTransactionDto transactionDto, Principal principal) {
        Transaction saved = transactionService.addTransaction(transactionDto, principal.getName());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id, Principal principal) {
        transactionService.deleteTransaction(id, principal.getName());
        return ResponseEntity.noContent().build(); // Mengembalikan status 204 No Content, standar untuk delete
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction, Principal principal) {
        Transaction updated = transactionService.updateTransaction(id, transaction, principal.getName());
        return ResponseEntity.ok(updated);
    }
}
