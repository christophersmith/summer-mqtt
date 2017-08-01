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

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public final class TopicSubscriptionTest
{
    private static final String TOPIC_FILTER = "status/client";
    @Rule
    public ExpectedException    thrown       = ExpectedException.none();

    @Test
    public void testInvalidTopic() throws IllegalArgumentException
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'topicFilter' must be set!");
        new TopicSubscription(null, MqttQualityOfService.QOS_0);
    }

    @Test
    public void testInvalidQualityOfService() throws IllegalArgumentException
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'qualityOfService' must be set!");
        new TopicSubscription(TOPIC_FILTER, null);
    }

    @Test
    public void testClone()
    {
        TopicSubscription topic = new TopicSubscription(TOPIC_FILTER, MqttQualityOfService.QOS_0);
        TopicSubscription clone = topic.clone();
        Assert.assertEquals(topic.getTopicFilter(), clone.getTopicFilter());
        Assert.assertEquals(topic.getQualityOfService(), clone.getQualityOfService());
        Assert.assertThat(topic.isSubscribed(), Is.is(CoreMatchers.equalTo(clone.isSubscribed())));
        topic.setSubscribed(true);
        Assert.assertThat(topic.isSubscribed(), Is.is(CoreMatchers.not(clone.isSubscribed())));
        clone = topic.clone();
        Assert.assertEquals(topic.getTopicFilter(), clone.getTopicFilter());
        Assert.assertEquals(topic.getQualityOfService(), clone.getQualityOfService());
        Assert.assertThat(topic.isSubscribed(), Is.is(CoreMatchers.equalTo(clone.isSubscribed())));
    }
}
