/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import static org.easymock.EasyMock.*;

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
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

/** unit test for the fileobject transferable.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 200712:07:06 PM
 */
public class FileObjectTransferableUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();
		f1 = createNiceMock(FileObject.class);

		final FileName n1 = createNiceMock(FileName.class);
		
		final FileContent fc1 =createNiceMock(FileContent.class);
		
		u1 = new URI("http://www.astrogrid.org");
		
		//stitch it all together.
		expect(f1.getName()).andStubReturn(n1);
		expect(n1.getURI()).andStubReturn(u1.toString());		
		expect(f1.getContent()).andStubReturn(fc1);
		
		expect(fc1.getContentInfo()).andStubReturn(new DefaultFileContentInfo(MIME,""));
		expect(fc1.getInputStream()).andStubReturn(new ByteArrayInputStream("".getBytes()));

		replay(f1,n1,fc1);
		
		v1 = new FileObjectView(f1,null);
		trans = new FileObjectTransferable(v1,false);
	}
	public static final String MIME = "application/pdf";
	FileObject f1;
	FileObjectView v1;
	URI u1;
	FileObjectTransferable trans;

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		f1 = null;
		u1 = null;
		trans = null;
	}


	public void testGetDataFlavors() throws Exception {
		final DataFlavor[] arr = trans.getTransferDataFlavors();
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		
	}
	
	public void testLocalFileobjectFlavor() throws Exception {
		assertTrue(trans.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_VIEW));
		final Object o = trans.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT_VIEW);
		assertFalse(VoDataFlavour.LOCAL_FILEOBJECT_VIEW.isFlavorSerializedObjectType());
		assertNotNull(o);
		assertSame(v1,o);
	}
	

	public void testLocalURIFlavor() throws Exception {
		assertTrue(trans.isDataFlavorSupported(VoDataFlavour.LOCAL_URI));
		final Object o = trans.getTransferData(VoDataFlavour.LOCAL_URI);
		assertNotNull(o);
		assertEquals(u1,o);
	}
	
	public void testURIListFlavor() throws Exception {
		assertTrue(trans.isDataFlavorSupported(VoDataFlavour.URI_LIST));
		final Object o = trans.getTransferData(VoDataFlavour.URI_LIST);
		assertTrue(VoDataFlavour.URI_LIST.isFlavorTextType());
		assertTrue(VoDataFlavour.URI_LIST.isRepresentationClassInputStream());
		assertNotNull(o);
		assertTrue(o instanceof InputStream);
		final BufferedReader br = new BufferedReader(new InputStreamReader((InputStream)o));
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
