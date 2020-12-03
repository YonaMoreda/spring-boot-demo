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

@Service
public class PaymentOrderService {

    private final PaymentOrderRepository repository;

    public PaymentOrderService(PaymentOrderRepository repository) {
        this.repository = repository;
    }

    public PaymentOrderRepository getRepository() {
        return repository;
    }

    public Optional<PaymentOrder> insertWithQuery(PaymentOrder paymentOrder) { //TODO:: EXTRACT TO SERVICE
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

    private void setPreparedStatementFields(PaymentOrder paymentOrder, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, paymentOrder.getOriginatorAccount());
        preparedStatement.setDate(2, paymentOrder.getCreationDateTime());
        preparedStatement.setDate(3, paymentOrder.getExpiryDateTime());
        preparedStatement.setString(4, paymentOrder.getOrderType().name());
        preparedStatement.setString(5, paymentOrder.getStatus().name());
        preparedStatement.setBigDecimal(6, paymentOrder.getInstructedAmount());
    }

    public Optional<List<PaymentOrder>> requestWithDateParameters(String fromDate, String toDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //TODO:: ADD MORE FLEXIBLE FORMATS!
        if (fromDate != null && toDate != null) {
            return Optional.of(repository.findPaymentOrdersFromToDate(formatter.parse(fromDate), formatter.parse(toDate)));
        } else if (fromDate != null) {
            return Optional.of(repository.findPaymentOrdersFromDate(formatter.parse(fromDate)));
        } else if (toDate != null) {
            return Optional.of(repository.findPaymentOrdersToDate(formatter.parse(toDate)));
        }
        return Optional.of(repository.findAllPaymentOrders());
    }

    public List<PaymentOrder> findOrdersByOrderStatus(OrderStatus orderStatus) {
        switch (orderStatus) {
            case CREATED:
                return repository.findCreatedOrders();
            case REJECTED:
                return repository.findRejectedOrders();
            case POSTPONED:
                return repository.findPostponedOrders();
            case OUTSTANDING:
                return repository.findOutstandingOrders();
        }
        return new ArrayList<>();
    }

    public List<PaymentOrder> findOrdersByOrderType(OrderType orderType) {
        switch (orderType) {
            case CREDIT:
                return repository.findCreditOrders();
            case DEBIT:
                return repository.findDebitOrders();
        }
        return new ArrayList<>();
    }
}
