/*$Id: NullJobFactory.java,v 1.2 2003/08/28 15:52:00 mch Exp $
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

import org.astrogrid.datacenter.FactoryProvider;
import org.astrogrid.datacenter.job.Job;
import org.astrogrid.datacenter.job.JobException;
import org.astrogrid.datacenter.job.JobFactory;
import org.w3c.dom.Document;

/** null implementation of the job factory - useful as a base class? and also used for testing.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public class NullJobFactory implements JobFactory {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.job.JobFactory#create(org.w3c.dom.Document, org.astrogrid.datacenter.FactoryProvider)
     */
    public Job create(Document jobDoc, FactoryProvider facMan)
        throws JobException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.job.JobFactory#update(org.astrogrid.datacenter.job.Job)
     */
    public void update(Job job) throws JobException {

    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.job.JobFactory#find(java.lang.String)
     */
    public Job find(String jobURN) throws JobException {
        return null;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.job.JobFactory#delete(org.astrogrid.datacenter.job.Job)
     */
    public String delete(Job job) throws JobException {
        return null;
    }

}


/*
$Log: NullJobFactory.java,v $
Revision 1.2  2003/08/28 15:52:00  mch
New Configuration package

Revision 1.1  2003/08/21 12:27:24  nw
added set of null-factory implementations.

*/
