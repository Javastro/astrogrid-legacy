/*$Id: ConstantToolLocator.java,v 1.1 2004/03/03 01:13:42 nw Exp $
 * Created on 27-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.locator;

import org.astrogrid.jes.JesException;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.jobscheduler.Locator;

import java.net.URL;

/** Constant tool locator - always returns same endpoint.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Feb-2004
 *
 */
public class ConstantToolLocator implements Locator {
    /** Construct a new ConstantToolLocator
     * 
     */
    public ConstantToolLocator(URL endpoint,String interfaceName) {
        this.endpoint = endpoint;
        this.interfaceName = interfaceName;
    }
    
    protected final URL endpoint;
    protected final String interfaceName;
    /**
     * @see org.astrogrid.jes.jobscheduler.Locator#locateTool(org.astrogrid.jes.job.JobStep)
     */
    public String locateTool(JobStep js) throws JesException {
        return endpoint.toString();
        
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.Locator#getToolInterface(org.astrogrid.jes.job.JobStep)
     */
    public String getToolInterface(JobStep js) throws JesException {
        return interfaceName;
    }
}


/* 
$Log: ConstantToolLocator.java,v $
Revision 1.1  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model
 
*/