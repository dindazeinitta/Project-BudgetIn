package com.budgetin.service;

import com.budgetin.model.Transaction;
import com.budgetin.model.User;
import com.budgetin.repository.TransactionRepository;
import com.budgetin.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Transaction> getAllTransactionsForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return transactionRepository.findByUser(user);
    }

    @Override
    @Transactional
    public Transaction addTransaction(Transaction transaction, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long transactionId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().equals(user)) {
            throw new SecurityException("User does not have permission to delete this transaction");
        }
        transactionRepository.delete(transaction);
    }

    @Override
    @Transactional
    public Transaction updateTransaction(Long transactionId, Transaction transactionDetails, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().equals(user)) {
            throw new SecurityException("User does not have permission to update this transaction");
        }

        transaction.setAmount(transactionDetails.getAmount());
        transaction.setTransactionDateTime(transactionDetails.getTransactionDateTime());
        transaction.setDescription(transactionDetails.getDescription());
        transaction.setType(transactionDetails.getType());
        transaction.setCategory(transactionDetails.getCategory());

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long transactionId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (!transaction.getUser().equals(user)) {
            throw new SecurityException("User does not have permission to access this transaction");
        }
        return transaction;
    }
}
