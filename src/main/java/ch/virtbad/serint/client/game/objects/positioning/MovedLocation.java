package ch.virtbad.serint.client.game.objects.positioning;

import ch.virtbad.serint.client.game.collisions.CollisionResult;
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

    @Override
    public void timeCollided(float delta, CollisionResult result) {
        float deltaX = velocityX * delta;
        float deltaY = velocityY * delta;

        if (deltaX > 0) posX += result.getXEndPadding() == -1 ? deltaX : Math.min(deltaX, result.getXEndPadding());
        if (deltaX < 0) posX += result.getXStartPadding() == -1 ? deltaX : Math.max(deltaX, -result.getXStartPadding());

        if (deltaY > 0) posY += result.getYEndPadding() == -1 ? deltaY : Math.min(deltaY, result.getYEndPadding());
        if (deltaY < 0) posY += result.getYStartPadding() == -1 ? deltaY : Math.max(deltaY, -result.getYStartPadding());
    }
}
