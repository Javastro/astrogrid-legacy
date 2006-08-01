package org.astrogrid.desktop.modules.plastic;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import org.apache.xmlrpc.WebServer;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.EchoHandler;
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
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        // Create a new listener and register it
        TestListenerXMLRPC listener = new TestListenerXMLRPC();
        listener.connect();
     
    }

    boolean connected = false;
    private URI plid;
    public synchronized void connect() throws IOException {
        if (connected) return;
        URL listeningOn = getCallBackURL();
        StandardXmlRpcPolicy policy = new StandardXmlRpcPolicy();
        hub = policy.getHub();
        if (hub==null) throw new IOException("No hub is running");
        List messages = handler.getHandledMessages();
        plid = hub.registerXMLRPC("TestListenerXMLRPC", messages,listeningOn);
        connected=true;
    }
    
    public synchronized void disconnect() throws IOException {
        if (!connected) return;
        hub.unregister(plid);
        connected=false;
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
     * TODO this whole class will need an overhaul when we update xml-rpc libs.  In particular, the Vector should => a List
     */
    public Object perform(String sender, String message, Vector args) {
        try {
            return handler.perform(new URI(sender), new URI(message), args);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return CommonMessageConstants.RPCNULL;
        }
    }
    
    MessageHandler handler;
    private PlasticHubListener hub;
    
    public TestListenerXMLRPC() {
        server = new Server(this);
        handler = new EchoHandler();
        MessageHandler handler1= new StandardHandler("TestListenerXMLRPC","A testing client that uses XMLRPC for comms","","",PlasticListener.CURRENT_VERSION);
        handler.setNextHandler(handler1);
    }
}

class Server {
    
    public int getPort() {
        return port;
    }
    private static final int port = 8081;

    public Server(Object handler) {
        WebServer webServer = new WebServer(port);
        webServer.addHandler("$default", handler);
      
        webServer.start();
    }
}
