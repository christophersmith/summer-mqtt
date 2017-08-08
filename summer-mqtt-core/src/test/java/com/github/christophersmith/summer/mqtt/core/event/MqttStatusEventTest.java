package com.github.christophersmith.summer.mqtt.core.event;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MqttStatusEventTest
{
    private static final String CLIENT_ID                   = "TESTCLIENT";
    private static final String EXCEPTION_MESSAGE_CLIENT_ID = "'clientId' must be set!";
    @Rule
    public ExpectedException    thrown                      = ExpectedException.none();

    @Test
    public void test()
    {
        MqttStatusEvent event = new MqttStatusEvent(CLIENT_ID, this);
        Assert.assertEquals(CLIENT_ID, event.getClientId());
    }

    @Test
    public void testNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        new MqttStatusEvent(null, this);
    }

    @Test
    public void testBlankClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        new MqttStatusEvent("", this);
    }
}
