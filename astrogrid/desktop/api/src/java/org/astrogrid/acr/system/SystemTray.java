/*$Id: SystemTray.java,v 1.6 2008/09/25 16:02:03 nw Exp $
 * Created on 21-Jun-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.system;

/** AR Service: Control the AstroRuntime icon in the desktop system tray. 
 * <p />
 * Can display popup alert messages in the notification area, and control the progress indicator.
 * 
 * @warning Not all operating systems provide a system tray. On those that don't, a best effort will be made to support popup
 * messages in some way
 * @service system.systray
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Jun-2005
 *
 */
public interface SystemTray {
    
    /** Display a popup message with an 'error' presentation.
     * @param caption popup title
     * @param message text of the message
     */
    void displayErrorMessage(String caption,String message);
    /** Display a popup message with an 'info' presentation.
     * @param caption popup title
     * @param message text of the message
     */
    void displayInfoMessage(String caption, String message);
    /** Display a popup message with a 'warning' presentation.
     * @param caption popup title
     * @param message text of the message
     */    
    void displayWarningMessage(String caption,String message);
    
    /** change the tray icon to indicate 'something in progress' */
    public void startThrobbing();
    /** change the tray icon to indicate 'something finished' */
    public void stopThrobbing();

}

/* 
 $Log: SystemTray.java,v $
 Revision 1.6  2008/09/25 16:02:03  nw
 documentation overhaul

 Revision 1.5  2007/01/24 14:04:44  nw
 updated my email address

 Revision 1.4  2006/04/18 23:25:47  nw
 merged asr development.

 Revision 1.3.6.1  2006/03/22 17:27:20  nw
 first development snapshot

 Revision 1.3  2006/02/02 14:19:47  nw
 fixed up documentation.

 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.1  2005/06/22 08:48:52  nw
 latest changes - for 1.0.3-beta-1
 
 */