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


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<?> index() {
        PaymentOrder pOrd = new PaymentOrder();
        pOrd.add(linkTo(methodOn(MainController.class).index()).withSelfRel());
        pOrd.add(linkTo(methodOn(MainController.class).findAllPaymentOrders(null, null)).withRel("payment_orders"));
        addHyperMediaLinks(pOrd);

        return ResponseEntity.ok(pOrd);
    }

    /**
     * Retrieves all the payment orders
     * @param fromDate date filter for creation date of payment order
     * @param toDate date filter for creation date of payment order
     * @return list of payment orders
     */
    @GetMapping("/payment_orders")
    public ResponseEntity<?> findAllPaymentOrders(
            @RequestParam(value = "from", required = false) String fromDate,
            @RequestParam(value = "to", required = false) String toDate) {
        try {
            Optional<List<PaymentOrder>> paymentOrders = service.requestWithDateParameters(fromDate, toDate);
            if(paymentOrders.isPresent()) {
                for (PaymentOrder pOrd : paymentOrders.get()){
                    pOrd.add(linkTo(methodOn(MainController.class).findAllPaymentOrders(fromDate, toDate)).withSelfRel());
                    pOrd.add(linkTo(methodOn(MainController.class).index()).withRel("index"));
                    addHyperMediaLinks(pOrd);
                }
            }
            return ResponseEntity.ok(paymentOrders);
        } catch (ParseException e) {
            e.printStackTrace(); //todo:: add logging
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
        }
    }

    /**
     * Helper function to add Hyper media links
     * @param pOrd Response PaymentOrder object to which Hyper media links are added
     */
    private void addHyperMediaLinks(PaymentOrder pOrd) {
        pOrd.add(linkTo(methodOn(MainController.class).createPaymentOrder(null)).withRel("post_payment_order"));
        pOrd.add(linkTo(methodOn(MainController.class).createUpdatePaymentOrder(null, null)).withRel("put_payment_order"));
        pOrd.add(linkTo(methodOn(MainController.class).deletePaymentOrder(null)).withRel("delete_payment_order"));

        pOrd.add(linkTo(methodOn(MainController.class).findPaymentOrderById(null)).withRel("find_by_id"));
        pOrd.add(linkTo(methodOn(MainController.class).findAllCreatedOrders()).withRel("created"));
        pOrd.add(linkTo(methodOn(MainController.class).findAllRejectedOrders()).withRel("rejected"));
        pOrd.add(linkTo(methodOn(MainController.class).findAllPostponedOrders()).withRel("postponed"));
        pOrd.add(linkTo(methodOn(MainController.class).findAllOutstandingOrders()).withRel("outstanding"));
        pOrd.add(linkTo(methodOn(MainController.class).findAllCreditOrders()).withRel("credit"));
        pOrd.add(linkTo(methodOn(MainController.class).findAllDebitOrders()).withRel("debit"));
    }

    /**
     * Create an entry for payment order
     * @param newPaymentOrder New payment order to be inserted
     * @return inserted payment order or empty if unsuccessful
     */
    @PostMapping("/payment_orders")
    public ResponseEntity<?> createPaymentOrder(@RequestBody PaymentOrder newPaymentOrder) {
        if (newPaymentOrder.isValidated()) {
            Optional<PaymentOrder> pOrd =  service.insertWithQuery(newPaymentOrder);
            pOrd.get().add(linkTo(methodOn(MainController.class).createPaymentOrder(newPaymentOrder)).withSelfRel());
            return ResponseEntity.ok(pOrd);
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
    public ResponseEntity<?> createUpdatePaymentOrder(@RequestBody PaymentOrder updatedPaymentOrder,
                                                                           @PathVariable String id) {
        if (updatedPaymentOrder.isValidated()) {
            if (service.getRepository().existsById(Integer.parseInt(id))) {
                //updating existing entry
                Optional<PaymentOrder> pOrd = service.updateWithQuery(updatedPaymentOrder, Integer.parseInt(id));
                pOrd.get().add(linkTo(methodOn(MainController.class).createUpdatePaymentOrder(updatedPaymentOrder, id)).withSelfRel());
                return ResponseEntity.ok(pOrd);
            } else {
                //creating a new entry
                Optional<PaymentOrder> pOrd = service.insertWithQuery(updatedPaymentOrder);
                pOrd.get().add(linkTo(methodOn(MainController.class).createUpdatePaymentOrder(updatedPaymentOrder, id)).withSelfRel());
                return ResponseEntity.ok(pOrd);
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
    public ResponseEntity<?> deletePaymentOrder(@PathVariable String id) {
        Optional<PaymentOrder> paymentOrder = service.getRepository().findById(Integer.parseInt(id));
        if (paymentOrder.isPresent()) {
            service.getRepository().delete(paymentOrder.get());
            paymentOrder.get().add(linkTo(methodOn(MainController.class).findAllPaymentOrders(null, null)).withRel("payment_orders"));
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
    public ResponseEntity<?> findPaymentOrderById(@PathVariable String id) {
        Optional<PaymentOrder> paymentOrderOptional = service.getRepository().findById(Integer.parseInt(id));
        if(paymentOrderOptional.isPresent()) {
            PaymentOrder paymentOrder = paymentOrderOptional.get();
            paymentOrder.add(linkTo(methodOn(MainController.class).findPaymentOrderById(id)).withSelfRel());
            paymentOrder.add(linkTo(methodOn(MainController.class).deletePaymentOrder(id)).withRel("delete"));
            paymentOrder.add(linkTo(methodOn(MainController.class).findAllPaymentOrders(null, null)).withRel("payment_orders"));
            return ResponseEntity.ok(paymentOrder);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    /**
     * Gets all the created payment orders
     * @return list of created payment orders
     */
    @GetMapping("/payment_orders/created")
    public ResponseEntity<?> findAllCreatedOrders() {
        Optional<List<PaymentOrder>> paymentOrders = service.findOrdersByOrderStatus(OrderStatus.CREATED);
        if(paymentOrders.isPresent()) {
            for(PaymentOrder pOrd : paymentOrders.get()) {
                pOrd.add(linkTo(methodOn(MainController.class).findAllCreatedOrders()).withSelfRel());
                pOrd.add(linkTo(methodOn(MainController.class).findAllPaymentOrders(null, null)).withRel("payment_orders"));
            }
            return ResponseEntity.ok(paymentOrders);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    /**
     * Gets all the rejected payment orders
     * @return list of rejected payment orders
     */
    @GetMapping("/payment_orders/rejected")
    public ResponseEntity<?> findAllRejectedOrders() {
        Optional<List<PaymentOrder>> paymentOrders = service.findOrdersByOrderStatus(OrderStatus.REJECTED);
        if(paymentOrders.isPresent()) {
            for(PaymentOrder pOrd : paymentOrders.get()) {
                pOrd.add(linkTo(methodOn(MainController.class).findAllRejectedOrders()).withSelfRel());
                pOrd.add(linkTo(methodOn(MainController.class).findAllPaymentOrders(null, null)).withRel("payment_orders"));
            }
            return ResponseEntity.ok(paymentOrders);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    /**
     * Gets all the postponed payment orders
     * @return list of postponed payment orders
     */
    @GetMapping("/payment_orders/postponed")
    public ResponseEntity<?> findAllPostponedOrders() {
        Optional<List<PaymentOrder>> paymentOrders = service.findOrdersByOrderStatus(OrderStatus.POSTPONED);
        if(paymentOrders.isPresent()) {
            for(PaymentOrder pOrd : paymentOrders.get()) {
                pOrd.add(linkTo(methodOn(MainController.class).findAllPostponedOrders()).withSelfRel());
                pOrd.add(linkTo(methodOn(MainController.class).findAllPaymentOrders(null, null)).withRel("payment_orders"));
            }
            return ResponseEntity.ok(paymentOrders);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    /**
     * Gets all the outstanding payment orders
     * @return list of outstanding payment orders
     */
    @GetMapping("/payment_orders/outstanding")
    public ResponseEntity<?> findAllOutstandingOrders() {
        Optional<List<PaymentOrder>> paymentOrders = service.findOrdersByOrderStatus(OrderStatus.OUTSTANDING);
        if(paymentOrders.isPresent()) {
            for(PaymentOrder pOrd : paymentOrders.get()) {
                pOrd.add(linkTo(methodOn(MainController.class).findAllOutstandingOrders()).withSelfRel());
                pOrd.add(linkTo(methodOn(MainController.class).findAllPaymentOrders(null, null)).withRel("payment_orders"));
            }
            return ResponseEntity.ok(paymentOrders);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    /**
     * Gets all the credit payment orders
     * @return list of credit payment orders
     */
    @GetMapping("/payment_orders/credit")
    public ResponseEntity<?> findAllCreditOrders() {
        Optional<List<PaymentOrder>> paymentOrders = service.findOrdersByOrderType(OrderType.CREDIT);
        if(paymentOrders.isPresent()) {
            for(PaymentOrder pOrd : paymentOrders.get()) {
                pOrd.add(linkTo(methodOn(MainController.class).findAllCreditOrders()).withSelfRel());
                pOrd.add(linkTo(methodOn(MainController.class).findAllPaymentOrders(null, null)).withRel("payment_orders"));
            }
            return ResponseEntity.ok(paymentOrders);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }

    /**
     * Gets all the debit payment orders
     * @return list of debit payment orders
     */
    @GetMapping("/payment_orders/debit")
    public ResponseEntity<?> findAllDebitOrders() {
        Optional<List<PaymentOrder>> paymentOrders = service.findOrdersByOrderType(OrderType.DEBIT);
        if(paymentOrders.isPresent()) {
            for(PaymentOrder pOrd : paymentOrders.get()) {
                pOrd.add(linkTo(methodOn(MainController.class).findAllDebitOrders()).withSelfRel());
                pOrd.add(linkTo(methodOn(MainController.class).findAllPaymentOrders(null, null)).withRel("payment_orders"));
            }
            return ResponseEntity.ok(paymentOrders);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
    }
}
