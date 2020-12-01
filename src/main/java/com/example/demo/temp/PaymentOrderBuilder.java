package com.example.demo.temp;

import com.example.demo.util.OrderStatus;
import com.example.demo.util.OrderType;

import java.util.Date;

public class PaymentOrderBuilder {
    private String originatorAccount;
    private Date creationDateTime;
    private Date expiryDateTime;
    private OrderType orderType;
    private OrderStatus status;
    private float instructedAmount;

    public PaymentOrderBuilder setOriginatorAccount(String originatorAccount) {
        this.originatorAccount = originatorAccount;
        return this;
    }

    public PaymentOrderBuilder setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
        return this;
    }

    public PaymentOrderBuilder setExpiryDateTime(Date expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
        return this;
    }

    public PaymentOrderBuilder setOrderType(OrderType orderType) {
        this.orderType = orderType;
        return this;
    }

    public PaymentOrderBuilder setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public PaymentOrderBuilder setInstructedAmount(float instructedAmount) {
        this.instructedAmount = instructedAmount;
        return this;
    }

    public PaymentOrder createPaymentOrder() {
        PaymentOrder paymentOrder = new PaymentOrder(originatorAccount, creationDateTime, expiryDateTime, orderType, status, instructedAmount);
        validate(paymentOrder);
        return paymentOrder;
    }

    private void validate(PaymentOrder paymentOrder) {
        //TODO:: validation work here
    }
}