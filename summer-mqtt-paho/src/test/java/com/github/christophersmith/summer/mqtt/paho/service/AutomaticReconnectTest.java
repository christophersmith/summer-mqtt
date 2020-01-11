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
package com.github.christophersmith.summer.mqtt.paho.service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netcrusher.core.reactor.NioReactor;
import org.netcrusher.tcp.TcpCrusher;
import org.netcrusher.tcp.TcpCrusherBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionFailureEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionLostEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientDisconnectedEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttConnectionStatusEvent;
import com.github.christophersmith.summer.mqtt.paho.service.util.BrokerHelper;
import com.github.christophersmith.summer.mqtt.paho.service.util.DefaultReconnectService;

public class AutomaticReconnectTest implements ApplicationListener<MqttConnectionStatusEvent>
{
    private static NioReactor REACTOR;
    private static TcpCrusher CRUSHER_PROXY;
    private AtomicInteger     clientConnectedCount        = new AtomicInteger(0);
    private AtomicInteger     clientDisconnectedCount     = new AtomicInteger(0);
    private AtomicInteger     clientLostConnectionCount   = new AtomicInteger(0);
    private AtomicInteger     clientFailedConnectionCount = new AtomicInteger(0);

    @BeforeClass
    public static void initialize() throws IOException
    {
        REACTOR = new NioReactor();
        CRUSHER_PROXY = TcpCrusherBuilder.builder().withReactor(REACTOR)
            .withBindAddress(BrokerHelper.getProxyHostName(), BrokerHelper.getProxyPort())
            .withConnectAddress(BrokerHelper.getBrokerHostName(), BrokerHelper.getBrokerPort())
            .buildAndOpen();
    }

    @AfterClass
    public static void shutdown()
    {
        CRUSHER_PROXY.close();
        REACTOR.close();
    }

    private StaticApplicationContext getStaticApplicationContext()
    {
        clientConnectedCount.set(0);
        clientDisconnectedCount.set(0);
        clientLostConnectionCount.set(0);
        clientFailedConnectionCount.set(0);
        StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.addApplicationListener(this);
        applicationContext.refresh();
        applicationContext.start();
        return applicationContext;
    }


