/*$Id: InMemoryIdGen.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 27-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.idgen;

import org.astrogrid.component.descriptor.ComponentDescriptor;

import junit.framework.Test;

/** simplest possible memory-only id generator.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-May-2004
 *
 */
public class InMemoryIdGen implements IdGen, ComponentDescriptor {
    /** Construct a new InMemoryIdGen
     * 
     */
    public InMemoryIdGen() {
        super();
    }
    private int i = 0;
    /**
     * @see org.astrogrid.applications.manager.persist.IdGen#getNewID()
     */
    public synchronized String getNewID() {
        return Integer.toString(++i);
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "In Memory ID Gen";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Simplest possible IDGen. not for use in production environments";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: InMemoryIdGen.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/