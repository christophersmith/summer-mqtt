package com.github.christophersmith.summer.mqtt.paho.service.util;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;

public class BrokerHelper
{
    private static final String CLIENT_ID         = MqttAsyncClient.generateClientId();
    private static final String BROKER_URI_FORMAT = "tcp://%s:%s";
    private static final String BROKER_HOST_NAME  = "localhost";
    // private static final String BROKER_HOST_NAME = "iot.eclipse.org";
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
