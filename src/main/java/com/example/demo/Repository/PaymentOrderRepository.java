package com.example.demo.Repository;

import com.example.demo.Model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Integer> {

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.orderStatus = 'CREATED'")
    List<PaymentOrder> findCreatedOrders();

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.orderStatus = 'REJECTED'")
    List<PaymentOrder> findRejectedOrders();

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.orderStatus = 'OUTSTANDING'")
    List<PaymentOrder> findOutstandingOrders();

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.orderStatus = 'POSTPONED'")
    List<PaymentOrder> findPostponedOrders();

    @Query("SELECT pOrd FROM PaymentOrder pOrd")
    List<PaymentOrder> findAllPaymentOrders();

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.creationDateTime >= :fromDate")
    List<PaymentOrder> findPaymentOrdersFromDate(@Param("fromDate") Date fromDate);

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.creationDateTime < :toDate")
    List<PaymentOrder> findPaymentOrdersToDate(@Param("toDate") Date toDate);

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.creationDateTime < :toDate AND pOrd.creationDateTime >= :fromDate")
    List<PaymentOrder> findPaymentOrdersFromToDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.orderType = 'CREDIT'")
    List<PaymentOrder> findCreditOrders();

    @Query("SELECT pOrd FROM PaymentOrder pOrd where pOrd.orderType = 'DEBIT'")
    List<PaymentOrder> findDebitOrders();
}
