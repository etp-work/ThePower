package org.etp.portalKit.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The purpose of this class is to provide a controller to send
 * start/stop command to embedded tomcat service.
 */
public class Controller {
    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0 || args.length != 2) {
            System.err.println("args should not be null.");
            System.err.println("you should set a command such as : start[stop] port");
            System.exit(-1);
        }

        @SuppressWarnings("null")
        String command = args[0];
        if (!"start".equalsIgnoreCase(command) && !"stop".equalsIgnoreCase(command)) {
            System.err.println("you should set a command such as : start[stop] port");
            System.exit(-1);
        }

        if ("start".equalsIgnoreCase(command))
            command = "start";
        else
            command = "stop";

        int port = Integer.parseInt(args[1]);

        Socket s = null;
        DataOutputStream dos = null;
        try {
            s = new Socket("127.0.0.1", port);
            dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(command);
            dos.flush();
            dos.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
