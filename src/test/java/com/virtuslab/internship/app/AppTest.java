package com.virtuslab.internship.app;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.ReceiptGenerator;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class AppTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReceiptController receiptController;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads(){
        assertNotNull(receiptController);
    }

    @Test
    public void shouldReturnReceiptData() throws Exception {
        //Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var receiptGenerator = new ReceiptGenerator();
        var milk = productDb.getProduct("Milk");
        var bread = productDb.getProduct("Bread");
        var apple = productDb.getProduct("Apple");

        cart.addProduct(milk);
        cart.addProduct(milk);
        cart.addProduct(bread);
        cart.addProduct(apple);

        String cartJson = objectMapper.writeValueAsString(cart);
        String expectedReceiptJson = objectMapper.writeValueAsString(receiptGenerator.generate(cart));

        //When
        String responseString = mockMvc.perform(MockMvcRequestBuilders
                        .post("/receipt")
                        .content(cartJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        //Then
         assertEquals(expectedReceiptJson, responseString);
    }
}
