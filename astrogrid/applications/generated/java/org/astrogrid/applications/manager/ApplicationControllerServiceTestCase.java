/**
 * ApplicationControllerServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.manager;

public class ApplicationControllerServiceTestCase extends junit.framework.TestCase {
    public ApplicationControllerServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1ApplicationContrllerServiceListApplications() throws Exception {
        org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub)
                          new org.astrogrid.applications.manager.ApplicationControllerServiceLocator().getApplicationContrllerService();
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
        java.lang.String[] value = null;
        value = binding.listApplications();
        // TBD - validate results
    }

    public void test2ApplicationContrllerServiceGetApplicationDescription() throws Exception {
        org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub)
                          new org.astrogrid.applications.manager.ApplicationControllerServiceLocator().getApplicationContrllerService();
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
        org.astrogrid.applications.ApplicationDescription value = null;
        value = binding.getApplicationDescription(new java.lang.String());
        // TBD - validate results
    }

    public void test3ApplicationContrllerServiceInitializeApplication() throws Exception {
        org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub)
                          new org.astrogrid.applications.manager.ApplicationControllerServiceLocator().getApplicationContrllerService();
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
        int value = -3;
        value = binding.initializeApplication(new java.lang.String(), 0, new org.astrogrid.applications.Parameter[0]);
        // TBD - validate results
    }

    public void test4ApplicationContrllerServiceExecuteApplication() throws Exception {
        org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub)
                          new org.astrogrid.applications.manager.ApplicationControllerServiceLocator().getApplicationContrllerService();
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
        binding.executeApplication();
        // TBD - validate results
    }

    public void test5ApplicationContrllerServiceQueryApplicationStatus() throws Exception {
        org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub)
                          new org.astrogrid.applications.manager.ApplicationControllerServiceLocator().getApplicationContrllerService();
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
        value = binding.queryApplicationStatus();
        // TBD - validate results
    }

}
