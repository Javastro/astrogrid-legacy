/**
 * AuthorizationServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.delegate.authorization;

public class AuthorizationServiceTestCase extends junit.framework.TestCase {
    public AuthorizationServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1AuthorizationCheckPermission() throws Exception {
        org.astrogrid.community.delegate.authorization.AuthorizationSoapBindingStub binding;
        try {
            binding = (org.astrogrid.community.delegate.authorization.AuthorizationSoapBindingStub)
                          new org.astrogrid.community.delegate.authorization.AuthorizationServiceLocator().getAuthorization();
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
        value = binding.checkPermission(new java.lang.String(), new java.lang.String(), new java.lang.String(), new java.lang.String(), new java.lang.String());
        // TBD - validate results
    }

}
