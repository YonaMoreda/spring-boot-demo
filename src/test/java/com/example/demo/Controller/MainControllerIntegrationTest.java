package com.example.demo.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.containsString;
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
                .andExpect(content().string("Home"));
    }

    @Test
    void createPaymentOrder() throws Exception {
        String requestContent = "{" +
                "\"originatorAccount\":\"NL00RABO1234567892\"," +
                "\"creationDateTime\": \"2020-12-01T23:00:00.000+00:00\"," +
                "\"expiryDateTime\": \"2020-12-15T23:00:00.000+00:00\"," +
                "\"orderType\": \"CREDIT\"," +
                "\"status\": \"REJECTED\"," +
                "\"instructedAmount\": 223.99" +
                "}";

        String responseContent = "{" +
                "\"id\":null," +
                "\"originatorAccount\":\"NL00RABO1234567892\"," +
                "\"creationDateTime\":\"2020-12-02\"," +
                "\"expiryDateTime\":\"2020-12-16\"," +
                "\"orderType\":\"CREDIT\"," +
                "\"instructedAmount\":223.99," +
                "\"status\":\"REJECTED\"," +
                "\"validated\":true" +
                "}";
        this.mockMvc.perform(post("/payment_orders")
                .content(requestContent)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(content().string(containsString(responseContent)))
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
        String responseContent = "{\"id\":2," +
                "\"originatorAccount\":\"NL00RABO1234567891\"," +
                "\"creationDateTime\":\"2020-11-27\"," +
                "\"expiryDateTime\":\"2020-09-29\"," +
                "\"orderType\":\"DEBIT\"," +
                "\"instructedAmount\":1000.01," +
                "\"status\":\"POSTPONED\"," +
                "\"validated\":true" +
                "}";
        this.mockMvc.perform(get("/payment_orders/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(responseContent)));
    }

    @Test
    void findAllCreatedOrders() throws Exception {
        String fromDate = "2020-01-12T11:12:14";
        String toDate = "2020-11-12T11:12:14";

        mockMvc.perform(get("/payment_orders"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/payment_orders")
                .param("from", fromDate))
                .andExpect(status().isOk());

        mockMvc.perform(get("/payment_orders")
                .param("to", toDate))
                .andExpect(status().isOk());

        mockMvc.perform(get("/payment_orders")
                .param("from", fromDate)
                .param("to", toDate))
                .andExpect(status().isOk());
    }

    @Test
    void findAllRejectedOrders() throws Exception {
        mockMvc.perform(get("/payment_orders/rejected"))
                .andExpect(status().isOk());
    }

    @Test
    void findAllPostponedOrders() throws Exception {
        mockMvc.perform(get("/payment_orders/postponed"))
                .andExpect(status().isOk());
    }

    @Test
    void findAllOutstandingOrders() throws Exception {
        mockMvc.perform(get("/payment_orders/outstanding"))
                .andExpect(status().isOk());
    }

    @Test
    void findAllCreditOrders() throws Exception {
        mockMvc.perform(get("/payment_orders/credit"))
                .andExpect(status().isOk());
    }

    @Test
    void findAllDebitOrders() throws Exception {
        mockMvc.perform(get("/payment_orders/debit"))
                .andExpect(status().isOk());
    }
}