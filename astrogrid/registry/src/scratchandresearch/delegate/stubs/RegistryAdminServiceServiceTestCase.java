/**
 * RegistryAdminServiceServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.uml03.axis.services.RegistryAdminService;

public class RegistryAdminServiceServiceTestCase extends junit.framework.TestCase {
    public RegistryAdminServiceServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1RegistryAdminServiceAdminQuery() throws Exception {
        org.astrogrid.uml03.axis.services.RegistryAdminService.RegistryAdminServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.uml03.axis.services.RegistryAdminService.RegistryAdminServiceSoapBindingStub)
                          new org.astrogrid.uml03.axis.services.RegistryAdminService.RegistryAdminServiceServiceLocator().getRegistryAdminService();
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
        value = binding.adminQuery(new java.lang.String());
        // TBD - validate results
    }

}
