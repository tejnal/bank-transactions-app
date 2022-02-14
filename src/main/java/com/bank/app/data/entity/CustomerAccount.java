package com.bank.app.data.entity;


import com.bank.app.common.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer_account")
@Getter
@Setter
@ToString
public class CustomerAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_account_id")
    private long id;


    //Lazy -- it fetch when it needed
    //Eager -- fetch immediately
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private  User user;

    //cascade : it transfer operation on one object
    //Reference type is the mappedBy
    @OneToMany(
            mappedBy = "customerAccount",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Withdrawals> withdrawals;

    @OneToMany(
            mappedBy = "customerAccount",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Deposits> deposits;

    @Column(name = "customer_account_balance", nullable = false)
    private Double customerAccountBalance;

    //it is attribute for enum mapping in hibernate
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    public CustomerAccount() {

    }

    public CustomerAccount(User user, Double customerAccountBalance, Currency currency) {
        this.user = user;
        this.customerAccountBalance = customerAccountBalance;
        this.currency = currency;
    }

}
