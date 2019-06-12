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
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;

public class MqttMessagePublishFailureEventTest
{
    private static final String CLIENT_ID = "TESTCLIENT";
    private static final String PAYLOAD   = "TEST PAYLOAD";
    @Rule
    public ExpectedException    thrown    = ExpectedException.none();

    @Test
    public void test()
    {
        final Message<String> message = MessageBuilder.withPayload(PAYLOAD).build();
        final MessagingException exception = new MessagingException(message, String.format(
            "Client ID '%s' could not publish this message because either the topic or payload isn't set, or the payload could not be converted.",
            CLIENT_ID));
        final MqttMessagePublishFailureEvent event = new MqttMessagePublishFailureEvent(CLIENT_ID,
            exception, this);
        Assert.assertEquals(PAYLOAD, event.getException().getFailedMessage().getPayload());
    }

    @Test
    public void testNullClientId()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'clientId' must be set!");
        final Message<String> message = MessageBuilder.withPayload(PAYLOAD).build();
        final MessagingException exception = new MessagingException(message, CLIENT_ID);
        new MqttMessagePublishFailureEvent(null, exception, this);
    }

    @Test
    public void testException()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'exception must be set!");
        new MqttMessagePublishFailureEvent(CLIENT_ID, null, this);
    }
}
