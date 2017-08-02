/*******************************************************************************
 * Copyright (c) 2017 Christopher Smith
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

/**
 * An Event that is sent when the {@link MqttClientService} implementation receives confirmation
 * that a Message was published to the Broker.
 * <p>
 * This message is only sent if the {@link MqttClientService} has an
 * {@link ApplicationEventPublisher} instance defined.
 */
public class MqttMessageDeliveredEvent extends MqttMessageStatusEvent
{
    private static final long serialVersionUID = -6490456656850505283L;
    private int               messageIdentifier;

    /**
     * The default constructor.
     * 
     * @param clientId the Client ID value
     * @param messageIdentifier the Message Identifier
     * @param source the {@link Object} that published this event
     */
    public MqttMessageDeliveredEvent(String clientId, int messageIdentifier, Object source)
    {
        super(clientId, source);
        this.messageIdentifier = messageIdentifier;
    }

    /**
     * The Message Identifier for the delivered message.
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
}
