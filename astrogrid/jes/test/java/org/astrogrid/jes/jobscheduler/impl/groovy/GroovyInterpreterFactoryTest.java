/*$Id: GroovyInterpreterFactoryTest.java,v 1.6 2005/07/27 15:35:08 clq2 Exp $
 * Created on 27-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.jes.util.TemporaryBuffer;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2004
 *
 */
public class GroovyInterpreterFactoryTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        fac = new GroovyInterpreterFactory(new XStreamPickler(),new TemporaryBuffer());
        workflow = new Workflow();
        workflow.setJobExecutionRecord(new JobExecutionRecord());
        workflow.getJobExecutionRecord().setJobId(new JobURN());
        interp = new GroovyInterpreter();
        Rule rule = new Rule();
        rule.setName("test rule");
        rule.setBody("fsdfs fgdgf\n gdgf");
        interp.ruleStore.add(rule);
    }
    protected GroovyInterpreterFactory fac;
    protected Workflow workflow;
    protected GroovyInterpreter interp;
    
    /** when round-tripping, with caching in effect, will get the same object back */
    public void testPickleRoundTrip() throws PickleException {
        fac.pickleTo(interp,workflow);
        GroovyInterpreter interp1 = fac.unpickleFrom(new JesInterface(workflow,new MockDispatcher(),null));
        assertSame(interp,interp1);
    }
    
    /** when round-tripping, without caching, will get a different but equal object back */
    public void testPickleRoundTripWithoutCache() throws PickleException {
        fac.pickleTo(interp,workflow);
        fac.interpreterCache.clear(); // force the cache to be cleared.
        GroovyInterpreter interp1 = fac.unpickleFrom(new JesInterface(workflow,new MockDispatcher(),null));
        assertNotNull(interp1.shell.jes);
        assertNotSame(interp,interp1);
        assertEquals(interp,interp1);
    }
    
    
    



    public void testNewInterpreter() throws PickleException {
        // setup up rulestre.
        String rulesDoc = ((XStreamPickler) fac.pickler).getXstream().toXML(new ArrayList(interp.ruleStore.values()));
        GroovyInterpreter interp1 = fac.newInterpreter(rulesDoc,new JesInterface(workflow,new MockDispatcher(),null));
        assertNotNull(interp1.shell.jes);
        assertNotSame(interp,interp1);
        assertEquals(interp,interp1);
    }

}


/* 
$Log: GroovyInterpreterFactoryTest.java,v $
Revision 1.6  2005/07/27 15:35:08  clq2
jes_nww_review_unit_tests

Revision 1.5.22.1  2005/07/19 15:38:06  nw
fixed unit tests -100% pass rate now.

Revision 1.5  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.4.56.1  2005/04/12 17:08:33  nw
caching to improve performance

Revision 1.4  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.3.18.1  2004/11/05 16:14:01  nw
updated to worrk with rulestore

Revision 1.3  2004/09/16 21:46:45  nw
made 3rd-party objects only persist for so many calls. - in case they're space leaking.

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.4  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.3  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.2  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.1  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.
 
*/