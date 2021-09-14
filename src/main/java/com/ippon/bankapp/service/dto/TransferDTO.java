package com.ippon.bankapp.service.dto;

import java.math.BigDecimal;

public class TransferDTO {

    private BigDecimal amount;
    private Integer destinationId;

    public TransferDTO() {}

    public TransferDTO(BigDecimal bigDecimal, int destinationId) {
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
