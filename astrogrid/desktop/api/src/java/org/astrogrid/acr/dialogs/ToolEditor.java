/*$Id: ToolEditor.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 16-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.dialogs;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-May-2005
 *
 */
public interface ToolEditor {
    Document   edit(Document t) throws InvalidArgumentException;

}


/* 
$Log: ToolEditor.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:33  nw
first release of application launcher
 
*/