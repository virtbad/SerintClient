package ch.virtbad.serint.client.game.objects;

import ch.virtbad.serint.client.game.objects.positioning.FixedLocation;

/**
 * This class represents a game object which has a position with a quad attached to it
 * @author Virt
 */
public class PositionedGameObject extends GameObject {

    protected FixedLocation location;

    protected int layer;

    /**
     * Creates a Positioned Game Object
     * @param location location of the object
     * @param layer layer the object is on
     */
    public PositionedGameObject(FixedLocation location, int layer) {
        super();

        this.location = location;
        this.layer = layer;
    }
}
