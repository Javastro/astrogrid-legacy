/*$Id: Main.java,v 1.1 2005/02/21 11:25:07 nw Exp $
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
import org.astrogrid.desktop.ui.Desktop;

import java.io.IOException;

/** Main class of Desktop server - creates server, initializes services, registers protocols, fire up gui.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class Main implements Runnable {

    /** Construct a new Server
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * 
     */
    public Main() throws Exception {
        services = new Services();
        server = new DesktopServer(services);
        services.registerComponentInstance(server); //tying the knot..
    }
    
    protected final DesktopServer server;
    protected final Services services;

    public static void main(String[] args) {
        
        // create Server
        Main m = null;
        try {
            m = new Main();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        m.run();   
    }

    public void run() {
        try {
            services.start();
            server.start();         
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


/* 
$Log: Main.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/