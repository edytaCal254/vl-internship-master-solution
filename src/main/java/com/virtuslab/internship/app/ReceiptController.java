package com.virtuslab.internship.app;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptGenerator;

import org.springframework.web.bind.annotation.*;

@RestController
public class ReceiptController {

    @RequestMapping(value = "/receipt", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Receipt generateReceiptFromGivenBasket(@RequestBody Basket basket){
        ReceiptGenerator receiptGenerator = new ReceiptGenerator();

        return receiptGenerator.generate(basket);
    }
}
