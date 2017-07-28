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
package com.github.christophersmith.summer.mqtt.core.util;

import org.springframework.context.ApplicationEventPublisher;

import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionFailureEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionLostEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientDisconnectedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessageDeliveredEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessagePublishedEvent;

public class MqttClientEventPublisher
{
    public void publishConnectedEvent(String clientId, String serverUri, String[] subscribedTopics,
        ApplicationEventPublisher applicationEventPublisher)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher.publishEvent(
                new MqttClientConnectedEvent(clientId, serverUri, subscribedTopics, this));
        }
    }

    public void publishConnectionFailureEvent(String clientId, boolean autoReconnect,
        Throwable throwable, ApplicationEventPublisher applicationEventPublisher)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher.publishEvent(
                new MqttClientConnectionFailureEvent(clientId, autoReconnect, throwable, this));
        }
    }

    public void publishConnectionLostEvent(String clientId, boolean autoReconnect,
        ApplicationEventPublisher applicationEventPublisher)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher
                .publishEvent(new MqttClientConnectionLostEvent(clientId, autoReconnect, this));
        }
    }

    public void publishDisconnectedEvent(String clientId,
        ApplicationEventPublisher applicationEventPublisher)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher.publishEvent(new MqttClientDisconnectedEvent(clientId, this));
        }
    }

    public void publishMessageDeliveredEvent(String clientId, int messageIdentifier,
        ApplicationEventPublisher applicationEventPublisher)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher
                .publishEvent(new MqttMessageDeliveredEvent(clientId, messageIdentifier, this));
        }
    }

    public void publishMessagePublishedEvent(String clientId, int messageIdentifier,
        String correlationId, ApplicationEventPublisher applicationEventPublisher)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher.publishEvent(
                new MqttMessagePublishedEvent(clientId, messageIdentifier, correlationId, this));
        }
    }
}
