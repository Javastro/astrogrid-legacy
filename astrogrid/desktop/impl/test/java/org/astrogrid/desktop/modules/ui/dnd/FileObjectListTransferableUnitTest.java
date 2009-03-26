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

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import static org.easymock.EasyMock.*;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 200711:20:00 AM
 */
public class FileObjectListTransferableUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		
		f1 = createNiceMock(FileObject.class);

		FileName n1 = createNiceMock(FileName.class);
		
		u1 = new URI("http://www.astrogrid.org");
		
		//stitch it all together.
		expect(f1.getName()).andStubReturn(n1);
		expect(n1.getURI()).andStubReturn(u1.toString());
		replay(f1,n1);


		/// same again for second fille object.

		f2 = createNiceMock(FileObject.class);
		FileName n2 = createNiceMock(FileName.class);
		u2 = new URI("http://www.nowhere.org");


        expect(f2.getName()).andStubReturn(n2);
        expect(n2.getURI()).andStubReturn(u2.toString());
        replay(f2,n2);		
		
		// now build a list, and the transferable.
		l = new ArrayList();
		l.add(f1);
		l.add(f2);
		trans = new FileObjectListTransferable(l);
	}
	FileObject f1;
	URI u1;
	FileObject f2;
	URI u2;
	List l;
	FileObjectListTransferable trans;
	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		f1 = null;
		u1 = null;
		f2 = null;
		u2 = null;
		l = null;
		trans = null;
	}

	public void testLocalFileObjectFlavor() throws Exception {
		assertTrue(trans.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_ARRAY));
		Object o = trans.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT_ARRAY);
		assertFalse(VoDataFlavour.LOCAL_FILEOBJECT_ARRAY.isFlavorSerializedObjectType());
		assertNotNull(o);
		assertTrue(o instanceof FileObject[]);
		assertEquals(l,Arrays.asList((FileObject[])o));
	}
	
	public void testLocalURIArrayFlavor() throws Exception {
		assertTrue(trans.isDataFlavorSupported(VoDataFlavour.LOCAL_URI_ARRAY));
		Object o = trans.getTransferData(VoDataFlavour.LOCAL_URI_ARRAY);
		assertNotNull(o);
		assertTrue(o instanceof URI[]);
		URI[] l1 = (URI[])o;
		assertEquals(l.size(),l1.length);
		assertNotSame(l,l1);
		assertEquals(u1,l1[0]);
		assertEquals(u2,l1[1]);		
	}
	

	public void testURIListFlavor() throws Exception {
		assertTrue(trans.isDataFlavorSupported(VoDataFlavour.URI_LIST));
		Object o = trans.getTransferData(VoDataFlavour.URI_LIST);
		assertTrue(VoDataFlavour.URI_LIST.isFlavorTextType());
		assertTrue(VoDataFlavour.URI_LIST.isRepresentationClassInputStream());
		assertNotNull(o);
		assertTrue(o instanceof InputStream);
		BufferedReader br = new BufferedReader(new InputStreamReader((InputStream)o));
		assertEquals(u1.toString(),br.readLine());
		assertEquals(u2.toString(),br.readLine());		
		assertNull(br.readLine());
	}
	
	
	public void testPlainFlavor() throws Exception {
		assertTrue(trans.isDataFlavorSupported(VoDataFlavour.PLAIN));
		Object o = trans.getTransferData(VoDataFlavour.PLAIN);
		assertTrue(VoDataFlavour.PLAIN.isFlavorTextType());
		assertTrue(VoDataFlavour.PLAIN.isRepresentationClassInputStream());
		assertNotNull(o);
		assertTrue(o instanceof InputStream);
		BufferedReader br = new BufferedReader(new InputStreamReader((InputStream)o));
		assertEquals(u1.toString(),br.readLine());
		assertEquals(u2.toString(),br.readLine());	
		assertNull(br.readLine());
	}
	
	
	
}
