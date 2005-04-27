/*$Id: Portal.java,v 1.2 2005/04/27 13:42:41 clq2 Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.astrogrid;

import java.util.Map;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
public interface Portal {
    /** open page in the portal 
     * @throws MalformedURL, Excepti
     * @throws ExceptiononException*/
    void openPage(String path) throws Exception;
    
    /** open a page in the portal, passing in a set of arguments 
     * @throws Exception*/
    void openPageWithParams(String path,Map args) throws Exception;
}

/* 
 $Log: Portal.java,v $
 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:11  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.3  2005/04/04 08:49:27  nw
 working job monitor, tied into pw launcher.

 Revision 1.1.2.2  2005/03/29 16:10:59  nw
 integration with the portal

 Revision 1.1.2.1  2005/03/23 14:36:18  nw
 got pw working
 
 */