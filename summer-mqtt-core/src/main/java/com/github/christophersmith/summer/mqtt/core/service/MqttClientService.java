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
package com.github.christophersmith.summer.mqtt.core.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.core.TopicSubscription;

/**
 * Enables communication from a MQTT server and Spring application components via one or more
 * {@link MessageChannel} instances.
 * <p>
 * While the actual MQTT Client implementation delves in the specifics, this interface defines the
 * contracts that are available. The specific features this interface define are:
 * 
 * <ul>
 * <li>Connection information and state reporting</li>
 * <li>Starting and stopping the underlying MQTT Client connection</li>
 * <li>Subscribing and unsubscribing from Topic Filters</li>
 * </ul>
 * 
 * For implementations that are configured for receiving in-bound messages, those messages should be
 * sent to an in-bound {@link MessageChannel}. An application provided {@link MessageHandler} would
 * then receive that message and process.
 * <p>
 * For implementations that are configured for sending out-bound messages and are subscribed to an
 * out-bound {@link MessageChannel}, those messages should be published via the implementation's
 * MQTT Client.
 */
public interface MqttClientService extends MessageHandler, ApplicationEventPublisherAware
{
    /**
     * Returns the assigned Client ID for this instance.
     * 
     * @return the assigned Client ID for this instance
     */
    String getClientId();

    /**
     * Returns the assigned {@link MqttClientConnectionType} for this instance.
     * 
     * @return the assigned {@link MqttClientConnectionType} for this instance
     */
    MqttClientConnectionType getConnectionType();

    /**
     * Returns a cloned {@link List} of {@link TopicSubscription} objects this client is concerned
     * with.
     * <p>
     * Since Topic Filters can be added while the MQTT Client is disconnected, this listed provides
     * a way to examine the state of each requested Topic Filter.
     * 
     * @return a cloned {@link List} of {@link TopicSubscription} objects
     */
    List<TopicSubscription> getTopicSubscriptions();

    /**
     * Starts the MQTT Client, if it's not already been started, and subscribes to any Topic Filters
     * that haven't yet been subscribed to.
     * 
     * @return whether the action was successful
     */
    boolean start();

    /**
     * Returns whether the MQTT Client is connected.
     * 
     * @return whether the MQTT Client is connected
     */
    boolean isConnected();

    /**
     * Returns whether the MQTT Client is connected, and all Topic Filters are subscribed to.
     * 
     * @return whether the MQTT Client is connected, and all Topic Filters are subscribed to
     */
    boolean isStarted();

    /**
     * Returns the Server URI that the MQTT Client is connected to.
     * <p>
     * This is useful if you provide a list of Server URIs in the specific MQTT Client
     * implementation, and will return the actual Server URI that was connected to.
     * 
     * @return the Server URI that the MQTT Client is connected to, or null if not connected
     */
    String getConnectedServerUri();

    /**
     * Subscribes to the provided {@code topicFilter} using the default {@link MqttQualityOfService}
     * for this instance.
     * <p>
     * This is a convenience method which calls {@link #subscribe(String, MqttQualityOfService)}.
     * 
     * @see #subscribe(String, MqttQualityOfService)
     * 
     * @param topicFilter the Topic Filter to subscribe to, which can include wildcards
     * 
     * @throws IllegalArgumentException if the {@code topicFilter} is null or empty
     * @throws IllegalStateException if the MQTT Client is setup as a
     *             {@link MqttClientConnectionType#PUBLISHER}
     */
    void subscribe(String topicFilter);

    /**
     * Subscribes to the provided {@code topicFilter} using the provided {@code qualityOfService}.
     * <p>
     * If the MQTT Client isn't connected, the Topic Filter will be added to the Topic Subscription
     * list and subscribed to when the MQTT Client next connects.
     * <p>
     * If the provided {@code topicFilter} is already in the Topic Subscription list and has a
     * different {@link MqttQualityOfService}, that record will be replaced with the new record and
     * the MQTT Client will update the subscription.
     * 
     * @param topicFilter the Topic Filter to subscribe to, which can include wildcards
     * @param qualityOfService the maximum QoS to use for this Topic Filter
     * 
     * @throws IllegalArgumentException if the {@code topicFilter} is null or empty, or if the
     *             {@code qualityOfService} is null
     * @throws IllegalStateException if the MQTT Client is setup as a
     *             {@link MqttClientConnectionType#PUBLISHER}
     */
    void subscribe(String topicFilter, MqttQualityOfService qualityOfService);

    /**
     * Unsubscribes from the provided {@code topicFilter}.
     * <p>
     * If the MQTT Client isn't connected, the Topic Filter will be removed from the Topic
     * Subscription list and won't be subscribed to when the MQTT Client next connects. Otherwise
     * the MQTT Client will unsubscribe immediately, provided that the Topic Filter was subscribed
     * to.
     * 
     * @param topicFilter the Topic Filter to unsubscribe from
     * 
     * @throws IllegalArgumentException if the {@code topic} is null or empty
     * @throws IllegalStateException if the MQTT Client is setup as a
     *             {@link MqttClientConnectionType#PUBLISHER}
     */
    void unsubscribe(String topicFilter);

    /**
     * Disconnects the MQTT Client if it's in a connected state.
     * <p>
     * If the MQTT Client is configured with Clean Sessions, all subscribed Topic Filters are
     * unsubscribed from.
     * <p>
     * If a reconnection task is scheduled, that reconnection task is also canceled.
     */
    void stop();

    void close();

    void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher);
}
