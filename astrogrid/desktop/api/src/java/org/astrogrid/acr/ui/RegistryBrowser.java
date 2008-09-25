/*$Id: RegistryBrowser.java,v 1.9 2008/09/25 16:02:09 nw Exp $
 * Created on 30-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.ui;

import java.net.URI;

import org.astrogrid.acr.dialogs.RegistryGoogle;


/** AR Service: Launch a new VOExplorer GUI
 * 
 * @service userInterface.registryBrowser
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 30-Mar-2005
 *@see org.astrogrid.acr.ivoa.Registry
 *@see RegistryGoogle Registry Dialogue
 */
public interface RegistryBrowser {
    /**show a new instance of VOExplorer*/
    public void show();
    /** Not implmented
     * @exclude 
     * @deprecated.*/
    public void hide();
    
    /** show an new instance of the VOExplorer, and populate it using an xquery 
     * @param xquery an xquery to populate the resource chooser with. Same format as in an xquery list in voexplorer UI - which is convenient for
     * constructing these queries.
     */
    public  void search(String xquery) ;
    
    /** Show a new instance of VOExplorer, and display a single resource record in it 
     * 
     * @param uri the resource ID of the record to display
     */
    public void open(URI uri);
}

/* 
 $Log: RegistryBrowser.java,v $
 Revision 1.9  2008/09/25 16:02:09  nw
 documentation overhaul

 Revision 1.8  2007/01/24 14:04:45  nw
 updated my email address

 Revision 1.7  2006/10/12 02:22:33  nw
 fixed up documentaiton

 Revision 1.6  2006/08/31 20:22:59  nw
 doc fix.

 Revision 1.5  2006/08/15 09:48:56  nw
 added new registry interface, and bean objects returned by it.

 Revision 1.4  2006/02/02 14:19:48  nw
 fixed up documentation.

 Revision 1.3  2005/11/24 01:18:42  nw
 merged in final changes from release branch.

 Revision 1.2.16.1  2005/11/23 04:32:54  nw
 tidied up

 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:01  nw
 finished split

 Revision 1.4  2005/05/12 15:59:10  clq2
 nww 1111 again

 Revision 1.2.8.1  2005/05/11 11:55:19  nw
 javadoc

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:18  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.1  2005/04/01 19:03:10  nw
 beta of job monitor
 
 */