package ch.virtbad.serint.client.engine.events;

/**
 * This class contains helper Methods for dealing with simple Events
 * @author Virt
 */
public class EventHelper {

    /**
     * Emits events, only if not null
     * @param event event to emmit
     */
    public static void emitEvent(BasicEvent event){
        if (event != null) event.emit();
    }

    /**
     * Emits boolean events, only if not null
     * @param event event to emit
     * @param value value to be emitted with
     */
    public static void emitEvent(BooleanEvent event, boolean value){
        if (event != null) event.emit(value);
    }

    /**
     * Emits integer events, only if not null
     * @param event event to emit
     * @param value value to be emitted with
     */
    public static void emitEvent(IntegerEvent event, int value){
        if (event != null) event.emit(value);
    }


    /**
     * Emits character events, only if not null
     * @param event event to emit
     * @param value value to be emitted with
     */
    public static void emitEvent(CharEvent event, char value){
        if (event != null) event.emit(value);
    }
}
