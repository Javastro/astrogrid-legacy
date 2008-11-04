/*$Id: ResourceChooserInternal.java,v 1.9 2008/11/04 14:35:52 nw Exp $
 * Created on 24-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import java.awt.Component;
import java.net.URI;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.dialogs.ResourceChooser;

/** Internal interface to resource chooser - allows you to specifiy a parent component.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 24-Aug-2005
 *
 */
public interface ResourceChooserInternal extends ResourceChooser {

   
    public URI chooseDirectoryWithParent(String title,boolean enableMySpace,boolean enableLocalFile, boolean enableURL,Component comp);
    
    public URI chooseResourceWithParent(String title,boolean enableMySpaceDir,boolean enableLocalDir, boolean enableURL,Component comp);    

    
    /** access the vfs component - for convenience */
    public FileSystemManager getVFS();
}


/* 
$Log: ResourceChooserInternal.java,v $
Revision 1.9  2008/11/04 14:35:52  nw
javadoc polishing

Revision 1.8  2007/10/07 10:40:14  nw
added method to access VFS, as it's present.

Revision 1.7  2007/08/30 23:46:48  nw
Complete - task 73: upgrade filechooser dialogue to new fileexplorer code
replaced uses of myspace by uses of vfs where sensible

Revision 1.6  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.5  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.4.46.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.4  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.16.1  2005/10/10 18:12:37  nw
merged kev's datascope lite.

Revision 1.3  2005/10/07 12:12:21  KevinBenson
resorted back to adding to the ResoruceChooserInterface a new method for selecting directories.
And then put back the older one.

Revision 1.2  2005/10/04 20:41:52  KevinBenson
added the ability to select directories on a local file system.  That way myspace can save to a directory.
Only myspacebrowser has this turned on at the moment.

Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/