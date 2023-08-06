package org.chit_chat.client;

import java.io.Closeable;
import java.io.IOException;

public class ChitChatClient implements Closeable {
    private final String userId;
    private final ConnectionManager connectionManager;

    public ChitChatClient(
        final String host,
        final int port,
        final String userId,
        final MessageHandler messageHandler
    ) throws IOException {
        this.userId = userId;

        connectionManager = new ConnectionManager(host, port,messageHandler);
        connectionManager.registerSocketDataHandler();

        registerUser();
    }

    private void registerUser() throws IOException {
        String userRegistrationPayload = PayloadEncoder.encodeUserRegistrationPayload(userId);
        connectionManager.writeData(userRegistrationPayload);
    }

    public synchronized void sendMessage(MessagePayload messagePayload) throws IOException {
        String encodedPayload = PayloadEncoder.encodeMessagePayload(messagePayload);
        connectionManager.writeData(encodedPayload);
    }

    public synchronized void sendStatusCheck(String userId) throws IOException {
        String socketData = PayloadEncoder.encodeStatusCommand(userId);
        connectionManager.writeData(socketData);
    }

    public synchronized boolean isOpen() {
        return connectionManager.isOpen();
    }

    @Override
    public synchronized void close() throws IOException {
        connectionManager.close();
    }
}
