package com.bridgingcode.bankaccountscqrsdemo.command.service;

import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import java.time.Instant;

/**
 * @author lixinhai
 * @date 2021/8/13
 */
@org.springframework.context.annotation.Configuration
public class Configuration {

    public TrackingEventProcessorConfiguration customConfiguration() {
        return TrackingEventProcessorConfiguration
                .forSingleThreadedProcessing()
                .andInitialTrackingToken(streamableMessageSource -> streamableMessageSource.createTokenAt(
                        Instant.parse("2007-12-03T10:15:30.00Z")
                ));
    }
}
