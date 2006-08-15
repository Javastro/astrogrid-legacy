/**
 * VSOiServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.vso;

public class VSOiServiceTestCase extends junit.framework.TestCase {
    public VSOiServiceTestCase(java.lang.String name) {
        super(name);
    }

    public void testsdacVSOiWSDL() throws Exception {
        javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory.newInstance();
        java.net.URL url = new java.net.URL(new org.astrogrid.stapsearch.ws.vso.VSOiServiceLocator().getsdacVSOiAddress() + "?WSDL");
        javax.xml.rpc.Service service = serviceFactory.createService(url, new org.astrogrid.stapsearch.ws.vso.VSOiServiceLocator().getServiceName());
        assertTrue(service != null);
    }

    public void test1sdacVSOiQuery() throws Exception {
        org.astrogrid.stapsearch.ws.vso.VSOiBindingStub binding;
        try {
            binding = (org.astrogrid.stapsearch.ws.vso.VSOiBindingStub)
                          new org.astrogrid.stapsearch.ws.vso.VSOiServiceLocator().getsdacVSOi();
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
        org.astrogrid.stapsearch.ws.vso.ProviderQueryResponse[] value = null;
        value = binding.query(new org.astrogrid.stapsearch.ws.vso.QueryRequest());
        // TBD - validate results
    }

    public void test2sdacVSOiGetData() throws Exception {
        org.astrogrid.stapsearch.ws.vso.VSOiBindingStub binding;
        try {
            binding = (org.astrogrid.stapsearch.ws.vso.VSOiBindingStub)
                          new org.astrogrid.stapsearch.ws.vso.VSOiServiceLocator().getsdacVSOi();
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
        org.astrogrid.stapsearch.ws.vso.ProviderGetDataResponse[] value = null;
        value = binding.getData(new org.astrogrid.stapsearch.ws.vso.VSOGetDataRequest());
        // TBD - validate results
    }

}
