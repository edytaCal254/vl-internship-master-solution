package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.FifteenPercentDiscount;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.product.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptGenerator {

    public Receipt generate(Basket basket) {
        Receipt receipt = new Receipt(basketToListOfReceiptEntries(basket));

        TenPercentDiscount tenPercentDiscount = new TenPercentDiscount();
        FifteenPercentDiscount fifteenPercentDiscount = new FifteenPercentDiscount();

        return tenPercentDiscount.apply(fifteenPercentDiscount.apply(receipt));

    }

    private List<ReceiptEntry> basketToListOfReceiptEntries (Basket basket){
        List<ReceiptEntry> receiptEntries = new ArrayList<>();

        Map<Product, Integer> products = new HashMap<>();
        for(Product product : basket.getProducts()){
            Integer count = products.get(product);
            products.put(product, (count == null) ? 1 : ++count);
        }

        products.keySet().forEach(p -> receiptEntries.add(new ReceiptEntry(p, products.get(p))));

        return receiptEntries;
    }
}
