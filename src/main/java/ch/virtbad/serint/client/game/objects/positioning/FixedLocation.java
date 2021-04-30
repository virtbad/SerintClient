package ch.virtbad.serint.client.game.objects.positioning;

import ch.virtbad.serint.client.game.collisions.CollisionResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

/**
 * This class handles a fixed location
 * @author Virt
 */
@AllArgsConstructor
public class FixedLocation {

    @Getter
    @Setter
    protected float posX, posY;

    /**
     * Empty constructor
     */
    public FixedLocation(){}

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

    /**
     * Gets the distance between this and the other location
     * @param location compare
     * @return distance
     */
    public float distanceTo(FixedLocation location){
        float xDelta = posX - location.getPosX();
        float yDelta = posY - location.getPosY();
        return (float) Math.sqrt(xDelta * xDelta + yDelta * yDelta);
    }
}
