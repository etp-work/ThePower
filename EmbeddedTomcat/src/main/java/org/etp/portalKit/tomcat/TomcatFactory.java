package org.etp.portalKit.tomcat;

/**
 * Hold tomcat instance in static.
 */
public class TomcatFactory {

    private static EmbeddedTomcat tomcat;

    private static boolean isStarted;

    /**
     * @param port
     * @return instance of tomcat
     */
    public static EmbeddedTomcat getInstance(int port) {
        if (tomcat == null) {
            tomcat = new EmbeddedTomcat(port);
        }
        return tomcat;
    }

    /**
     * @return Returns the isStarted.
     */
    public static boolean isStarted() {
        return isStarted;
    }

    /**
     * @param isStarted The isStarted to set.
     */
    public static void setStarted(boolean isStarted) {
        TomcatFactory.isStarted = isStarted;
    }
}
