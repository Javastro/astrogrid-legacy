/*$Id: WebServer.java,v 1.6 2008/09/25 16:02:03 nw Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.system;

import java.net.URL;


/** AR System Service: Information about the web-server running with the AstroRuntime.
 * 
 * The web-server provides the HTML and XMLRPC interfaces into the AR
 * This interface provides only informational methods, useful for working out how to connect to the webserver. 
 * It provides no way of administering the server or deploying new servlets. (although such operations are possible via startup commandline parameters)
 * @service system.webserver
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 15-Mar-2005
 *
 */
public interface WebServer  {
    /** key that ACR is accessible from servlet context by
     *  
     * */
    String ACR_CONTEXT_KEY = "module-registry";

    /** get the root url of the the HTML interface.
     * 
     * Append <tt>xmlrpc</tt> to this root url to access the XMLRPC interface 
     * @return a url of form <tt>http://<i>machine</i>:<i>getPort()</i>/<i>getKey()</i>/</tt>
     * */
    public URL getRoot();
    
    /** @exclude 
     * @deprecated usee {@link #getRoot} */
    @Deprecated
    public abstract String getUrlRoot();   
    /**
     * Access the obfuscated key that the main session of the AstroRuntime is running on.
     *  @see #getUrlRoot */
    public abstract String getKey();

    /**
     * Access the number of the port the AR webserver is running on.
     *  @see #getUrlRoot() */
    public abstract int getPort();
    
 
}

/* 
 $Log: WebServer.java,v $
 Revision 1.6  2008/09/25 16:02:03  nw
 documentation overhaul

 Revision 1.5  2007/03/22 18:54:10  nw
 added support for sessions.

 Revision 1.4  2007/01/24 14:04:44  nw
 updated my email address

 Revision 1.3  2006/02/02 14:19:47  nw
 fixed up documentation.

 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.5  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.4  2005/05/12 15:59:12  clq2
 nww 1111 again

 Revision 1.2.8.1  2005/05/11 11:55:19  nw
 javadoc

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:12  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.1  2005/03/18 12:09:31  nw
 got framework, builtin and system levels working.
 
 */