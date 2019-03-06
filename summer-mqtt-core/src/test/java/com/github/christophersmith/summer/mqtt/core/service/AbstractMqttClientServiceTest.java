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
package com.github.christophersmith.summer.mqtt.core.service;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.TaskScheduler;

import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.core.TopicSubscription;

public class AbstractMqttClientServiceTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test()
    {
        AbstractMqttClientService clientService = Mockito.mock(AbstractMqttClientService.class,
            Mockito.withSettings().useConstructor(MqttClientConnectionType.PUBSUB)
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        Assert.assertEquals(MqttClientConnectionType.PUBSUB, clientService.getConnectionType());
        Assert.assertFalse(clientService.isStarted());
        Assert.assertNotNull(clientService.getMqttClientConfiguration());
        Assert.assertNull(clientService.applicationEventPublisher);
        clientService.setApplicationEventPublisher(Mockito.mock(ApplicationEventPublisher.class));
        Assert.assertNotNull(clientService.applicationEventPublisher);
        clientService.subscribe("inbound/client");
        Mockito.verify(clientService, Mockito.times(1)).subscribe(Mockito.anyString(),
            Mockito.any());
        Assert.assertNull(clientService.inboundMessageChannel);
        clientService.setInboundMessageChannel(Mockito.mock(MessageChannel.class));
        Assert.assertNotNull(clientService.inboundMessageChannel);
        Assert.assertNull(clientService.reconnectService);
        Assert.assertNull(clientService.taskScheduler);
        clientService.setReconnectDetails(Mockito.mock(ReconnectService.class),
            Mockito.mock(TaskScheduler.class));
        Assert.assertNotNull(clientService.reconnectService);
        Assert.assertNotNull(clientService.taskScheduler);
        Assert.assertEquals(0, clientService.getTopicSubscriptions().size());
        clientService.topicSubscriptions
            .add(new TopicSubscription("test", MqttQualityOfService.QOS_0));
        Assert.assertEquals(1, clientService.getTopicSubscriptions().size());
    }

    @Test
    public void testPublisherSetInboundMessageChannel()
    {
        AbstractMqttClientService clientService = Mockito.mock(AbstractMqttClientService.class,
            Mockito.withSettings().useConstructor(MqttClientConnectionType.PUBLISHER)
                .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        Assert.assertNull(clientService.inboundMessageChannel);
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(
            "Client ID null is setup as a PUBLISHER and cannot receive messages from the Broker!");
        clientService.setInboundMessageChannel(Mockito.mock(MessageChannel.class));
    }
}
