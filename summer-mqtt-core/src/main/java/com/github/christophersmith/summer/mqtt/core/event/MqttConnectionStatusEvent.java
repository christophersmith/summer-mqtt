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

import org.springframework.context.ApplicationListener;

/**
 * A base {@link MqttStatusEvent} implementation specifically for capturing MQTT Connection Status
 * Events like Connected, Connection Failure, Connection Lost or Disconnected.
 * <p>
 * This class is meant to be extended, and allows you to easily subscribe to all MQTT Connection
 * Status Events with one {@linkplain ApplicationListener}.
 */
public class MqttConnectionStatusEvent extends MqttStatusEvent
{
    private static final long serialVersionUID = -531996904510858561L;

    /**
     * The default constructor.
     * 
     * @param clientId the Client ID value
     * @param source the {@link Object} that published this event
     * 
     * @throws IllegalArgumentException if the {@code clientId} is null or empty
     */
    public MqttConnectionStatusEvent(String clientId, Object source)
    {
        super(clientId, source);
    }
}
