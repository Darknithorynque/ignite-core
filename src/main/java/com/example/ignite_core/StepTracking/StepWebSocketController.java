package com.example.ignite_core.StepTracking;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class StepWebSocketController {
    StepTrackingService service;

    @MessageMapping("/updateStep")
    @SendTo("/topic/steps")
    public StepUpdate handleStepUpdate(StepUpdate stepUpdate) {
        service.updateStep(stepUpdate.getUserId(), stepUpdate.getStepCount());
        return stepUpdate;
    }
}
