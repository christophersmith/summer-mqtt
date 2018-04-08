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
package com.github.christophersmith.summer.mqtt.paho.service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;

import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessageDeliveredEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessagePublishedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessageStatusEvent;
import com.github.christophersmith.summer.mqtt.core.util.MqttHeaderHelper;
import com.github.christophersmith.summer.mqtt.paho.service.util.BrokerHelper;

public class PublishMessageTest implements ApplicationListener<MqttMessageStatusEvent>
{
    private static final String                               EXCEPTION_SUBSCRIBER_CANNOT_PUBLISH_FORMAT = "Client ID %s is setup as a SUBSCRIBER and could not publish this message.";
    private ConcurrentMap<String, MqttMessagePublishedEvent>  publishedMessages                          = new ConcurrentHashMap<String, MqttMessagePublishedEvent>();
    private ConcurrentMap<Integer, MqttMessageDeliveredEvent> deliveredMessages                          = new ConcurrentHashMap<Integer, MqttMessageDeliveredEvent>();
    @Rule
    public ExpectedException                                  thrown                                     = ExpectedException
        .none();

    @Test
    public void testSubcriberCannotPublish() throws MqttException
    {
        thrown.expect(MessagingException.class);
        thrown.expectMessage(
            String.format(EXCEPTION_SUBSCRIBER_CANNOT_PUBLISH_FORMAT, BrokerHelper.getClientId()));
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.SUBSCRIBER, null);
        service.handleMessage(null);
    }

    @Test
    public void testPublishNullMessage() throws MqttException
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'message' must be set!");
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBLISHER, null);
        service.handleMessage(null);
    }

    @Test
    public void testPublishMessageClientNotConnected() throws MqttException
    {
        thrown.expect(MessagingException.class);
        thrown.expectMessage(String.format("Client ID %s is disconnected. Could not send message.",
            BrokerHelper.getClientId()));
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBLISHER, null);
        service.handleMessage(MessageBuilder.withPayload("Test").build());
    }

    @Test
    public void testPublishMessageTopicNotProvided() throws MqttException
    {
        thrown.expect(MessagingException.class);
        thrown.expectMessage(String.format(
            "Client ID '%s' could not publish this message because either the topic or payload isn't set, or the payload could not be converted.",
            BrokerHelper.getClientId()));
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBLISHER, null);
        service.start();
        service.handleMessage(MessageBuilder.withPayload("Test").build());
    }

    @Test
    public void testPublishMessageWithCorrelationIdVerification()
        throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.addApplicationListener(this);
        applicationContext.refresh();
        applicationContext.start();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBLISHER, null);
        service.setApplicationEventPublisher(applicationContext);
        service.start();
        String correlationIdentifier = UUID.randomUUID().toString();
        service.handleMessage(MessageBuilder.withPayload("Test")
            .setHeader(MqttHeaderHelper.TOPIC,
                String.format("client/%s", BrokerHelper.getClientId()))
            .setHeader(MqttHeaderHelper.CORRELATION_ID, correlationIdentifier).build());
        Thread.sleep(500);
        Assert.assertTrue(publishedMessages.containsKey(correlationIdentifier));
        MqttMessagePublishedEvent publishedEvent = publishedMessages.get(correlationIdentifier);
        Assert.assertTrue(deliveredMessages.containsKey(publishedEvent.getMessageIdentifier()));
        correlationIdentifier = UUID.randomUUID().toString();
        service.handleMessage(MessageBuilder.withPayload("Test".getBytes())
            .setHeader(MqttHeaderHelper.TOPIC,
                String.format("client/%s", BrokerHelper.getClientId()))
            .setHeader(MqttHeaderHelper.CORRELATION_ID, correlationIdentifier).build());
        Thread.sleep(500);
        Assert.assertTrue(publishedMessages.containsKey(correlationIdentifier));
        publishedEvent = publishedMessages.get(correlationIdentifier);
        Assert.assertTrue(deliveredMessages.containsKey(publishedEvent.getMessageIdentifier()));
        service.stop();
        service.close();
    }

    @Override
    public void onApplicationEvent(MqttMessageStatusEvent event)
    {
        if (event instanceof MqttMessagePublishedEvent)
        {
            MqttMessagePublishedEvent publishedEvent = (MqttMessagePublishedEvent) event;
            if (publishedEvent.getCorrelationId() != null)
            {
                publishedMessages.put(publishedEvent.getCorrelationId(), publishedEvent);
            }
        }
        if (event instanceof MqttMessageDeliveredEvent)
        {
            MqttMessageDeliveredEvent deliveredEvent = (MqttMessageDeliveredEvent) event;
            deliveredMessages.put(deliveredEvent.getMessageIdentifier(), deliveredEvent);
        }

    }
}
