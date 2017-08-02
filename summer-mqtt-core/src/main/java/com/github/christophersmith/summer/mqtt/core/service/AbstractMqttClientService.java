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

    public MqttClientConfiguration getMqttClientConfiguration()
    {
        return mqttClientConfiguration;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
    {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setInboundMessageChannel(MessageChannel inboundMessageChannel)
    {
        if (MqttClientConnectionType.PUBLISHER == connectionType)
        {
            throw new IllegalStateException(String.format(
                "Client ID '%s' is setup as a PUBLISHER and cannot receive messages from the Broker!",
                getClientId()));
        }
        Assert.notNull(inboundMessageChannel, "'inboundMessageChannel' must be set!");
        this.inboundMessageChannel = inboundMessageChannel;
    }

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
