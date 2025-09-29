package com.projects.airBnbApp.repository;

import com.projects.airBnbApp.entity.Booking;
import com.projects.airBnbApp.entity.Hotel;
import com.projects.airBnbApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // For previous Stripe integration (optional)
    Optional<Booking> findByPaymentSessionId(String sessionId);

    // New: For Razorpay integration
    Optional<Booking> findByPaymentOrderId(String paymentOrderId);

    List<Booking> findByHotel(Hotel hotel);

    List<Booking> findByHotelAndCreatedAtBetween(Hotel hotel, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Booking> findByUser(User user);
}
