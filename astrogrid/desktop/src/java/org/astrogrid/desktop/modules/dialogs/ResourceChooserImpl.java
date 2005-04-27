/*$Id: ResourceChooserImpl.java,v 1.2 2005/04/27 13:42:41 clq2 Exp $
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

import org.astrogrid.acr.astrogrid.Vospace;
import org.astrogrid.acr.dialogs.ResourceChooser;

import java.net.URI;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2005
 * wrapper component around the dialog to publish the chooser methods 
 */
public class ResourceChooserImpl implements ResourceChooser {

    public ResourceChooserImpl(Vospace vos) {
        this.vos = vos;
    }
    protected final Vospace vos;
    public URI chooseResource(String title,boolean enableMySpace) {
        return ResourceChooserDialog.chooseResource(vos,title,enableMySpace);
    }

}


/* 
$Log: ResourceChooserImpl.java,v $
Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.1.2.1  2005/04/22 10:54:03  nw
start of a new module.
 
*/