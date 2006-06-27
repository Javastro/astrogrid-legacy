/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.datatransfer.DataFlavor;
import java.io.InputStream;

import org.astrogrid.desktop.modules.ui.sendto.VoDataFlavour;

import junit.framework.TestCase;

/** Test the constants in vodataflavor - also the behaviour of DataFlavor
 * @author Noel Winstanley
 * @since Jun 19, 20068:47:53 AM
 */
public class VoDataFlavourUnitTest extends TestCase {
	
	public void testVotable() {
		doTestMimeAndStringMime(VoDataFlavour.VOTABLE,VoDataFlavour.VOTABLE_STRING);
	}
	
	public void testSiapResponse() {
		doTestMimeAndStringMime(VoDataFlavour.SIAP_RESPONSE, VoDataFlavour.SIAP_RESPONSE_STRING);

	}

	public void testSsapResponse() {
		doTestMimeAndStringMime(VoDataFlavour.SSAP_RESPONSE,VoDataFlavour.SSAP_RESPONSE_STRING);
	}
	
	public void testVoresouce() {
		doTestMimeAndStringMime(VoDataFlavour.VORESOURCE,VoDataFlavour.VORESOURCE_STRING);
	}
	
	public void testAdqlx() {
		doTestMimeAndStringMime(VoDataFlavour.ADQLX,VoDataFlavour.ADQLX_STRING);
	}
	
	public void testWorkflow() {
		doTestMimeAndStringMime(VoDataFlavour.WORKFLOW,VoDataFlavour.WORKFLOW_STRING);
	}
	
	public void testWorkflowTranscript() {
		doTestMimeAndStringMime(VoDataFlavour.WORKFLOW_TRANSCRIPT, VoDataFlavour.WORKFLOW_TRANSCRIPT_STRING);
	}
	
	public void testCeaTool() {
		doTestMimeAndStringMime(VoDataFlavour.CEA_TOOL,VoDataFlavour.CEA_TOOL_STRING);
	}
	
	public void testHtml() {
		doTestMimeAndStringMime(VoDataFlavour.HTML,VoDataFlavour.HTML_STRING);
	}
	
	public void testXml() {
		doTestMimeAndStringMime(VoDataFlavour.XML,VoDataFlavour.XML_STRING);
	}
	
	public void doTestMimeAndStringMime(DataFlavor m,DataFlavor sm) {
		assertEquals(InputStream.class, m.getRepresentationClass());
		assertEquals(String.class,sm.getRepresentationClass());
		
		// the two objects aren't equal
		assertFalse(m.equals(sm));
		
		
		// their mime types arent equal - as getMimeType() gives the full mime, plus params - misleading */
		assertFalse(m.getMimeType().equals(sm.getMimeType()));
	
		// assert the primary mime type is equal
		assertEquals(m.getPrimaryType(), sm.getPrimaryType());
		// human readable description should be equal too - as rep class is just an implementation detail.
		assertEquals(m.getHumanPresentableName(),sm.getHumanPresentableName());
	}

	// should really repeat same for all the rest of my types.
	
}
