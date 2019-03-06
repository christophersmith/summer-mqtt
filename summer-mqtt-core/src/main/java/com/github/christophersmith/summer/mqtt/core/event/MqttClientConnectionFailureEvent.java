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
package com.github.christophersmith.summer.mqtt.core.event;

import org.springframework.context.ApplicationEventPublisher;

import com.github.christophersmith.summer.mqtt.core.service.MqttClientService;

/**
 * An Event that is sent when the {@link MqttClientService} implementation cannot connect to the
 * Broker.
 * <p>
 * This message is only sent if the {@link MqttClientService} has an
 * {@link ApplicationEventPublisher} instance defined.
 */
public class MqttClientConnectionFailureEvent extends MqttConnectionStatusEvent
{
    private static final long serialVersionUID = -5582288189040167240L;
    private boolean           autoReconnect;
    private Throwable         throwable;

    /**
     * The default constructor.
     * 
     * @param clientId the Client ID value
     * @param autoReconnect whether the Client will automatically reconnect
     * @param throwable the originating {@link Throwable}
     * @param source the {@link Object} that published this event
     * 
     * @throws IllegalArgumentException if the {@code clientId} is null or empty
     */
    public MqttClientConnectionFailureEvent(String clientId, boolean autoReconnect,
        Throwable throwable, Object source)
    {
        super(clientId, source);
        this.autoReconnect = autoReconnect;
        this.throwable = throwable;
    }

    /**
     * Returns whether the {@link MqttClientService} instance will reconnect automatically.
     * 
     * @return true or false
     */
    public boolean isAutoReconnect()
    {
        return autoReconnect;
    }

    /**
     * Returns the originating {@link Throwable} that caused this issue.
     * 
     * @return a {@link Throwable}
     */
    public Throwable getThrowable()
    {
        return throwable;
    }
}
