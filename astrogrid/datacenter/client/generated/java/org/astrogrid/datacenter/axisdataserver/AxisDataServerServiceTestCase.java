/**
 * AxisDataServerServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.axisdataserver;

public class AxisDataServerServiceTestCase extends junit.framework.TestCase {         protected java.net.URL serviceURL;
    public AxisDataServerServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1AxisDataServerGetMetadata() throws Exception {
        org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub)
                          new org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator().getAxisDataServer(serviceURL);
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
        value = binding.getMetadata(new java.lang.String());
        // TBD - validate results
    }

    public void test2AxisDataServerDoQuery() throws Exception {
        org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub)
                          new org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator().getAxisDataServer(serviceURL);
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
        try {
            java.lang.String value = null;
            value = binding.doQuery(new java.lang.String(), org.astrogrid.datacenter.common.QueryHelper.buildMinimalQuery());
        }
        catch (java.io.IOException e1) {
            throw new junit.framework.AssertionFailedError("IOException Exception caught: " + e1);
        }
        catch (org.astrogrid.datacenter.query.QueryException e2) {
            throw new junit.framework.AssertionFailedError("QueryException Exception caught: " + e2);
        }
        catch (org.xml.sax.SAXException e3) {
            throw new junit.framework.AssertionFailedError("SAXException Exception caught: " + e3);
        }
            // TBD - validate results
    }

    public void test3AxisDataServerMakeQuery() throws Exception {
        org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub)
                          new org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator().getAxisDataServer(serviceURL);
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
        try {
            org.astrogrid.datacenter.axisdataserver.types.QueryId value = null;
            value = binding.makeQuery(org.astrogrid.datacenter.common.QueryHelper.buildMinimalQuery());
        }
        catch (java.io.IOException e1) {
            throw new junit.framework.AssertionFailedError("IOException Exception caught: " + e1);
        }
        catch (org.astrogrid.datacenter.query.QueryException e2) {
            throw new junit.framework.AssertionFailedError("QueryException Exception caught: " + e2);
        }
        catch (org.xml.sax.SAXException e3) {
            throw new junit.framework.AssertionFailedError("SAXException Exception caught: " + e3);
        }
            // TBD - validate results
    }

    public void test4AxisDataServerMakeQueryWithId() throws Exception {
        org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub)
                          new org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator().getAxisDataServer(serviceURL);
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
        try {
            org.astrogrid.datacenter.axisdataserver.types.QueryId value = null;
            value = binding.makeQueryWithId(org.astrogrid.datacenter.common.QueryHelper.buildMinimalQuery(), new java.lang.String());
        }
        catch (java.io.IOException e1) {
            throw new junit.framework.AssertionFailedError("IOException Exception caught: " + e1);
        }
        catch (org.astrogrid.datacenter.query.QueryException e2) {
            throw new junit.framework.AssertionFailedError("QueryException Exception caught: " + e2);
        }
        catch (org.xml.sax.SAXException e3) {
            throw new junit.framework.AssertionFailedError("SAXException Exception caught: " + e3);
        }
            // TBD - validate results
    }

    public void test5AxisDataServerSetResultsDestination() throws Exception {
        org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub)
                          new org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator().getAxisDataServer(serviceURL);
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
        binding.setResultsDestination(new org.astrogrid.datacenter.axisdataserver.types.QueryId(), new org.apache.axis.types.URI("urn:testing"));
        // TBD - validate results
    }

    public void test6AxisDataServerStartQuery() throws Exception {
        org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub)
                          new org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator().getAxisDataServer(serviceURL);
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
        binding.startQuery(new org.astrogrid.datacenter.axisdataserver.types.QueryId());
        // TBD - validate results
    }

    public void test7AxisDataServerGetResultsAndClose() throws Exception {
        org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub)
                          new org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator().getAxisDataServer(serviceURL);
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
        value = binding.getResultsAndClose(new org.astrogrid.datacenter.axisdataserver.types.QueryId());
        // TBD - validate results
    }

    public void test8AxisDataServerAbortQuery() throws Exception {
        org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub)
                          new org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator().getAxisDataServer(serviceURL);
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
        binding.abortQuery(new org.astrogrid.datacenter.axisdataserver.types.QueryId());
        // TBD - validate results
    }

    public void test9AxisDataServerGetStatus() throws Exception {
        org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub)
                          new org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator().getAxisDataServer(serviceURL);
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
        value = binding.getStatus(new org.astrogrid.datacenter.axisdataserver.types.QueryId());
        // TBD - validate results
    }

    public void test10AxisDataServerRegisterWebListener() throws Exception {
        org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub)
                          new org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator().getAxisDataServer(serviceURL);
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
        binding.registerWebListener(new org.astrogrid.datacenter.axisdataserver.types.QueryId(), new org.apache.axis.types.URI("urn:testing"));
        // TBD - validate results
    }

    public void test11AxisDataServerRegisterJobMonitor() throws Exception {
        org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub)
                          new org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator().getAxisDataServer(serviceURL);
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
        binding.registerJobMonitor(new org.astrogrid.datacenter.axisdataserver.types.QueryId(), new org.apache.axis.types.URI("urn:testing"));
        // TBD - validate results
    }

}
