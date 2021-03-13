package ch.virtbad.serint.client.graphics;

import ch.virtbad.serint.client.util.Globals;
import ch.virtbad.serint.client.util.Time;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Virt
 */
@Slf4j
public class DisplayUpdater {

    private long lastUpdate;
    private long lastSecond;
    private int frameAmount;

    private long frameDelta;

    private DisplayHandler handler;

    /**
     * Creates the thead without starting it
     * @param handler handler to handle thread
     */
    public DisplayUpdater(DisplayHandler handler) {
        this.handler = handler;
    }

    /**
     * Sets the framerate
     * @param frameRate framerate to try to achieve
     */
    public void setFrameRate(int frameRate){
        frameDelta = Time.NANO_AMOUNT / frameRate;
    }


    public void call(){
        long time = Time.getNanos();

        if (time - lastUpdate > frameDelta) {
            lastUpdate = time;
            frameAmount++;

            handler.update();
        }

        if (time - lastSecond > Time.NANO_AMOUNT){
            lastSecond = Time.getNanos();
            Globals.getRendering().setFps(frameAmount);
            frameAmount = 0;
        }
    }

    public void forceCall(){
        handler.update();
    }
}
