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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;

/**
 * TODO
 */
public final class MqttHeaderHelper
{
    private static final Logger LOG            = LoggerFactory.getLogger(MqttHeaderHelper.class);
    /**
     * 
     */
    public static final String  DUPLICATE      = "mqtt_duplicate";
    /**
     * 
     */
    public static final String  ID             = "mqtt_id";
    /**
     * 
     */
    public static final String  QOS            = "mqtt_qos";
    /**
     * 
     */
    public static final String  RETAINED       = "mqtt_retained";
    /**
     * 
     */
    public static final String  TOPIC          = "mqtt_topic";
    /**
     * 
     */
    public static final String  CORRELATION_ID = "mqtt_correlation_id";

    /**
     * TODO
     * 
     * @param message
     * @return
     */
    public static String getTopicHeaderValue(Message<?> message)
    {
        String value = null;
        if (message != null
            && message.getHeaders().containsKey(TOPIC))
        {
            value = message.getHeaders().get(TOPIC, String.class);
        }
        return value;
    }

    /**
     * TODO
     * 
     * @param message
     * @param defaultLevelIdentifier
     * @return
     */
    public static MqttQualityOfService getMqttQualityOfServiceHeaderValue(Message<?> message,
        int defaultLevelIdentifier)
    {
        Integer levelIdentifier = defaultLevelIdentifier;
        try
        {
            if (message != null
                && message.getHeaders().containsKey(QOS))
            {
                levelIdentifier = message.getHeaders().get(QOS, Integer.class);
            }
        }
        catch (IllegalArgumentException ex)
        {
            // TODO: need a message here
            LOG.debug("", ex);
        }
        return MqttQualityOfService.findByLevelIdentifier(levelIdentifier);
    }

    /**
     * TODO
     * 
     * @param message
     * @return
     */
    public static boolean getRetainedHeaderValue(Message<?> message)
    {
        boolean retained = false;
        try
        {
            if (message != null
                && message.getHeaders().containsKey(RETAINED))
            {
                retained = message.getHeaders().get(RETAINED, Boolean.class);
            }
        }
        catch (IllegalArgumentException ex)
        {
            // TODO: need a message here
            LOG.debug("", ex);
        }
        return retained;
    }

    /**
     * 
     * @param message
     * @return
     */
    public static String getCorrelationIdHeaderValue(Message<?> message)
    {
        String correlationId = null;
        if (message != null
            && message.getHeaders().containsKey(CORRELATION_ID))
        {
            correlationId = message.getHeaders().get(CORRELATION_ID, String.class);
        }
        return correlationId;
    }
}
