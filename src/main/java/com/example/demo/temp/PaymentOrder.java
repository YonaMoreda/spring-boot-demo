package com.example.demo.temp;

import com.example.demo.util.OrderStatus;
import com.example.demo.util.OrderType;

import java.util.Date;

public class PaymentOrder {
    private String originatorAccount;
    private Date creationDateTime;
    private Date expiryDateTime;
    private OrderType orderType;
    private OrderStatus status;
    private float instructedAmount;

    public PaymentOrder(String originatorAccount, Date creationDateTime, Date expiryDateTime, OrderType orderType,
                        OrderStatus status, float instructedAmount) {
        this.originatorAccount = originatorAccount;
        this.creationDateTime = creationDateTime;
        this.expiryDateTime = expiryDateTime;
        this.orderType = orderType;
        this.status = status;
        this.instructedAmount = instructedAmount;
    }


}
