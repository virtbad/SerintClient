package ch.virtbad.serint.client.game.client;

import ch.virtbad.serint.client.game.GameContext;
import ch.virtbad.serint.client.game.positioning.FixedLocation;

/**
 * This class handles camera movement
 * @author Virt
 */
public class Cinematography {

    private static final int NO_MODE = 0;
    private static final int FOLLOW_MODE = 1;
    private static final int FIXED_MODE = 2;

    private final GameContext context;

    private int mode = NO_MODE;
    private FixedLocation location;
    private boolean dirty;

    /**
     * Creates a cinematography
     * @param context game context
     */
    public Cinematography(GameContext context) {
        this.context = context;
    }

    /**
     * Sets the camera to follow a location
     * @param location location to follow
     */
    public void follow(FixedLocation location){
        this.location = location;
        this.mode = FOLLOW_MODE;
    }

    /**
     * Sets the camera to stay on a location
     * @param location location to stay on
     */
    public void fix(FixedLocation location){
        dirty = true;
        mode = FIXED_MODE;
        this.location = location;
    }

    /**
     * Updates the Camera Position
     */
    public void update(){
        if (mode == FOLLOW_MODE) {
            context.getCamera().setWorldLocation(location.getPosX() - context.getCamera().getXUnits() / 2, location.getPosY() - context.getCamera().getYUnits() / 2);
        }
        if (mode == FIXED_MODE){
            if (dirty) { // Only recalculate camera matrix if needed
                context.getCamera().setWorldLocation(location.getPosX() - context.getCamera().getXUnits() / 2, location.getPosY() - context.getCamera().getYUnits() / 2);
                dirty = false;
            }
        }
    }






}
