/**
 * JobControllerServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.wslink.jobController;

public class JobControllerServiceTestCase extends junit.framework.TestCase {
    public JobControllerServiceTestCase(java.lang.String name) {
        super(name);
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
        // TBD - validate results
    }

}
