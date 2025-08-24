package com.temporal.spring_temporal.starter;

import com.temporal.spring_temporal.dto.TravelRequest;
import com.temporal.spring_temporal.workflow.TravelWorkFlow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TravelBookingWorkflowStarter {

    private final WorkflowServiceStubs serviceStubs;

    public void startWorkflow(TravelRequest travelRequest) {
        WorkflowClient client = WorkflowClient.newInstance(serviceStubs);

        TravelWorkFlow workFlow = client.newWorkflowStub(TravelWorkFlow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("TRAVEL_TASK_QUEUE")
                        .setWorkflowId("travel_" + travelRequest.getUserId())
                        .build()
        );

        WorkflowClient.start(workFlow::bookTrip, travelRequest);

    }

    public void sendConfirmationSignal(String userId) {
        WorkflowClient client = WorkflowClient.newInstance(serviceStubs);

        String workflowId = "travel_" + userId;
        TravelWorkFlow workflow = client.newWorkflowStub(TravelWorkFlow.class, workflowId);

        workflow.sendConfirmationSignal();
    }

}
