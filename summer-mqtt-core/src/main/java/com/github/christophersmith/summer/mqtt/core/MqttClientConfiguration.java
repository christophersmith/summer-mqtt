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

public final class MqttClientConfiguration
{
    private MqttQualityOfService                defaultQualityOfService                 = MqttQualityOfService.AT_MOST_ONCE;
    private long                                subscribeWaitMilliseconds               = 30;
    private long                                topicUnsubscribeWaitTimeoutMilliseconds = 30;
    private long                                disconnectWaitMilliseconds              = 60;
    private MqttClientConnectionStatusPublisher mqttClientConnectionStatusPublisher;

    public MqttClientConfiguration()
    {
        super();
    }

    public MqttQualityOfService getDefaultQualityOfService()
    {
        return defaultQualityOfService;
    }

    public void setDefaultQualityOfService(MqttQualityOfService defaultQualityOfService)
    {
        Assert.notNull(defaultQualityOfService, "'defaultQualityOfService' must be set!");
        this.defaultQualityOfService = defaultQualityOfService;
    }

    public long getSubscribeWaitMilliseconds()
    {
        return subscribeWaitMilliseconds;
    }

    public void setSubscribeWaitMilliseconds(long subscribeWaitMilliseconds)
    {
        this.subscribeWaitMilliseconds = subscribeWaitMilliseconds;
    }

    public long getTopicUnsubscribeWaitTimeoutMilliseconds()
    {
        return topicUnsubscribeWaitTimeoutMilliseconds;
    }

    public void setTopicUnsubscribeWaitTimeoutMilliseconds(
        long topicUnsubscribeWaitTimeoutMilliseconds)
    {
        this.topicUnsubscribeWaitTimeoutMilliseconds = topicUnsubscribeWaitTimeoutMilliseconds;
    }

    public long getDisconnectWaitMilliseconds()
    {
        return disconnectWaitMilliseconds;
    }

    public void setDisconnectWaitMilliseconds(long disconnectWaitMilliseconds)
    {
        this.disconnectWaitMilliseconds = disconnectWaitMilliseconds;
    }

    public MqttClientConnectionStatusPublisher getMqttClientConnectionStatusPublisher()
    {
        return mqttClientConnectionStatusPublisher;
    }

    public void setMqttClientConnectionStatusPublisher(
        MqttClientConnectionStatusPublisher mqttClientConnectionStatusPublisher)
    {
        this.mqttClientConnectionStatusPublisher = mqttClientConnectionStatusPublisher;
    }
}
