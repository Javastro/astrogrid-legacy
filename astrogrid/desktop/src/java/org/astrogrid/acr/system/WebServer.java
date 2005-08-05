/*$Id: WebServer.java,v 1.5 2005/08/05 11:46:55 nw Exp $
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


/** Interface into the ACR web server
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public interface WebServer  {
    /** key that ACR is accessible from servlet context by 
     * */
    String ACR_CONTEXT_KEY = "module-registry";

    
    public abstract String getKey();

    /** get the port the webserver is running on */
    public abstract int getPort();
    
    /** get the root url of the context the HTML interface is running on */
    public abstract String getUrlRoot();
}

/* 
 $Log: WebServer.java,v $
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