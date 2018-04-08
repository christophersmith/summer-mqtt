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
package com.github.christophersmith.summer.mqtt.core;

import com.github.christophersmith.summer.mqtt.core.service.MqttClientService;

/**
 * A utility interface that allows you to define messages that get published when the
 * {@link MqttClientService} instance connects or disconnects from the Broker.
 * <p>
 * This is optional, but allows you to follow best practices for publishing Connection Status
 * messages.
 */
public interface MqttClientConnectionStatusPublisher
{
    /**
     * Returns the message that should be published once the associated {@link MqttClientService}
     * instance connects to the Broker, and subscribes to any associated Topic Filters.
     * <p>
     * If the returned value is null or empty, no message will be published on the Connected event.
     * 
     * @param clientId the Client ID the client is configured with
     * @param connectionType the {@link MqttClientConnectionType} the client is configured with
     * @return a {@link byte[]} value containing the payload to publish, or null if undefined
     */
    byte[] getConnectedPayload(String clientId, MqttClientConnectionType connectionType);

    /**
     * Returns the message that should published once the associated {@link MqttClientService}
     * instance disconnects from the Broker.
     * <p>
     * If the returned value is null or empty, no message will be published on the Disconnected
     * event.
     * 
     * @param clientId the Client ID the client is configured with
     * @param connectionType the {@link MqttClientConnectionType} the client is configured with
     * @return a {@link byte[]} value containing the payload to publish, or null if undefined
     */
    byte[] getDisconnectedPayload(String clientId, MqttClientConnectionType connectionType);

    /**
     * Returns the {@link MqttQualityOfService} value that the Connection Status should be published
     * with.
     * <p>
     * If this value is null, the Default MQTT Quality of Service value defined from the
     * {@link MqttClientConfiguration} will be used.
     * 
     * @return a {@link MqttQualityOfService} value, or null if not set
     */
    MqttQualityOfService getStatusMqttQualityOfService();

    /**
     * Returns whether the Status Message should be marked as Retained.
     * 
     * @return true or false
     */
    boolean isStatusMessageRetained();

    /**
     * Returns the Topic the Connection Status messages should be published on.
     * <p>
     * If the returned value is null or empty, then Connection Status messages will not be sent.
     * 
     * @return the Status Topic, or null if not set
     */
    String getStatusTopic();
}
