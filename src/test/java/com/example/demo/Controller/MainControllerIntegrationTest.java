package com.example.demo.Controller;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void index() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/"))
                .andExpect(jsonPath("$._links.payment_orders.href").value("http://localhost/payment_orders{?from,to}"))
                .andExpect(jsonPath("$._links.post_payment_order.href").value("http://localhost/payment_orders"))
                .andExpect(jsonPath("$._links.put_payment_order.href").value("http://localhost/payment_orders/{id}"))
                .andExpect(jsonPath("$._links.delete_payment_order.href").value("http://localhost/payment_orders/{id}"))
                .andExpect(jsonPath("$._links.find_by_id.href").value("http://localhost/payment_orders/{id}"))
                .andExpect(jsonPath("$._links.created.href").value("http://localhost/payment_orders/created"))
                .andExpect(jsonPath("$._links.rejected.href").value("http://localhost/payment_orders/rejected"))
                .andExpect(jsonPath("$._links.postponed.href").value("http://localhost/payment_orders/postponed"))
                .andExpect(jsonPath("$._links.outstanding.href").value("http://localhost/payment_orders/outstanding"))
                .andExpect(jsonPath("$._links.credit.href").value("http://localhost/payment_orders/credit"))
                .andExpect(jsonPath("$._links.debit.href").value("http://localhost/payment_orders/debit"));
    }

    @Test
    void createPaymentOrder() throws Exception {
        String originatorAccount = "NL00RABO1234567892";
        String creationDateTime = "2020-12-02";
        String expiryDateTime = "2020-12-16";
        String orderType = "CREDIT";
        String status = "REJECTED";
        double instructedAmount = 223.99;

        String requestContent = new JSONObject()
                .put("originatorAccount", originatorAccount)
                .put("creationDateTime", creationDateTime)
                .put("expiryDateTime", expiryDateTime)
                .put("orderType", orderType)
                .put("status", status)
                .put("instructedAmount", instructedAmount)
                .toString();

        this.mockMvc.perform(post("/payment_orders")
                .content(requestContent)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.originatorAccount").value(originatorAccount))
                .andExpect(jsonPath("$.creationDateTime").value(creationDateTime))
                .andExpect(jsonPath("$.expiryDateTime").value(expiryDateTime))
                .andExpect(jsonPath("$.orderType").value(orderType))
                .andExpect(jsonPath("$.instructedAmount").value(String.valueOf(instructedAmount)))
                .andExpect(jsonPath("$.status").value(status))
                .andExpect(jsonPath("$.validated").value("true"))
                .andExpect(status().isOk());
    }

    @Test
    void deletePaymentOrder() throws Exception {
        mockMvc.perform(delete("/payment_orders/{id}", 0))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(delete("/payment_orders/{id}", -1))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void findPaymentOrderById() throws Exception {
        int paymentOrderId = 2;
        this.mockMvc.perform(get("/payment_orders/{id}", paymentOrderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentOrderId))
                .andExpect(jsonPath("$.originatorAccount").value("NL00RABO1234567891"))
                .andExpect(jsonPath("$.creationDateTime").value("2020-11-27"))
                .andExpect(jsonPath("$.expiryDateTime").value("2020-09-29"))
                .andExpect(jsonPath("$.orderType").value("DEBIT"))
                .andExpect(jsonPath("$.instructedAmount").value(1000.01))
                .andExpect(jsonPath("$.status").value("POSTPONED"))
                .andExpect(jsonPath("$.validated").value(true));
    }

    @Test
    void findAllPaymentOrders() throws Exception {
        String fromDate = "2020-01-12T11:12:14";
        String toDate = "2020-11-12T11:12:14";

        mockMvc.perform(get("/payment_orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

        mockMvc.perform(get("/payment_orders")
                .param("from", fromDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

        mockMvc.perform(get("/payment_orders")
                .param("to", toDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

        mockMvc.perform(get("/payment_orders")
                .param("from", fromDate)
                .param("to", toDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void findAllCreatedOrders() throws Exception {
        mockMvc.perform(get("/payment_orders/created"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.status != \"CREATED\")]").doesNotExist());

    }

    @Test
    void findAllRejectedOrders() throws Exception {
        mockMvc.perform(get("/payment_orders/rejected"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.status != \"REJECTED\")]").doesNotExist());
    }

    @Test
    void findAllPostponedOrders() throws Exception {
        mockMvc.perform(get("/payment_orders/postponed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.status != \"POSTPONED\")]").doesNotExist());
    }

    @Test
    void findAllOutstandingOrders() throws Exception {
        mockMvc.perform(get("/payment_orders/outstanding"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.status != \"OUTSTANDING\")]").doesNotExist());
    }

    @Test
    void findAllCreditOrders() throws Exception {
        mockMvc.perform(get("/payment_orders/credit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.orderType != \"CREDIT\")]").doesNotExist());
    }

    @Test
    void findAllDebitOrders() throws Exception {
        mockMvc.perform(get("/payment_orders/debit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.orderType != \"DEBIT\")]").doesNotExist());
    }
}