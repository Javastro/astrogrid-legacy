/*$Id: ResourceChooserImpl.java,v 1.5 2005/10/07 12:12:21 KevinBenson Exp $
 * Created on 21-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.system.UIInternal;

import java.awt.Component;
import java.net.URI;

/** Implementation of the ResourceChooser component
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2005
 * wrapper component around the dialog to publish the chooser methods 
 */
public class ResourceChooserImpl implements ResourceChooserInternal {

    public ResourceChooserImpl(MyspaceInternal vos,Configuration conf,HelpServer help,UIInternal ui, Community comm) {
        this.vos = vos;
        dialog = new ResourceChooserDialog(vos,conf,help,ui, comm) ;
        dialog.pack();
    }
    protected final MyspaceInternal vos;
    protected final ResourceChooserDialog dialog;
    public synchronized URI chooseResource(String title,boolean enableMySpace) {
        dialog.setTitle(title);
        dialog.setEnableMySpacePanel(enableMySpace);
        dialog.setUri(null);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return dialog.getUri();        
    }
    
    public synchronized URI fullChooseResource(String title,boolean enableMySpace,boolean enableLocalFile,boolean enableURI) {
        dialog.setEnableLocalFilePanel(enableLocalFile);
        dialog.setEnableURIPanel(enableURI);
        return chooseResource(title,enableMySpace);        
    }
    
    public synchronized URI chooseResourceWithParent(String title,boolean enableMySpace,boolean enableLocalFile, boolean enableURI,Component comp) {
        dialog.setLocationRelativeTo(comp);
        dialog.setTitle(title);
        dialog.setEnableLocalFilePanel(enableLocalFile);
        dialog.setEnableURIPanel(enableURI);
        dialog.setEnableMySpacePanel(enableMySpace);
        dialog.setUri(null);
        dialog.setVisible(true);
        return dialog.getUri();    
    }    
    
    public synchronized URI chooseResourceWithParent(String title,boolean enableMySpace,boolean enableLocalFile, boolean enableDirectorySelection, boolean enableURI,Component comp) {
        dialog.setLocationRelativeTo(comp);
        dialog.setTitle(title);
        dialog.setEnableLocalFilePanel(enableLocalFile);
        dialog.setEnableURIPanel(enableURI);
        dialog.setEnableMySpacePanel(enableMySpace);
        dialog.setEnabledDirectorySelection(enableDirectorySelection);
        dialog.setUri(null);
        dialog.setVisible(true);
        return dialog.getUri();    
    }

}


/* 
$Log: ResourceChooserImpl.java,v $
Revision 1.5  2005/10/07 12:12:21  KevinBenson
resorted back to adding to the ResoruceChooserInterface a new method for selecting directories.
And then put back the older one.

Revision 1.4  2005/10/04 20:41:52  KevinBenson
added the ability to select directories on a local file system.  That way myspace can save to a directory.
Only myspacebrowser has this turned on at the moment.

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.4  2005/05/12 15:59:08  clq2
nww 1111 again

Revision 1.2.8.1  2005/05/09 14:51:02  nw
renamed to 'myspace' and 'workbench'
added confirmation on app exit.

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.1.2.1  2005/04/22 10:54:03  nw
start of a new module.
 
*/