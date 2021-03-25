package pl.jakub.chatapp.rooms;

/**
 * Represents a simple room.
 *
 * @author Jakub Zelmanowicz
 */
public class Room {

    // Id of the room.
    private final String id;

    // Name of the room.
    private final String name;

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
