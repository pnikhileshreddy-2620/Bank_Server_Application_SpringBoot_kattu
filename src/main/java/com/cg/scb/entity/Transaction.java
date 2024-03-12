package com.cg.scb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "Transactions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    private int transactionId;

    @ManyToOne
    @JoinColumn(name="account_number",referencedColumnName = "accountNumber")
    private Account account;
    @Column
    private LocalDate transactionDate;
    @Column
    private String transactionType;
    @Column
    private double amount;

    @Override
    public String toString() {
        return
                "transactionId=" + transactionId +
                ", transactionDate=" + transactionDate +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                '}';
    }
}
