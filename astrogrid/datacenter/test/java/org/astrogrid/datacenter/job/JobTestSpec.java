/*$Id: JobTestSpec.java,v 1.2 2003/08/28 16:12:29 mch Exp $
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
import java.util.Date;
import junit.framework.TestCase;
import org.astrogrid.datacenter.impl.abstr.AbstractJobStep;
import org.astrogrid.datacenter.query.QueryException;

/** test specification for the Job interface.
 * implementors of the job interface should extend this class in their corresponding unit test.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public abstract class JobTestSpec extends TestCase {

    /**
     * Constructor for JobTest.
     * @param arg0
     */
    public JobTestSpec(String arg0) {
        super(arg0);
    }
    protected Job job = createJob();
    /** implementors to probide definition of this */
    protected abstract Job createJob();
    /** used to create a JobStep object for testing with - can be overridden if needed */
    protected JobStep createJobStep() {
        return new AbstractJobStep() {;};
    }
    /** set to result of createJob. final
    protected final Configurable createConfigurable() {
        return createJob();
    }
     */

    public void testCheckNull() {
        assertNotNull(job);
    }

     /** no true testing here -- can only check the method doesn't barf an exception */
    public void testInformJobMonitor() {
        job.informJobMonitor();
    }

    public void testId() {
        String key1 = job.getId();
        assertNotNull(key1);
        //assertTrue(key1.length() > 0);
        // maybe a bit too strong -- leave it up to the implementor to check properties of keys
    }


    public void testName() {
        String name = job.getName();
        assertNotNull(name);
        job.setName("foo");
        String name1 = job.getName();
        assertNotNull(name1);
        assertEquals(name1,"foo");
    }

    public void testDate() {
        Date d = job.getDate();
        assertNotNull(d);
        Date newDate  = new Date();
        job.setDate(newDate);
        Date d1 = job.getDate();
        assertNotNull(d1);
        assertEquals(d1,newDate);
    }

    public void testCommunity() {
        String comm = job.getCommunity();
        assertNotNull(comm);
        job.setCommunity("foo");
        String comm1 = job.getCommunity();
        assertNotNull(comm1);
        assertEquals(comm1,"foo");
    }


    public void testUserId() {
        String uid = job.getUserId();
        assertNotNull(uid);
        job.setUserId("foo");
        String uid1 = job.getUserId();
        assertNotNull(uid1);
        assertEquals(uid1,"foo");
    }

    public void testJobMonitorURL() {
        String url = job.getJobMonitorURL();
        assertNotNull(url);
        // should we check for a valid url here too?
        job.setJobMonitorURL("http://www.slashdot.org");
        String url1 = job.getJobMonitorURL();
        assertNotNull(url1);
        assertEquals(url1,"http://www.slashdot.org");

    }

    public void testJobStep() throws QueryException {
        JobStep js1 = createJobStep();
        job.setJobStep(js1);
        JobStep js2= job.getJobStep();
        assertNotNull(js2);
        assertEquals(js1,js2);
    }

    public void testComment() {
        String comment = job.getComment();
        assertNotNull(comment);
        job.setComment("foo");
        String c1 = job.getComment();
        assertNotNull(c1);
        assertEquals(c1,"foo");
    }


}


/*
$Log: JobTestSpec.java,v $
Revision 1.2  2003/08/28 16:12:29  mch
New Configuration package

Revision 1.1  2003/08/22 10:37:48  nw
added test hierarchy for Job / JobStep

*/
