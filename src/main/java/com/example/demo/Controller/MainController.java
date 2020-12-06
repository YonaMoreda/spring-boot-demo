package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;
import java.text.ParseException;

import com.example.demo.Model.PaymentOrder;
import com.example.demo.util.OrderStatus;
import com.example.demo.util.OrderType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Service.PaymentOrderService;

/**
 * Main Controller class for handling HTTP requests
 */
@RestController
public class MainController {

    private final PaymentOrderService service;

    public MainController(PaymentOrderService service) {
        this.service = service;
    }

    /**
     * Index page
     * @return links to other resources
     */
    @GetMapping(value = "/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("{ \"links\": [" +
                "\"/payment_orders\"," +
                "\"/payment_orders/{id}\"," +
                "\"/payment_orders/created\"," +
                "\"/payment_orders/rejected\"," +
                "\"/payment_orders/postponed\"," +
                "\"/payment_orders/outstanding\"," +
                "\"/payment_orders/credit\"," +
                "\"/payment_orders/debit\"" +
                "]}");
    }

    /**
     * Retrieves all the payment orders
     * @param fromDate date filter for creation date of payment order
     * @param toDate date filter for creation date of payment order
     * @return list of payment orders
     */
    @GetMapping("/payment_orders")
    public ResponseEntity<Optional<List<PaymentOrder>>> findAllPaymentOrders(
            @RequestParam(value = "from", required = false) String fromDate,
            @RequestParam(value = "to", required = false) String toDate) {
        try {
            return ResponseEntity.ok(service.requestWithDateParameters(fromDate, toDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
        }
    }

    /**
     * Create an entry for payment order
     * @param newPaymentOrder New payment order to be inserted
     * @return inserted payment order or empty if unsuccessful
     */
    @PostMapping("/payment_orders")
    public ResponseEntity<Optional<PaymentOrder>> createPaymentOrder(@RequestBody PaymentOrder newPaymentOrder) {
        if (newPaymentOrder.isValidated()) {
            return ResponseEntity.ok(service.insertWithQuery(newPaymentOrder));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    /**
     * Update payment order entry or create a new entry if payment order is not pre-existing
     * @param updatedPaymentOrder payment order to be created/inserted or updated
     * @param id variable to identify payment order entry
     * @return inserted/updated payment order or empty if unsuccessful
     */
    @PutMapping("/payment_orders/{id}")
    public ResponseEntity<Optional<PaymentOrder>> createUpdatePaymentOrder(@RequestBody PaymentOrder updatedPaymentOrder,
                                                                           @PathVariable String id) {
        if (updatedPaymentOrder.isValidated()) {
            if (service.getRepository().existsById(Integer.parseInt(id))) {
                //updating existing entry
                return ResponseEntity.ok(service.updateWithQuery(updatedPaymentOrder, Integer.parseInt(id)));
            } else {
                //creating a new entry
                return ResponseEntity.ok(service.insertWithQuery(updatedPaymentOrder));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    /**
     * Delete payment order entry
     * @param id variable to identify payment order entry
     * @return deleted payment order
     */
    @DeleteMapping("/payment_orders/{id}")
    public ResponseEntity<Optional<PaymentOrder>> deletePaymentOrder(@PathVariable String id) {
        Optional<PaymentOrder> paymentOrder = service.getRepository().findById(Integer.parseInt(id));
        if (paymentOrder.isPresent()) {
            service.getRepository().delete(paymentOrder.get());
            return ResponseEntity.ok(paymentOrder);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    /**
     * Get payment order by id
     * @param id identifying variable for payment order
     * @return the payment order with the given id
     */
    @GetMapping("/payment_orders/{id}")
    public ResponseEntity<Optional<PaymentOrder>> findPaymentOrderById(@PathVariable String id) {
        Optional<PaymentOrder> paymentOrder = service.getRepository().findById(Integer.parseInt(id));
        if (paymentOrder.isPresent()) {
            return ResponseEntity.ok(paymentOrder);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    /**
     * Gets all the created payment orders
     * @return list of created payment orders
     */
    @GetMapping("/payment_orders/created")
    public List<PaymentOrder> findAllCreatedOrders() {
        return service.findOrdersByOrderStatus(OrderStatus.CREATED);
    }

    /**
     * Gets all the rejected payment orders
     * @return list of rejected payment orders
     */
    @GetMapping("/payment_orders/rejected")
    public List<PaymentOrder> findAllRejectedOrders() {
        return service.findOrdersByOrderStatus(OrderStatus.REJECTED);
    }

    /**
     * Gets all the postponed payment orders
     * @return list of postponed payment orders
     */
    @GetMapping("/payment_orders/postponed")
    public List<PaymentOrder> findAllPostponedOrders() {
        return service.findOrdersByOrderStatus(OrderStatus.POSTPONED);
    }

    /**
     * Gets all the outstanding payment orders
     * @return list of outstanding payment orders
     */
    @GetMapping("/payment_orders/outstanding")
    public List<PaymentOrder> findAllOutstandingOrders() {
        return service.findOrdersByOrderStatus(OrderStatus.OUTSTANDING);
    }

    /**
     * Gets all the credit payment orders
     * @return list of credit payment orders
     */
    @GetMapping("/payment_orders/credit")
    public List<PaymentOrder> findAllCreditOrders() {
        return service.findOrdersByOrderType(OrderType.CREDIT);
    }

    /**
     * Gets all the debit payment orders
     * @return list of debit payment orders
     */
    @GetMapping("/payment_orders/debit")
    public List<PaymentOrder> findAllDebitOrders() {
        return service.findOrdersByOrderType(OrderType.DEBIT);
    }
}
