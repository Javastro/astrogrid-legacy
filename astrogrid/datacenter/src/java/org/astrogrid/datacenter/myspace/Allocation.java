/*$Id: Allocation.java,v 1.10 2003/08/22 15:49:48 nw Exp $
 * Created on 22-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.myspace;

import java.io.OutputStream;

import org.astrogrid.datacenter.job.Job;

/** represnents some temporary storage.
 * can be written to via the output stream. {@link #close} closes this stream,
 * {@link #informMySpace} tells MySpace to come and collect the data written to the stream.
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Aug-2003
 *
 */
public interface Allocation {
    public abstract OutputStream getOutputStream();
    public abstract void close() ;
   public abstract void informMySpace(Job job) throws AllocationException;

    }
 /* 
$Log: Allocation.java,v $
Revision 1.10  2003/08/22 15:49:48  nw
refactored MySpace module - replaced Allocation class by
an interface and implementaiton class, calling to a helper class.
simplified interface and implementation of MySpaceFactory.
 
 */