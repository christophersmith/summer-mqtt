/*******************************************************************************
 * Copyright (c) 2017 Christopher Smith
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
package com.github.christophersmith.summer.mqtt.core.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.messaging.support.MessageBuilder;

import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;

public class MqttHeaderHelperTest
{
    private static final String TOPIC = "status/client";

    @Test
    public void testTopicHeader()
    {
        Assert.assertNull(MqttHeaderHelper.getTopicHeaderValue(null));
        MessageBuilder<String> builder = MessageBuilder.withPayload("See topic header");
        Assert.assertNull(MqttHeaderHelper.getTopicHeaderValue(builder.build()));
        builder.setHeader(MqttHeaderHelper.TOPIC, TOPIC);
        Assert.assertEquals(TOPIC, MqttHeaderHelper.getTopicHeaderValue(builder.build()));
    }

    @Test
    public void testMqttQualityOfServiceHeader()
    {
        Assert.assertEquals(MqttQualityOfService.QOS_2,
            MqttHeaderHelper.getMqttQualityOfServiceHeaderValue(null,
                MqttQualityOfService.QOS_2.getLevelIdentifier()));
        MessageBuilder<String> builder = MessageBuilder.withPayload("See QoS header");
        Assert.assertEquals(MqttQualityOfService.QOS_2,
            MqttHeaderHelper.getMqttQualityOfServiceHeaderValue(builder.build(),
                MqttQualityOfService.QOS_2.getLevelIdentifier()));
        builder.setHeader(MqttHeaderHelper.QOS, "blah!");
        Assert.assertEquals(MqttQualityOfService.QOS_2,
            MqttHeaderHelper.getMqttQualityOfServiceHeaderValue(builder.build(),
                MqttQualityOfService.QOS_2.getLevelIdentifier()));
        builder.setHeader(MqttHeaderHelper.QOS, MqttQualityOfService.QOS_1.getLevelIdentifier());
        Assert.assertEquals(MqttQualityOfService.QOS_1,
            MqttHeaderHelper.getMqttQualityOfServiceHeaderValue(builder.build(),
                MqttQualityOfService.QOS_2.getLevelIdentifier()));
    }

    @Test
    public void testRetainedHeader()
    {
        Assert.assertFalse(MqttHeaderHelper.getRetainedHeaderValue(null));
        MessageBuilder<String> builder = MessageBuilder.withPayload("See retained header");
        Assert.assertFalse(MqttHeaderHelper.getRetainedHeaderValue(builder.build()));
        builder.setHeader(MqttHeaderHelper.RETAINED, "foo");
        Assert.assertFalse(MqttHeaderHelper.getRetainedHeaderValue(builder.build()));
        builder.setHeader(MqttHeaderHelper.RETAINED, true);
        Assert.assertTrue(MqttHeaderHelper.getRetainedHeaderValue(builder.build()));
    }

    @Test
    public void testCorrelationIdHeader()
    {
        Assert.assertNull(MqttHeaderHelper.getCorrelationIdHeaderValue(null));
        MessageBuilder<String> builder = MessageBuilder.withPayload("See Correlation ID header");
        Assert.assertNull(MqttHeaderHelper.getCorrelationIdHeaderValue(builder.build()));
        builder.setHeader(MqttHeaderHelper.CORRELATION_ID, "foo");
        Assert.assertSame("foo", MqttHeaderHelper.getCorrelationIdHeaderValue(builder.build()));
    }
}
