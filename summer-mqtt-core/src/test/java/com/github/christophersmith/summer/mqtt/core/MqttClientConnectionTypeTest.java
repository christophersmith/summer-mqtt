package com.github.christophersmith.summer.mqtt.core;

import org.junit.Assert;
import org.junit.Test;

public class MqttClientConnectionTypeTest
{
    @Test
    public void test()
    {
        Assert.assertEquals(MqttClientConnectionType.PUBLISHER,
            MqttClientConnectionType.valueOf("PUBLISHER"));
    }
}
