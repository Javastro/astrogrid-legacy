/**
 * RegistryInterfaceTestCase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.portal.registry;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

public class RegistryTest extends DefaultHandler {
	public void characters(char[] buf, int start, int length) {
		System.err.println("got some characters");
	}

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

		String s = null;
		s = binding.submitQuery(new String());

		System.out.println("value is " + s);

		StringReader sr = new StringReader(s);
		InputSource is = new InputSource(sr);

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		sp.parse(is, new RegistryTest());
	}
}
