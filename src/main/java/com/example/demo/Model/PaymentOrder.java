package com.example.demo.Model;

import com.example.demo.util.OrderStatus;
import com.example.demo.util.OrderType;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "payment_order")
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "originator_account", nullable = false)
    private String originatorAccount;

    @Column(name = "creation_date_time", nullable = false)
    private Date creationDateTime;

    @Column(name = "expiry_date_time", nullable = false)
    private Date expiryDateTime;

    @Column(name = "order_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "instructed_amount", nullable = false)
    private BigDecimal instructedAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginatorAccount() {
        return originatorAccount;
    }

    public void setOriginatorAccount(String originatorAccount) {
        this.originatorAccount = originatorAccount;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public Date getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(Date expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getInstructedAmount() {
        return instructedAmount;
    }

    public void setInstructedAmount(BigDecimal instructedAmount) {
        this.instructedAmount = instructedAmount;
    }

    public boolean isValidated() {
        return originatorAccount != null && creationDateTime != null && expiryDateTime != null && orderType != null && status != null && instructedAmount != null;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PaymentOrder {\n");
        for(Field field : getClass().getDeclaredFields()) {
            try {
                stringBuilder.append("\t" + field.getName() + ": " + field.get(this) + ",\n");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();

    }
}
