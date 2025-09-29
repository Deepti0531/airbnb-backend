package com.projects.airBnbApp.controller;

import com.projects.airBnbApp.dto.*;
import com.projects.airBnbApp.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody BookingRequest bookingRequest) {
        BookingDto bookingDto = bookingService.initialiseBooking(bookingRequest);
        return ResponseEntity.ok(bookingDto);
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId,
                                                @RequestBody List<GuestDto> guestDtoList) {
        List<Long> guestIds = guestDtoList.stream()
                .map(GuestDto::getId)
                .collect(Collectors.toList());
        BookingDto updatedBooking = bookingService.addGuests(bookingId, guestIds);
        return ResponseEntity.ok(updatedBooking);
    }

    @PostMapping("/{bookingId}/payments")
    @Operation(summary = "Initiate payments flow for the booking", tags = {"Booking Flow"})
    public ResponseEntity<BookingPaymentInitResponseDto> initiatePayment(@PathVariable Long bookingId) throws Exception {
        String paymentSessionUrl = bookingService.initiatePayments(bookingId);
        BookingPaymentInitResponseDto response = new BookingPaymentInitResponseDto(paymentSessionUrl);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{bookingId}/cancel")
    @Operation(summary = "Cancel the booking", tags = {"Booking Flow"})
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) throws Exception {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bookingId}/status")
    @Operation(summary = "Check the status of the booking", tags = {"Booking Flow"})
    public ResponseEntity<BookingStatusResponseDto> getBookingStatus(@PathVariable Long bookingId) {
        BookingStatusResponseDto response = new BookingStatusResponseDto(bookingService.getBookingStatus(bookingId));
        return ResponseEntity.ok(response);
    }
}
