/**
 * RegistryInterfaceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.wslink.registry;
import java.util.*;
import junit.framework.*;


public class RegistryInterfaceTestCase extends junit.framework.TestCase {
    public RegistryInterfaceTestCase(java.lang.String name) {
        super(name);
    }

	public static Test suite() {
		return new TestSuite(RegistryInterfaceTestCase.class);
	}

    public void test1RegistryInterfacePortSubmitQuery() throws Exception {
        org.astrogrid.wslink.registry.RegistryInterface_BindingStub binding;
        try {
            binding = (org.astrogrid.wslink.registry.RegistryInterface_BindingStub)
                          new org.astrogrid.wslink.registry.RegistryInterfaceLocator().getRegistryInterfacePort();
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
        value = binding.submitQuery(new java.lang.String());
        //System.out.println("okay query done the value = " + value);
		assertNotNull("value of query is", value);
        // TBD - validate results
    }

	public static void main (String[] args) {		
		junit.textui.TestRunner.run(suite());
	}

}
