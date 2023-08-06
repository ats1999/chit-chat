package org.chit_chat.client;

public class PayloadEncoder {
    public static String encodeStatusCommand(String userId) {
        return COMMAND.STATUS + " " + userId;
    }

    public static String encodeMessagePayload(MessagePayload messagePayload) {
        String[] params = {
            COMMAND.SEND.name(),
            messagePayload.to(),
            messagePayload.message()
        };

        return String.join(" ", params);
    }

    public static String encodeUserRegistrationPayload(String userId) {
        return COMMAND.REGISTER + " " + userId;
    }
}
