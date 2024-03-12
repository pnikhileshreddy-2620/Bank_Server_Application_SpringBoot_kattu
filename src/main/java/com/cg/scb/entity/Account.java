package com.cg.scb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Accounts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    private int accountNumber;

    @OneToOne
    @JoinColumn(name="customer_id",referencedColumnName = "customerId")
    private Customer customer;
    @Column
    private double currentBalance;
    @Column
    private String accountType;

    @Override
    public String toString() {
        return
                "accountNumber=" + accountNumber +
                ", currentBalance=" + currentBalance +
                ", accountType='" + accountType;
    }
}
