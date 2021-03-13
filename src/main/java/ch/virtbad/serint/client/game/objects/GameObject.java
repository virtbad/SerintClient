package ch.virtbad.serint.client.game.objects;

import ch.virtbad.serint.client.game.GameContext;
import lombok.Setter;

/**
 * This class handles the basics of a game object
 * @author Virt
 */
public class GameObject {

    @Setter
    protected GameContext context;

    /**
     * Is called when the object should be initialized
     */
    public void init(){

    }

    /**
     * Is called when the object should be drawn
     */
    public void draw(){

    }

    /**
     * Is called when the object should be updated
     * @param updateDelta time from the last update
     */
    public void update(float updateDelta){

    }

}
