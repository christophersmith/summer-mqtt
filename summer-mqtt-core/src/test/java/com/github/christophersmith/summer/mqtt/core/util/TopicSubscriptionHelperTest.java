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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.core.TopicSubscription;

public class TopicSubscriptionHelperTest
{
    private static final String     TOPIC_FILTER_1     = "status/client";
    private static final String     TOPIC_FILTER_2     = "client/delivered";
    private List<TopicSubscription> topicSubscriptions = new ArrayList<TopicSubscription>();

    @Before
    public void initialize()
    {
        new TopicSubscriptionHelper();
        topicSubscriptions.clear();
        topicSubscriptions.add(new TopicSubscription(TOPIC_FILTER_1, MqttQualityOfService.QOS_0));
        topicSubscriptions.add(new TopicSubscription(TOPIC_FILTER_2, MqttQualityOfService.QOS_0));
    }

    @Test
    public void testFindMatch()
    {
        Assert.assertNotNull(
            TopicSubscriptionHelper.findByTopicFilter(TOPIC_FILTER_1, topicSubscriptions));
    }

    @Test
    public void testFindNoMatchNoTopicFilter()
    {
        Assert.assertNull(TopicSubscriptionHelper.findByTopicFilter("test", topicSubscriptions));
    }

    @Test
    public void testFindNoMatchWrongCase()
    {
        Assert.assertNull(TopicSubscriptionHelper.findByTopicFilter(TOPIC_FILTER_1.toUpperCase(),
            topicSubscriptions));
    }

    @Test
    public void testFindNullTopicFilter()
    {
        Assert.assertNull(TopicSubscriptionHelper.findByTopicFilter(null, topicSubscriptions));
    }

    @Test
    public void testFindEmptyTopicSubscriptions()
    {
        Assert.assertNull(TopicSubscriptionHelper.findByTopicFilter(TOPIC_FILTER_1.toUpperCase(),
            new ArrayList<TopicSubscription>()));
    }

    @Test
    public void testGetSubscribedEmptyTopicSubscriptions()
    {
        Assert.assertArrayEquals(new String[0],
            TopicSubscriptionHelper.getSubscribedTopicFilters(new ArrayList<TopicSubscription>()));
    }

    @Test
    public void testGetSubscribedTopicSubscriptions()
    {
        Assert.assertArrayEquals(new String[0],
            TopicSubscriptionHelper.getSubscribedTopicFilters(topicSubscriptions));
        topicSubscriptions.get(0).setSubscribed(true);
        Assert.assertArrayEquals(new String[]{
            TOPIC_FILTER_1}, TopicSubscriptionHelper.getSubscribedTopicFilters(topicSubscriptions));
        topicSubscriptions.get(1).setSubscribed(true);
        Assert.assertArrayEquals(new String[]{
            TOPIC_FILTER_1,
            TOPIC_FILTER_2}, TopicSubscriptionHelper.getSubscribedTopicFilters(topicSubscriptions));
    }

    @Test
    public void testMarkUnsubscribedEmptyTopicSubscriptions()
    {
        TopicSubscriptionHelper.markUnsubscribed(new ArrayList<TopicSubscription>());
    }

    @Test
    public void testMarkUnsubscribed()
    {
        Assert.assertArrayEquals(new String[0],
            TopicSubscriptionHelper.getSubscribedTopicFilters(topicSubscriptions));
        topicSubscriptions.get(0).setSubscribed(true);
        Assert.assertArrayEquals(new String[]{
            TOPIC_FILTER_1}, TopicSubscriptionHelper.getSubscribedTopicFilters(topicSubscriptions));
        topicSubscriptions.get(1).setSubscribed(true);
        Assert.assertArrayEquals(new String[]{
            TOPIC_FILTER_1,
            TOPIC_FILTER_2}, TopicSubscriptionHelper.getSubscribedTopicFilters(topicSubscriptions));
        TopicSubscriptionHelper.markUnsubscribed(topicSubscriptions);
        Assert.assertArrayEquals(new String[0],
            TopicSubscriptionHelper.getSubscribedTopicFilters(topicSubscriptions));
    }
}
