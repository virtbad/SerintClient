package ch.virtbad.serint.client.game.positioning;

import lombok.Getter;
import lombok.Setter;

/**
 * This class handles a fixed location
 * @author Virt
 */
public class FixedLocation {

    @Getter
    @Setter
    protected float posX, posY;

    /**
     * Manipulates the position based on time
     *
     * @param delta time that has passed
     */
    public void time(float delta) {

    }
}
