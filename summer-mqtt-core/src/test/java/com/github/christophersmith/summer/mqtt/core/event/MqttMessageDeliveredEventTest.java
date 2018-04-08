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

public class MqttMessageDeliveredEventTest
{
    private static final String CLIENT_ID  = "TESTCLIENT";
    private static final int    MESSAGE_ID = 12;
    @Rule
    public ExpectedException    thrown     = ExpectedException.none();

    @Test
    public void test()
    {
        MqttMessageDeliveredEvent event = new MqttMessageDeliveredEvent(CLIENT_ID, MESSAGE_ID,
            this);
        Assert.assertEquals(MESSAGE_ID, event.getMessageIdentifier());
    }

    @Test
    public void testNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'clientId' must be set!");
        new MqttMessageDeliveredEvent(null, MESSAGE_ID, this);
    }
}
