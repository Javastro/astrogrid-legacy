/*$Id: AbstractJobStep.java,v 1.1 2003/08/22 10:35:02 nw Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.impl.abstr;

import org.astrogrid.datacenter.job.JobStep;
import org.astrogrid.datacenter.query.Query;

/** abstract implementation of the job step interface - provides the basic getter / setter functionality.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public abstract  class AbstractJobStep  implements JobStep {

    protected String name ="";

    protected String stepNumber = "";

    protected Query query = null;

    public String getName() { return this.name ; }

    public void setName(String name) { this.name = name ; }

    public Query getQuery() { return this.query ; }

    public void setQuery(Query query) { this.query = query ; }

    public void setStepNumber(String stepNumber) { this.stepNumber = stepNumber ; }

    public String getStepNumber() { return stepNumber; }

}


/* 
$Log: AbstractJobStep.java,v $
Revision 1.1  2003/08/22 10:35:02  nw
refactored job and job step into interface, abstract base class and implementation
 
*/