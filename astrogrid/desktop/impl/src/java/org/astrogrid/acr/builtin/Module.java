/*$Id: Module.java,v 1.2 2005/09/02 14:03:34 nw Exp $
 * Created on 09-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.builtin;

import org.astrogrid.acr.ACRException;

/** interface to a module - a collection of components
 * @todo move this into the framework package
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Aug-2005
 *
 */
public interface Module {

    /**
     * @param name
     * @return
     * @throws ACRException
     */
    Object getComponent(String name) throws ACRException;

}


/* 
$Log: Module.java,v $
Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:01  nw
finished split
 
*/