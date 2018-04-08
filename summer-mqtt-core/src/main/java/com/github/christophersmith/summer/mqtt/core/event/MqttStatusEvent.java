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

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;

import com.github.christophersmith.summer.mqtt.core.service.MqttClientService;

/**
 * A base level {@linkplain ApplicationEvent} instance that is published for various MQTT Status
 * Events.
 * <p>
 * This class is meant to be extended, and allows you to easily subscribe to all MQTT Status Events
 * with one {@linkplain ApplicationListener}.
 */
public class MqttStatusEvent extends ApplicationEvent
{
    private static final long serialVersionUID = 5008242020022488108L;
    private String            clientId;

    /**
     * The default constructor.
     * 
     * @param clientId the Client ID value
     * @param source the {@link Object} that published this event
     * 
     * @throws IllegalArgumentException if the {@code clientId} is null or empty
     */
    public MqttStatusEvent(String clientId, Object source)
    {
        super(source);
        Assert.hasText(clientId, "'clientId' must be set!");
        this.clientId = clientId;
    }

    /**
     * Returns the Client ID of the {@link MqttClientService} instance this event is for.
     * 
     * @return the Client ID value
     */
    public String getClientId()
    {
        return clientId;
    }
}
