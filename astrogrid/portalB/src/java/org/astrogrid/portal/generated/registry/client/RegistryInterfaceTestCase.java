/**
 * RegistryInterfaceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.portal.generated.registry.client;

public class RegistryInterfaceTestCase extends junit.framework.TestCase {
    public RegistryInterfaceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1RegistryInterfacePortSubmitQuery() throws Exception {
        org.astrogrid.portal.generated.registry.client.RegistryInterface_BindingStub binding;
        try {
            binding = (org.astrogrid.portal.generated.registry.client.RegistryInterface_BindingStub)
                          new org.astrogrid.portal.generated.registry.client.RegistryInterfaceLocator().getRegistryInterfacePort();
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

}
