package ch.virtbad.serint.client.game.collisions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Virt
 */
@Getter @Setter @AllArgsConstructor
public class AABB {

    private float x, y, width, height;

    public void setPosition(float x, float y){
        setX(x);
        setY(y);
    }

    public float getXStart(){
        return x;
    }

    public float getXEnd(){
        return x + width;
    }

    public float getYStart(){
        return y;
    }

    public float getYEnd(){
        return y + height;
    }



}
