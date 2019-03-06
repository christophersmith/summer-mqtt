/*******************************************************************************
 * Copyright (c) 2019 Christopher Smith - https://github.com/christophersmith
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
 * A utility class that provides values and commons methods for dealing with {@link Message} header
 * values.
 */
public final class MqttHeaderHelper
{
    private static final Logger LOG            = LoggerFactory.getLogger(MqttHeaderHelper.class);
    /**
     * The {@link Message} header key for whether the in-coming message is likely a duplicate.
     * <p>
     * This header value should always be present for in-coming messages. If this value isn't
     * specifically set, a default value of false will be sent.
     */
    public static final String  DUPLICATE      = "mqtt_duplicate";
    /**
     * The {@link Message} header key for Message ID for the in-coming message.
     * <p>
     * This header value should always be present for in-coming messages and is set by the MQTT
     * Client.
     */
    public static final String  ID             = "mqtt_id";
    /**
     * The {@link Message} header key for the Quality of Service (QoS) the message was received as,
     * or should be published with.
     * <p>
     * This header value should always be present for in-coming and out-going messages. If this
     * value isn't specifically set for out-going message, a defined default value will be sent.
     */
    public static final String  QOS            = "mqtt_qos";
    /**
     * The {@link Message} header key for whether the message was, or should, be retained.
     * <p>
     * This header value should always be present for in-coming and out-going messages. If this
     * value isn't specifically set, a default value of false will be sent.
     */
    public static final String  RETAINED       = "mqtt_retained";
    /**
     * The {@link Message} header key for the Topic the message was received from or should be
     * published to.
     * <p>
     * This header value should always be present for in-coming and out-going messages.
     */
    public static final String  TOPIC          = "mqtt_topic";
    /**
     * The {@link Message} header key for an optional Correlation ID.
     * <p>
     * This can be assigned to published messages for the purpose of associating Message Status
     * Events back to a specific message.
     */
    public static final String  CORRELATION_ID = "mqtt_correlation_id";

    /**
     * Retrieves the {@link #TOPIC} value from the {@code message} parameter.
     * <p>
     * If the {@code message} parameter is null or doesn't contain the {@link #TOPIC} header, a null
     * value is returned.
     * 
     * @param message a {@link Message} value
     * @return the Topic
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
     * Retrieves the {@link #QOS} value from the {@code message} parameter.
     * <p>
     * If the {@code message} parameter is null, doesn't contain the {@link #QOS} header or the
     * value cannot be converted to an Integer, the {@code defaultLevelIdentifier} value is
     * returned.
     * 
     * @param message a {@link Message} value
     * @param defaultLevelIdentifier a default {@link MqttQualityOfService} value if value wasn't
     *            found or couldn't be parsed
     * @return a {@link MqttQualityOfService} value
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
            LOG.debug("Could not convert the QOS header value to an Integer!", ex);
        }
        return MqttQualityOfService.findByLevelIdentifier(levelIdentifier);
    }

    /**
     * Retrieves the {@link #RETAINED} value from the {@code message} parameter.
     * <p>
     * If the {@code message} parameter is null, doesn't contain the {@link #RETAINED} header or the
     * value cannot be converted to a boolean, a false value is returned.
     * 
     * @param message a {@link Message} value
     * @return true or false
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
            LOG.debug("Could not convert the RETAINED header value to a Boolean!", ex);
        }
        return retained;
    }

    /**
     * Retrieves the {@link #CORRELATION_ID} value from the {@code message} parameter.
     * <p>
     * If the {@code message} parameter is null, or if it doesn't contain the
     * {@link #CORRELATION_ID} header, a null value is returned.
     * 
     * @param message a {@link Message} value
     * @return the Correlation ID, or null if not available
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
