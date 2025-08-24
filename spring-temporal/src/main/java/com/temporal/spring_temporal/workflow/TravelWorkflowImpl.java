package com.temporal.spring_temporal.workflow;

import com.temporal.spring_temporal.activities.TravelActivities;
import com.temporal.spring_temporal.dto.TravelRequest;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class TravelWorkflowImpl implements TravelWorkFlow {


    private boolean isUserConfirmed = false;

    @SignalMethod
    public void sendConfirmationSignal() {
        log.info("📩 Received user confirmation signal.");
        isUserConfirmed = true;
    }


    @Override
    public void bookTrip(TravelRequest travelRequest) {
        log.info("🚀 Starting travel booking for user: {}", travelRequest.getUserId());

        TravelActivities activities = Workflow.newActivityStub(TravelActivities.class,
                ActivityOptions.newBuilder()
                        .setRetryOptions(RetryOptions.newBuilder()
                                .setMaximumAttempts(3)
                                .build())
                        .setStartToCloseTimeout(Duration.ofSeconds(10))
                        .build());

        activities.bookFlight(travelRequest);
        activities.bookHotel(travelRequest);
        activities.arrangeTransport(travelRequest);

        // 2 minutes -> wait for user confirmation if you won't
        // get any withing 2 min then cancel it

        log.info("Waiting for user confirmation for 2 minutes...");

        boolean isConfirmed = Workflow.await(
                Duration.ofMinutes(2),
                () -> isUserConfirmed);

        if (!isConfirmed) {
            //cancel the booking
            log.info("🛑 User did not confirm within 2 minutes, cancelling the booking for user: {}", travelRequest.getUserId());
            activities.cancelBooking(travelRequest);
        } else {
            //confirm booking
            log.info("✅ User confirmed the booking: {}", travelRequest.getUserId());
            activities.confirmBooking(travelRequest);
        }

        log.info("✅ Travel booking completed for user: {}", travelRequest.getUserId());

    }
}