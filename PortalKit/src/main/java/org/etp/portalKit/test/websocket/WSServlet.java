package org.etp.portalKit.test.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

/**
 * WebSocketServlet
 */
public class WSServlet extends WebSocketServlet {
    private static final long serialVersionUID = 1L;
    private static ArrayList<WSMessageInbound> connList = new ArrayList<WSMessageInbound>();

    private class WSMessageInbound extends MessageInbound {
        WsOutbound wsoutbound;

        @Override
        public void onOpen(WsOutbound outbound) {
            try {
                this.wsoutbound = outbound;
                connList.add(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClose(int status) {
            connList.remove(this);
        }

        @Override
        public void onTextMessage(CharBuffer cb) throws IOException {
            for (WSMessageInbound conn : connList) {
                CharBuffer buffer = CharBuffer.wrap(cb);
                conn.wsoutbound.writeTextMessage(buffer);
                conn.wsoutbound.flush();
            }
        }

        @Override
        public void onBinaryMessage(ByteBuffer bb) throws IOException {
            // Not implemented
        }
    }

    @Override
    protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest arg1) {
        return new WSMessageInbound();
    }
}