    @Test
    public void testGoodConnection() throws MqttException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        MessageChannel inboundMessageChannel = new ExecutorSubscribableChannel();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getProxyUri(), BrokerHelper.getClientId(), MqttClientConnectionType.PUBSUB,
            null);
        service.setApplicationEventPublisher(applicationContext);
        service.setInboundMessageChannel(inboundMessageChannel);
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        service.getMqttConnectOptions().setCleanSession(true);
        Assert.assertTrue(service.start());
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Assert.assertEquals(1, clientConnectedCount.get());
        Assert.assertEquals(0, clientDisconnectedCount.get());
        Assert.assertEquals(0, clientLostConnectionCount.get());
        Assert.assertEquals(0, clientFailedConnectionCount.get());
        service.stop();
        service.close();
        applicationContext.close();
    }

    @Test
    public void testMqttConnectOptionsAutomaticReconnectDefaultServerAvailableAtStartup()
        throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        MessageChannel inboundMessageChannel = new ExecutorSubscribableChannel();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getProxyUri(), BrokerHelper.getClientId(), MqttClientConnectionType.PUBSUB,
            null);
        service.setApplicationEventPublisher(applicationContext);
        service.setInboundMessageChannel(inboundMessageChannel);
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        service.getMqttConnectOptions().setCleanSession(true);
        Assert.assertTrue(service.start());
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        // simulate a lost connection
        CRUSHER_PROXY.reopen();
        Assert.assertFalse(service.isStarted());
        Assert.assertFalse(service.isConnected());
        Thread.sleep(1000);
        Assert.assertFalse(service.isStarted());
        Assert.assertFalse(service.isConnected());
        Assert.assertEquals(1, clientConnectedCount.get());
        Assert.assertEquals(0, clientDisconnectedCount.get());
        Assert.assertEquals(1, clientLostConnectionCount.get());
        Assert.assertEquals(0, clientFailedConnectionCount.get());
        service.stop();
        service.close();
        applicationContext.close();
    }

    @Test
    public void testMqttConnectOptionsAutomaticReconnectFalseServerAvailableAtStartup()
        throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        MessageChannel inboundMessageChannel = new ExecutorSubscribableChannel();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getProxyUri(), BrokerHelper.getClientId(), MqttClientConnectionType.PUBSUB,
            null);
        service.setApplicationEventPublisher(applicationContext);
        service.setInboundMessageChannel(inboundMessageChannel);
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        service.getMqttConnectOptions().setCleanSession(true);
        service.getMqttConnectOptions().setAutomaticReconnect(false);
        Assert.assertTrue(service.start());
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        // simulate a lost connection
        CRUSHER_PROXY.reopen();
        Assert.assertFalse(service.isStarted());
        Assert.assertFalse(service.isConnected());
        Thread.sleep(1000);
        Assert.assertFalse(service.isStarted());
        Assert.assertFalse(service.isConnected());
        Assert.assertEquals(1, clientConnectedCount.get());
        Assert.assertEquals(0, clientDisconnectedCount.get());
        Assert.assertEquals(1, clientLostConnectionCount.get());
        Assert.assertEquals(0, clientFailedConnectionCount.get());
        service.stop();
        service.close();
        applicationContext.close();
    }

    @Test
    public void testMqttConnectOptionsAutomaticReconnectTrueServerAvailableAtStartup()
        throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        MessageChannel inboundMessageChannel = new ExecutorSubscribableChannel();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getProxyUri(), BrokerHelper.getClientId(), MqttClientConnectionType.PUBSUB,
            null);
        service.setApplicationEventPublisher(applicationContext);
        service.setInboundMessageChannel(inboundMessageChannel);
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        service.getMqttConnectOptions().setCleanSession(true);
        service.getMqttConnectOptions().setAutomaticReconnect(true);
        Assert.assertTrue(service.start());
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        // simulate a lost connection
        CRUSHER_PROXY.reopen();
        Assert.assertFalse(service.isStarted());
        Assert.assertFalse(service.isConnected());
        Thread.sleep(2100);
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Assert.assertEquals(2, clientConnectedCount.get());
        Assert.assertEquals(0, clientDisconnectedCount.get());
        Assert.assertEquals(1, clientLostConnectionCount.get());
        Assert.assertEquals(0, clientFailedConnectionCount.get());
        service.stop();
        service.close();
        applicationContext.close();
    }

    @Test
    public void testMqttConnectOptionsAutomaticReconnectDefaultServerUnavailableAtStartup()
        throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        CRUSHER_PROXY.close();
        MessageChannel inboundMessageChannel = new ExecutorSubscribableChannel();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getProxyUri(), BrokerHelper.getClientId(), MqttClientConnectionType.PUBSUB,
            null);
        service.setApplicationEventPublisher(applicationContext);
        service.setInboundMessageChannel(inboundMessageChannel);
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        service.getMqttConnectOptions().setCleanSession(true);
        Assert.assertFalse(service.start());
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        Thread.sleep(1000);
        CRUSHER_PROXY.open();
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        Thread.sleep(1100);
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        Assert.assertEquals(0, clientConnectedCount.get());
        Assert.assertEquals(0, clientDisconnectedCount.get());
        Assert.assertEquals(0, clientLostConnectionCount.get());
        Assert.assertEquals(1, clientFailedConnectionCount.get());
        service.stop();
        service.close();
        applicationContext.close();
    }

    @Test
    public void testMqttConnectOptionsAutomaticReconnectFalseServerUnavailableAtStartup()
        throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        CRUSHER_PROXY.close();
        MessageChannel inboundMessageChannel = new ExecutorSubscribableChannel();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getProxyUri(), BrokerHelper.getClientId(), MqttClientConnectionType.PUBSUB,
            null);
        service.setApplicationEventPublisher(applicationContext);
        service.setInboundMessageChannel(inboundMessageChannel);
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        service.getMqttConnectOptions().setCleanSession(true);
        service.getMqttConnectOptions().setAutomaticReconnect(false);
        Assert.assertFalse(service.start());
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        Thread.sleep(1000);
        CRUSHER_PROXY.open();
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        Thread.sleep(1100);
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        Assert.assertEquals(0, clientConnectedCount.get());
        Assert.assertEquals(0, clientDisconnectedCount.get());
        Assert.assertEquals(0, clientLostConnectionCount.get());
        Assert.assertEquals(1, clientFailedConnectionCount.get());
        service.stop();
        service.close();
        applicationContext.close();
    }

    @Test
    public void testMqttConnectOptionsAutomaticReconnectTrueServerUnavailableAtStartup()
        throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        CRUSHER_PROXY.close();
        MessageChannel inboundMessageChannel = new ExecutorSubscribableChannel();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getProxyUri(), BrokerHelper.getClientId(), MqttClientConnectionType.PUBSUB,
            null);
        service.setApplicationEventPublisher(applicationContext);
        service.setInboundMessageChannel(inboundMessageChannel);
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        service.getMqttConnectOptions().setCleanSession(true);
        service.getMqttConnectOptions().setAutomaticReconnect(true);
        Assert.assertFalse(service.start());
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        Thread.sleep(1000);
        CRUSHER_PROXY.open();
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        Thread.sleep(1100);
        Assert.assertFalse(service.isStarted());
        Assert.assertFalse(service.isConnected());
        Thread.sleep(1100);
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        Assert.assertEquals(0, clientConnectedCount.get());
        Assert.assertEquals(0, clientDisconnectedCount.get());
        Assert.assertEquals(0, clientLostConnectionCount.get());
        Assert.assertEquals(1, clientFailedConnectionCount.get());
        service.stop();
        service.close();
        applicationContext.close();
    }

    @Test
    public void testReconnectDetailsSetServerAvailableAtStartup()
        throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        TaskScheduler taskScheduler = new ConcurrentTaskScheduler();
        MessageChannel inboundMessageChannel = new ExecutorSubscribableChannel();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getProxyUri(), BrokerHelper.getClientId(), MqttClientConnectionType.PUBSUB,
            null);
        service.setApplicationEventPublisher(applicationContext);
        service.setInboundMessageChannel(inboundMessageChannel);
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        service.getMqttConnectOptions().setCleanSession(true);
        service.setReconnectDetails(new DefaultReconnectService(), taskScheduler);
        Assert.assertTrue(service.start());
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        // simulate a lost connection
        CRUSHER_PROXY.reopen();
        Assert.assertFalse(service.isStarted());
        Assert.assertFalse(service.isConnected());
        Thread.sleep(2100);
        Assert.assertTrue(service.isStarted());
        Assert.assertTrue(service.isConnected());
        Assert.assertEquals(2, clientConnectedCount.get());
        Assert.assertEquals(0, clientDisconnectedCount.get());
        Assert.assertEquals(1, clientLostConnectionCount.get());
        Assert.assertEquals(0, clientFailedConnectionCount.get());
        service.stop();
        service.close();
        applicationContext.close();
    }

    @Test
    public void testReconnectDetailsSetServerUnavailableAtStartup()
        throws MqttException, InterruptedException
    {
        StaticApplicationContext applicationContext = getStaticApplicationContext();
        CRUSHER_PROXY.close();
        TaskScheduler taskScheduler = new ConcurrentTaskScheduler();
        MessageChannel inboundMessageChannel = new ExecutorSubscribableChannel();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getProxyUri(), BrokerHelper.getClientId(), MqttClientConnectionType.PUBSUB,
            null);
        service.setApplicationEventPublisher(applicationContext);
        service.setInboundMessageChannel(inboundMessageChannel);
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        service.getMqttConnectOptions().setCleanSession(true);
        service.setReconnectDetails(new DefaultReconnectService(), taskScheduler);
        Assert.assertFalse(service.start());
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        CRUSHER_PROXY.open();
        Thread.sleep(3100);
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Assert.assertEquals(1, clientConnectedCount.get());
        Assert.assertEquals(0, clientDisconnectedCount.get());
        Assert.assertEquals(0, clientLostConnectionCount.get());
        Assert.assertEquals(1, clientFailedConnectionCount.get());
        service.stop();
        service.close();
        applicationContext.close();
    }

    @Override
    public void onApplicationEvent(MqttConnectionStatusEvent event)
    {
        if (event instanceof MqttClientConnectedEvent)
        {
            clientConnectedCount.incrementAndGet();
        }
        if (event instanceof MqttClientConnectionLostEvent)
        {
            clientLostConnectionCount.incrementAndGet();
        }
        if (event instanceof MqttClientDisconnectedEvent)
        {
            clientDisconnectedCount.incrementAndGet();
        }
        if (event instanceof MqttClientConnectionFailureEvent)
        {
            clientFailedConnectionCount.incrementAndGet();
        }
    }
}
