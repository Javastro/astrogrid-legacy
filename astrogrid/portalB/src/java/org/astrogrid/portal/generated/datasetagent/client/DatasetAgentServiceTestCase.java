/**
 * DatasetAgentServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.portal.generated.datasetagent.client;

public class DatasetAgentServiceTestCase extends junit.framework.TestCase {
    public DatasetAgentServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1DatasetAgentRunQuery() {
        org.astrogrid.portal.generated.datasetagent.client.DatasetAgent binding;
        try {
            binding = new org.astrogrid.portal.generated.datasetagent.client.DatasetAgentServiceLocator().getDatasetAgent();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            java.lang.String value = null;
            value = binding.runQuery(new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

}
