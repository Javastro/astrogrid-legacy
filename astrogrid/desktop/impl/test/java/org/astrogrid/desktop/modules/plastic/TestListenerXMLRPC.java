package org.astrogrid.desktop.modules.plastic;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.xmlrpc.WebServer;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;
import org.votech.plastic.outgoing.policies.StandardXmlRpcPolicy;


/**
 * A listener that works by xml-rpc rather than rmi.
 * @author jdt
 *
 */
public class TestListenerXMLRPC {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Create a new listener and register it
        TestListenerXMLRPC listener = new TestListenerXMLRPC();
        listener.connect();
     
    }

    private void connect() {
        URL listeningOn = getCallBackURL();
        StandardXmlRpcPolicy policy = new StandardXmlRpcPolicy();
        PlasticHubListener hub = policy.getHub();
        List messages = handler.getHandledMessages();
        hub.registerXMLRPC("TestListenerXMLRPC", messages,listeningOn);
        
    }

    private Server server;

    private URL getCallBackURL() {
        int port = server.getPort();
        URL url;
        try {
            url = new URL("http","127.0.0.1", port,"");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Coding error - made an invalid URL",e);
        }
        return  url;
    }

    /**
     * The usual plastic client method.
     * @param sender
     * @param message
     * @param args
     */
    public void perform(String sender, String message, List args) {
        System.out.println("Got message "+message+" from "+sender);

    }
    
    MessageHandler handler;
    
    public TestListenerXMLRPC() {
        server = new Server(this);
        handler= new StandardHandler("TestListenerXMLRPC","A testing client that uses XMLRPC for comms","","",PlasticListener.CURRENT_VERSION);
        
    }
}

class Server {
    
    public int getPort() {
        return port;
    }
    private static final int port = 8081;

    public Server(Object handler) {
        WebServer webServer = new WebServer(port);
        webServer.addHandler("plastic", handler);
      
        webServer.start();
    }
}
