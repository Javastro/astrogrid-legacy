/**
 * MySpaceManagerServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.portal.generated.myspace.client;

public class MySpaceManagerServiceTestCase extends junit.framework.TestCase {
    public MySpaceManagerServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1MySpaceManagerUpLoad() {
        org.astrogrid.portal.generated.myspace.client.MySpaceManager binding;
        try {
            binding = new org.astrogrid.portal.generated.myspace.client.MySpaceManagerServiceLocator().getMySpaceManager();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            java.lang.String value = null;
            value = binding.upLoad(new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test2MySpaceManagerLookupDataHolderDetails() {
        org.astrogrid.portal.generated.myspace.client.MySpaceManager binding;
        try {
            binding = new org.astrogrid.portal.generated.myspace.client.MySpaceManagerServiceLocator().getMySpaceManager();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            java.lang.String value = null;
            value = binding.lookupDataHolderDetails(new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test3MySpaceManagerLookupDataHoldersDetails() {
        org.astrogrid.portal.generated.myspace.client.MySpaceManager binding;
        try {
            binding = new org.astrogrid.portal.generated.myspace.client.MySpaceManagerServiceLocator().getMySpaceManager();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            java.lang.String value = null;
            value = binding.lookupDataHoldersDetails(new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test4MySpaceManagerCopyDataHolder() {
        org.astrogrid.portal.generated.myspace.client.MySpaceManager binding;
        try {
            binding = new org.astrogrid.portal.generated.myspace.client.MySpaceManagerServiceLocator().getMySpaceManager();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            java.lang.String value = null;
            value = binding.copyDataHolder(new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test5MySpaceManagerMoveDataHolder() {
        org.astrogrid.portal.generated.myspace.client.MySpaceManager binding;
        try {
            binding = new org.astrogrid.portal.generated.myspace.client.MySpaceManagerServiceLocator().getMySpaceManager();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            java.lang.String value = null;
            value = binding.moveDataHolder(new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test6MySpaceManagerExportDataHolder() {
        org.astrogrid.portal.generated.myspace.client.MySpaceManager binding;
        try {
            binding = new org.astrogrid.portal.generated.myspace.client.MySpaceManagerServiceLocator().getMySpaceManager();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            java.lang.String value = null;
            value = binding.exportDataHolder(new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test7MySpaceManagerCreateContainer() {
        org.astrogrid.portal.generated.myspace.client.MySpaceManager binding;
        try {
            binding = new org.astrogrid.portal.generated.myspace.client.MySpaceManagerServiceLocator().getMySpaceManager();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            java.lang.String value = null;
            value = binding.createContainer(new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

    public void test8MySpaceManagerDeleteDataHolder() {
        org.astrogrid.portal.generated.myspace.client.MySpaceManager binding;
        try {
            binding = new org.astrogrid.portal.generated.myspace.client.MySpaceManagerServiceLocator().getMySpaceManager();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            java.lang.String value = null;
            value = binding.deleteDataHolder(new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

}
