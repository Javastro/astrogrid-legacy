/*$Id: ScriptedSubmitNewJobNotifierFailsTest.java,v 1.1 2004/07/09 09:32:12 nw Exp $
 * Created on 13-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.scripting;

import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.FileJobFactoryImpl;
import org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl;
import org.astrogrid.jes.jobscheduler.impl.SubmitNewJobNotifierFailsTest;

import java.io.File;
import java.io.IOException;

/** test what happens when the notifier fails in the scripted scheduler
 * @author Noel Winstanley nw@jb.man.ac.uk 13-May-2004
 *
 */
public class ScriptedSubmitNewJobNotifierFailsTest extends SubmitNewJobNotifierFailsTest {
    /** Construct a new ScriptedSubmitNewJobNotifierFailsTeste
     * @param arg
     */
    public ScriptedSubmitNewJobNotifierFailsTest(String arg) {
        super(arg);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.AbstractTestForSchedulerImpl#createScheduler()
     */
    protected AbstractJobSchedulerImpl createScheduler() throws Exception {        
        return new ScriptedSchedulerImpl(fac,new DefaultTransformers(),dispatcher,new WorkflowInterpreterFactory(new DevelopmentJarPaths()));
    }

    /** for some reason, won't work with in-memory store - probably an aliasing problem.
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#createJobFactory()
     */
    protected AbstractJobFactoryImpl createJobFactory() {
        try {
        final File tmp = File.createTempFile("script-work",null);
        tmp.delete();
        tmp.mkdirs();
        tmp.deleteOnExit();
        FileJobFactoryImpl.BaseDirectory bd = new FileJobFactoryImpl.BaseDirectory() {

            public File getDir() {
                return tmp;
            }
        };
        
            return new FileJobFactoryImpl(bd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }    
}


/* 
$Log: ScriptedSubmitNewJobNotifierFailsTest.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:42  nw
first checkin of prototype scrpting workflow interpreter
 
*/