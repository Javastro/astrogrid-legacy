/**
 * CDASWSTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.solarsearch.ws.cdaw;

public class CDASWSTestCase extends junit.framework.TestCase {
    public CDASWSTestCase(java.lang.String name) {
        super(name);
    }

    public void testCoordinatedDataAnalysisSystemPortWSDL() throws Exception {
        javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory.newInstance();
        java.net.URL url = new java.net.URL(new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPortAddress() + "?WSDL");
        javax.xml.rpc.Service service = serviceFactory.createService(url, new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getServiceName());
        assertTrue(service != null);
    }

    public void test1CoordinatedDataAnalysisSystemPortGetAllInstrumentTypes() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        value = binding.getAllInstrumentTypes();
        // TBD - validate results
    }

    public void test2CoordinatedDataAnalysisSystemPortGetAllInstruments() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        java.lang.String[][] value = null;
        value = binding.getAllInstruments();
        // TBD - validate results
    }

    public void test3CoordinatedDataAnalysisSystemPortGetAllMissionGroups() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        value = binding.getAllMissionGroups();
        // TBD - validate results
    }

    public void test4CoordinatedDataAnalysisSystemPortGetAllViewDescriptions() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        org.astrogrid.solarsearch.ws.cdaw.ViewDescription[] value = null;
        value = binding.getAllViewDescriptions();
        // TBD - validate results
    }

    public void test5CoordinatedDataAnalysisSystemPortGetCdfmlDataUrls() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        value = binding.getCdfmlDataUrls(new java.lang.String(), java.util.Calendar.getInstance(), java.util.Calendar.getInstance(), new java.lang.String[0], 0);
        // TBD - validate results
    }

    public void test6CoordinatedDataAnalysisSystemPortGetCdfmlDataUrls2() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        value = binding.getCdfmlDataUrls2(new java.lang.String(), java.util.Calendar.getInstance(), java.util.Calendar.getInstance(), 0);
        // TBD - validate results
    }

    public void test7CoordinatedDataAnalysisSystemPortGetDataAsText() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        org.astrogrid.solarsearch.ws.cdaw.ResultDescription value = null;
        value = binding.getDataAsText(new java.lang.String(), java.util.Calendar.getInstance(), java.util.Calendar.getInstance(), new java.lang.String[0]);
        // TBD - validate results
    }

    public void test8CoordinatedDataAnalysisSystemPortGetDataGraph() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        org.astrogrid.solarsearch.ws.cdaw.ResultDescription value = null;
        value = binding.getDataGraph(new java.lang.String(), java.util.Calendar.getInstance(), java.util.Calendar.getInstance(), new java.lang.String[0]);
        // TBD - validate results
    }

    public void test9CoordinatedDataAnalysisSystemPortGetDataGraph2() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        org.astrogrid.solarsearch.ws.cdaw.ResultDescription value = null;
        value = binding.getDataGraph2(new java.lang.String(), java.util.Calendar.getInstance(), java.util.Calendar.getInstance(), new java.lang.String[0], 0);
        // TBD - validate results
    }

    public void test10CoordinatedDataAnalysisSystemPortGetDataUrls() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        value = binding.getDataUrls(new java.lang.String(), java.util.Calendar.getInstance(), java.util.Calendar.getInstance());
        // TBD - validate results
    }

    public void test11CoordinatedDataAnalysisSystemPortGetDataUrls2() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        value = binding.getDataUrls2(new java.lang.String(), java.util.Calendar.getInstance(), java.util.Calendar.getInstance(), new java.lang.String[0]);
        // TBD - validate results
    }

    public void test12CoordinatedDataAnalysisSystemPortGetDatasetVariables() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        java.lang.String[][] value = null;
        value = binding.getDatasetVariables(new java.lang.String());
        // TBD - validate results
    }

    public void test13CoordinatedDataAnalysisSystemPortGetDatasets() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        org.astrogrid.solarsearch.ws.cdaw.DatasetDescription[] value = null;
        value = binding.getDatasets(new java.lang.String[0], new java.lang.String[0]);
        // TBD - validate results
    }

    public void test14CoordinatedDataAnalysisSystemPortGetDatasetsByInstrument() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        org.astrogrid.solarsearch.ws.cdaw.DatasetDescription[] value = null;
        value = binding.getDatasetsByInstrument(new java.lang.String[0], new java.lang.String[0]);
        // TBD - validate results
    }

    public void test15CoordinatedDataAnalysisSystemPortGetDatasetsBySource() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        org.astrogrid.solarsearch.ws.cdaw.DatasetDescription[] value = null;
        value = binding.getDatasetsBySource(new java.lang.String[0], new java.lang.String[0]);
        // TBD - validate results
    }

    public void test16CoordinatedDataAnalysisSystemPortGetInstrumentTypes() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        value = binding.getInstrumentTypes(new java.lang.String[0]);
        // TBD - validate results
    }

    public void test17CoordinatedDataAnalysisSystemPortGetInstruments() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        java.lang.String[][] value = null;
        value = binding.getInstruments(new java.lang.String[0]);
        // TBD - validate results
    }

    public void test18CoordinatedDataAnalysisSystemPortGetSources() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        java.lang.String[][] value = null;
        value = binding.getSources(new java.lang.String[0]);
        // TBD - validate results
    }

    public void test19CoordinatedDataAnalysisSystemPortGetSourcesByInstrument() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        java.lang.String[][] value = null;
        value = binding.getSourcesByInstrument(new java.lang.String[0]);
        // TBD - validate results
    }

    public void test20CoordinatedDataAnalysisSystemPortGetThumbnailExpansion() throws Exception {
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
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
        org.astrogrid.solarsearch.ws.cdaw.ResultDescription value = null;
        value = binding.getThumbnailExpansion(new org.astrogrid.solarsearch.ws.cdaw.ThumbnailDescription(), 0);
        // TBD - validate results
    }

}
