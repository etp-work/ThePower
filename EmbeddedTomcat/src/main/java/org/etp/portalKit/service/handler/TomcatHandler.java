package org.etp.portalKit.service.handler;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.etp.portalKit.tomcat.EmbeddedTomcat;
import org.etp.portalKit.tomcat.TomcatFactory;

/**
 * The purpose of this class is ...
 */
public class TomcatHandler implements ServiceHandler {

    private Socket socket;
    private DataInputStream dis;
    private boolean stoped;

    private EmbeddedTomcat tomcat;

    private List<ServiceHandler> handlers;

    /**
     * Creates a new instance of <code>TomcatHandler</code>.
     * 
     * @param socket
     * @param tomcatPort
     * @param handlers
     * @throws IOException
     * @throws ServletException
     */
    public TomcatHandler(Socket socket, int tomcatPort, List<ServiceHandler> handlers) throws ServletException,
            IOException {
        this.socket = socket;
        this.handlers = handlers;
        try {
            this.dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            closeHandler();
            throw e;
        }
        tomcat = TomcatFactory.getInstance(tomcatPort);
    }

    /**
     * 
     */
    private void closeHandler() {
        try {
            if (dis != null)
                dis.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TomcatFactory.isStarted()) {
            try {
                TomcatFactory.setStarted(false);
                tomcat.stop();
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        }
        handlers.remove(this);
    }

    @Override
    public void run() {
        String command = null;
        while (!stoped) {
            try {
                if ((command = dis.readUTF()) != null) {
                    System.out.println("command = " + command);
                    if ("start".equals(command)) {
                        tomcat.start();
                        TomcatFactory.setStarted(true);
                    } else if ("stop".equals(command)) {
                        TomcatFactory.setStarted(false);
                        tomcat.stop();
                    }
                }
            } catch (EOFException e) {
                stoped = true;
            } catch (IOException | LifecycleException e) {
                e.printStackTrace();
                stoped = true;
            } catch (ServletException e) {
                e.printStackTrace();
                stoped = true;
            }
        }
        System.out.println("closing");
        closeHandler();
    }

    @Override
    public void stopHandler() {
        stoped = true;
    }
}
