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
public class UpdateThread extends Thread {

    private long lastUpdate;
    private long lastSecond;
    private int frameAmount;

    private long frameDelta;

    @Getter
    private boolean running;
    @Setter
    private boolean paused;

    private DisplayHandler handler;

    /**
     * Creates the thead without starting it
     * @param handler handler to handle thread
     */
    public UpdateThread(DisplayHandler handler) {
        super ("Rendering");
        this.handler = handler;
    }

    /**
     * Sets the framerate
     * @param frameRate framerate to try to achieve
     */
    public void setFrameRate(int frameRate){
        frameDelta = Time.NANO_AMOUNT / frameRate;
    }

    @Override
    public void run() {
        running = true;
        log.info("Started Rendering Thread");
        lastUpdate = Time.getNanos();

        while (running) {
            if (paused) continue;
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
    }
}
