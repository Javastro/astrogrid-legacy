/**
 * RegistryInterfaceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.portal.registry;

public class RegistryTest {
	public static void main(String[] args) throws Exception {
		org.astrogrid.portal.generated.registry.client.RegistryInterface_BindingStub binding = null;
		try {
			binding = (org.astrogrid.portal.generated.registry.client.RegistryInterface_BindingStub)
				new org.astrogrid.portal.generated.registry.client.RegistryInterfaceLocator().getRegistryInterfacePort();
		} catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
			System.err.println("exception: " + jre);
		}

		// Time out after a minute
		binding.setTimeout(60000);

		// Test operation
		java.lang.String value = null;
		value = binding.submitQuery(new java.lang.String());

		System.out.println("value is " + value);
		// TBD - validate results
	}

}
