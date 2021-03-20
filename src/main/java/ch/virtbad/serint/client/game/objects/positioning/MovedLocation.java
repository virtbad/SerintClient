package ch.virtbad.serint.client.game.objects.positioning;

import lombok.Getter;
import lombok.Setter;

/**
 * This class handles positions that are moved
 * @author Virt
 */
public class MovedLocation extends FixedLocation {

    @Getter @Setter
    protected float velocityX, velocityY;

    @Override
    public void time(float delta) {
        posX += velocityX * delta;
        posY += velocityY * delta;

        super.time(delta);
    }
}
