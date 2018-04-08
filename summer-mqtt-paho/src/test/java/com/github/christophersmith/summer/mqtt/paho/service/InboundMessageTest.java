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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.core.util.MqttHeaderHelper;
import com.github.christophersmith.summer.mqtt.paho.service.util.BrokerHelper;

public class InboundMessageTest implements MessageHandler
{
    private static final int                  TASK_EXECUTOR_CORE_POOL_SIZE = 2;
    private ConcurrentMap<String, Message<?>> inboundMessages              = new ConcurrentHashMap<String, Message<?>>();

    @Test
    public void test() throws MqttException, InterruptedException
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(TASK_EXECUTOR_CORE_POOL_SIZE);
        executor.initialize();
        ExecutorSubscribableChannel inboundMessageChannel = new ExecutorSubscribableChannel(
            executor);
        inboundMessageChannel.subscribe(this);
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()));
        service.setInboundMessageChannel(inboundMessageChannel);
        service.start();
        service.handleMessage(MessageBuilder.withPayload("Test").setHeader(MqttHeaderHelper.TOPIC,
            String.format("client/%s", BrokerHelper.getClientId())).build());
        Thread.sleep(1000);
        Assert.assertTrue(inboundMessages.containsKey(String.format("%s||%s",
            String.format("client/%s", BrokerHelper.getClientId()), "Test")));
        service.stop();
        service.close();
        executor.shutdown();
    }


    @Override
    public void handleMessage(Message<?> message) throws MessagingException
    {
        if (message.getPayload() != null
            && message.getPayload() instanceof byte[])
        {
            String topic = (String) message.getHeaders().get(MqttHeaderHelper.TOPIC);
            String payload = new String((byte[]) message.getPayload());
            inboundMessages.put(String.format("%s||%s", topic, payload), message);
        }
    }
}
