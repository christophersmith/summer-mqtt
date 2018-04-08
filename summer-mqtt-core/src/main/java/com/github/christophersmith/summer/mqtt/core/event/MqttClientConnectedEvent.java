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
package com.github.christophersmith.summer.mqtt.core.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;

import com.github.christophersmith.summer.mqtt.core.service.MqttClientService;

/**
 * An Event that is sent when the {@link MqttClientService} implementation is connected to the
 * Broker.
 * <p>
 * This message is only sent if the {@link MqttClientService} has an
 * {@link ApplicationEventPublisher} instance defined.
 */
public class MqttClientConnectedEvent extends MqttConnectionStatusEvent
{
    private static final long serialVersionUID = 8351820761307561719L;
    private String            serverUri;
    private String[]          subscribedTopics;

    /**
     * The default constructor.
     * 
     * @param clientId the Client ID value
     * @param serverUri the Server URI value
     * @param subscribedTopics the subscribed Topics
     * @param source the {@link Object} that published this event
     * 
     * @throws IllegalArgumentException if the {@code clientId} or {@code serverUri} is null or
     *             empty, or if the {@code subscribedTopics} value is null
     */
    public MqttClientConnectedEvent(String clientId, String serverUri, String[] subscribedTopics,
        Object source)
    {
        super(clientId, source);
        Assert.hasText(serverUri, "'serverUri' must be set!");
        Assert.notNull(subscribedTopics, "'subscribedTopics' must be set!");
        this.serverUri = serverUri;
        this.subscribedTopics = subscribedTopics;
    }

    /**
     * Returns the Server URI this Client is connected to.
     * 
     * @return the Server URI value
     */
    public String getServerUri()
    {
        return serverUri;
    }

    /**
     * Returns a {@link String[]} of the Topics this Client is subscribed to.
     * 
     * @return the subscribed Topics
     */
    public String[] getSubscribedTopics()
    {
        return subscribedTopics;
    }
}
