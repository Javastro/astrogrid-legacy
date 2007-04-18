/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.io.InputStream;

import junit.framework.TestCase;

/** Test the constants in vodataflavor - also the behaviour of DataFlavor
 * @author Noel Winstanley
 * @since Jun 19, 20068:47:53 AM
 */
public class VoDataFlavourUnitTest extends TestCase {
	
public void testsNothing() {
	//@todo think whether to add anything extra here
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
