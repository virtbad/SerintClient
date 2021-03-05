package ch.virtbad.serint.client.engine.events;

/**
 * Basic Interface, used for Event Reference containing a boolean
 * @author Virt
 */
public interface BooleanEvent {
    /**
     * Passes the boolean down when emitted
     * @param bool boolean
     */
    void emit(boolean bool);
}
