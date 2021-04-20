package ch.virtbad.serint.client.util;

/**
 * This class handles time related stuff
 *
 * @author Virt
 */
public class Time {
    public static final long NANO_AMOUNT = (long) 1e+9;
    public static final long MICRO_AMOUNT = (long) 1e+6;
    public static final long MILLI_AMOUNT = (long) 1e+3;

    public static final long START = System.currentTimeMillis();
    public static final long START_SECONDS = START / MILLI_AMOUNT;

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
        return (System.currentTimeMillis() - START) / 1000f; //TODO: Make Unified and why does it need this?
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
