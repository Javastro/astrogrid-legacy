package org.astrogrid.desktop.modules.util;


import java.io.ByteArrayOutputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.io.Piper;
import org.easymock.MockControl;

/**
 * @author Noel Winstanley
 * @since Aug 25, 20061:34:48 AM
 */
public class TablesImplUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		myspaceControl = MockControl.createControl(MyspaceInternal.class);
		mockMs = (MyspaceInternal)myspaceControl.getMock();
		this.tables = new TablesImpl(mockMs);
		this.input = this.getClass().getResource("siap.vot");
	}

	private MockControl myspaceControl;
	private MyspaceInternal mockMs;
	protected void tearDown() throws Exception {
		super.tearDown();
		myspaceControl = null;
		mockMs = null;
	}
	
	TablesImpl tables;
	URL input;

	public void testListInputFormats() {
		String[] names = tables.listInputFormats();
		assertNotNull(names);
		assertTrue(names.length > 0);
	}
	
	public void testListOutputFormats() {
		String[] names = tables.listInputFormats();
		assertNotNull(names);
		assertTrue(names.length > 0);
	}
	public void testConvert() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Piper.pipe(input.openStream(),bos);
		String result = tables.convert(bos.toString(),"votable","ascii");
		
	}
	// requires live implementation of myspace, for now.
	//@todo revive this test when I've installed protocol handlers throughout.
//	public void testConvertFile() throws Exception {
//		assertNotNull(input);
//		File tmp = File.createTempFile(this.getClass().getName(),"txt");
//		tmp.deleteOnExit();
//		tables.convertFile(input.toURI(),"votable",tmp.toURI(),"ascii");
//		
//	}
	
	
	// more advanced. check we can stream through, from votable to votable.
	// then compare results.
	/* doesn't work - as stil outputs a votable1.1, while input is a v1.0
	 * 
	 */
//	public void testConvert1() throws IOException, SAXException, ParserConfigurationException {
//		File of = File.createTempFile(this.getClass().getName(),".vot");
//		of.deleteOnExit();
//		tables.convert(input.toString(),"votable",of.toString(),"votable");
//		XMLAssert.assertXMLEqual("input and output aren't the same"
//				,new InputStreamReader(input.openStream())
//				,new FileReader(of));
//	}

}
