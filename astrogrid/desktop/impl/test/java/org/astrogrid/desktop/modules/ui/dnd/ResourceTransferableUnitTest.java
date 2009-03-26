/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import junit.framework.TestCase;

import org.astrogrid.acr.ivoa.resource.Resource;
import static org.easymock.EasyMock.*;

/** test behaviour of resource transfewrable.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20071:57:35 PM
 */
public class ResourceTransferableUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		u = new URI("ivo://wibble/pling");
		r = createNiceMock(Resource.class);
		expect(r.getId()).andStubReturn(u);
		replay(r);
		t = new ResourceTransferable(r);
	}
	URI u;
	Resource r;
	ResourceTransferable t ;

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		r = null;
		t= null;
	}
	
	public void testLocalResourceFlavor() throws Exception {
		assertTrue(t.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE));
		Object o = t.getTransferData(VoDataFlavour.LOCAL_RESOURCE);
		assertFalse(VoDataFlavour.LOCAL_RESOURCE.isFlavorSerializedObjectType());
		assertNotNull(o);
		assertSame(r,o);
	}
	
	public void testResourceFlavor() throws Exception {
		assertTrue(t.isDataFlavorSupported(VoDataFlavour.RESOURCE));
		Object o = t.getTransferData(VoDataFlavour.RESOURCE);
		assertTrue(VoDataFlavour.RESOURCE.isFlavorSerializedObjectType());
		assertNotNull(o);
		assertSame(r,o);
	}
	
	public void testLocalURIFlavor() throws Exception {
		assertTrue(t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI));
		Object o = t.getTransferData(VoDataFlavour.LOCAL_URI);
		assertNotNull(o);
		assertSame(u,o);
	}
	
	public void testURIListFlavor() throws Exception {
		assertTrue(t.isDataFlavorSupported(VoDataFlavour.URI_LIST));
		Object o = t.getTransferData(VoDataFlavour.URI_LIST);
		assertTrue(VoDataFlavour.URI_LIST.isFlavorTextType());
		assertTrue(VoDataFlavour.URI_LIST.isRepresentationClassInputStream());
		assertNotNull(o);
		assertTrue(o instanceof InputStream);
		BufferedReader br = new BufferedReader(new InputStreamReader((InputStream)o));
		assertEquals(u.toString(),br.readLine());
		assertNull(br.readLine());
	}
	
	
	public void testPlainFlavor() throws Exception {
		assertTrue(t.isDataFlavorSupported(VoDataFlavour.PLAIN));
		Object o = t.getTransferData(VoDataFlavour.PLAIN);
		assertTrue(VoDataFlavour.PLAIN.isFlavorTextType());
		assertTrue(VoDataFlavour.PLAIN.isRepresentationClassInputStream());
		assertNotNull(o);
		assertTrue(o instanceof InputStream);
		BufferedReader br = new BufferedReader(new InputStreamReader((InputStream)o));
		assertEquals(u.toString(),br.readLine());
		assertNull(br.readLine());
	}
	/*
	public void testXMLFlavor() throws Exception {
		assertTrue(t.isDataFlavorSupported(VoDataFlavour.XML));
		Object o = t.getTransferData(VoDataFlavour.XML);
		assertTrue(VoDataFlavour.XML.isFlavorTextType());
		assertTrue(VoDataFlavour.XML.isRepresentationClassInputStream());
		assertNotNull(o);
		assertTrue(o instanceof InputStream);
		BufferedReader br = new BufferedReader(new InputStreamReader((InputStream)o));
		String s= br.readLine();
		assertTrue(s.indexOf("<html>") != -1);
		assertTrue(s.indexOf(u.toString()) != -1);
	}
	
	public void testHtmlFlavor() throws Exception {
		assertTrue(t.isDataFlavorSupported(VoDataFlavour.HTML));
		Object o = t.getTransferData(VoDataFlavour.HTML);
		assertTrue(VoDataFlavour.HTML.isFlavorTextType());
		assertTrue(VoDataFlavour.HTML.isRepresentationClassInputStream());
		assertNotNull(o);
		assertTrue(o instanceof InputStream);		
		BufferedReader br = new BufferedReader(new InputStreamReader((InputStream)o));
		String s= br.readLine();
		assertTrue(s.indexOf("<html>") != -1);
		assertTrue(s.indexOf(u.toString()) != -1);
	}*/

}
