/**
 * JobControllerServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.portal.generated.jobcontroller.client;

public class JobControllerServiceTestCase extends junit.framework.TestCase {
    public JobControllerServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1JobControllerServiceSubmitJob() {
        org.astrogrid.portal.generated.jobcontroller.client.JobController binding;
        try {
            binding = new org.astrogrid.portal.generated.jobcontroller.client.JobControllerServiceLocator().getJobControllerService();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            java.lang.String value = null;
            value = binding.submitJob(new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

}
