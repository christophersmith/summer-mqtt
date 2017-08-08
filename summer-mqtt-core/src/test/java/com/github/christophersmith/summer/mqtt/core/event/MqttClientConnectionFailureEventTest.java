package com.github.christophersmith.summer.mqtt.core.event;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MqttClientConnectionFailureEventTest
{
    private static final String CLIENT_ID = "TESTCLIENT";
    @Rule
    public ExpectedException    thrown    = ExpectedException.none();

    @Test
    public void test()
    {
        MqttClientConnectionFailureEvent event = new MqttClientConnectionFailureEvent(CLIENT_ID,
            true, new Exception(), this);
        Assert.assertTrue(event.isAutoReconnect());
        Assert.assertNotNull(event.getThrowable());
        event = new MqttClientConnectionFailureEvent(CLIENT_ID, false, new Exception(), this);
        Assert.assertFalse(event.isAutoReconnect());
    }
}
