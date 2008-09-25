/*$Id: UI.java,v 1.5 2008/09/25 16:02:03 nw Exp $
 * Created on 17-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.system;



/** Control the main user interface of the workbench.
 * @exclude not interesting to the scripter. probably shouldn't be public in the first place.
 * Show or Hide the main user interface, display messages in the status bar<br />
 * @service system.ui
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Mar-2005
 *
 */
public interface UI{
    
    /** show the user interface */
    public void show();
    
    /** hide the user interface - ACR server will continue to run */
    public void hide();
    
    /** change the activity icon to indicate 'communication in progress' */
    public void startThrobbing() ;
    
    /** change the activity icon to indicate 'communication completed' */
    public void stopThrobbing();
    
    /** set the login indicator
     * 
     * @param value true to indicate logged in, false to indicate 'not logged in'
     */
    public void setLoggedIn(boolean value);
        
    
    /** set the status message */
    public void setStatusMessage(String msg);

    
}

/* 
 $Log: UI.java,v $
 Revision 1.5  2008/09/25 16:02:03  nw
 documentation overhaul

 Revision 1.4  2007/01/24 14:04:44  nw
 updated my email address

 Revision 1.3  2006/02/02 14:19:47  nw
 fixed up documentation.

 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.6  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.5  2005/06/22 08:48:52  nw
 latest changes - for 1.0.3-beta-1

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

 Revision 1.1.2.5  2005/04/06 15:03:54  nw
 added new front end - more modern, with lots if icons.

 Revision 1.1.2.4  2005/04/04 16:43:48  nw
 made frames remember their previous positions.
 synchronized guiLogin, so only one login box ever comes up.
 made refresh action in jobmonitor more robust

 Revision 1.1.2.3  2005/04/04 08:49:27  nw
 working job monitor, tied into pw launcher.

 Revision 1.1.2.2  2005/03/18 15:47:37  nw
 worked in swingworker.
 got community login working.

 Revision 1.1.2.1  2005/03/18 12:09:31  nw
 got framework, builtin and system levels working.
 
 */