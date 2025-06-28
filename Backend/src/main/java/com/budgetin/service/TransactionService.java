package com.budgetin.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.budgetin.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionService {

    private final String TRANSACTION_FILE = "src/main/java/com/budgetin/data/transaction.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Transaction> getAllTransactions() throws IOException {
        return readData().getTransactions();
    }

    public Transaction addTransaction(Transaction transaction) throws IOException {
        TransactionData data = readData();

        transaction.setId(generateNextId(data.getTransactions()));
        data.getTransactions().add(transaction);
        writeData(data);
        return transaction;
    }

    public boolean deleteTransaction(Long id) throws IOException {
        TransactionData data = readData();
        boolean removed = data.getTransactions().removeIf(t -> t.getId().equals(id));
        writeData(data);
        return removed;
    }

    private TransactionData readData() throws IOException {
        File file = new File(TRANSACTION_FILE);
        if (!file.exists()) return new TransactionData(new ArrayList<>());
        return objectMapper.readValue(file, TransactionData.class);
    }

    private void writeData(TransactionData data) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(TRANSACTION_FILE), data);
    }

    private Long generateNextId(List<Transaction> transactions) {
        return transactions.isEmpty() ? 1L :
            transactions.stream().mapToLong(Transaction::getId).max().getAsLong() + 1;
    }

    // helper inner class untuk representasi file JSON
    private static class TransactionData {
        private List<Transaction> transactions;

        public TransactionData() {
            this.transactions = new ArrayList<>();
        }

        public TransactionData(List<Transaction> transactions) {
            this.transactions = transactions;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }

        public void setTransactions(List<Transaction> transactions) {
            this.transactions = transactions;
        }
    }
}
