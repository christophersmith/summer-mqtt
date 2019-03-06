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
package com.github.christophersmith.summer.mqtt.core;

/**
 * MQTT Client Connection Type used for defining how a MQTT Client behaves.
 */
public enum MqttClientConnectionType
{
        /**
         * Represents a connection type that can only publish messages.
         */
        PUBLISHER,
        /**
         * Represents a connection type that can both publish and receive messages.
         */
        PUBSUB,
        /**
         * Represents a connection type can only receive messages.
         */
        SUBSCRIBER;
}
