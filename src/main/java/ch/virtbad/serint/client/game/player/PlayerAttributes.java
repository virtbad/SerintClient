package ch.virtbad.serint.client.game.player;

import com.google.gson.annotations.Expose;
import lombok.Getter;

/**
 * @author Virt
 */
public class PlayerAttributes {

    @Getter
    private int health;
    @Getter
    private int maxHealth;
    private float speed;
    private float boostedSpeed;
    @Getter
    private float maxSpeed;
    private float vision;
    private float boostedVision;
    @Getter
    private float maxVision;

    public float getVision(){
        return vision + boostedVision;
    }

    public float getSpeed(){
        return speed + boostedSpeed;
    }

    public boolean isSpeedBoosted(){
        return boostedSpeed > 0;
    }

    public boolean isVisionBoosted(){
        return boostedVision > 0;
    }


}
