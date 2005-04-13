/*$Id: JettyWebServer.java,v 1.2 2005/04/13 12:59:12 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.desktop.framework.ModuleRegistry;
import org.astrogrid.desktop.framework.NewModuleEvent;
import org.astrogrid.desktop.framework.NewModuleListener;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.util.InetAddrPort;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.Startable;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Random;

/** Factory to create a webserver, listening to a random port, with a hashed key path.
 * @todo find a spare port
 * @todo remove 'helpful' root page.
 * @todo improve error reporting.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class JettyWebServer implements UrlRoot, Startable, WebServer, NewModuleListener{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JettyWebServer.class);

    public JettyWebServer(ModuleRegistry reg) throws Exception {
        super();
        this.server = new Server();
        this.server.setStopGracefully(true);        
        findSparePort();
         genKey();
        recordDetails()  ;      
        server.addListener(new InetAddrPort(port));
       
        this.context = (ServletHttpContext) server.addContext(key);     

        this.context.getServletContext().setAttribute(WebServer.MODULE_REGISTRY,reg);
        
        // configure digester.
        this.dig = new Digester() {{
            addCallMethod("*/servlet","addServlet",3,new Class[]{String.class,String.class,Class.class});
            addCallParam("*/servlet",0,"name");
            addCallParam("*/servlet",1,"path");
            addCallParam("*/servlet",2,"class");
        }};
     
        // we're ready to go. finally register as a listener
        reg.addNewModuleListener(this);        
        
    }
    
    public void addServlet(String name,String path,Class clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
            logger.info("Adding servlet " + name + ", " + path + ", " + clazz);
            context.addServlet(name,path,clazz.getName());
    }
    
    public void start() {
        logger.info("Strarting webserver");
        try {
            server.start();
        } catch (Exception e) {
            throw new PicoInitializationException("Could not start webserver",e);
        }
    }

    protected final Server server;
    protected final ServletHttpContext context;
    protected int port;
    protected String key;
    protected String urlRoot;
    protected final Digester dig;
    //@todo implement
    private void findSparePort() {
        port= 8050;
    }
    /* generates a random string */
    private void genKey() {
        Random r = new Random();
        key = Long.toString(Math.abs(r.nextLong()),16);        
    }
    
    
    private void recordDetails() throws IOException {
        urlRoot = "http://localhost:" + port + "/" + key + "/";
        File homeDir = new File(System.getProperty("user.home"));
        File f = new File(homeDir,".astrogrid-desktop");
        if (f.exists()) {
            f.delete();
        }
        f.deleteOnExit();
        PrintWriter pw = new PrintWriter(new FileWriter(f));
        pw.println(urlRoot);
        pw.close();
    }
    

    public String getKey() {
        return this.key;
    }

    public int getPort() {
        return this.port;
    }

    public String getUrlRoot() {
        return this.urlRoot;
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        try {
            server.stop();
        } catch (InterruptedException e) {
            logger.debug("InterruptedException",e);
        }
    }

    /**
     * @see org.astrogrid.desktop.framework.NewModuleListener#newModuleRegistered(org.astrogrid.desktop.framework.NewModuleEvent)
     */
    public void newModuleRegistered(NewModuleEvent e) {
        // scan for any modules declaring servlets..
        ModuleDescriptor m = e.getModule().getDescriptor();
        String p  = m.getPropertyDocument("system.webserver");
        if (p != null) {
            logger.info("Found webserver block");
            logger.info(p);
            try {
                dig.clear();
                dig.push(this);
                dig.parse(new StringReader( p));
            } catch (IOException e1) {
                logger.warn("Error registering servlet ",e1);
            } catch (SAXException e1) {
                logger.warn("Error registering servlet ",e1);
            }
        }
    }
    

}


/* 
$Log: JettyWebServer.java,v $
Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/03/23 14:36:18  nw
got pw working

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/