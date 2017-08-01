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
package com.github.christophersmith.summer.mqtt.core;

import org.springframework.util.Assert;

import com.github.christophersmith.summer.mqtt.core.service.MqttClientService;

/**
 * A configuration class that allows you to affect behaviors of the {@link MqttClientService}
 * instance.
 */
public final class MqttClientConfiguration
{
    private MqttQualityOfService                defaultQualityOfService                 = MqttQualityOfService.QOS_0;
    private long                                subscribeWaitMilliseconds               = 30000;
    private long                                topicUnsubscribeWaitTimeoutMilliseconds = 30000;
    private long                                disconnectWaitMilliseconds              = 60000;
    private MqttClientConnectionStatusPublisher mqttClientConnectionStatusPublisher;

    /**
     * The default constructor.
     */
    public MqttClientConfiguration()
    {
        super();
    }

    /**
     * Returns the default {@link MqttQualityOfService} instance.
     * <p>
     * This default {@link MqttQualityOfService} will be used for published messages that haven't
     * supplied a {@link MqttQualityOfService} in the Message Header.
     * <p>
     * The default value is {@link MqttQualityOfService#QOS_0}.
     * 
     * @return a {@link MqttQualityOfService} value
     */
    public MqttQualityOfService getDefaultQualityOfService()
    {
        return defaultQualityOfService;
    }

    /**
     * Sets the default {@link MqttQualityOfService} to use when published messages haven't defined
     * one.
     * 
     * @param defaultQualityOfService the {@link MqttQualityOfService}
     * @throws IllegalArgumentException if the parameter {@code defaultQualityOfService} is null
     */
    public void setDefaultQualityOfService(MqttQualityOfService defaultQualityOfService)
    {
        Assert.notNull(defaultQualityOfService, "'defaultQualityOfService' must be set!");
        this.defaultQualityOfService = defaultQualityOfService;
    }

    /**
     * Returns the wait time, in milliseconds, the {@link MqttClientService} instance will wait to
     * subscribe to a Topic Filter.
     * <p>
     * The default value is 30,000 milliseconds.
     * 
     * @return the Subscribe Wait Time in Milliseconds
     */
    public long getSubscribeWaitMilliseconds()
    {
        return subscribeWaitMilliseconds;
    }

    /**
     * Sets the wait time, in milliseconds, the {@link MqttClientService} instance will wait to
     * subscribe to a Topic Filter.
     * 
     * @param subscribeWaitMilliseconds the Subscribe Wait Time in Milliseconds
     */
    public void setSubscribeWaitMilliseconds(long subscribeWaitMilliseconds)
    {
        this.subscribeWaitMilliseconds = subscribeWaitMilliseconds;
    }

    /**
     * Returns the wait time, in milliseconds, the {@link MqttClientService} instance will wait to
     * unsubscribe from a Topic Filter.
     * <p>
     * The default value is 30,000 milliseconds.
     * 
     * @return the Unsubscribe Wait Time in Milliseconds
     */
    public long getTopicUnsubscribeWaitTimeoutMilliseconds()
    {
        return topicUnsubscribeWaitTimeoutMilliseconds;
    }

    /**
     * Sets the wait time, in milliseconds, the {@link MqttClientService} instance will wait to
     * unsubscribe from a Topic Filter.
     * 
     * @param topicUnsubscribeWaitTimeoutMilliseconds the Unsubscribe Wait Time in Milliseconds
     */
    public void setTopicUnsubscribeWaitTimeoutMilliseconds(
        long topicUnsubscribeWaitTimeoutMilliseconds)
    {
        this.topicUnsubscribeWaitTimeoutMilliseconds = topicUnsubscribeWaitTimeoutMilliseconds;
    }

    /**
     * Returns the wait time, in milliseconds, the {@link MqttClientService} instance will wait to
     * disconnect from the Broker.
     * <p>
     * The default value is 60,000 milliseconds.
     * 
     * @return the Disconnect Wait Time in Milliseconds
     */
    public long getDisconnectWaitMilliseconds()
    {
        return disconnectWaitMilliseconds;
    }

    /**
     * Sets the wait time, in milliseconds, the {@link MqttClientService} instance will wait to
     * disconnect from the Broker.
     * 
     * @param disconnectWaitMilliseconds the Disconnect Wait Time in Milliseconds
     */
    public void setDisconnectWaitMilliseconds(long disconnectWaitMilliseconds)
    {
        this.disconnectWaitMilliseconds = disconnectWaitMilliseconds;
    }

    /**
     * Returns the defined {@link MqttClientConnectionStatusPublisher} to use for this instance.
     * <p>
     * The default value is {@literal null}.
     * 
     * @return a {@link MqttClientConnectionStatusPublisher}, or null if undefined
     */
    public MqttClientConnectionStatusPublisher getMqttClientConnectionStatusPublisher()
    {
        return mqttClientConnectionStatusPublisher;
    }

    /**
     * Sets the {@link MqttClientConnectionStatusPublisher}, or null if undefined.
     * 
     * @param mqttClientConnectionStatusPublisher the {@link MqttClientConnectionStatusPublisher},
     *            or null if undefined
     */
    public void setMqttClientConnectionStatusPublisher(
        MqttClientConnectionStatusPublisher mqttClientConnectionStatusPublisher)
    {
        this.mqttClientConnectionStatusPublisher = mqttClientConnectionStatusPublisher;
    }
}
