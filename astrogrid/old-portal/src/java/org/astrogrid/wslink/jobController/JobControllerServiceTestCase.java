/**
 * JobControllerServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.wslink.jobController;
import junit.framework.*;

public class JobControllerServiceTestCase extends junit.framework.TestCase {
    public JobControllerServiceTestCase(java.lang.String name) {
        super(name);
    }
    
	public static Test suite() {
		return new TestSuite(JobControllerServiceTestCase.class);
	}
    
    public void test1JobControllerServiceSubmitJob() throws Exception {
        org.astrogrid.wslink.jobController.JobControllerServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.jobController.JobControllerServiceSoapBindingStub)
                          new org.astrogrid.wslink.jobController.JobControllerServiceLocator().getJobControllerService();
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
        java.lang.String value = null;
        value = binding.submitJob(new java.lang.String());
		System.out.println("okay query done the value = " + value);
		assertNotNull("value of query is", value);

        // TBD - validate results
    }
    
	public static void main (String[] args) {		
		junit.textui.TestRunner.run(suite());
	}
    

}
