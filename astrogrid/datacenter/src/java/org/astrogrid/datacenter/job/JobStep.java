/*$Id: JobStep.java,v 1.6 2003/08/22 10:35:02 nw Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.job;

import org.astrogrid.datacenter.query.Query;

/**
 * interface describing a step of a job.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public interface JobStep {
    public abstract String getName();
    public abstract void setName(String name);
    public abstract Query getQuery();
    public abstract void setQuery(Query query);
    /** would this make more sense as an int ? */
    public abstract void setStepNumber(String stepNumber);
    public abstract String getStepNumber();
}

/* 
$Log: JobStep.java,v $
Revision 1.6  2003/08/22 10:35:02  nw
refactored job and job step into interface, abstract base class and implementation
 
*/