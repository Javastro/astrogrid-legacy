/*$Id: ResourceChooserInternal.java,v 1.4 2005/10/12 13:30:10 nw Exp $
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

import org.astrogrid.acr.dialogs.ResourceChooser;

import java.awt.Component;
import java.net.URI;

/** Internal interface to resource chooser - allows you to specifiy a parent component
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Aug-2005
 *
 */
public interface ResourceChooserInternal extends ResourceChooser {

    public URI chooseResourceWithParent(String title,boolean enableMySpace,boolean enableLocalFile, boolean enableDirectorySelection, boolean enableURI,Component comp);
    
    public URI chooseResourceWithParent(String title,boolean enableMySpace,boolean enableLocalFile, boolean enableURI,Component comp);    

}


/* 
$Log: ResourceChooserInternal.java,v $
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