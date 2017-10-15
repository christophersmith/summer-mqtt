package com.github.christophersmith.summer.mqtt.paho.service;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.netcrusher.core.reactor.NioReactor;
import org.netcrusher.tcp.TcpCrusher;
import org.netcrusher.tcp.TcpCrusherBuilder;

import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.paho.service.util.BrokerHelper;

public class SubscribeUnsubscribeTest
{
    private static final String EXCEPTION_MESSAGE_CANNOT_SUBSCRIBE_FORMAT = "Client ID %s is a %s and cannot subscribe or unsubscribe from Topic Filters.";
    @Rule
    public ExpectedException    thrown                                    = ExpectedException
        .none();

    @Test
    public void testSubscribeBeforeStartClientSubscriber() throws MqttException
    {
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.SUBSCRIBER, null);
        Assert.assertEquals(0, service.getTopicSubscriptions().size());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
    }

    @Test
    public void testSubscribeBeforeStartClientPublisher() throws MqttException
    {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(String.format(EXCEPTION_MESSAGE_CANNOT_SUBSCRIBE_FORMAT,
            BrokerHelper.getClientId(), MqttClientConnectionType.PUBLISHER.name()));
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBLISHER, null);
        Assert.assertEquals(0, service.getTopicSubscriptions().size());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
    }

    @Test
    public void testSubscribeBeforeStartClientPubSub() throws MqttException
    {
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
        Assert.assertEquals(0, service.getTopicSubscriptions().size());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
    }

    @Test
    public void testSubscribeDuplicateBeforeStart() throws MqttException
    {
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
        Assert.assertEquals(0, service.getTopicSubscriptions().size());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
    }

    @Test
    public void testSubscribeDuplicateTopicNameDifferentQualityOfServiceBeforeStart()
        throws MqttException
    {
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
        Assert.assertEquals(0, service.getTopicSubscriptions().size());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_0,
            service.getTopicSubscriptions().get(0).getQualityOfService());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_1);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_1,
            service.getTopicSubscriptions().get(0).getQualityOfService());
    }

    @Test
    public void testSubscribeDuplicateTopicNameDifferentQualityOfServiceAfterStart()
        throws MqttException
    {
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
        service.start();
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Assert.assertEquals(0, service.getTopicSubscriptions().size());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_0,
            service.getTopicSubscriptions().get(0).getQualityOfService());
        Assert.assertTrue(service.getTopicSubscriptions().get(0).isSubscribed());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_1);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_1,
            service.getTopicSubscriptions().get(0).getQualityOfService());
        Assert.assertTrue(service.getTopicSubscriptions().get(0).isSubscribed());
        service.stop();
        service.close();
    }

    @Test
    public void testSubscribeDuplicateTopicNameDifferentQualityOfServiceAfterStartStopStart()
        throws MqttException
    {
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
        service.start();
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Assert.assertEquals(0, service.getTopicSubscriptions().size());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_0,
            service.getTopicSubscriptions().get(0).getQualityOfService());
        Assert.assertTrue(service.getTopicSubscriptions().get(0).isSubscribed());
        service.stop();
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_1);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_1,
            service.getTopicSubscriptions().get(0).getQualityOfService());
        Assert.assertFalse(service.getTopicSubscriptions().get(0).isSubscribed());
        service.start();
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_1,
            service.getTopicSubscriptions().get(0).getQualityOfService());
        Assert.assertTrue(service.getTopicSubscriptions().get(0).isSubscribed());
        service.stop();
        service.close();
    }

    @Test
    public void testSubscribeNonDuplicateTopicNameAfterStartStopStart() throws MqttException
    {
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
        service.start();
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Assert.assertEquals(0, service.getTopicSubscriptions().size());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_0,
            service.getTopicSubscriptions().get(0).getQualityOfService());
        Assert.assertTrue(service.getTopicSubscriptions().get(0).isSubscribed());
        service.stop();
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        service.subscribe("client/test", MqttQualityOfService.QOS_1);
        Assert.assertEquals(2, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_1,
            service.getTopicSubscriptions().get(1).getQualityOfService());
        Assert.assertFalse(service.getTopicSubscriptions().get(1).isSubscribed());
        service.start();
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Assert.assertEquals(2, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_0,
            service.getTopicSubscriptions().get(0).getQualityOfService());
        Assert.assertTrue(service.getTopicSubscriptions().get(0).isSubscribed());
        Assert.assertEquals(MqttQualityOfService.QOS_1,
            service.getTopicSubscriptions().get(1).getQualityOfService());
        Assert.assertTrue(service.getTopicSubscriptions().get(1).isSubscribed());
        service.stop();
        service.close();
    }


    @Test
    public void testSubscribeNonDuplicateTopicNameAfterStartLostConnectionReconnect()
        throws MqttException, IOException, InterruptedException
    {
        NioReactor reactor = new NioReactor();
        TcpCrusher proxy = TcpCrusherBuilder.builder().withReactor(reactor)
            .withBindAddress(BrokerHelper.getProxyHostName(), BrokerHelper.getProxyPort())
            .withConnectAddress(BrokerHelper.getBrokerHostName(), BrokerHelper.getBrokerPort())
            .buildAndOpen();
        PahoAsyncMqttClientService service = new PahoAsyncMqttClientService(
            BrokerHelper.getProxyUri(), BrokerHelper.getClientId(), MqttClientConnectionType.PUBSUB,
            null);
        service.start();
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Assert.assertEquals(0, service.getTopicSubscriptions().size());
        service.subscribe(String.format("client/%s", BrokerHelper.getClientId()),
            MqttQualityOfService.QOS_0);
        Assert.assertEquals(1, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_0,
            service.getTopicSubscriptions().get(0).getQualityOfService());
        Assert.assertTrue(service.getTopicSubscriptions().get(0).isSubscribed());
        // simulate a lost connection
        proxy.reopen();
        Assert.assertFalse(service.isConnected());
        Assert.assertFalse(service.isStarted());
        service.subscribe("client/test", MqttQualityOfService.QOS_1);
        Assert.assertEquals(2, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_0,
            service.getTopicSubscriptions().get(0).getQualityOfService());
        Assert.assertFalse(service.getTopicSubscriptions().get(0).isSubscribed());
        Assert.assertEquals(MqttQualityOfService.QOS_1,
            service.getTopicSubscriptions().get(1).getQualityOfService());
        Assert.assertFalse(service.getTopicSubscriptions().get(1).isSubscribed());
        service.start();
        Assert.assertTrue(service.isConnected());
        Assert.assertTrue(service.isStarted());
        Assert.assertEquals(2, service.getTopicSubscriptions().size());
        Assert.assertEquals(MqttQualityOfService.QOS_0,
            service.getTopicSubscriptions().get(0).getQualityOfService());
        Assert.assertTrue(service.getTopicSubscriptions().get(0).isSubscribed());
        Assert.assertEquals(MqttQualityOfService.QOS_1,
            service.getTopicSubscriptions().get(1).getQualityOfService());
        Assert.assertTrue(service.getTopicSubscriptions().get(1).isSubscribed());
        service.stop();
        service.close();
        proxy.close();
        reactor.close();
    }
}
