package org.chit_chat.client;

import java.io.*;
import java.net.Socket;

public class ConnectionManager implements Closeable {
    private final Socket socket;
    BufferedWriter socketOut;
    BufferedReader socketIn;
    MessageHandler messageHandler;

    ConnectionManager(
        final String host,
        final int port,
        final MessageHandler messageHandler
    ) throws IOException {
        this.messageHandler = messageHandler;

        socket = new Socket(host, port);

        OutputStreamWriter socketOutputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
        socketOut = new BufferedWriter(socketOutputStreamWriter);

        InputStreamReader socketInputStreamReader = new InputStreamReader(socket.getInputStream());
        socketIn = new BufferedReader(socketInputStreamReader);
    }

    synchronized boolean isOpen() {
        return socket != null && socket.isConnected();
    }

    synchronized void writeData(String data) throws IOException {
        if (!isOpen()) {
            throw new IllegalStateException("Client is not connected...");
        }

        socketOut.write(data);
        socketOut.newLine();
        socketOut.flush();
    }

    synchronized void registerSocketDataHandler() {
        Runnable reader = () -> {
            while (isOpen()) {
                try {
                    String socketData = socketIn.readLine();
                    this.messageHandler.handleMessage(socketData);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread readerThread = new Thread(reader);
        readerThread.start();
    }

    @Override
    public void close() throws IOException {
        if (isOpen()) {
            socket.close();
        }
    }
}
