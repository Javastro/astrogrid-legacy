/**
 * JobMonitorServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.wslink.jobMonitor;
import junit.framework.*;

public class JobMonitorServiceTestCase extends junit.framework.TestCase {
    public JobMonitorServiceTestCase(java.lang.String name) {
        super(name);
    }
    
	public static void main (String[] args) {		
		junit.textui.TestRunner.run(suite());
	}    

	public static Test suite() {
		return new TestSuite(JobMonitorServiceTestCase.class);
	}    
    
    public void test1JobMonitorServiceMonitorJob() throws Exception {
        org.astrogrid.wslink.jobMonitor.JobMonitorServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.jobMonitor.JobMonitorServiceSoapBindingStub)
                          new org.astrogrid.wslink.jobMonitor.JobMonitorServiceLocator().getJobMonitorService();
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
        binding.monitorJob(new java.lang.String());
        // TBD - validate results
    }
}
