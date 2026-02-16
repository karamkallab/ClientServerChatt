package Both;

import java.io.Serializable;

/**
 * Represents a message that can be sent between a client and server.
 * This interface extends Serializable to allow messages to be serialized for transmission.
 */
public interface Message extends Serializable {
}
