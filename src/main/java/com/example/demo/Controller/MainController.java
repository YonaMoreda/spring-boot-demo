package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;
import java.text.ParseException;

import com.example.demo.Model.PaymentOrder;
import com.example.demo.util.OrderStatus;
import com.example.demo.util.OrderType;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Service.PaymentOrderService;

@RestController
public class MainController {

    private final PaymentOrderService service;

    public MainController(PaymentOrderService service) {
        this.service = service;
    }

    @GetMapping(value = "/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("X");
    }

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

    @PostMapping("/payment_orders")
    public ResponseEntity<Optional<PaymentOrder>> createPaymentOrder(@RequestBody PaymentOrder newPaymentOrder) {
        if (newPaymentOrder.isValidated()) {
            return ResponseEntity.ok(service.insertWithQuery(newPaymentOrder));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

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

    @DeleteMapping("/payment_orders/{id}")
    public ResponseEntity<Optional<PaymentOrder>> deletePaymentOrder(@PathVariable String id) {
        Optional<PaymentOrder> paymentOrder = service.getRepository().findById(Integer.parseInt(id));
        if (paymentOrder.isPresent()) {
            service.getRepository().delete(paymentOrder.get());
            return ResponseEntity.ok(paymentOrder);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    @GetMapping("/payment_orders/{id}")
    public ResponseEntity<Optional<PaymentOrder>> findPaymentOrderById(@PathVariable String id) {
        Optional<PaymentOrder> paymentOrder = service.getRepository().findById(Integer.parseInt(id));
        if (paymentOrder.isPresent()) {
            return ResponseEntity.ok(paymentOrder);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    @GetMapping("/payment_orders/created")
    public List<PaymentOrder> findAllCreatedOrders() {
        return service.findOrdersByOrderStatus(OrderStatus.CREATED);
    }

    @GetMapping("/payment_orders/rejected")
    public List<PaymentOrder> findAllRejectedOrders() {
        return service.findOrdersByOrderStatus(OrderStatus.REJECTED);
    }

    @GetMapping("/payment_orders/postponed")
    public List<PaymentOrder> findAllPostponedOrders() {
        return service.findOrdersByOrderStatus(OrderStatus.POSTPONED);
    }

    @GetMapping("/payment_orders/outstanding")
    public List<PaymentOrder> findAllOutstandingOrders() {
        return service.findOrdersByOrderStatus(OrderStatus.OUTSTANDING);
    }

    @GetMapping("/payment_orders/credit")
    public List<PaymentOrder> findAllCreditOrders() {
        return service.findOrdersByOrderType(OrderType.CREDIT);
    }

    @GetMapping("/payment_orders/debit")
    public List<PaymentOrder> findAllDebitOrders() {
        return service.findOrdersByOrderType(OrderType.DEBIT);
    }
}
