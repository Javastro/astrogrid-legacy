/*$Id: NullMySpaceFactory.java,v 1.1 2003/08/21 12:27:24 nw Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.impl.empty;

import org.astrogrid.datacenter.config.ConfigurableImpl;
import org.astrogrid.datacenter.myspace.Allocation;
import org.astrogrid.datacenter.myspace.AllocationException;
import org.astrogrid.datacenter.myspace.MySpaceFactory;

/** Null implementation of a my space factory.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public class NullMySpaceFactory
    extends ConfigurableImpl
    implements MySpaceFactory {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.myspace.MySpaceFactory#allocateCacheSpace(java.lang.String)
     */
    public Allocation allocateCacheSpace(String jobURN)
        throws AllocationException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.myspace.MySpaceFactory#close(org.astrogrid.datacenter.myspace.Allocation)
     */
    public void close(Allocation allocation) throws AllocationException {

    }

}


/* 
$Log: NullMySpaceFactory.java,v $
Revision 1.1  2003/08/21 12:27:24  nw
added set of null-factory implementations.
 
*/