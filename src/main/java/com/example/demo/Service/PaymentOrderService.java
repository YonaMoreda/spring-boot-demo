package com.example.demo.Service;

import com.example.demo.util.OrderStatus;
import com.example.demo.Model.PaymentOrder;
import com.example.demo.util.OrderType;
import org.springframework.stereotype.Service;
import com.example.demo.Repository.PaymentOrderRepository;

import java.util.List;
import java.util.Optional;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.text.ParseException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

/**
 * PaymentOrderService to have access functionality via payment order repository
 */
@Service
public class PaymentOrderService {

    private final PaymentOrderRepository repository;

    /**
     * Create payment order service with repository
     * @param repository JpaRepository to provide access functions to payment order
     */
    public PaymentOrderService(PaymentOrderRepository repository) {
        this.repository = repository;
    }

    /**
     * gets payment order repository
     * @return payment order repository
     */
    public PaymentOrderRepository getRepository() {
        return repository;
    }

    /**
     * Inserts a Payment order to database (repository)
     * @param paymentOrder payment order to be inserted
     * @return inserted payment order if successful, empty when not successful
     */
    public Optional<PaymentOrder> insertWithQuery(PaymentOrder paymentOrder) {
        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres_demo",
                    "postgres", "admin");
            String table = "payment_order(originator_account, creation_date_time, expiry_date_time, order_type, " +
                    "order_status, instructed_amount)";
            PreparedStatement preparedStatement = con.prepareStatement("insert into " + table +
                    " values (?, ?, ?, ?::\"order_type\", ?::\"status\", ?);");
            setPreparedStatementFields(paymentOrder, preparedStatement);
            preparedStatement.executeUpdate();
            con.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(paymentOrder);
    }

    /**
     * Updates a payment order to database (repository)
     * @param paymentOrder payment order to be updated
     * @param id identifying variable for payment order to be updated
     * @return updated payment order if successful, empty when not successful
     */
    public Optional<PaymentOrder> updateWithQuery(PaymentOrder paymentOrder, Integer id) {
        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres_demo", "postgres", "admin");
            PreparedStatement preparedStatement = con.prepareStatement("update payment_order set " +
                    "originator_account = ?," +
                    "creation_date_time = ?," +
                    "expiry_date_time =  ?," +
                    "order_type = ?::\"order_type\"," +
                    "order_status = ?::\"status\"," +
                    "instructed_amount = ? " +
                    "where id = ?;");
            setPreparedStatementFields(paymentOrder, preparedStatement);
            preparedStatement.setInt(7, id);
            preparedStatement.executeUpdate();
            con.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(paymentOrder);
    }

    /**
     * Helper function to set payment order fields when constructing database query
     * @param paymentOrder payment order object for which query is constructed
     * @param preparedStatement the query statement prepared using paymentOrder
     * @throws SQLException SQL exception when constructing statement
     */
    private void setPreparedStatementFields(PaymentOrder paymentOrder, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, paymentOrder.getOriginatorAccount());
        preparedStatement.setDate(2, paymentOrder.getCreationDateTime());
        preparedStatement.setDate(3, paymentOrder.getExpiryDateTime());
        preparedStatement.setString(4, paymentOrder.getOrderType().name());
        preparedStatement.setString(5, paymentOrder.getStatus().name());
        preparedStatement.setBigDecimal(6, paymentOrder.getInstructedAmount());
    }

    /**
     * Requests payment orders via From and To Date parameters on the creation date of payment orders
     * @param fromDate start date for the list of payment orders after this date
     * @param toDate end date for the list of payment orders before this date
     * @return List of payment orders after fromDate and before toDate
     * @throws ParseException fromDate and toDate are correctly parsed to java.util.date
     */
    public Optional<List<PaymentOrder>> requestWithDateParameters(String fromDate, String toDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        if (fromDate != null && toDate != null) {
            return Optional.of(repository.findPaymentOrdersFromToDate(formatter.parse(fromDate), formatter.parse(toDate)));
        } else if (fromDate != null) {
            return Optional.of(repository.findPaymentOrdersFromDate(formatter.parse(fromDate)));
        } else if (toDate != null) {
            return Optional.of(repository.findPaymentOrdersToDate(formatter.parse(toDate)));
        }
        return Optional.of(repository.findAllPaymentOrders());
    }

    /**
     * Finds a list of payment orders by their order status field
     * @param orderStatus category field for filtering/creating a list of payment orders
     * @return filtered list of payment orders by Order Status
     */
    public Optional<List<PaymentOrder>> findOrdersByOrderStatus(OrderStatus orderStatus) {
        switch (orderStatus) {
            case CREATED:
                return Optional.of(repository.findCreatedOrders());
            case REJECTED:
                return Optional.of(repository.findRejectedOrders());
            case POSTPONED:
                return Optional.of(repository.findPostponedOrders());
            case OUTSTANDING:
                return Optional.of(repository.findOutstandingOrders());
        }
        return Optional.empty();
    }

    /**
     * Finds a list of payment orders by their order type field
     * @param orderType category field for filtering/creating a list of payment orders
     * @return filtered list of payment orders by Order Type
     */
    public Optional<List<PaymentOrder>> findOrdersByOrderType(OrderType orderType) {
        switch (orderType) {
            case CREDIT:
                return Optional.of(repository.findCreditOrders());
            case DEBIT:
                return Optional.of(repository.findDebitOrders());
        }
        return Optional.empty();
    }
}
