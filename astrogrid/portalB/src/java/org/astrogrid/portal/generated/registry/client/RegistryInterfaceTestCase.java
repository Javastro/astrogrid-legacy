/**
 * RegistryInterfaceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.portal.generated.registry.client;

public class RegistryInterfaceTestCase extends junit.framework.TestCase {
    public RegistryInterfaceTestCase(java.lang.String name) {
        super(name);
    }
    public void test1RegistryInterfacePortSubmitQuery() {
        org.astrogrid.portal.generated.registry.client.RegistryInterface_Port binding;
        try {
            binding = new org.astrogrid.portal.generated.registry.client.RegistryInterfaceLocator().getRegistryInterfacePort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertTrue("binding is null", binding != null);

        try {
            java.lang.String value = null;
            value = binding.submitQuery(new java.lang.String());
        }
        catch (java.rmi.RemoteException re) {
            throw new junit.framework.AssertionFailedError("Remote Exception caught: " + re);
        }
    }

}
