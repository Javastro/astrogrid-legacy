/*$Id: SystemTray.java,v 1.1 2005/06/22 08:48:52 nw Exp $
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

/** Interface to ACR icon in the system tray.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Jun-2005
 *
 */
public interface SystemTray {
    void displayErrorMessage(String caption,String message);
    void displayInfoMessage(String caption, String message);
    void displayWarningMessage(String caption,String message);
    
    public void startThrobbing();
    public void stopThrobbing();

}

/* 
 $Log: SystemTray.java,v $
 Revision 1.1  2005/06/22 08:48:52  nw
 latest changes - for 1.0.3-beta-1
 
 */