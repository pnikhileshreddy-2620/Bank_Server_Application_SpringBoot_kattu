package com.cg.scb.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Customers")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer
{
    @Id
    private int customerId;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String password;

    @Override
    public String toString() {
        return
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", email='" + email;
    }
}
