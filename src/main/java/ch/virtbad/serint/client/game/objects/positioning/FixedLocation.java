package ch.virtbad.serint.client.game.objects.positioning;

import ch.virtbad.serint.client.game.collisions.CollisionResult;
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


    /**
     * Manipulates the position based time and collisions
     *
     * @param delta time that has passed
     * @param result results of the collision
     */
    public void timeCollided(float delta, CollisionResult result) {

    }
}
