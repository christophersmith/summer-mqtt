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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.core.TopicSubscription;

public class TopicSubscriptionHelperTest
{
    private static final String     TOPIC_FILTER       = "status/client";
    private List<TopicSubscription> topicSubscriptions = new ArrayList<TopicSubscription>();

    @Before
    public void initialize()
    {
        topicSubscriptions.clear();
        topicSubscriptions
            .add(new TopicSubscription(TOPIC_FILTER, MqttQualityOfService.AT_MOST_ONCE));
        topicSubscriptions
            .add(new TopicSubscription("incoming/message", MqttQualityOfService.AT_MOST_ONCE));
    }

    @Test
    public void testMatch()
    {
        Assert.assertNotNull(
            TopicSubscriptionHelper.findByTopicFilter(TOPIC_FILTER, topicSubscriptions));
    }

    @Test
    public void testNoMatchNoTopicFilter()
    {
        Assert.assertNull(TopicSubscriptionHelper.findByTopicFilter("test", topicSubscriptions));
    }

    @Test
    public void testNoMatchWrongCase()
    {
        Assert.assertNull(TopicSubscriptionHelper.findByTopicFilter(TOPIC_FILTER.toUpperCase(),
            topicSubscriptions));
    }
}
