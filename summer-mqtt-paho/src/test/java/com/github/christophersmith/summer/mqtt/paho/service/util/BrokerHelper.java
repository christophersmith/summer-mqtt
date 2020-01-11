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
package com.github.christophersmith.summer.mqtt.paho.service.util;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;

public class BrokerHelper
{
    private static final String CLIENT_ID         = MqttAsyncClient.generateClientId();
    private static final String BROKER_URI_FORMAT = "tcp://%s:%s";
    private static final String BROKER_HOST_NAME = "mqtt.eclipse.org";
    private static final int    BROKER_PORT       = 1883;
    private static final String PROXY_HOST_NAME   = "localhost";
    private static final int    PROXY_PORT        = 10080;

    public static String getClientId()
    {
        return CLIENT_ID;
    }

    public static String getBrokerHostName()
    {
        return BROKER_HOST_NAME;
    }

    public static int getBrokerPort()
    {
        return BROKER_PORT;
    }

    public static String getBrokerUri()
    {
        return String.format(BROKER_URI_FORMAT, BROKER_HOST_NAME, String.valueOf(BROKER_PORT));
    }

    public static String getProxyHostName()
    {
        return PROXY_HOST_NAME;
    }

    public static int getProxyPort()
    {
        return PROXY_PORT;
    }

    public static String getProxyUri()
    {
        return String.format(BROKER_URI_FORMAT, PROXY_HOST_NAME, String.valueOf(PROXY_PORT));
    }
}
