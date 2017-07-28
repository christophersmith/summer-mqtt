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
        Assert.assertEquals(MqttQualityOfService.AT_MOST_ONCE,
            MqttQualityOfService.findByLevelIdentifier(levelIdentifier));
    }

    @Test
    public void testEquality()
    {
        Assert.assertEquals(MqttQualityOfService.AT_MOST_ONCE, MqttQualityOfService
            .findByLevelIdentifier(MqttQualityOfService.QOS_0.getLevelIdentifier()));
        Assert.assertEquals(MqttQualityOfService.AT_LEAST_ONCE, MqttQualityOfService
            .findByLevelIdentifier(MqttQualityOfService.QOS_1.getLevelIdentifier()));
        Assert.assertEquals(MqttQualityOfService.EXACTLY_ONCE, MqttQualityOfService
            .findByLevelIdentifier(MqttQualityOfService.QOS_2.getLevelIdentifier()));
    }
}
