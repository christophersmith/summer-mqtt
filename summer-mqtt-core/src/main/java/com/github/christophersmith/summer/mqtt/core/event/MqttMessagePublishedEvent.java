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
package com.github.christophersmith.summer.mqtt.core.event;

public class MqttMessagePublishedEvent extends MqttMessageStatusEvent
{
    private static final long serialVersionUID = 8406165555641322553L;
    private int               messageIdentifier;
    private String            correlationId;

    public MqttMessagePublishedEvent(String clientId, int messageIdentifier, String correlationId,
        Object source)
    {
        super(clientId, source);
        this.messageIdentifier = messageIdentifier;
        this.correlationId = correlationId;
    }

    public int getMessageIdentifier()
    {
        return messageIdentifier;
    }

    public String getCorrelationId()
    {
        return correlationId;
    }
}
