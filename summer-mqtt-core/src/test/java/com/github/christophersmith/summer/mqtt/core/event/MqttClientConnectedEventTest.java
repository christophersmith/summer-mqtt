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
package com.github.christophersmith.summer.mqtt.core.event;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MqttClientConnectedEventTest
{
    private static final String   CLIENT_ID            = "TESTCLIENT";
    private static final String   SERVER_URI           = "tcp://localhost:1883";
    private static final String[] SUBSCRIBED_TOPICS    = new String[0];
    private static final String   EXCEPTION_SERVER_URI = "'serverUri' must be set!";
    @Rule
    public ExpectedException      thrown               = ExpectedException.none();

    @Test
    public void test()
    {
        MqttClientConnectedEvent event = new MqttClientConnectedEvent(CLIENT_ID, SERVER_URI,
            SUBSCRIBED_TOPICS, this);
        Assert.assertEquals(SERVER_URI, event.getServerUri());
        Assert.assertEquals(SUBSCRIBED_TOPICS.length, event.getSubscribedTopics().length);
    }

    @Test
    public void testNullServerUri()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_SERVER_URI);
        new MqttClientConnectedEvent(CLIENT_ID, null, SUBSCRIBED_TOPICS, this);
    }

    @Test
    public void testBlankServerUri()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_SERVER_URI);
        new MqttClientConnectedEvent(CLIENT_ID, "", SUBSCRIBED_TOPICS, this);
    }

    @Test
    public void testNullSubscribedTopics()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'subscribedTopics' must be set!");
        new MqttClientConnectedEvent(CLIENT_ID, SERVER_URI, null, this);
    }
}
