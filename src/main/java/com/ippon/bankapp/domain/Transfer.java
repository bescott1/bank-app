package com.ippon.bankapp.domain;

import java.math.BigDecimal;

public class Transfer {

    private BigDecimal amount;
    private Integer destinationId;

    public Transfer() {}

    public Transfer(BigDecimal bigDecimal, int destinationId) {
        this.amount = bigDecimal;
        this.destinationId = destinationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

}
