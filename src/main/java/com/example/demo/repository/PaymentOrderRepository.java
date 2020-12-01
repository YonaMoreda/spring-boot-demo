package com.example.demo.repository;

import com.example.demo.Model.PaymentOrder;
import com.example.demo.util.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Id;
import java.util.List;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Integer> {

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.status = 'CREATED'")
    public List<PaymentOrder> findCreatedOrders();

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.status = 'REJECTED'")
    public List<PaymentOrder> findRejectedOrders();

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.status = 'OUTSTANDING'")
    public List<PaymentOrder> findOutstandingOrders();

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.status = 'POSTPONED'")
    public List<PaymentOrder> findPostponedOrders();


}
