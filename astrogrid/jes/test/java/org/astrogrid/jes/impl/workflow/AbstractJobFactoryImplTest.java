/*$Id: AbstractJobFactoryImplTest.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import org.astrogrid.jes.AbstractTestWorkflowInputs;
import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.SubmitJobRequest;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.InputStream;

/** Tests featires of abstract job factory impl - in particular {@lnk AbstractjobFactoryImpl#buildJob}
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 *
 */
public class AbstractJobFactoryImplTest extends AbstractTestWorkflowInputs {
    /**
     * Constructor for AbstractJobFactoryImplTest.
     * @param arg0
     */
    public AbstractJobFactoryImplTest(String arg0) {
        super(arg0);
        
    }
    
    protected void setUp() {
        fac = new MockJobFactoryImpl(); 
    }
    protected AbstractJobFactoryImpl fac;

    protected  void testIt(InputStream is,int resourceNum) throws MarshalException, ValidationException, JobException {
        SubmitJobRequest req = new SubmitJobRequestImpl(is);
        assertNotNull(req);
        Job job = fac.buildJob(req);
        assertNotNull(job);
        assertNotNull(job.getId());
        assertNotNull(job.getName());        
    }
}


/* 
$Log: AbstractJobFactoryImplTest.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.3  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.2  2004/02/17 12:57:11  nw
improved documentation

Revision 1.1.2.1  2004/02/12 15:42:16  nw
started testing castor code.
 
*/