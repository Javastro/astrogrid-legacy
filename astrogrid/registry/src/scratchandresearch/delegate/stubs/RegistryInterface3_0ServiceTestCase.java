/**
 * RegistryInterface3_0ServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.uml03.axis.services.RegistryInterface3_0;

public class RegistryInterface3_0ServiceTestCase extends junit.framework.TestCase {
    public RegistryInterface3_0ServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1RegistryInterface3_0SubmitQuery() throws Exception {
        org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0SoapBindingStub binding;
        try {
            binding = (org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0SoapBindingStub)
                          new org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0ServiceLocator().getRegistryInterface3_0();
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
        value = binding.submitQuery(new java.lang.String());
        // TBD - validate results
    }

    public void test2RegistryInterface3_0FullNodeQuery() throws Exception {
        org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0SoapBindingStub binding;
        try {
            binding = (org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0SoapBindingStub)
                          new org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0ServiceLocator().getRegistryInterface3_0();
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
        value = binding.fullNodeQuery(new java.lang.String());
        // TBD - validate results
    }

}
