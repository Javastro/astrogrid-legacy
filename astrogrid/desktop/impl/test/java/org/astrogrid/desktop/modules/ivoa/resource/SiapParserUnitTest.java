/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.io.InputStream;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.collections.IteratorUtils;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SiapService;

/** test parsing of siap resources.
 * @todo implement parsing like for other classes of resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 20079:19:31 PM
 */
public class SiapParserUnitTest extends AbstractTestForParser {


	
	public void testSiapService() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("siap1.xml");
		assertNotNull(is);
		XMLStreamReader in = fac.createXMLStreamReader(is);
		ResourceStreamParser p = new ResourceStreamParser(in);
		Resource[] arr =(Resource[]) IteratorUtils.toArray(p,Resource.class);	
		assertTrue(arr[0] instanceof Service);
		Service s = (Service)arr[0];
		assertNotNull(s.getCapabilities());
		assertEquals(1,s.getCapabilities().length);
		Capability c = s.getCapabilities()[0];
		assertNotNull(c);
		assertTrue(arr[0] instanceof SiapService);
		assertTrue(c instanceof SiapCapability);
		assertSame(c,((SiapService)arr[0]).findSiapCapability());
		
		SiapCapability siapCap = (SiapCapability)c;
		assertNotNull(siapCap.getImageServiceType());
		assertEquals("Cutout",siapCap.getImageServiceType());
	}
	

}
