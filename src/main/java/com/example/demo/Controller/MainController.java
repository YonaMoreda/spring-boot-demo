package com.example.demo.Controller;

import com.example.demo.Model.PaymentOrder;
import com.example.demo.repository.PaymentOrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MainController {

    private final PaymentOrderRepository repository;

    public MainController(PaymentOrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index() {
        return "Homepage";
    }

    @GetMapping("/payment_orders")
    public List<PaymentOrder> findAllPaymentOrders() {
        return repository.findAll();
    }

    @PostMapping("/payment_orders")
    public PaymentOrder createPaymentOrder(@RequestBody PaymentOrder newPaymentOrder) {
        if (newPaymentOrder.isValidated()) {
            return repository.save(newPaymentOrder);
        }
        return null; //FIXME:: RETURNING NULL!
    }

    @PutMapping("/payment_orders/{id}")
    public Optional<PaymentOrder> updatePaymentOrder(@RequestBody PaymentOrder updatedPaymentOrder, @PathVariable String id) {
        if (updatedPaymentOrder.isValidated()) {
            return repository.findById(Integer.parseInt(id)).map(
                    paymentOrder -> {
                        paymentOrder.setOriginatorAccount(updatedPaymentOrder.getOriginatorAccount());
                        paymentOrder.setCreationDateTime(updatedPaymentOrder.getCreationDateTime());
                        paymentOrder.setExpiryDateTime(updatedPaymentOrder.getExpiryDateTime());
                        paymentOrder.setOrderType(updatedPaymentOrder.getOrderType());
                        paymentOrder.setStatus(updatedPaymentOrder.getStatus());
                        paymentOrder.setInstructedAmount(updatedPaymentOrder.getInstructedAmount());
                        return repository.save(paymentOrder);
                    }
            );
        }
        return Optional.empty();
    }

    @DeleteMapping("/payment_orders/{id}")
    public void deletePaymentOrder(@PathVariable String id) {
        repository.deleteById(Integer.parseInt(id));
    }

    @GetMapping("/payment_orders/{id}")
    public Optional<PaymentOrder> findPaymentOrderById(@PathVariable String id) {
        return repository.findById(Integer.parseInt(id));
    }

    @GetMapping("/payment_orders/created")
    public List<PaymentOrder> findAllCreatedOrders() {
        return repository.findCreatedOrders();
    }

    @GetMapping("/payment_orders/rejected")
    public List<PaymentOrder> findAllRejectedOrders() {
        return repository.findRejectedOrders();
    }

    @GetMapping("/payment_orders/postponed")
    public List<PaymentOrder> findAllPostponedOrders() {
        return repository.findPostponedOrders();
    }

    @GetMapping("/payment_orders/outstanding")
    public List<PaymentOrder> findAllOutstandingOrders() {
        return repository.findOutstandingOrders();
    }
}
