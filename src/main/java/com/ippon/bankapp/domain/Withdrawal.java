package com.ippon.bankapp.domain;

import java.math.BigDecimal;

public class Withdrawal {
    private BigDecimal amount;

    public Withdrawal() {}

    public Withdrawal(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
