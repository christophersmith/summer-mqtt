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
