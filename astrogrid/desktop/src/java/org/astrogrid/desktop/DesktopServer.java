/*$Id: DesktopServer.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop;

import org.astrogrid.desktop.protocol.http.HtmlServlet;
import org.astrogrid.desktop.protocol.xmlrpc.XmlRpcServlet;
import org.astrogrid.desktop.service.Services;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHttpContext;
import org.mortbay.util.InetAddrPort;
import org.picocontainer.Startable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/** Factory to create a webserver, listening to a random port, with a hashed key path.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class DesktopServer extends Server implements UrlRoot{

    public DesktopServer(Services services) throws Exception {
        super();
        findSparePort();
         genKey();
        recordDetails()  ;      
        this.addListener(new InetAddrPort(port));
        ServletHttpContext context = (ServletHttpContext) this.addContext(key);     
       context.getServletContext().setAttribute(DesktopServer.SERVICES,services);
        context.addServlet("xmlrpc","/xmlrpc",XmlRpcServlet.class.getName());        
        context.addServlet("html","/*",HtmlServlet.class.getName());        
    }

    
    protected int port;
    protected String key;
    protected String urlRoot;
    public final static String SERVICES = "services";
    
    //@todo implement
    private void findSparePort() {
        port= 8050;
    }
    //@todo implement
    private void genKey() {
        key= "wibble";
    }
    
    
    private void recordDetails() throws IOException {
        urlRoot = "http://localhost:" + port + "/" + key + "/";
        File homeDir = new File(System.getProperty("user.home"));
        File f = new File(homeDir,".astrogrid-desktop");
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
}


/* 
$Log: DesktopServer.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/