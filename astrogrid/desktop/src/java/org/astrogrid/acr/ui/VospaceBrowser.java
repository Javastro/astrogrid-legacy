/*$Id: VospaceBrowser.java,v 1.2 2005/04/27 13:42:41 clq2 Exp $
 * Created on 07-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ui;

import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;

import java.net.URISyntaxException;
import java.rmi.RemoteException;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Apr-2005
 *
 */
public interface VospaceBrowser {
    public void show(); // shows last, or default position.
    public void hide();
}


/* 
$Log: VospaceBrowser.java,v $
Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 10:55:32  nw
implemented vospace file chooser dialogue.

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.4  2005/04/13 12:23:29  nw
refactored a common base class for ui components
 
*/