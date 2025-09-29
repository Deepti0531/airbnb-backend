package com.projects.airBnbApp.controller;

import com.projects.airBnbApp.service.BookingService;
import com.razorpay.Utils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final BookingService bookingService;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    @PostMapping("/payment")
    @Operation(summary = "Capture the payments", tags = {"Webhook"})
    public ResponseEntity<Void> capturePayments(@RequestBody Map<String, Object> payload,
                                                @RequestHeader("X-Razorpay-Signature") String razorpaySignature) {
        try {
            // Convert payload map directly to JSONObject
            JSONObject payloadJson = new JSONObject(payload);

            // Verify Razorpay signature
            Utils.verifyPaymentSignature(payloadJson, razorpaySignature);

            // Extract payment_id and order_id from payload
            JSONObject paymentEntity = payloadJson
                    .getJSONObject("payload")
                    .getJSONObject("payment")
                    .getJSONObject("entity");

            String paymentId = paymentEntity.getString("id");
            String orderId = paymentEntity.getString("order_id");

            // Call BookingService to handle captured payment
            bookingService.capturePayment(paymentId, orderId, razorpaySignature);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException("Webhook verification failed", e);
        }
    }
}
