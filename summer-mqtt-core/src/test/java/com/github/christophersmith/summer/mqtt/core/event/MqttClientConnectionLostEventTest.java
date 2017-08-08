package com.github.christophersmith.summer.mqtt.core.event;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MqttClientConnectionLostEventTest
{
    private static final String CLIENT_ID = "TESTCLIENT";
    @Rule
    public ExpectedException    thrown    = ExpectedException.none();

    @Test
    public void test()
    {
        MqttClientConnectionLostEvent event = new MqttClientConnectionLostEvent(CLIENT_ID, true,
            this);
        Assert.assertTrue(event.isAutoReconnect());
        event = new MqttClientConnectionLostEvent(CLIENT_ID, false, this);
        Assert.assertFalse(event.isAutoReconnect());
    }

    @Test
    public void testNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'clientId' must be set!");
        new MqttClientConnectionLostEvent(null, true, this);
    }
}
