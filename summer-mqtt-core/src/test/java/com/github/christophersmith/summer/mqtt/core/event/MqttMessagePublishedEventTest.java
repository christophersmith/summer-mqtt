package com.github.christophersmith.summer.mqtt.core.event;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MqttMessagePublishedEventTest
{
    private static final String CLIENT_ID      = "TESTCLIENT";
    private static final int    MESSAGE_ID     = 12;
    private static final String CORRELATION_ID = UUID.randomUUID().toString();
    @Rule
    public ExpectedException    thrown         = ExpectedException.none();

    @Test
    public void test()
    {
        MqttMessagePublishedEvent event = new MqttMessagePublishedEvent(CLIENT_ID, MESSAGE_ID,
            CORRELATION_ID, this);
        Assert.assertEquals(MESSAGE_ID, event.getMessageIdentifier());
        Assert.assertEquals(CORRELATION_ID, event.getCorrelationId());
        event = new MqttMessagePublishedEvent(CLIENT_ID, MESSAGE_ID, null, this);
        Assert.assertNull(event.getCorrelationId());
    }

    @Test
    public void testNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'clientId' must be set!");
        new MqttMessagePublishedEvent(null, MESSAGE_ID, CORRELATION_ID, this);
    }
}
