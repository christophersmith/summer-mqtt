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
package com.github.christophersmith.summer.mqtt.paho.service;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType;
import com.github.christophersmith.summer.mqtt.core.MqttQualityOfService;
import com.github.christophersmith.summer.mqtt.core.TopicSubscription;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionFailureEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttClientConnectionLostEvent;
import com.github.christophersmith.summer.mqtt.core.event.MqttMessageDeliveredEvent;
import com.github.christophersmith.summer.mqtt.core.service.AbstractMqttClientService;
import com.github.christophersmith.summer.mqtt.core.service.MqttClientService;
import com.github.christophersmith.summer.mqtt.core.service.ReconnectService;
import com.github.christophersmith.summer.mqtt.core.util.MqttHeaderHelper;
import com.github.christophersmith.summer.mqtt.core.util.TopicSubscriptionHelper;

/**
 * This is a Paho Asynchronous MQTT Client Service implementation which allows you configure
 * specific features of the underlying Java Paho MQTT Client.
 */
public final class PahoAsyncMqttClientService extends AbstractMqttClientService
    implements MqttClientService, MqttCallbackExtended, IMqttActionListener
{
    private static final Logger                   LOG                = LoggerFactory
        .getLogger(PahoAsyncMqttClientService.class);
    private transient final MqttClientPersistence clientPersistence;
    private transient final MqttAsyncClient       mqttClient;
    private transient final MqttConnectOptions    mqttConnectOptions = new MqttConnectOptions();

    /**
     * Default constructor
     * 
     * @param serverUri the Server URI to connect to
     * @param clientId the Client ID to connect as
     * @param connectionType the
     *            {@link com.github.christophersmith.summer.mqtt.core.MqttClientConnectionType} used
     *            to define this instance's connection behavior
     * @param clientPersistence an optional {@link MqttClientPersistence} used to control how
     *            messages that are yet delivered are persisted
     * @throws IllegalArgumentException if the {@code serverUri} is blank or null, the
     *             {@code clientId} is blank or null, or if the {@code connectionType} value is null
     * @throws MqttException if the underlying {@link MqttAsyncClient} instance cannot be created
     */
    public PahoAsyncMqttClientService(final String serverUri, final String clientId,
        final MqttClientConnectionType connectionType,
        final MqttClientPersistence clientPersistence)
        throws MqttException
    {
        super(connectionType);
        Assert.hasText(serverUri, "'serverUri' must be set!");
        Assert.hasText(clientId, "'clientId' must be set!");
        this.clientPersistence = clientPersistence;
        mqttClient = new MqttAsyncClient(serverUri, clientId, this.clientPersistence);
        mqttClient.setCallback(this);
    }

    @Override
    /**
     * Publishes a {@link Message} to the MQTT Broker.
     * 
     * @param message the {@link Message} to send
     * @throws IllegalArgumentException if the {@code message} is null
     * @throws MessagingException if the {@code message} could not be sent
     */
    public void handleMessage(Message<?> message) throws MessagingException
    {
        if (MqttClientConnectionType.SUBSCRIBER == connectionType)
        {
            throw new MessagingException(message,
                String.format(
                    "Client ID %s is setup as a SUBSCRIBER and could not publish this message.",
                    getClientId()));
        }
        Assert.notNull(message, "'message' must be set!");
        try
        {
            if (mqttClient.isConnected())
            {
                String topic = MqttHeaderHelper.getTopicHeaderValue(message);
                byte[] payload = null;
                // TODO: really need to use a payload converter
                if (message.getPayload() != null
                    && message.getPayload() instanceof byte[])
                {
                    payload = (byte[]) message.getPayload();
                }
                else if (message.getPayload() != null
                    && message.getPayload() instanceof String)
                {
                    payload = ((String) message.getPayload()).getBytes();
                }
                MqttQualityOfService qualityOfService = MqttHeaderHelper
                    .getMqttQualityOfServiceHeaderValue(message,
                        mqttClientConfiguration.getDefaultQualityOfService().getLevelIdentifier());
                boolean retained = MqttHeaderHelper.getRetainedHeaderValue(message);
                if (StringUtils.isEmpty(topic)
                    || payload == null)
                {
                    throw new MessagingException(message, String.format(
                        "Client ID '%s' could not publish this message because either the topic or payload isn't set, or the payload could not be converted.",
                        getClientId()));
                }
                IMqttDeliveryToken token = mqttClient.publish(topic, payload,
                    qualityOfService.getLevelIdentifier(), retained);
                mqttClientEventPublisher.publishMessagePublishedEvent(getClientId(),
                    token.getMessageId(), MqttHeaderHelper.getCorrelationIdHeaderValue(message),
                    applicationEventPublisher, this);
            }
            else
            {
                throw new MessagingException(message, String.format(
                    "Client ID %s is disconnected. Could not send message.", getClientId()));
            }
        }
        catch (MqttException ex)
        {
            throw new MessagingException(message,
                String.format("Client ID %s encountered an issue and the message couldn't be sent.",
                    getClientId()),
                ex);
        }
    }

    @Override
    public String getClientId()
    {
        return mqttClient.getClientId();
    }

    @Override
    public boolean start()
    {
        boolean successful = false;
        reentrantLock.lock();
        try
        {
            if (!mqttClient.isConnected())
            {
                mqttClient.connect(mqttConnectOptions, null, this)
                    .waitForCompletion(mqttConnectOptions.getConnectionTimeout()
                        * 1000);
            }
            if (mqttClient.isConnected()
                && topicSubscriptions.size() > 0)
            {
                for (TopicSubscription topicSubscription : topicSubscriptions)
                {
                    if (!topicSubscription.isSubscribed())
                    {
                        mqttClient
                            .subscribe(topicSubscription.getTopicFilter(),
                                topicSubscription.getQualityOfService().getLevelIdentifier())
                            .waitForCompletion(
                                mqttClientConfiguration.getSubscribeWaitMilliseconds());
                        topicSubscription.setSubscribed(true);
                    }
                }
            }
            if (mqttClient.isConnected())
            {
                mqttClientEventPublisher.publishConnectedEvent(getClientId(),
                    getConnectedServerUri(),
                    TopicSubscriptionHelper.getSubscribedTopicFilters(topicSubscriptions),
                    applicationEventPublisher, this);
                publishConnectionStatus(true);
                LOG.info(String.format(
                    "Client ID %s is connected to Broker %s with the topic(s): [%s]", getClientId(),
                    getConnectedServerUri(), StringUtils.arrayToCommaDelimitedString(
                        TopicSubscriptionHelper.getSubscribedTopicFilters(topicSubscriptions))));
                started = true;
                firstStartOccurred = true;
                successful = true;
                if (reconnectService != null)
                {
                    reconnectService.connected(true);
                }
            }
        }
        catch (MqttException ex)
        {
            LOG.error(String.format("Client ID %s encountered an issue and could not be started.",
                getClientId()), ex);
            scheduleReconnect();
        }
        finally
        {
            reentrantLock.unlock();
        }
        return successful;
    }

    @Override
    public boolean isConnected()
    {
        return mqttClient.isConnected();
    }

    @Override
    public String getConnectedServerUri()
    {
        return mqttClient.getCurrentServerURI();
    }

    @Override
    public void subscribe(String topicFilter, MqttQualityOfService qualityOfService)
    {
        if (MqttClientConnectionType.PUBLISHER == connectionType)
        {
            throw new IllegalStateException(String.format(
                "Client ID %s is a PUBLISHER and cannot subscribe or unsubscribe from Topic Filters.",
                getClientId()));
        }
        Assert.hasText(topicFilter, "'topicFilter' must be set!");
        Assert.notNull(qualityOfService, "'qualityOfService' must be set!");
        reentrantLock.lock();
        try
        {
            TopicSubscription topicSubscription = TopicSubscriptionHelper
                .findByTopicFilter(topicFilter, topicSubscriptions);
            if (topicSubscription != null
                && topicSubscription.getQualityOfService() != qualityOfService)
            {
                unsubscribe(topicFilter);
                topicSubscription = null;
            }
            if (topicSubscription == null)
            {
                topicSubscription = new TopicSubscription(topicFilter, qualityOfService);
                topicSubscriptions.add(topicSubscription);
                if (mqttClient.isConnected())
                {
                    try
                    {
                        mqttClient
                            .subscribe(topicSubscription.getTopicFilter(),
                                topicSubscription.getQualityOfService().getLevelIdentifier())
                            .waitForCompletion(
                                mqttClientConfiguration.getSubscribeWaitMilliseconds());
                        topicSubscription.setSubscribed(true);
                    }
                    catch (MqttException ex)
                    {
                        LOG.error(String.format(
                            "Client ID %s could not subscribe to the Topic Filter [%s].",
                            getClientId(), topicFilter), ex);
                    }
                }
                else if (firstStartOccurred)
                {
                    LOG.warn(String.format(
                        "Client ID %s did not subscribe to the Topic Filter [%s] because it is not connected. A subscription will be made once the Client is connected.",
                        getClientId(), topicFilter));
                }
            }
            else
            {
                LOG.warn(String.format(
                    "Client ID %s did not add the Topic Filter [%s] because it is a duplicate.",
                    getClientId(), topicFilter));
            }
        }
        finally
        {
            reentrantLock.unlock();
        }
    }

    @Override
    public void unsubscribe(String topicFilter)
    {
        if (MqttClientConnectionType.PUBLISHER == connectionType)
        {
            throw new IllegalStateException(String.format(
                "Client ID %s is a PUBLISHER and cannot subscribe or unsubscribe from Topic Filters.",
                getClientId()));
        }
        Assert.hasText(topicFilter, "'topicFilter' must be set!");
        reentrantLock.lock();
        try
        {
            TopicSubscription topicSubscription = TopicSubscriptionHelper
                .findByTopicFilter(topicFilter, topicSubscriptions);
            if (topicSubscription != null)
            {
                if (mqttClient.isConnected()
                    && topicSubscription.isSubscribed())
                {
                    try
                    {
                        mqttClient.unsubscribe(topicSubscription.getTopicFilter())
                            .waitForCompletion(mqttClientConfiguration
                                .getTopicUnsubscribeWaitTimeoutMilliseconds());
                    }
                    catch (MqttException ex)
                    {
                        LOG.error(String.format(
                            "Client ID %s could not unsubscribe to the Topic Filter [%s].",
                            getClientId(), topicFilter), ex);
                    }
                }
                topicSubscriptions.remove(topicSubscription);
            }
        }
        finally
        {
            reentrantLock.unlock();
        }
    }

    @Override
    public void stop()
    {
        if (scheduledFuture != null)
        {
            scheduledFuture.cancel(true);
        }
        started = false;
        firstStartOccurred = false;
        reentrantLock.lock();
        try
        {
            TopicSubscriptionHelper.markUnsubscribed(topicSubscriptions);
            if (mqttClient.isConnected())
            {
                publishConnectionStatus(false);
                try
                {
                    mqttClient.disconnect()
                        .waitForCompletion(mqttClientConfiguration.getDisconnectWaitMilliseconds());
                    mqttClientEventPublisher.publishDisconnectedEvent(getClientId(),
                        applicationEventPublisher, this);
                    LOG.info(String.format("Client ID %s is stopped.", getClientId()));
                }
                catch (MqttException ex)
                {
                    try
                    {
                        if (mqttClient.isConnected())
                        {
                            mqttClient.disconnectForcibly(
                                mqttClientConfiguration.getDisconnectWaitMilliseconds());
                        }
                        mqttClientEventPublisher.publishDisconnectedEvent(getClientId(),
                            applicationEventPublisher, this);
                        LOG.info(String.format("Client ID %s is stopped.", getClientId()));
                    }
                    catch (MqttException e)
                    {
                        LOG.error(
                            String.format("Client ID %s could not disconnect.", getClientId()), ex);
                    }
                }
            }
        }
        finally
        {
            reentrantLock.unlock();
        }
    }

    @Override
    public void close()
    {
        reentrantLock.lock();
        try
        {
            stop();
            mqttClient.close();
            LOG.info(
                String.format("Client ID %s is closed and cannot be restarted.", getClientId()));
        }
        catch (MqttException ex)
        {
            LOG.error(
                String.format("Client ID %s encountered an error while closing.", getClientId()),
                ex);
        }
        finally
        {
            reentrantLock.unlock();
        }
    }

    /**
     * Overridden from the {@link MqttCallbackExtended#connectionLost(Throwable)} method.
     * <p>
     * Marks all {@link TopicSubscription} instances as unsubscribed and attempts to publish a
     * {@link MqttClientConnectionLostEvent} message.
     */
    @Override
    public void connectionLost(Throwable throwable)
    {
        started = false;
        LOG.error(String.format("Client ID %s lost the connection.", getClientId()), throwable);
        reentrantLock.lock();
        try
        {
            TopicSubscriptionHelper.markUnsubscribed(topicSubscriptions);
        }
        finally
        {
            reentrantLock.unlock();
        }
        mqttClientEventPublisher.publishConnectionLostEvent(getClientId(), isAutoReconnect(),
            applicationEventPublisher, this);
        scheduleReconnect();
    }

    /**
     * Overridden from the {@link MqttCallbackExtended#deliveryComplete(IMqttDeliveryToken)} method.
     * <p>
     * Attempts to publish a {@link MqttMessageDeliveredEvent} message.
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token)
    {
        mqttClientEventPublisher.publishMessageDeliveredEvent(getClientId(), token.getMessageId(),
            applicationEventPublisher, this);
    }

    /**
     * Overridden from the {@link MqttCallbackExtended#messageArrived(String, MqttMessage)} method.
     * <p>
     * If the {@code inboundMessageChannel} is not null, the received message is sent to this
     * {@link MessageChannel}.
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        try
        {
            if (inboundMessageChannel != null)
            {
                inboundMessageChannel.send(MessageBuilder.withPayload(message.getPayload().clone())
                    .setHeader(MqttHeaderHelper.TOPIC, topic)
                    .setHeader(MqttHeaderHelper.ID, message.getId())
                    .setHeader(MqttHeaderHelper.QOS,
                        MqttQualityOfService.findByLevelIdentifier(message.getQos()))
                    .setHeader(MqttHeaderHelper.RETAINED, message.isRetained())
                    .setHeader(MqttHeaderHelper.DUPLICATE, message.isDuplicate()).build());
            }
        }
        catch (Exception | Error ex)
        {
            LOG.error(String.format(
                "Client ID %s could not send the message to the Inbound Channel. Topic: %s, Message: %s",
                getClientId(), topic, message.toString()), ex);
        }
    }

    /**
     * Returns the {@link MqttConnectOptions} that will be used for this instance.
     * 
     * @return the {@link MqttConnectOptions} to be used for this instance
     */
    public MqttConnectOptions getMqttConnectOptions()
    {
        return mqttConnectOptions;
    }

    /**
     * Overridden from the {@link MqttCallbackExtended#connectComplete(boolean, String)} method.
     * <p>
     * If the connection was made from a reconnect, the {@link #start()} method is called.
     */
    @Override
    public void connectComplete(boolean reconnect, String serverUri)
    {
        if (reconnect)
        {
            start();
        }
    }

    private void publishConnectionStatus(boolean connected)
    {
        if (mqttClientConfiguration.getMqttClientConnectionStatusPublisher() != null)
        {
            byte[] payload = null;
            String topic = null;
            if (connected)
            {
                payload = mqttClientConfiguration.getMqttClientConnectionStatusPublisher()
                    .getConnectedPayload(getClientId(), getConnectionType());
                topic = mqttClientConfiguration.getMqttClientConnectionStatusPublisher()
                    .getStatusTopic();
            }
            else
            {
                payload = mqttClientConfiguration.getMqttClientConnectionStatusPublisher()
                    .getDisconnectedPayload(getClientId(), getConnectionType());
                topic = mqttClientConfiguration.getMqttClientConnectionStatusPublisher()
                    .getStatusTopic();
            }
            if (payload != null
                && !StringUtils.isEmpty(topic))
            {
                MqttQualityOfService qualityOfService = mqttClientConfiguration
                    .getMqttClientConnectionStatusPublisher()
                    .getStatusMqttQualityOfService() == null
                        ? mqttClientConfiguration.getDefaultQualityOfService()
                        : mqttClientConfiguration.getMqttClientConnectionStatusPublisher()
                            .getStatusMqttQualityOfService();
                try
                {
                    IMqttDeliveryToken token = mqttClient.publish(topic, payload,
                        qualityOfService.getLevelIdentifier(), mqttClientConfiguration
                            .getMqttClientConnectionStatusPublisher().isStatusMessageRetained());
                    mqttClientEventPublisher.publishMessagePublishedEvent(getClientId(),
                        token.getMessageId(), null, applicationEventPublisher, this);
                }
                catch (MqttException ex)
                {
                    LOG.warn(String.format(
                        "Client ID %s could not publish the Connection Status message.",
                        getClientId()), ex);
                }
            }
        }
    }

    private void scheduleReconnect()
    {
        if (mqttConnectOptions.isAutomaticReconnect()
            && firstStartOccurred)
        {
            LOG.info(String.format("Client ID %s is scheduled to reconnect.", getClientId()));
        }
        else if (reconnectService != null
            && taskScheduler != null)
        {
            Date nextReconnectDate = reconnectService.getNextReconnectionDate();
            if (nextReconnectDate != null)
            {
                firstStartOccurred = true;
                scheduledFuture = taskScheduler.schedule(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        start();
                    }
                }, nextReconnectDate);
                LOG.info(String.format("Client ID %s is scheduled to reconnect.", getClientId()));
            }
            else
            {
                LOG.warn(String.format(
                    "Client ID %s was not scheuled to reconnect because the Next Reconnect Date was null.",
                    getClientId()));
            }
        }
        else
        {
            LOG.warn(String.format("Client ID %s is not scheduled to reconnect.", getClientId()));
        }
    }

    /**
     * Overridden to publish a {@link MqttClientConnectionFailureEvent} message.
     * <p>
     * If a {@link ReconnectService} instance is defined, the
     * {@link ReconnectService#connected(boolean)} method is called with a value of false.
     */
    @Override
    public void onFailure(IMqttToken token, Throwable throwable)
    {
        mqttClientEventPublisher.publishConnectionFailureEvent(getClientId(), isAutoReconnect(),
            throwable, applicationEventPublisher, this);
        if (reconnectService != null)
        {
            reconnectService.connected(false);
        }
    }

    @Override
    public void onSuccess(IMqttToken token)
    {

    }

    private boolean isAutoReconnect()
    {
        boolean autoReconnect = false;
        if ((mqttConnectOptions.isAutomaticReconnect()
            && firstStartOccurred)
            || (reconnectService != null
                && taskScheduler != null))
        {
            autoReconnect = true;
        }
        return autoReconnect;
    }
}
