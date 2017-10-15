package com.github.christophersmith.summer.mqtt.paho.service;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.paho.service.util.BrokerHelper;

public class PahoAsyncMqttClientServiceTest
{
    private static final String VALUE_BLANK                  = "";
    private static final String EXCEPTION_MESSAGE_SERVER_URI = "'serverUri' must be set!";
    private static final String EXCEPTION_MESSAGE_CLIENT_ID  = "'clientId' must be set!";
    @Rule
    public ExpectedException    thrown                       = ExpectedException.none();

    @Test
    public void testConstructionBlankServerUri() throws MqttException
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_SERVER_URI);
        new PahoAsyncMqttClientService(VALUE_BLANK, BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
    }

    @Test
    public void testConstructionNullServerUri() throws MqttException
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_SERVER_URI);
        new PahoAsyncMqttClientService(null, BrokerHelper.getClientId(),
            MqttClientConnectionType.PUBSUB, null);
    }

    @Test
    public void testConstructionBlankClientId() throws MqttException
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        new PahoAsyncMqttClientService(BrokerHelper.getBrokerUri(), VALUE_BLANK,
            MqttClientConnectionType.PUBSUB, null);
    }

    @Test
    public void testConstructionNullClientId() throws MqttException
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        new PahoAsyncMqttClientService(BrokerHelper.getBrokerUri(), null,
            MqttClientConnectionType.PUBSUB, null);
    }

    @Test
    public void testConstructionNullConnectionType() throws MqttException
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'connectionType' must be set!");
        new PahoAsyncMqttClientService(BrokerHelper.getBrokerUri(), BrokerHelper.getClientId(),
            null, null);
    }
}
