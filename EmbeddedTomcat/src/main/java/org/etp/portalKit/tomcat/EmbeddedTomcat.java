package org.etp.portalKit.tomcat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * The purpose of this class is to provide a customized tomcat.
 */
public class EmbeddedTomcat {

    private Tomcat tomcat;
    private int port;

    /**
     * Creates a new instance of <code>EmbeddedTomcat</code>.
     * 
     * @param port
     */
    public EmbeddedTomcat(int port) {
        this.port = port;
    }

    /**
     * @throws LifecycleException
     * @throws ServletException
     */
    public void start() throws LifecycleException, ServletException {
        if (tomcat == null) {
            tomcat = new Tomcat();
            tomcat.setPort(port);
            tomcat.setBaseDir(".");
            tomcat.addWebapp("/PortalKit", "PortalKit.war");
        }
        tomcat.start();
        tomcat.getServer().await();
    }

    /**
     * @throws LifecycleException
     */
    public void stop() throws LifecycleException {
        try {
            tomcat.stop();
            tomcat.getServer().destroy();
            tomcat.destroy();
        } catch (Exception e) {
            //
        }
        tomcat = null;
    }

    /**
     * entrance of EmbeddedTomcat. you can set args like port=9876 to
     * start tomcat by binding port 9876.
     * 
     * @param args
     */
    @SuppressWarnings("null")
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println("args should not be null.");
            System.err.println("you should set them in key=value style, such as : port=9876");
            System.exit(-1);
        }
        Map<String, String> parameters = new HashMap<String, String>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            String[] keyvalues = arg.split("=");
            if (keyvalues.length != 2) {
                System.err.println("you should set arg in key=value style, such as : port=9876");
                System.exit(-1);

            }
            String key = keyvalues[0];
            String value = keyvalues[1];
            parameters.put(key, value);
        }

        int tomcatPort = Integer.parseInt(parameters.get("port"));

        EmbeddedTomcat tomcat = new EmbeddedTomcat(tomcatPort);
        try {
            tomcat.start();
        } catch (LifecycleException | ServletException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
}
