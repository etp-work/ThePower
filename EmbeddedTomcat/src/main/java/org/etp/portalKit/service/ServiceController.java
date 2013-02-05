package org.etp.portalKit.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.etp.portalKit.service.handler.ServiceHandler;
import org.etp.portalKit.service.handler.TomcatHandler;

/**
 * The purpose of this class is to provide a service controller to
 * handle issues about start/stop tomcat.
 */
public class ServiceController {
    private ServerSocket server;

    private List<ServiceHandler> handlers;

    private boolean stopped;

    /**
     * Creates a new instance of <code>ServiceController</code>.
     * 
     * @param port of service
     */
    public ServiceController(int port) {
        handlers = new ArrayList<ServiceHandler>();
        try {
            server = new ServerSocket(port);
        } catch (IOException e1) {
            e1.printStackTrace();
            System.err.println(e1.getMessage());
            System.exit(-1);
        }
    }

    /**
     * @param tomcatPort
     */
    public void startService(int tomcatPort) {
        while (!stopped) {
            Socket socket = null;
            try {
                socket = server.accept();
                ServiceHandler handler = new TomcatHandler(socket, tomcatPort, handlers);
                new Thread(handler).start();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < handlers.size(); i++) {
            ServiceHandler handler = handlers.get(i);
            handler.stopHandler();
        }
    }

    /**
     * 
     */
    public void stopService() {
        stopped = true;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println("args should not be null.");
            System.err.println("you should set them in key=value style, such as : servicePort=9876");
            System.exit(-1);
        }
        Map<String, String> parameters = new HashMap<String, String>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            String[] keyvalues = arg.split("=");
            if (keyvalues.length != 2) {
                System.err.println("you should set arg in key=value style, such as : tomcatPort=9876");
                System.exit(-1);

            }
            String key = keyvalues[0];
            String value = keyvalues[1];
            parameters.put(key, value);
        }

        int servicePort = Integer.parseInt(parameters.get("servicePort"));
        int tomcatPort = Integer.parseInt(parameters.get("tomcatPort"));

        final ServiceController controller = new ServiceController(servicePort);
        controller.startService(tomcatPort);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                controller.stopService();
            }
        });
    }
}
