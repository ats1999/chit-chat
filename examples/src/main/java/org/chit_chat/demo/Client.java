package org.chit_chat.demo;

import org.chit_chat.client.COMMAND;
import org.chit_chat.client.ChitChatClient;
import org.chit_chat.client.MessageHandler;
import org.chit_chat.client.MessagePayload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            throw new IllegalStateException("Host and port is required...");
        }

        String host = "localhost";
        int port = 5000;
        String userId = args[0];

        MessageHandler messageHandler = System.out::println;

        try (
            ChitChatClient chitChatClient = new ChitChatClient(host, port, userId, messageHandler);
            InputStreamReader in = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(in);
        ) {
            String input;
            while (!(input = bufferedReader.readLine()).equals("END")) {
                String[] cArgs = input.split(" ");

                String command = cArgs[0];

                if (command.equals(COMMAND.SEND.name())) {
                    MessagePayload messagePayload = new MessagePayload(cArgs[1], cArgs[2]);
                    chitChatClient.sendMessage(messagePayload);
                } else if (command.equals(COMMAND.STATUS.name())) {
                    chitChatClient.sendStatusCheck(cArgs[1]);
                } else {
                    System.out.println("Invalid command...");
                }
            }
        }
    }
}
