/*$Id: SystemTray.java,v 1.4 2006/04/18 23:25:47 nw Exp $
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

/** Control the  ACR icon in the Desktop system tray. 
 * 
 * Can display popup alert messages in the notification area, and control the progress indicator.
 * <br/>
 * Only supported on some operating systems - windows and some flavours of unix. If not supported on the current OS,
 * {@link org.astrogrid.acr.builtin.ACR#getService(Class)} will throw a {@link org.astrogrid.acr.NotFoundException} when attempting to retreive this service.
 * @service system.systray
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Jun-2005
 *
 */
public interface SystemTray {
    
    /** Display a popup message with the os-local 'error' presentation
     * @param caption popup title
     * @param message text of the message
     */
    void displayErrorMessage(String caption,String message);
    /** Display a popup message with the os-local 'info' presentation
     * @param caption popup title
     * @param message text of the message
     */
    void displayInfoMessage(String caption, String message);
    /** Display a popup message with the os-local 'warning' presentation
     * @param caption popup title
     * @param message text of the message
     */    
    void displayWarningMessage(String caption,String message);
    
    /** change the tray icon to indicate ' communication in progress' */
    public void startThrobbing();
    /** change the tray icon to indicate 'communication finished' */
    public void stopThrobbing();

}

/* 
 $Log: SystemTray.java,v $
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