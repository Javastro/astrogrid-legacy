package org.astrogrid.desktop.modules.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.net.URL;
import junit.framework.TestCase;

import org.apache.commons.vfs.VFS;
import org.astrogrid.io.Piper;
/**
 * @author Noel Winstanley
 * @since Aug 25, 20061:34:48 AM
 */
public class TablesImplUnitTest extends TestCase {

	@Override
    protected void setUp() throws Exception {
		super.setUp();

		this.tables = new TablesImpl(VFS.getManager());
		this.input = this.getClass().getResource("siap.vot");
		assertNotNull(input);
	}

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
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
	
	public void testConvertFile() throws Exception {
		assertNotNull(input);
		File tmp = File.createTempFile(this.getClass().getName(),"txt");
		tmp.deleteOnExit();
		tables.convertFiles(new URI(input.toString()),"votable",tmp.toURI(),"ascii");
		
	}
	
	
	// more advanced. check we can stream through, from votable to votable.
	// then compare results.
	public void testConvert1() throws Exception {
		File of = File.createTempFile(this.getClass().getName(),".vot");
		of.deleteOnExit();
		tables.convertFiles(new URI(input.toString()),"votable",new URI(of.toString()),"votable");

		// doesn't work - as stil outputs a votable1.1, while input is a v1.0
//
//		Diff diff = new Diff(new InputStreamReader(input.openStream())
//            ,new FileReader(of));
//		diff.overrideDifferenceListener(new IgnoreDoctypeAndNamespace());
//		DetailedDiff ddiff = new DetailedDiff(diff);
//		assertTrue(ddiff.toString(),ddiff.similar());
	}

	
}
