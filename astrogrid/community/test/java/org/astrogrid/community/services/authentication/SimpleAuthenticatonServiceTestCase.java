/**
 * SimpleAuthenticatonServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.services.authentication;

public class SimpleAuthenticatonServiceTestCase extends junit.framework.TestCase {
    public SimpleAuthenticatonServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1SimpleAuthenticationAuthenticateLogin() throws Exception {
        org.astrogrid.community.services.authentication.SimpleAuthenticationSoapBindingStub binding;
        try {
            binding = (org.astrogrid.community.services.authentication.SimpleAuthenticationSoapBindingStub)
                          new org.astrogrid.community.services.authentication.SimpleAuthenticatonServiceLocator().getSimpleAuthentication();
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
        value = binding.authenticateLogin(new java.lang.String(), new java.lang.String());
        assertEquals("TestValue", value);
        // TBD - validate results
    }

    public void test2SimpleAuthenticationAuthenticateToken() throws Exception {
        org.astrogrid.community.services.authentication.SimpleAuthenticationSoapBindingStub binding;
        try {
            binding = (org.astrogrid.community.services.authentication.SimpleAuthenticationSoapBindingStub)
                          new org.astrogrid.community.services.authentication.SimpleAuthenticatonServiceLocator().getSimpleAuthentication();
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
        boolean value = false;
        value = binding.authenticateToken(new java.lang.String());
        // TBD - validate results
        assertEquals(true, value);
    }

}
