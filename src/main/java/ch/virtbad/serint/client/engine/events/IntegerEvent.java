package ch.virtbad.serint.client.engine.events;

/**
 * Basic Interface, used for Event Reference containing a integer
 * @author Virt
 */
public interface IntegerEvent {
    /**
     * Passes the boolean down when emitted
     * @param i integer
     */
    void emit(int i);
}
