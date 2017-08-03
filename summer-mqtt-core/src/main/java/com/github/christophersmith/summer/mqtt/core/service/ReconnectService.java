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

import java.util.Date;

/**
 * Enables the {@link MqttClientService} to reconnect, upon a lost connection, to the Broker.
 */
public interface ReconnectService
{
    /**
     * This method is called when the {@link MqttClientService} makes or loses a connection, or has
     * a connection failure.
     * <p>
     * Calls to this method can useful for resetting any incrementer for the determining the Next
     * Reconnect Date.
     * 
     * @param successful true or false
     */
    void connected(boolean successful);

    /**
     * Returns the {@link Date} that the next reconnection attempt should be made.
     * <p>
     * If a null value is returned by this method, a reconnect attempt will not be made.
     * 
     * @return a {@link Date} value
     */
    Date getNextReconnectionDate();
}
