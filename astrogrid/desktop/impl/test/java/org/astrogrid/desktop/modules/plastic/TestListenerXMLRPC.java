package org.astrogrid.desktop.modules.plastic;


import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.xmlrpc.WebServer;
import org.astrogrid.desktop.modules.plastic.AbstractPlasticBase.TestPlasticApplication;
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
public class TestListenerXMLRPC implements TestPlasticApplication{



    boolean connected = false;

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
    
    /** Add a new message handler to the start of the chain */
    public synchronized void addHandler(MessageHandler h) {
        h.setNextHandler(handler);
        handler = h;
    }
    
    /**
     * Construct a new Test listener, with the given metadata if desired.  
     * @param metaData meta data about the app...see the keys in the TestPlasticApplication interface.  If null or empty then defaults are used.
     * 
     */
    public TestListenerXMLRPC(Properties metaData) {
        if (metaData==null) metaData = new Properties();
        final String name = metaData.getProperty(NAME,"TestListenerXMLRPC");
        final String desc = metaData.getProperty(DESC,"A testing client that uses XMLRPC for comms");
        final String ivorn = metaData.getProperty(IVORN,"");
        final String logoUrl = metaData.getProperty(LOGOURL,"");
        final String version = metaData.getProperty(VERSION,PlasticListener.CURRENT_VERSION);
        
        server = new Server(this);
        handler = new EchoHandler();
        MessageHandler handler1= new StandardHandler(name,desc,ivorn,logoUrl,version);
        handler.setNextHandler(handler1);
    }

    public URI registerWith(PlasticHubListener hub, String name) {
        return hub.registerXMLRPC(name, getMessages(), getCallBackURL());
    }

    public List getMessages() {
        return handler.getHandledMessages();
    }
}

class Server {
    
    public int getPort() {
        return port;
    }
    private int port;

    public Server(Object handler) {
        try {
            port = new PortFinder(8000,9999).getFreePort();
        } catch (Exception e) {
            throw new RuntimeException("Problem getting free port for XML-RPC server",e);
        } 
        WebServer webServer = new WebServer(port);
        webServer.addHandler("$default", handler);
      
        webServer.start();
    }
    

}
/**
 * Code culled from {@link org.astrogrid.desktop.modules.system.AbstractRmiServerImpl} to find a spare port.
 * 
 *
 */
class PortFinder {
    private int start;
    private int end;
    public PortFinder(int start, int end) {
        this.start=start;
        this.end=end;
    }
    /**
     * Scan for a free port
     * @return
     * @throws NoFreePortException if there's none available in the range selected
     * @throws UnknownHostException can't see how this could happen unless you don't have a network card
     */
    public int getFreePort() throws NoFreePortException, UnknownHostException {

        InetAddress local;

        local = InetAddress.getLocalHost();
        for (int i = start; i < end; i++) {
            if (checkPort(i,local)) {
                return i;
            }            
        } 
            throw new NoFreePortException();         
    }
    public static class NoFreePortException extends Exception {

        /**
         * 
         */
        private static final long serialVersionUID = 172137606940449490L;}
    
  
    /** will return true if this port can be connected to */
    private  boolean checkPort(int i,InetAddress addr) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(i,50,addr);            
            return true;
        } catch (IOException e) {    // oh well, that port is already taken. try another.
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    // ignore.
                }
            }
        }
        return false;
    }
}
