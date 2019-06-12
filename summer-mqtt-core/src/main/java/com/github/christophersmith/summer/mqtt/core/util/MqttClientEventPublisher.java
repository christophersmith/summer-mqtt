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
package com.github.christophersmith.summer.mqtt.core.util;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.MessagingException;

import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionFailureEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionLostEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientDisconnectedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessageDeliveredEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessagePublishFailureEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessagePublishedEvent;

/**
 * This is a convenience class that facilitates the publishing of Events to an
 * {@link ApplicationEventPublisher} instance.
 * <p>
 * This class in only used internally.
 */
public final class MqttClientEventPublisher
{
    /**
     * Publishes a {@link MqttClientConnectedEvent} message to the
     * {@link ApplicationEventPublisher}.
     * <p>
     * If the {@link ApplicationEventPublisher} instance is null, no event message will be
     * published.
     * 
     * @param clientId the Client ID value
     * @param serverUri the Server URI the MQTT Client is connected to
     * @param subscribedTopics the Topic Filters the MQTT Client is subscribed to
     * @param applicationEventPublisher the {@link ApplicationEventPublisher} value
     * @param source the source that sent this event
     */
    public void publishConnectedEvent(String clientId, String serverUri, String[] subscribedTopics,
        ApplicationEventPublisher applicationEventPublisher, Object source)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher.publishEvent(
                new MqttClientConnectedEvent(clientId, serverUri, subscribedTopics, source));
        }
    }

    /**
     * Publishes a {@link MqttClientConnectionFailureEvent} message to the
     * {@link ApplicationEventPublisher}.
     * <p>
     * If the {@link ApplicationEventPublisher} instance is null, no event message will be
     * published.
     * 
     * @param clientId the Client ID value
     * @param autoReconnect whether the MQTT Client will automatically reconnect
     * @param throwable the originating {@link Throwable}
     * @param applicationEventPublisher the {@link ApplicationEventPublisher} value
     * @param source the source that sent this event
     */
    public void publishConnectionFailureEvent(String clientId, boolean autoReconnect,
        Throwable throwable, ApplicationEventPublisher applicationEventPublisher, Object source)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher.publishEvent(
                new MqttClientConnectionFailureEvent(clientId, autoReconnect, throwable, source));
        }
    }

    /**
     * Publishes a {@link MqttClientConnectionLostEvent} message to the
     * {@link ApplicationEventPublisher}.
     * <p>
     * If the {@link ApplicationEventPublisher} instance is null, no event message will be
     * published.
     * 
     * @param clientId the Client ID value
     * @param autoReconnect whether the MQTT Client will automatically reconnect
     * @param applicationEventPublisher the {@link ApplicationEventPublisher} value
     * @param source the source that sent this event
     */
    public void publishConnectionLostEvent(String clientId, boolean autoReconnect,
        ApplicationEventPublisher applicationEventPublisher, Object source)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher
                .publishEvent(new MqttClientConnectionLostEvent(clientId, autoReconnect, source));
        }
    }

    /**
     * Publishes a {@link MqttClientDisconnectedEvent} message to the
     * {@link ApplicationEventPublisher}.
     * <p>
     * If the {@link ApplicationEventPublisher} instance is null, no event message will be
     * published.
     * 
     * @param clientId the Client ID value
     * @param applicationEventPublisher the {@link ApplicationEventPublisher} value
     * @param source the source that sent this event
     */
    public void publishDisconnectedEvent(String clientId,
        ApplicationEventPublisher applicationEventPublisher, Object source)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher
                .publishEvent(new MqttClientDisconnectedEvent(clientId, source));
        }
    }

    /**
     * Publishes a {@link MqttMessageDeliveredEvent} message to the
     * {@link ApplicationEventPublisher}.
     * <p>
     * If the {@link ApplicationEventPublisher} instance is null, no event message will be
     * published.
     * 
     * @param clientId the Client ID value
     * @param messageIdentifier the Message Identifier
     * @param applicationEventPublisher the {@link ApplicationEventPublisher} value
     * @param source the source that sent this event
     */
    public void publishMessageDeliveredEvent(String clientId, int messageIdentifier,
        ApplicationEventPublisher applicationEventPublisher, Object source)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher
                .publishEvent(new MqttMessageDeliveredEvent(clientId, messageIdentifier, source));
        }
    }

    /**
     * Publishes a {@link MqttMessagePublishedEvent} message to the
     * {@link ApplicationEventPublisher}.
     * <p>
     * If the {@link ApplicationEventPublisher} instance is null, no event message will be
     * published.
     * 
     * @param clientId the Client ID value
     * @param messageIdentifier the Message Identifier
     * @param correlationId the Correlation ID
     * @param applicationEventPublisher the {@link ApplicationEventPublisher} value
     * @param source the source that sent this event
     */
    public void publishMessagePublishedEvent(String clientId, int messageIdentifier,
        String correlationId, ApplicationEventPublisher applicationEventPublisher, Object source)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher.publishEvent(
                new MqttMessagePublishedEvent(clientId, messageIdentifier, correlationId, source));
        }
    }

    /**
     * Publishes a {@link MqttMessagePublishFailureEvent} message to the
     * {@link ApplicationEventPublisher}.
     * <p>
     * If the {@link ApplicationEventPublisher} instance is null, no event message will be
     * published.
     * 
     * @param clientId the Client ID value
     * @param exception the {@link MessagingException} for this event
     * @param applicationEventPublisher the {@link ApplicationEventPublisher} value
     * @param source the source that sent this event
     */
    public void publishMessagePublishFailureEvent(String clientId, MessagingException exception,
        ApplicationEventPublisher applicationEventPublisher, Object source)
    {
        if (applicationEventPublisher != null)
        {
            applicationEventPublisher
                .publishEvent(new MqttMessagePublishFailureEvent(clientId, exception, source));
        }
    }
}
