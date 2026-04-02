package com.example.societymainteinance.service;

import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;

@Service
public class PaymentService {

    // ⚠️ For now hardcoded (later move to application.properties)
    private static final String KEY_ID = "rzp_test_S6xwjVnV69Ran9";
    private static final String KEY_SECRET = "rjZSjBaaXhCrGDWchrbwDdHE";

    public JSONObject createOrder(Double amount) throws Exception {

        System.out.println("=== PAYMENT ORDER DEBUG ===");
        System.out.println("Amount received (in rupees): " + amount);
        System.out.println("Amount to send to Razorpay (in paise): " + (amount * 100));
        
        RazorpayClient client =
                new RazorpayClient(KEY_ID, KEY_SECRET);

        JSONObject options = new JSONObject();
        options.put("amount", (int)(amount * 100)); // Convert to int for paise
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = client.orders.create(options);
        
        System.out.println("Order created with amount: " + order.get("amount"));
        System.out.println("========================");

        return order.toJson();
    }
}
