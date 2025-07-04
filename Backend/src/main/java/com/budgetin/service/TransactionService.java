package com.budgetin.service;

import com.budgetin.model.Transaction;
import com.budgetin.web.dto.CreateTransactionDto;
import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactionsForUser(String email);
    Transaction addTransaction(CreateTransactionDto transactionDto, String email);
    void deleteTransaction(Long transactionId, String email);
    Transaction updateTransaction(Long transactionId, Transaction transaction, String email);
    Transaction getTransactionById(Long transactionId, String email);
}
