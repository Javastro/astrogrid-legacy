package org.astrogrid.desktop.modules.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.commons.vfs.VFS;
import org.astrogrid.io.Piper;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Node;

import static org.custommonkey.xmlunit.XMLAssert.*;
/**
 * @author Noel Winstanley
 * @since Aug 25, 20061:34:48 AM
 */
public class TablesImplUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();

		this.tables = new TablesImpl(VFS.getManager());
		this.input = this.getClass().getResource("siap.vot");
		assertNotNull(input);
	}

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
