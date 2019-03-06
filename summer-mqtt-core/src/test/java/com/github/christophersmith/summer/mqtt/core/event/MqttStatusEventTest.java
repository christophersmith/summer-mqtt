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

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MqttStatusEventTest
{
    private static final String CLIENT_ID                   = "TESTCLIENT";
    private static final String EXCEPTION_MESSAGE_CLIENT_ID = "'clientId' must be set!";
    @Rule
    public ExpectedException    thrown                      = ExpectedException.none();

    @Test
    public void test()
    {
        MqttStatusEvent event = new MqttStatusEvent(CLIENT_ID, this);
        Assert.assertEquals(CLIENT_ID, event.getClientId());
    }

    @Test
    public void testNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        new MqttStatusEvent(null, this);
    }

    @Test
    public void testBlankClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(EXCEPTION_MESSAGE_CLIENT_ID);
        new MqttStatusEvent("", this);
    }
}
