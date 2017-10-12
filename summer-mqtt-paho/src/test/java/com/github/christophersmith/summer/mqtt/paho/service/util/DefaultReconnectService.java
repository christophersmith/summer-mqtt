package com.github.christophersmith.summer.mqtt.paho.service.util;

import java.time.ZonedDateTime;
import java.util.Date;

import com.github.christophersmith.summer.mqtt.core.service.ReconnectService;

public class DefaultReconnectService implements ReconnectService
{
    private static final long DEFAULT_CONNECTION_DELAY_SECONDS = 1;
    private static final long MAX_CONNECTION_DELAY_SECONDS     = 20;
    private boolean           connected;
    private long              connectionDelaySeconds           = DEFAULT_CONNECTION_DELAY_SECONDS;

    @Override
    public void connected(boolean successful)
    {
        this.connected = successful;
        if (connected)
        {
            connectionDelaySeconds = DEFAULT_CONNECTION_DELAY_SECONDS;
        }
        else
        {
            connectionDelaySeconds = connectionDelaySeconds
                * 2;
        }
        if (connectionDelaySeconds > MAX_CONNECTION_DELAY_SECONDS)
        {
            connectionDelaySeconds = MAX_CONNECTION_DELAY_SECONDS;
        }
    }

    @Override
    public Date getNextReconnectionDate()
    {
        return Date.from(ZonedDateTime.now().plusSeconds(connectionDelaySeconds).toInstant());
    }
}
