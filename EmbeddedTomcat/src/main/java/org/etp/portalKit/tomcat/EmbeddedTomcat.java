package org.etp.portalKit.tomcat;

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

}
