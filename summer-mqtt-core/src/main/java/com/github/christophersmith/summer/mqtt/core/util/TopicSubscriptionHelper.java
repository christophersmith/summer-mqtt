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

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.github.christophersmith.summer.mqtt.core.TopicSubscription;

/**
 * A {@link TopicSubscription} utility class that provides common functionality.
 */
public final class TopicSubscriptionHelper
{
    /**
     * Finds the {@link TopicSubscription} object from the {@code topicSubscriptions} list that
     * matches the {@code topicFilter}.
     * <p>
     * The search on the {@code topicFilter} is case-sensitive.
     * 
     * @param topicFilter
     * @param topicSubscriptions
     * 
     * @return a {@link TopicSubscription} object, or null if not found
     */
    public static TopicSubscription findByTopicFilter(final String topicFilter,
        List<TopicSubscription> topicSubscriptions)
    {
        TopicSubscription record = null;
        if (StringUtils.hasText(topicFilter)
            && !CollectionUtils.isEmpty(topicSubscriptions))
        {
            for (TopicSubscription topicSubscription : topicSubscriptions)
            {
                if (topicSubscription.getTopicFilter().equals(topicFilter))
                {
                    record = topicSubscription;
                    break;
                }
            }
        }
        return record;
    }

    public static String[] getSubscribedTopicFilters(List<TopicSubscription> topicSubscriptions)
    {
        List<String> records = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(topicSubscriptions))
        {
            for (TopicSubscription topicSubscription : topicSubscriptions)
            {
                if (topicSubscription.isSubscribed())
                {
                    records.add(topicSubscription.getTopicFilter());
                }
            }
        }
        return records.toArray(new String[records.size()]);
    }

    public static void markUnsubscribed(List<TopicSubscription> topicSubscriptions)
    {
        if (!CollectionUtils.isEmpty(topicSubscriptions))
        {
            for (TopicSubscription topicSubscription : topicSubscriptions)
            {
                topicSubscription.setSubscribed(false);
            }
        }
    }
}
