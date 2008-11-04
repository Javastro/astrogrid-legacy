/*$Id: Module.java,v 1.6 2008/11/04 14:35:52 nw Exp $
 * Created on 09-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ModuleDescriptor;


/** Description of an AR module - a collection of components
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 09-Aug-2005
 *
 */
public interface Module  {


    Object getComponent(String name) throws ACRException;

    Object getComponent(Class iface) throws ACRException;
    
    public ModuleDescriptor getDescriptor() ;
    
}


/* 
$Log: Module.java,v $
Revision 1.6  2008/11/04 14:35:52  nw
javadoc polishing

Revision 1.5  2007/06/18 16:20:04  nw
javadoc fixes.

Revision 1.4  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.3  2006/06/02 00:16:15  nw
Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.

Revision 1.2  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.1.2.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.2.60.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:01  nw
finished split
 
*/