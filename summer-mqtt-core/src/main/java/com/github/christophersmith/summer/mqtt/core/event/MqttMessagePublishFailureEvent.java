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

import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;

public class MqttMessagePublishFailureEvent extends MqttMessageStatusEvent
{
    private static final long                  serialVersionUID = 4945103078856231302L;
    private transient final MessagingException exception;

    public MqttMessagePublishFailureEvent(final String clientId, final MessagingException exception,
        final Object source)
    {
        super(clientId, source);
        Assert.notNull(exception, "'exception must be set!");
        this.exception = exception;
    }

    /**
     * The {@link MessagingException} for this event.
     * <p>
     * This will contain the {@link Message} object that was not sent.
     * 
     * @return the {@link MessagingException} for this event
     */
    public MessagingException getException()
    {
        return exception;
    }
}
