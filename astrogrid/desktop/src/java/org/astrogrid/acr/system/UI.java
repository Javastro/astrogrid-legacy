/*$Id: UI.java,v 1.2 2005/04/27 13:42:41 clq2 Exp $
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

import java.awt.Component;


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Mar-2005
 *
 */
public interface UI{
    
    public void show();
    
    public void hide();
    
    public Component getComponent();
    
    
    public void startThrobbing() ;
    
    public void stopThrobbing();
    
    public void setLoggedIn(boolean value);
        
    
    public void setStatusMessage(String msg);
    
}

/* 
 $Log: UI.java,v $
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