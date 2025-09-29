package com.projects.airBnbApp.service;

import com.projects.airBnbApp.entity.Booking;
import com.projects.airBnbApp.entity.User;
import com.projects.airBnbApp.repository.BookingRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutServiceImplementation implements CheckoutService {

    private final BookingRepository bookingRepository;

    // Ideally use @Value from application.properties instead of hardcoding
    private final String RAZORPAY_KEY_ID = "rzp_live_RM4FixUQTGqTBN";
    private final String RAZORPAY_KEY_SECRET = "6aELq0p8DEW8DdEZW8mn3AiZ";

    @Override
    public String getCheckoutSession(Booking booking, String successUrl, String failureUrl) {
        log.info("Creating Razorpay order for booking ID: {}", booking.getId());

        // Fetch current user from Security Context
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            RazorpayClient client = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);

            // Amount in paise
            int amountInPaise = booking.getAmount().multiply(java.math.BigDecimal.valueOf(100)).intValue();

            // Create order payload
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "booking_" + booking.getId());
            orderRequest.put("payment_capture", 1); // Auto-capture

            // Create Razorpay order (Orders is lowercase)
            Order order = client.Orders.create(orderRequest);

            // Save Razorpay order ID in booking
            booking.setPaymentSessionId(order.get("id").toString());
            bookingRepository.save(booking);

            log.info("Razorpay order created: Booking ID: {}, Order ID: {}", booking.getId(), order.get("id"));

            // Return checkout session details for frontend
            JSONObject response = new JSONObject();
            response.put("key", RAZORPAY_KEY_ID);
            // Safely cast Razorpay order values
            response.put("order_id", order.get("id").toString());
            response.put("amount", ((Number) order.get("amount")).intValue());
            response.put("currency", order.get("currency").toString());

            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("success_url", successUrl);
            response.put("failure_url", failureUrl);

            return response.toString();

        } catch (Exception e) {
            log.error("Error creating Razorpay order", e);
            throw new RuntimeException("Failed to create Razorpay order", e);
        }
    }
}
