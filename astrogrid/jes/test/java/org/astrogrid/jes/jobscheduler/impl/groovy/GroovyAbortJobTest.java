/*$Id: GroovyAbortJobTest.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 13-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.jes.jobscheduler.impl.AbstractTestForAbort;
import org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl;

/** test abort functionality works in script scheduler.
 * @author Noel Winstanley nw@jb.man.ac.uk 13-May-2004
 *
 */
public class GroovyAbortJobTest extends AbstractTestForAbort {
    /** Construct a new ScriptedAbortJobTest
     * @param arg0
     */
    public GroovyAbortJobTest(String arg0) {
        super(arg0);
    }
    
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.AbstractTestForSchedulerImpl#createScheduler()
     */
    protected AbstractJobSchedulerImpl createScheduler() throws Exception {        
        return new GroovySchedulerImpl(fac,new GroovyTransformers(),dispatcher,new GroovyInterpreterFactory(new XStreamPickler())); 
    }

 
}


/* 
$Log: GroovyAbortJobTest.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.3  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.2  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.1  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:42  nw
first checkin of prototype scrpting workflow interpreter
 
*/