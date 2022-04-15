package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.FifteenPercentDiscount;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.product.ProductDb;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptGeneratorTest {

    @Test
    void shouldGenerateReceiptForGivenBasket() {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var milk = productDb.getProduct("Milk");
        var bread = productDb.getProduct("Bread");
        var apple = productDb.getProduct("Apple");
        var expectedTotalPrice = milk.price().multiply(BigDecimal.valueOf(2)).add(bread.price()).add(apple.price());

        cart.addProduct(milk);
        cart.addProduct(milk);
        cart.addProduct(bread);
        cart.addProduct(apple);

        var receiptGenerator = new ReceiptGenerator();

        // When
        var receipt = receiptGenerator.generate(cart);

        // Then
        assertNotNull(receipt);
        assertEquals(3, receipt.entries().size());
        assertEquals(expectedTotalPrice, receipt.totalPrice());
        assertEquals(0, receipt.discounts().size());
    }

    @Test
    void shouldApply15PercentAnd10PercentDiscounts() {
        //Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var bread = productDb.getProduct("Bread");
        var cheese = productDb.getProduct("Cheese");
        var steak = productDb.getProduct("Steak");
        var cereals = productDb.getProduct("Cereals");
        var expectedTotalPrice = bread.price().add(cereals.price()).multiply(BigDecimal.valueOf(2))
                        .add(cheese.price()).add(steak.price())
                        .multiply(BigDecimal.valueOf(0.85)).multiply(BigDecimal.valueOf(0.9));


        cart.addProduct(bread);
        cart.addProduct(bread);
        cart.addProduct(cheese);
        cart.addProduct(steak);
        cart.addProduct(cereals);
        cart.addProduct(cereals);

        var receiptGenerator = new ReceiptGenerator();

        // When
        var receipt = receiptGenerator.generate(cart);

        // Then
        assertNotNull(receipt);
        assertEquals(4, receipt.entries().size());
        assertEquals(expectedTotalPrice, receipt.totalPrice());
        assertEquals(2, receipt.discounts().size());
        assertTrue(receipt.discounts().contains(FifteenPercentDiscount.NAME));
        assertTrue(receipt.discounts().contains(TenPercentDiscount.NAME));

    }

    @Test
    void shouldApply15PercentDiscountFirst(){
        //Checks if 15 percent discount is applied first
        //With given conditions, after a 15 percent discount,
        //the receipt no longer meets the condition for a 10 percent discount.

        //Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var bread = productDb.getProduct("Bread");
        var cheese = productDb.getProduct("Cheese");
        var cereals = productDb.getProduct("Cereals");
        var tomato = productDb.getProduct("Tomato");
        var expectedTotalPrice = bread.price().multiply(BigDecimal.valueOf(2))
                .add(cereals.price()).add(cheese.price()).add(tomato.price())
                .multiply(BigDecimal.valueOf(0.85));

        cart.addProduct(bread);
        cart.addProduct(bread);
        cart.addProduct(cheese);
        cart.addProduct(cereals);
        cart.addProduct(tomato);

        var receiptGenerator = new ReceiptGenerator();

        // When
        var receipt = receiptGenerator.generate(cart);

        // Then
        assertNotNull(receipt);
        assertEquals(4, receipt.entries().size());
        assertEquals(expectedTotalPrice, receipt.totalPrice());
        assertEquals(1, receipt.discounts().size());
        assertTrue(receipt.discounts().contains(FifteenPercentDiscount.NAME));
    }
}
