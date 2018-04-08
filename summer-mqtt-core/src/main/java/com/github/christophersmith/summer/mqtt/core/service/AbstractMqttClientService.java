/*******************************************************************************
 * Copyright (c) 2018 Christopher Smith - https://github.com/christophersmith
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;

import com.github.christophersmith.summer.mqtt.core.MqttClientConfiguration;
import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.core.TopicSubscription;
import com.github.christophersmith.summer.mqtt.core.util.MqttClientEventPublisher;

/**
 * This class provides common functionality of the {@link MqttClientService} interface, to minimize
 * effort in actual implementations.
 */
public abstract class AbstractMqttClientService implements MqttClientService
{
    protected transient final ReentrantLock            reentrantLock            = new ReentrantLock(
        true);
    protected transient final List<TopicSubscription>  topicSubscriptions       = new ArrayList<TopicSubscription>();
    protected transient final MqttClientConnectionType connectionType;
    protected transient final MqttClientConfiguration  mqttClientConfiguration  = new MqttClientConfiguration();
    protected transient final MqttClientEventPublisher mqttClientEventPublisher = new MqttClientEventPublisher();
    protected ApplicationEventPublisher                applicationEventPublisher;
    protected ReconnectService                         reconnectService;
    protected TaskScheduler                            taskScheduler;
    protected ScheduledFuture<?>                       scheduledFuture;
    protected boolean                                  firstStartOccurred;
    protected boolean                                  started;
    protected MessageChannel                           inboundMessageChannel;

    /**
     * The default constructor.
     * 
     * @param connectionType a {@link MqttClientConnectionType} value
     * 
     * @throws IllegalArgumentException if the {@code connectionType} value is null
     */
    protected AbstractMqttClientService(final MqttClientConnectionType connectionType)
    {
        Assert.notNull(connectionType, "'connectionType' must be set!");
        this.connectionType = connectionType;
    }

    @Override
    public MqttClientConnectionType getConnectionType()
    {
        return connectionType;
    }

    public abstract String getClientId();

    @Override
    public boolean isStarted()
    {
        return started;
    }

    /**
     * Returns the {@link MqttClientConfiguration} object that is associated to this instance.
     * 
     * @return a {@link MqttClientConfiguration} value
     */
    public MqttClientConfiguration getMqttClientConfiguration()
    {
        return mqttClientConfiguration;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
    {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Sets the in-bound {@link MessageChannel} this instance will push messages it receives from
     * the Broker.
     * <p>
     * This is only a valid action if your {@link MqttClientService} instance has a Connection Type
     * of either {@link MqttClientConnectionType#PUBSUB} or
     * {@link MqttClientConnectionType#SUBSCRIBER}.
     * 
     * @param inboundMessageChannel a {@link MessageChannel} for in-bound messages
     * 
     * @throws IllegalStateException if the Connection Type is
     *             {@link MqttClientConnectionType#PUBLISHER}
     * @throws IllegalArgumentException if the {@code inboundMessageChannel} value is null
     */
    public void setInboundMessageChannel(MessageChannel inboundMessageChannel)
    {
        if (MqttClientConnectionType.PUBLISHER == connectionType)
        {
            throw new IllegalStateException(String.format(
                "Client ID %s is setup as a PUBLISHER and cannot receive messages from the Broker!",
                getClientId()));
        }
        Assert.notNull(inboundMessageChannel, "'inboundMessageChannel' must be set!");
        this.inboundMessageChannel = inboundMessageChannel;
    }

    /**
     * Sets a {@link ReconnectService} and {@link TaskScheduler} that will be used to reconnect upon
     * a connection lost or failure event.
     * <p>
     * If either the {@code reconnectionService} or {@code taskScheduler} values are null, a
     * reconnection attempt will not be made.
     * 
     * @param reconnectService a {@link ReconnectService} value
     * @param taskScheduler a {@link TaskScheduler} value
     */
    public void setReconnectDetails(ReconnectService reconnectService, TaskScheduler taskScheduler)
    {
        this.reconnectService = reconnectService;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public List<TopicSubscription> getTopicSubscriptions()
    {
        List<TopicSubscription> records = new ArrayList<TopicSubscription>();
        reentrantLock.lock();
        try
        {
            topicSubscriptions.forEach(record -> records.add(record.clone()));
        }
        finally
        {
            reentrantLock.unlock();
        }
        return records;
    }

    @Override
    public void subscribe(String topicFilter)
    {
        subscribe(topicFilter, mqttClientConfiguration.getDefaultQualityOfService());
    }

    public abstract void subscribe(String topicFilter, MqttQualityOfService qualityOfService);
}
