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

/**
 * MQTT Quality of Service levels used for sending MQTT messages.
 */
public enum MqttQualityOfService
{
        /**
         * Represents a Quality of Service (Qos) of 1, for At Least Once.
         * <p>
         * This is synonymous with the enum type {@link #QOS_1}.
         */
        AT_LEAST_ONCE(1),
        /**
         * Represents a Quality of Service (Qos) of 0, for At Most Once.
         * <p>
         * This is synonymous with the enum type {@link #QOS_0}.
         */
        AT_MOST_ONCE(0),
        /**
         * Represents a Quality of Service (Qos) of 2, for Exactly Once.
         * <p>
         * This is synonymous with the enum type {@link #QOS_2}.
         */
        EXACTLY_ONCE(2),
        /**
         * Represents a Quality of Service (Qos) of 0, for At Most Once.
         * <p>
         * This is synonymous with the enum type {@link #AT_MOST_ONCE}.
         */
        QOS_0(0),
        /**
         * Represents a Quality of Service (Qos) of 1, for At Least Once.
         * <p>
         * This is synonymous with the enum type {@link #AT_LEAST_ONCE}.
         */
        QOS_1(1),
        /**
         * Represents a Quality of Service (Qos) of 2, for Exactly Once.
         * <p>
         * This is synonymous with the enum type {@link #EXACTLY_ONCE}.
         */
        QOS_2(2);

    private transient final int levelIdentifier;

    MqttQualityOfService(final int levelIdentifier)
    {
        this.levelIdentifier = levelIdentifier;
    }

    /**
     * Returns an {@code int} value representation of the Quality of Service Level for this
     * instance.
     * 
     * @return the Level Identifier
     */
    public int getLevelIdentifier()
    {
        return levelIdentifier;
    }

    /**
     * Finds the {@code MqttQualityOfService} instance that matches the {@code levelIdentifier}
     * value.
     * <p>
     * If the {@code levelIdentifier} does not match a {@code MqttQualityOfService} instance, a
     * default value of {@link MqttQualityOfService#AT_MOST_ONCE} will be returned.
     * 
     * @param levelIdentifier the Level Identifier to search by
     * @return a {@code MqttQualityOfService} instance
     */
    public static final MqttQualityOfService findByLevelIdentifier(final int levelIdentifier)
    {
        MqttQualityOfService record = AT_MOST_ONCE;
        for (MqttQualityOfService value : values())
        {
            if (value.getLevelIdentifier() == levelIdentifier)
            {
                record = value;
                break;
            }
        }
        return record;
    }
}
