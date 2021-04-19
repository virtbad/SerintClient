package ch.virtbad.serint.client.engine.events;

/**
 * Basic Interface, used for Event Reference, with passing a character
 * @author Virt
 */
public interface CharEvent {
    /**
     * Call Event
     */
    void emit(char c);
}
