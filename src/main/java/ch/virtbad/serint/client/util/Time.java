package ch.virtbad.serint.client.util;

/**
 * This class handles time related stuff
 *
 * @author Virt
 */
public class Time {
    public static final long NANO_AMOUNT = (long) 1e+9;
    public static final long MILLI_AMOUNT = (long) 1e+6;

    /**
     * Returns the current time in nanoseconds
     *
     * @return current nanoseconds
     */
    public static long getNanos() {
        return System.nanoTime();
    }

    /**
     * Returns the current time in milliseconds
     *
     * @return current milliseconds
     */
    public static long getMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Returns the current time in seconds
     *
     * @return current seconds
     */
    public static float getSeconds() {
        return System.currentTimeMillis() / 1000f;
    }

    /**
     * Sleeps for an amount of time (without exceptions)
     * @param millis time to sleep in ms
     */
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) { }
    }
}
