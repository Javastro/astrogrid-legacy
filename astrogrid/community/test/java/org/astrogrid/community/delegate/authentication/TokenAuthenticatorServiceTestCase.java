/**
 * N.B this test is made superfluous by AuthenticationDelegateTest
 * TokenAuthenticatorServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 *
 */

package org.astrogrid.community.delegate.authentication;

public class TokenAuthenticatorServiceTestCase extends junit.framework.TestCase {
    public TokenAuthenticatorServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1AuthenticationServiceAuthenticateLogin() throws Exception {
        org.astrogrid.community.delegate.authentication.AuthenticationServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.community.delegate.authentication.AuthenticationServiceSoapBindingStub)
                          new org.astrogrid.community.delegate.authentication.TokenAuthenticatorServiceLocator().getAuthenticationService();
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
        org.astrogrid.community.service.authentication.data.SecurityToken value = null;
        value = binding.authenticateLogin(new java.lang.String(), new java.lang.String());
        assertEquals("test@nowhere", value.getAccount());
        // TBD - validate results
    }

    public void test2AuthenticationServiceAuthenticateToken() throws Exception {
        org.astrogrid.community.delegate.authentication.AuthenticationServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.community.delegate.authentication.AuthenticationServiceSoapBindingStub)
                          new org.astrogrid.community.delegate.authentication.TokenAuthenticatorServiceLocator().getAuthenticationService();
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
        org.astrogrid.community.service.authentication.data.SecurityToken value = null;
        value = binding.authenticateToken(new org.astrogrid.community.service.authentication.data.SecurityToken());
        // TBD - validate results
    }

    public void test3AuthenticationServiceCreateToken() throws Exception {
        org.astrogrid.community.delegate.authentication.AuthenticationServiceSoapBindingStub binding;
        try {
            binding = (org.astrogrid.community.delegate.authentication.AuthenticationServiceSoapBindingStub)
                          new org.astrogrid.community.delegate.authentication.TokenAuthenticatorServiceLocator().getAuthenticationService();
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
        org.astrogrid.community.service.authentication.data.SecurityToken value = null;
        value = binding.createToken(new java.lang.String(), new org.astrogrid.community.service.authentication.data.SecurityToken(), new java.lang.String());
        // TBD - validate results
    }

}
