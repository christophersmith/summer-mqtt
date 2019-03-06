/*******************************************************************************
 * Copyright (c) 2019 Christopher Smith - https://github.com/christophersmith
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.christophersmith.summer.mqtt.core.event;

import org.springframework.context.ApplicationEventPublisher;

import com.github.christophersmith.summer.mqtt.core.service.MqttClientService;
import com.github.christophersmith.summer.mqtt.core.util.MqttHeaderHelper;

/**
 * An Event that is sent when the {@link MqttClientService} implementation publishes a Message to
 * the Broker.
 * <p>
 * This message is only sent if the {@link MqttClientService} has an
 * {@link ApplicationEventPublisher} instance defined.
 */
public class MqttMessagePublishedEvent extends MqttMessageStatusEvent
{
    private static final long serialVersionUID = 8406165555641322553L;
    private int               messageIdentifier;
    private String            correlationId;

    /**
     * The default constructor.
     * 
     * @param clientId the Client ID value
     * @param messageIdentifier the Message Identifier
     * @param correlationId the Correlation ID
     * @param source the {@link Object} that published this event
     * 
     * @throws IllegalArgumentException if the {@code clientId} is null or empty
     */
    public MqttMessagePublishedEvent(String clientId, int messageIdentifier, String correlationId,
        Object source)
    {
        super(clientId, source);
        this.messageIdentifier = messageIdentifier;
        this.correlationId = correlationId;
    }

    /**
     * The Message Identifier for the published message.
     * <p>
     * This is the Message Identifier set by the MQTT Client implementation for messages that are
     * published.
     * 
     * @return the Message Identifier
     */
    public int getMessageIdentifier()
    {
        return messageIdentifier;
    }

    /**
     * The Correlation ID for the published message.
     * <p>
     * This value is optionally provided by the caller that created the message, and is assigned by
     * adding a Correlation ID to the Message Header with the key of
     * {@link MqttHeaderHelper#CORRELATION_ID}. It's useful when you want to track when specific
     * messages are published, and be able to match that back to a {@link MqttMessageDeliveredEvent}
     * Event.
     * 
     * @return the Correlation ID that was set by the sender, or null if not set
     */
    public String getCorrelationId()
    {
        return correlationId;
    }
}
