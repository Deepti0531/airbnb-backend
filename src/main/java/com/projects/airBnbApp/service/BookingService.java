package com.projects.airBnbApp.service;

import com.projects.airBnbApp.dto.BookingDto;
import com.projects.airBnbApp.dto.BookingRequest;
import com.projects.airBnbApp.dto.HotelReportDto;
import com.projects.airBnbApp.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {



    BookingDto addGuests(Long bookingId, List<Long> guestIdList);

    /**
     * Initiates Razorpay payment and returns the payment URL
     */
    String initiatePayments(Long bookingId) throws Exception;

    /**
     * Captures a Razorpay payment after verifying signature
     *
     * @param paymentId Razorpay Payment ID
     * @param orderId   Razorpay Order ID
     * @param signature Razorpay Signature
     */
    void capturePayment(String paymentId, String orderId, String signature) throws Exception;

    /**
     * Cancels a booking and refunds payment via Razorpay
     */
    void cancelBooking(Long bookingId) throws Exception;

    BookingStatus getBookingStatus(Long bookingId);

    List<BookingDto> getAllBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingDto> getMyBookings();

    BookingDto initialiseBooking(BookingRequest bookingRequest);
}
