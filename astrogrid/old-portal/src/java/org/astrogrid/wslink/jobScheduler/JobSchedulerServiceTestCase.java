/**
 * JobSchedulerServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.wslink.jobScheduler;
import junit.framework.*;

public class JobSchedulerServiceTestCase extends junit.framework.TestCase {
    public JobSchedulerServiceTestCase(java.lang.String name) {
        super(name);
    }
    
	public static void main (String[] args) {		
		junit.textui.TestRunner.run(suite());
	}    

	public static Test suite() {
		return new TestSuite(JobSchedulerServiceTestCase.class);
	}        
    
    public void test1JobSchedulerServiceScheduleJob() throws Exception {
        org.astrogrid.wslink.jobScheduler.JobSchedulerServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.jobScheduler.JobSchedulerServiceSoapBindingStub)
                          new org.astrogrid.wslink.jobScheduler.JobSchedulerServiceLocator().getJobSchedulerService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);

        // Time out after a minute
        binding.setTimeout(60000);

        // Test operation
        binding.scheduleJob(new java.lang.String());
        // TBD - validate results
    }

}
