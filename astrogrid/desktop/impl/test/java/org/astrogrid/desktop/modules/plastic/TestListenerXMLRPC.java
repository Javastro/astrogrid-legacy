package org.astrogrid.desktop.modules.plastic;


import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Vector;

import org.apache.xmlrpc.WebServer;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.incoming.handlers.MessageHandler;


/**
 * A listener that works by xml-rpc rather than rmi.
 * @author jdt
 *
 */
public class TestListenerXMLRPC extends AbstractTestListener implements TestPlasticApplication{



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
     * Construct a new Test listener, with the given metadata if desired.  
     * @param metaData meta data about the app...see the keys in the TestPlasticApplication interface.  If null or empty then defaults are used.
     * 
     */
    public TestListenerXMLRPC(Properties metaData, MessageHandler handler) {
        super(metaData, "XMLRPC", handler);
        server = new Server(this);
    }

    public URI registerWith(PlasticHubListener hub) {
        return hub.registerXMLRPC(getName(), getMessages(), getCallBackURL());
    }
    
    public Object perform(String sender, String message, Vector args) {
        // a little annoying, but the xml-rpc lib won't recognise this if the
        // third arg is a List.
        try {
            return super.perform(new URI(sender), new URI(message), args);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return CommonMessageConstants.RPCNULL;
        }
    }

}
class Server {
    private static PortFinder portFinder = new PortFinder(9000,9999);
    public int getPort() {
        return port;
    }
    private int port;

    public Server(Object handler) {
        try {
            port = portFinder.getFreePort();
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
    public synchronized int getFreePort() throws NoFreePortException, UnknownHostException {

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
