/**
 * MySpaceManagerServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.generated.myspace.client;
import junit.framework.*;

public class MySpaceManagerServiceTestCase extends junit.framework.TestCase {
    public MySpaceManagerServiceTestCase(java.lang.String name) {
        super(name);
    }
    
	public static void main (String[] args) {		
		junit.textui.TestRunner.run(suite());
	}    

	public static Test suite() {
		return new TestSuite(MySpaceManagerServiceTestCase.class);
	}      
    
    public void test1MySpaceManagerUpLoad() throws Exception {
        org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.wslink.myspace.MySpaceManagerServiceLocator().getMySpaceManager();
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
        value = binding.upLoad(new java.lang.String());
		System.out.println("okay query done the value = " + value);
		assertNotNull("value of query is", value);
        
        // TBD - validate results
    }

    public void test2MySpaceManagerLookupDataHolderDetails() throws Exception {
        org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.wslink.myspace.MySpaceManagerServiceLocator().getMySpaceManager();
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
        value = binding.lookupDataHolderDetails(new java.lang.String());
		System.out.println("okay query done the value = " + value);
		assertNotNull("value of query is", value);
        
        // TBD - validate results
    }

    public void test3MySpaceManagerLookupDataHoldersDetails() throws Exception {
        org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.wslink.myspace.MySpaceManagerServiceLocator().getMySpaceManager();
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
        value = binding.lookupDataHoldersDetails(new java.lang.String());
		System.out.println("okay query done the value = " + value);
		assertNotNull("value of query is", value);
        
        // TBD - validate results
    }

    public void test4MySpaceManagerCopyDataHolder() throws Exception {
        org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.wslink.myspace.MySpaceManagerServiceLocator().getMySpaceManager();
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
        value = binding.copyDataHolder(new java.lang.String());
		System.out.println("okay query done the value = " + value);
		assertNotNull("value of query is", value);
        
        // TBD - validate results
    }

    public void test5MySpaceManagerMoveDataHolder() throws Exception {
        org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.wslink.myspace.MySpaceManagerServiceLocator().getMySpaceManager();
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
        value = binding.moveDataHolder(new java.lang.String());
		System.out.println("okay query done the value = " + value);
		assertNotNull("value of query is", value);
        
        // TBD - validate results
    }

    public void test6MySpaceManagerExportDataHolder() throws Exception {
        org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.wslink.myspace.MySpaceManagerServiceLocator().getMySpaceManager();
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
        value = binding.exportDataHolder(new java.lang.String());
		System.out.println("okay query done the value = " + value);
		assertNotNull("value of query is", value);
        
        // TBD - validate results
    }

    public void test7MySpaceManagerCreateContainer() throws Exception {
        org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.wslink.myspace.MySpaceManagerServiceLocator().getMySpaceManager();
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
        value = binding.createContainer(new java.lang.String());
		System.out.println("okay query done the value = " + value);
		assertNotNull("value of query is", value);
        
        // TBD - validate results
    }

    public void test8MySpaceManagerDeleteDataHolder() throws Exception {
        org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.myspace.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.wslink.myspace.MySpaceManagerServiceLocator().getMySpaceManager();
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
        value = binding.deleteDataHolder(new java.lang.String());
		System.out.println("okay query done the value = " + value);
		assertNotNull("value of query is", value);
        
        // TBD - validate results
    }

}
