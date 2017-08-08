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
package com.github.christophersmith.summer.mqtt.core;

import org.junit.Assert;
import org.junit.Test;

public final class MqttQualityOfServiceTest
{
    @Test
    public void testInvalidLevelIdentifier()
    {
        int levelIdentifier = -1;
        Assert.assertEquals(MqttQualityOfService.QOS_0,
            MqttQualityOfService.findByLevelIdentifier(levelIdentifier));
        Assert.assertEquals(MqttQualityOfService.QOS_2, MqttQualityOfService.valueOf("QOS_2"));
    }

    @Test
    public void testEquality()
    {
        Assert.assertEquals(MqttQualityOfService.QOS_0,
            MqttQualityOfService.findByLevelIdentifier(0));
        Assert.assertEquals(MqttQualityOfService.QOS_1,
            MqttQualityOfService.findByLevelIdentifier(1));
        Assert.assertEquals(MqttQualityOfService.QOS_2,
            MqttQualityOfService.findByLevelIdentifier(2));
    }
}
