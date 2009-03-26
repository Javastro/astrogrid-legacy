/**
 * 
 */
package org.astrogrid.desktop.modules.system.transformers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Transformer;
import org.apache.hivemind.Registry;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** integration for xhtml transformer. NOT Automatable - requires checking by eye.
 * a test that takes stylesheets provided in production configuration,
 * and applies each to sample inputs - to verify that correct sheets are being selected,
 * and that the results are pleasant. (display results in browser).
 * @author Noel Winstanley
 * @since Jun 7, 20066:25:18 PM
 */
public class Xml2XhtmlTransformerIntegrationTest extends InARTestCase {
    public static Test suite() {
        return new ARTestSetup(new TestSuite(Xml2XhtmlTransformerIntegrationTest.class));
    }
	/*
	 * @see TestCase#setUp()
	 */
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		Registry reg = getHivemindRegistry();
		trans = (Transformer)reg.getService("framework.stylesheetTransformer",Transformer.class);
		assertNotNull(trans);
		browser = (BrowserControl)reg.getService(BrowserControl.class);
		assertNotNull(browser);
	}
	protected Transformer trans;
	protected BrowserControl browser;

	@Override
    protected void tearDown() throws Exception {
		super.tearDown();
		trans = null;
		browser = null;
	}
	/*
	 * Test method for 'org.astrogrid.desktop.modules.system.transformers.Xml2XhtmlTransformer.transform(Object)'
	 */
	public void testTransformUnknownXML() throws ParserConfigurationException, SAXException, IOException, ACRException {
		transformAndDisplay("/org/astrogrid/desktop/hivemind/framework.xml", "test-html");
	}
	
	public void testTransformWorkflow() throws MalformedURLException, ParserConfigurationException, SAXException, IOException, ACRException {
		transformAndDisplay("test-workflow.xml", "test-wf");
	}
	//@todo causes an out of memory error at the moment - commented out, as halts the test run.
	/*protected void  testTransformVotable() throws MalformedURLException, ParserConfigurationException, SAXException, IOException, ACRException {
		transformAndDisplay("test-votable.xml", "test-vot");
	}*/
	/**
	 * @param resourcePath
	 * @param resultFileName
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ACRException
	 * @throws MalformedURLException
	 */
	private void transformAndDisplay(String resourcePath, String resultFileName) throws ParserConfigurationException, SAXException, IOException, ACRException, MalformedURLException {
		InputStream is = this.getClass().getResourceAsStream(resourcePath);
		assertNotNull("Input Stream is null",is);
		Document doc = DomHelper.newDocument(is);
		assertNotNull("Returned Document is null",doc);
		Object o = trans.transform(doc);
		assertNotNull(o);
		assertEquals(String.class,o.getClass());
		File f = File.createTempFile(resultFileName,".html");
		//f.deleteOnExit(); - causes race condition.
		FileWriter fw =new FileWriter(f);
		fw.write((String)o);
		fw.close();
		browser.openURL(f.toURL());
	}
}
