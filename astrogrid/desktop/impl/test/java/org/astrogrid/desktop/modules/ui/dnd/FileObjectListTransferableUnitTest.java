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
import org.easymock.MockControl;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 200711:20:00 AM
 */
public class FileObjectListTransferableUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		
		MockControl m = MockControl.createNiceControl(FileObject.class);
		f1 = (FileObject)m.getMock();

		MockControl mn1 = MockControl.createNiceControl(FileName.class);
		FileName n1 = (FileName)mn1.getMock();
		
		u1 = new URI("http://www.astrogrid.org");
		
		//stitch it all together.
		f1.getName();
		m.setDefaultReturnValue(n1);
		n1.getURI();
		mn1.setDefaultReturnValue(u1.toString());
		m.replay();
		mn1.replay();

		/// same again for second fille object.

		MockControl m2 = MockControl.createNiceControl(FileObject.class);
		f2 = (FileObject)m2.getMock();

		MockControl mn2 = MockControl.createNiceControl(FileName.class);
		FileName n2 = (FileName)mn2.getMock();
		
		u2 = new URI("http://www.nowhere.org");
		
		//stitch it all together.
		f2.getName();
		m2.setDefaultReturnValue(n2);
		n2.getURI();
		mn2.setDefaultReturnValue(u2.toString());
		m2.replay();
		mn2.replay();		
		
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
		assertTrue(trans.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_LIST));
		Object o = trans.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT_LIST);
		assertFalse(VoDataFlavour.LOCAL_FILEOBJECT_LIST.isFlavorSerializedObjectType());
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
