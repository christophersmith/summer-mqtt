package com.github.christophersmith.summer.mqtt.core.event;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MqttMessageDeliveredEventTest
{
    private static final String CLIENT_ID  = "TESTCLIENT";
    private static final int    MESSAGE_ID = 12;
    @Rule
    public ExpectedException    thrown     = ExpectedException.none();

    @Test
    public void test()
    {
        MqttMessageDeliveredEvent event = new MqttMessageDeliveredEvent(CLIENT_ID, MESSAGE_ID,
            this);
        Assert.assertEquals(MESSAGE_ID, event.getMessageIdentifier());
    }

    @Test
    public void testNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'clientId' must be set!");
        new MqttMessageDeliveredEvent(null, MESSAGE_ID, this);
    }
}
