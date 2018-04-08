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

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.StaticApplicationContext;

import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionStatusPublisher;
import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessageDeliveredEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessagePublishedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessageStatusEvent;
import com.github.christophersmith.summer.mqtt.paho.service.util.BrokerHelper;

public class ConnectionStatusTest
    implements ApplicationListener<MqttMessageStatusEvent>, MqttClientConnectionStatusPublisher
{
    private AtomicInteger messageDeliveredCount = new AtomicInteger(0);
    private AtomicInteger messagePublishedCount = new AtomicInteger(0);

    @Test
    public void testWithDefaultStatusPublisher() throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
        service.setApplicationEventPublisher(applicationContext);
        service.start();
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Thread.sleep(1100);
        Assert.assertEquals(0, messageDeliveredCount.get());
        Assert.assertEquals(0, messagePublishedCount.get());
        service.stop();
        service.close();
    }

    @Test
    public void testWithNullStatusPublisher() throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
        service.setApplicationEventPublisher(applicationContext);
        service.getMqttClientConfiguration().setMqttClientConnectionStatusPublisher(null);
        service.start();
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Thread.sleep(1100);
        Assert.assertEquals(0, messageDeliveredCount.get());
        Assert.assertEquals(0, messagePublishedCount.get());
        service.stop();
        service.close();
    }

    @Test
    public void testWithCustomStatusPublisher() throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
        service.setApplicationEventPublisher(applicationContext);
        service.getMqttClientConfiguration().setMqttClientConnectionStatusPublisher(this);
        service.start();
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Thread.sleep(1100);
        Assert.assertEquals(1, messageDeliveredCount.get());
        Assert.assertEquals(1, messagePublishedCount.get());
        service.stop();
        Thread.sleep(1100);
        Assert.assertEquals(2, messageDeliveredCount.get());
        Assert.assertEquals(2, messagePublishedCount.get());
        service.close();
    }

    private StaticApplicationContext getStaticApplicationContext()
    {
        StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.addApplicationListener(this);
        applicationContext.refresh();
        applicationContext.start();
        return applicationContext;
    }

    @Override
    public void onApplicationEvent(MqttMessageStatusEvent event)
    {
        if (event instanceof MqttMessageDeliveredEvent)
        {
            messageDeliveredCount.incrementAndGet();
        }
        if (event instanceof MqttMessagePublishedEvent)
        {
            messagePublishedCount.incrementAndGet();
        }
    }

    @Override
    public byte[] getConnectedPayload(String clientId, MqttClientConnectionType connectionType)
    {
        return "Client connected!".getBytes();
    }

    @Override
    public byte[] getDisconnectedPayload(String clientId, MqttClientConnectionType connectionType)
    {
        return "Client disconnected!".getBytes();
    }

    @Override
    public MqttQualityOfService getStatusMqttQualityOfService()
    {
        return MqttQualityOfService.QOS_0;
    }

    @Override
    public boolean isStatusMessageRetained()
    {
        return false;
    }

    @Override
    public String getStatusTopic()
    {
        return "client/status";
    }
}
