/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import junit.framework.TestCase;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.impl.DefaultFileContentInfo;
import org.easymock.MockControl;

/** unit test for the fileobject transferable.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 200712:07:06 PM
 */
public class FileObjectTransferableUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		MockControl m = MockControl.createNiceControl(FileObject.class);
		f1 = (FileObject)m.getMock();

		MockControl mn1 = MockControl.createNiceControl(FileName.class);
		FileName n1 = (FileName)mn1.getMock();
		
		MockControl mc1 = MockControl.createNiceControl(FileContent.class);
		FileContent fc1 = (FileContent)mc1.getMock();
		
		u1 = new URI("http://www.astrogrid.org");
		
		//stitch it all together.
		f1.getName();
		m.setDefaultReturnValue(n1);
		n1.getURI();
		mn1.setDefaultReturnValue(u1.toString());
		
		f1.getContent();
		m.setDefaultReturnValue(fc1);
		
		fc1.getContentInfo();
		mc1.setDefaultReturnValue(new DefaultFileContentInfo(MIME,""));
		fc1.getInputStream();
		mc1.setDefaultReturnValue(new ByteArrayInputStream("".getBytes()));
		
		m.replay();
		mn1.replay();		
		mc1.replay();
		
		trans = new FileObjectTransferable(f1);
	}
	public static final String MIME = "application/pdf";
	FileObject f1;
	URI u1;
	FileObjectTransferable trans;

	protected void tearDown() throws Exception {
		super.tearDown();
		f1 = null;
		u1 = null;
		trans = null;
	}

	public void testGetDataFlavors() throws Exception {
		DataFlavor[] arr = trans.getTransferDataFlavors();
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		
	}
	
	public void testLocalFileobjectFlavor() throws Exception {
		assertTrue(trans.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT));
		Object o = trans.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT);
		assertFalse(VoDataFlavour.LOCAL_FILEOBJECT.isFlavorSerializedObjectType());
		assertNotNull(o);
		assertSame(f1,o);
	}
	

	public void testLocalURIFlavor() throws Exception {
		assertTrue(trans.isDataFlavorSupported(VoDataFlavour.LOCAL_URI));
		Object o = trans.getTransferData(VoDataFlavour.LOCAL_URI);
		assertNotNull(o);
		assertEquals(u1,o);
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
		assertNull(br.readLine());
	}	
	
	/* @todo - seems as if we don't expose the flavors of the content itself anymore
	 * need to revist this later and remind myself what I'm trying to do.
	public void testFileContentFlavor() throws Exception {
		DataFlavor f = new DataFlavor(MIME);
		assertTrue(f.isRepresentationClassInputStream());
		assertTrue(trans.isDataFlavorSupported(f));
		Object o = trans.getTransferData(f);
		assertNotNull(o);
		assertTrue(o instanceof InputStream);		
		
		
	}
	*/
	
	
}
