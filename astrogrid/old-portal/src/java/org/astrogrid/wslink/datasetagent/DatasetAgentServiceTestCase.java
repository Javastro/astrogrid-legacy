/**
 * DatasetAgentServiceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.wslink.datasetagent;

public class DatasetAgentServiceTestCase extends junit.framework.TestCase {
    public DatasetAgentServiceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1DatasetAgentRunQuery() throws Exception {
        org.astrogrid.wslink.datasetagent.DatasetAgentSoapBindingStub binding;
        try {
            binding = (org.astrogrid.wslink.datasetagent.DatasetAgentSoapBindingStub)
                          new org.astrogrid.wslink.datasetagent.DatasetAgentServiceLocator().getDatasetAgent();
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
        value = binding.runQuery(new java.lang.String());
        // TBD - validate results
    }

}
