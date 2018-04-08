/*******************************************************************************
 * Copyright (c) 2018 Christopher Smith - https://github.com/christophersmith
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

import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionFailureEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionLostEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientDisconnectedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessageDeliveredEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessagePublishedEvent;

public class MqttClientEventPublisherTest
{
    private static final String      CLIENT_ID                   = "TESTCLIENT";
    private static final String      SERVER_URI                  = "tcp://localhost:1883";
    private static final String[]    SUBSCRIBED_TOPICS_EMPTY     = new String[0];
    private static final int         MESSAGE_ID                  = 12;
    private static final String      CORRELATION_ID              = UUID.randomUUID().toString();
    private static final String      EXCEPTION_MESSAGE_CLIENT_ID = "'clientId' must be set!";
    private MqttClientEventPublisher mqttClientEventPublisher    = new MqttClientEventPublisher();
    @Rule
    public ExpectedException         thrown                      = ExpectedException.none();

    @Test
    public void testPublishConnectedEventNullApplicationEventPublisher()
    {
        mqttClientEventPublisher.publishConnectedEvent(CLIENT_ID, SERVER_URI,
            SUBSCRIBED_TOPICS_EMPTY, null, this);
    }

    @Test
    public void testPublishConnectedEventNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishConnectedEvent(null, SERVER_URI, SUBSCRIBED_TOPICS_EMPTY,
            applicationEventPublisher, this);
    }

    @Test
    public void testPublishConnectedEventNullServerUri()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'serverUri' must be set!");
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishConnectedEvent(CLIENT_ID, null, SUBSCRIBED_TOPICS_EMPTY,
            applicationEventPublisher, this);
    }

    @Test
    public void testPublishConnectedEventNullSubscribedTopics()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'subscribedTopics' must be set!");
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishConnectedEvent(CLIENT_ID, SERVER_URI, null,
            applicationEventPublisher, this);
    }

    @Test
    public void testPublishConnectedEvent()
    {
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishConnectedEvent(CLIENT_ID, SERVER_URI,
            SUBSCRIBED_TOPICS_EMPTY, applicationEventPublisher, this);
        Mockito.verify(applicationEventPublisher, Mockito.atLeast(1))
            .publishEvent(Mockito.any(MqttClientConnectedEvent.class));
    }

    @Test
    public void testPublishConnectionFailureEventNullApplicationEventPublisher()
    {
        mqttClientEventPublisher.publishConnectionFailureEvent(CLIENT_ID, true, new Exception(),
            null, this);
    }

    @Test
    public void testPublishConnectionFailureEventNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishConnectionFailureEvent(null, true, new Exception(),
            applicationEventPublisher, this);
    }

    @Test
    public void testPublishConnectionFailureEvent()
    {
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishConnectionFailureEvent(CLIENT_ID, true, new Exception(),
            applicationEventPublisher, this);
        Mockito.verify(applicationEventPublisher, Mockito.atLeast(1))
            .publishEvent(Mockito.any(MqttClientConnectionFailureEvent.class));
    }

    @Test
    public void testPublishConnectionLostEventNullApplicationEventPublisher()
    {
        mqttClientEventPublisher.publishConnectionLostEvent(CLIENT_ID, true, null, this);
    }

    @Test
    public void testPublishConnectionLostEventNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishConnectionLostEvent(null, true, applicationEventPublisher,
            this);
    }

    @Test
    public void testPublishConnectionLostEvent()
    {
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishConnectionLostEvent(CLIENT_ID, true,
            applicationEventPublisher, this);
        Mockito.verify(applicationEventPublisher, Mockito.atLeast(1))
            .publishEvent(Mockito.any(MqttClientConnectionLostEvent.class));
    }

    @Test
    public void testPublishDisconnectedEventNullApplicationEventPublisher()
    {
        mqttClientEventPublisher.publishDisconnectedEvent(CLIENT_ID, null, this);
    }

    @Test
    public void testPublishDisconnectedEventNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishDisconnectedEvent(null, applicationEventPublisher, this);
    }

    @Test
    public void testPublishDisconnectedEvent()
    {
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishDisconnectedEvent(CLIENT_ID, applicationEventPublisher,
            this);
        Mockito.verify(applicationEventPublisher, Mockito.atLeast(1))
            .publishEvent(Mockito.any(MqttClientDisconnectedEvent.class));
    }

    @Test
    public void testPublishMessageDeliveredEventNullApplicationEventPublisher()
    {
        mqttClientEventPublisher.publishMessageDeliveredEvent(CLIENT_ID, MESSAGE_ID, null, this);
    }

    @Test
    public void testPublishMessageDeliveredEventNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishMessageDeliveredEvent(null, MESSAGE_ID,
            applicationEventPublisher, this);
    }

    @Test
    public void testPublishMessageDeliveredEvent()
    {
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishMessageDeliveredEvent(CLIENT_ID, MESSAGE_ID,
            applicationEventPublisher, this);
        Mockito.verify(applicationEventPublisher, Mockito.atLeast(1))
            .publishEvent(Mockito.any(MqttMessageDeliveredEvent.class));
    }

    @Test
    public void testPublishMessagePublishedEventNullApplicationEventPublisher()
    {
        mqttClientEventPublisher.publishMessagePublishedEvent(CLIENT_ID, MESSAGE_ID, CORRELATION_ID,
            null, this);
    }

    @Test
    public void testPublishMessagePublishedEventNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishMessagePublishedEvent(null, MESSAGE_ID, CORRELATION_ID,
            applicationEventPublisher, this);
    }

    @Test
    public void testPublishMessagePublishedEventNullCorrelationId()
    {
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishMessagePublishedEvent(CLIENT_ID, MESSAGE_ID, null,
            applicationEventPublisher, this);
    }

    @Test
    public void testPublishMessagePublishedEvent()
    {
        ApplicationEventPublisher applicationEventPublisher = Mockito
            .mock(ApplicationEventPublisher.class);
        mqttClientEventPublisher.publishMessagePublishedEvent(CLIENT_ID, MESSAGE_ID, CORRELATION_ID,
            applicationEventPublisher, this);
        Mockito.verify(applicationEventPublisher, Mockito.atLeast(1))
            .publishEvent(Mockito.any(MqttMessagePublishedEvent.class));
    }
}
