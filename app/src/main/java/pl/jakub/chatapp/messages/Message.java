package pl.jakub.chatapp.messages;

/**
 * Represents a message received or sent
 * by the user.
 *
 * @author Jakub Zelmanowicz.
 */
public class Message {

    /**
     * Name of the sender of the message.
     */
    private final String sender;

    /**
     * Text content of the message.
     */
    private final String content;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

}
