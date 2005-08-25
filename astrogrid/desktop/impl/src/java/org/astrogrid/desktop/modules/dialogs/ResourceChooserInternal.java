/*$Id: ResourceChooserInternal.java,v 1.1 2005/08/25 16:59:58 nw Exp $
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

    public URI chooseResourceWithParent(String title,boolean enableMySpace,boolean enableLocalFile,boolean enableURI,Component comp);

}


/* 
$Log: ResourceChooserInternal.java,v $
Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/