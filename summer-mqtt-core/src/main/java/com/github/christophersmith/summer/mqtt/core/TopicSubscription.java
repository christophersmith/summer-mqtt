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
package com.github.christophersmith.summer.mqtt.core;

import org.springframework.util.Assert;

/**
 * A container for a requested subscription that contains the Topic Filter, QoS Level and the
 * subscription state.
 */
public final class TopicSubscription
{
    private transient final String               topicFilter;
    private transient final MqttQualityOfService qualityOfService;
    private boolean                              subscribed;

    /**
     * Creates a new {@code TopicSubscription} object that represents the Topic Filter and
     * subscription information.
     * 
     * @param topicFilter the Topic Filter to subscribe to, which can include wildcards
     * @param qualityOfService the maximum QoS to use for this Topic Filter
     * 
     * @throws IllegalArgumentException if the {@code topicFilter} is null or empty, or if the
     *             {@code qualityOfService} is null
     */
    public TopicSubscription(final String topicFilter, final MqttQualityOfService qualityOfService)
    {
        Assert.hasText(topicFilter, "'topicFilter' must be set!");
        Assert.notNull(qualityOfService, "'qualityOfService' must be set!");
        this.topicFilter = topicFilter;
        this.qualityOfService = qualityOfService;
    }

    /**
     * Returns the Topic Filter for this instance.
     * 
     * @return the Topic Filter for this instance
     */
    public String getTopicFilter()
    {
        return topicFilter;
    }

    /**
     * Returns the {@link MqttQualityOfService} for this instance.
     * 
     * @return a {@link MqttQualityOfService} value
     */
    public MqttQualityOfService getQualityOfService()
    {
        return qualityOfService;
    }

    /**
     * Returns whether the Topic Filter is subscribed to.
     * 
     * @return true if the Topic Filter is subscribed to, otherwise false
     */
    public boolean isSubscribed()
    {
        return subscribed;
    }

    /**
     * Sets whether the Topic Filter is currently subscribed to.
     * 
     * @param subscribed true if the Topic Filter is subscribed to, otherwise false
     */
    public void setSubscribed(boolean subscribed)
    {
        this.subscribed = subscribed;
    }

    /**
     * Returns a deep copy of this instance.
     */
    @Override
    public TopicSubscription clone()
    {
        TopicSubscription record = new TopicSubscription(this.topicFilter, this.qualityOfService);
        record.setSubscribed(this.subscribed);
        return record;
    }
}
