package com.temporal.spring_temporal.controller;

import com.temporal.spring_temporal.dto.TravelRequest;
import com.temporal.spring_temporal.starter.TravelBookingWorkflowStarter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/travel")
public class TravelWorkflowController {

    private final TravelBookingWorkflowStarter starter;

    public TravelWorkflowController(TravelBookingWorkflowStarter starter) {
        this.starter = starter;
    }

    // Endpoint to start the travel booking workflow
    @PostMapping("/book")
    public ResponseEntity<String> bookTravel(@RequestBody TravelRequest travelRequest) {
        starter.startWorkflow(travelRequest);
        return ResponseEntity.ok("Travel booking workflow started for user: " + travelRequest.getUserId());
    }

//     Endpoint to confirm the booking by sending a signal to the workflow
    @PostMapping("/confirm/{userId}")
    public ResponseEntity<String> confirmBooking(@RequestParam String userId) {
        starter.sendConfirmationSignal(userId);
        return ResponseEntity.ok("✅ Booking confirmed by user!");
    }


}