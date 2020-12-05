package com.example.demo.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.containsString;

import com.example.demo.Model.PaymentOrder;
import com.example.demo.Repository.PaymentOrderRepository;
import com.example.demo.Service.PaymentOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
                "\"id\": null," +
                "\"originatorAccount\": \"NL00RABO1234567892\"," +
                "\"creationDateTime\": \"2020-12-02\"," +
                "\"expiryDateTime\": \"2020-12-16\"," +
                "\"orderType\": \"CREDIT\"," +
                "\"instructedAmount\": 223.99," +
                "\"status\": \"REJECTED\"," +
//                "\"validated\": true" +
                "}";
        String xD = "{\"id\":1,\"originatorAccount\":\"NL00RABO1234567890\",\"creationDateTime\":\"2020-11-27\",\"expiryDateTime\":\"2020-11-29\",\"orderType\":\"CREDIT\",\"instructedAmount\":100.01,\"status\":\"CREATED\",\"validated\":true}";
        String wth = asJsonString(new PaymentOrder(null, "NL00RABO1234567892", null, null, null, null, null));
        this.mockMvc.perform(post("/payment_orders")
                .content(requestContent))
                .andDo(print())
                .andExpect(content().string(containsString(xD)))
                .andExpect(status().isOk());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Mock
//    PaymentOrderService paymentOrderService;
//
//    @MockBean
//    PaymentOrderRepository paymentOrderRepository;
//
//    @Mock
//    PaymentOrder paymentOrder;          //Model
//
//    MainController mainController;
//    MockMvc mockMvc;
//
//
//    @Test
//    public void contextLoads() throws Exception {
//        assertThat(mainController).isNotNull();
//        assertThat(paymentOrder).isNotNull();
//        assertThat(paymentOrderService).isNotNull();
//    }
//
//    @BeforeEach
//    void setUp() {
//        if (mainController == null) {
//            MockitoAnnotations.initMocks(this);
//            mainController = new MainController(paymentOrderService);
//            mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
//        }
//    }
//
////    @Test
////    void index() throws Exception {
////        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/");
////        mockMvc.perform(requestBuilder)
////                .andExpect(status().isOk())
////                .andExpect(content().string("X"));
////    }
//
//    @Test
//    void createPaymentOrder() throws Exception {
//        String requestContent = "{\n" +
//                "    \"originatorAccount\":\"NL00RABO1234567892\",\n" +
//                "    \"creationDateTime\": \"2020-12-01T23:00:00.000+00:00\",\n" +
//                "    \"expiryDateTime\": \"2020-12-15T23:00:00.000+00:00\",\n" +
//                "    \"orderType\": \"CREDIT\",\n" +
//                "    \"status\": \"REJECTED\",\n" +
//                "    \"instructedAmount\": 223.99\n" +
//                "}";
//
//        String responseContent = "{\n" +
//                "    \"id\": null,\n" +
//                "    \"originatorAccount\": \"NL00RABO1234567892\",\n" +
//                "    \"creationDateTime\": \"2020-12-02\",\n" +
//                "    \"expiryDateTime\": \"2020-12-16\",\n" +
//                "    \"orderType\": \"CREDIT\",\n" +
//                "    \"instructedAmount\": 223.99,\n" +
//                "    \"status\": \"REJECTED\",\n" +
//                "    \"validated\": true\n" +
//                "}";
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/payment_orders")
//                .contentType(MediaType.APPLICATION_JSON)
//                .characterEncoding("utf-8")
//                .content(requestContent);
//
//        mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void deletePaymentOrder() throws Exception {
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/payment_orders/{id}", 1);
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void findPaymentOrderById() throws Exception {
//        String xD = "{" +
//                    "\"id\":1," +
//                    "\"originatorAccount\":\"NL00RABO1234567890\"," +
//                    "\"creationDateTime\":\"2020-11-27\"," +
//                    "\"expiryDateTime\":\"2020-11-29\"," +
//                    "\"orderType\":\"CREDIT\"," +
//                    "\"instructedAmount\":100.01," +
//                    "\"status\":\"CREATED\"," +
//                    "\"validated\":true" +
//                "}";
//        this.mockMvc.perform(get("/payment_orders/{id}", 1))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString(xD)));
//    }
//
//    @Test
//    void findAllCreatedOrders() throws Exception {
//        Date fromDate = new Date(2020, 4, 1);
//        Date toDate = new Date(2020, 12, 30);
//
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/payment_orders");
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk());
//
//        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/payment_orders")
//                .param("from", fromDate.toString());
//        mockMvc.perform(requestBuilder2)
//                .andExpect(status().isOk());
//
//        RequestBuilder requestBuilder3 = MockMvcRequestBuilders.get("/payment_orders")
//                .param("to", toDate.toString());
//        mockMvc.perform(requestBuilder3)
//                .andExpect(status().isOk());
//
//        RequestBuilder requestBuilder4 = MockMvcRequestBuilders.get("/payment_orders")
//                .param("from", fromDate.toString())
//                .param("to", toDate.toString());
//        mockMvc.perform(requestBuilder4)
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void findAllRejectedOrders() throws Exception {
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/payment_orders/rejected");
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void findAllPostponedOrders() throws Exception {
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/payment_orders/postponed");
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void findAllOutstandingOrders() throws Exception {
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/payment_orders/outstanding");
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void findAllCreditOrders() throws Exception {
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/payment_orders/credit");
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void findAllDebitOrders() throws Exception {
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/payment_orders/debit");
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk());
//    }
}