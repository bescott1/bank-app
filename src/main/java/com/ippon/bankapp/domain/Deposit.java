package com.ippon.bankapp.domain;

import java.math.BigDecimal;

public class Deposit {
    private BigDecimal amount;

    public Deposit() {}

    public Deposit(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
