/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.lang.ArrayUtils;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.easymock.MockControl;

/** test for resource lists.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20073:23:21 PM
 */
public class ResourceListTransferableUnitTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
		u = new URI("ivo://wibble/pling");
		u1 = new URI("ivo://wiblle/plong");
		
		MockControl m = MockControl.createNiceControl(Resource.class);
		r = (Resource)m.getMock();
		r.getId();
		m.setDefaultReturnValue(u);
		m.replay();
		
		MockControl m1 = MockControl.createNiceControl(Resource.class);
		r1 = (Resource)m1.getMock();
		r1.getId();
		m1.setDefaultReturnValue(u1);
		m1.replay();
		
		l = new ArrayList();
		l.add(r);
		l.add(r1);
		t = new ResourceListTransferable(l);
	}
	URI u;
	URI u1;
	Resource r;
	Resource r1;
	List l;
	ResourceListTransferable t ;

	protected void tearDown() throws Exception {
		super.tearDown();
		u = null;
		u1 = null;
		l = null;
		r = null;
		r1 = null;
		t= null;
	}
	
	public void testLocalResourceListFlavor() throws Exception {
		assertTrue(t.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE_ARRAY));
		Object o = t.getTransferData(VoDataFlavour.LOCAL_RESOURCE_ARRAY);
		assertFalse(VoDataFlavour.LOCAL_RESOURCE_ARRAY.isFlavorSerializedObjectType());
		assertNotNull(o);
		assertTrue(o instanceof Resource[]);
		assertEquals(l,Arrays.asList((Resource[])o));
	}
	
	public void testResourceListFlavor() throws Exception {
		assertTrue(t.isDataFlavorSupported(VoDataFlavour.RESOURCE_ARRAY));
		Object o = t.getTransferData(VoDataFlavour.RESOURCE_ARRAY);
		assertTrue(VoDataFlavour.RESOURCE_ARRAY.isFlavorSerializedObjectType());
		assertNotNull(o);
		assertTrue(o instanceof Resource[]);
		assertEquals(l,Arrays.asList((Resource[])o));
	}
	
	public void testLocalURIFlavor() throws Exception {
		assertFalse("2 data flavours can't be the same",VoDataFlavour.LOCAL_RESOURCE_ARRAY.equals(VoDataFlavour.LOCAL_URI_ARRAY));
		assertTrue(t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI_ARRAY));
		Object o = t.getTransferData(VoDataFlavour.LOCAL_URI_ARRAY);
		assertNotNull(o);
		assertTrue(o instanceof URI[]);
		URI[] l1 = (URI[])o;
		assertEquals(l.size(),l1.length);
		assertNotSame(l,l1);
		assertEquals(u,l1[0]);
		assertEquals(u1,l1[1]);
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
		assertEquals(u1.toString(),br.readLine());		
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
		assertEquals(u1.toString(),br.readLine());	
		assertNull(br.readLine());
	}
}
