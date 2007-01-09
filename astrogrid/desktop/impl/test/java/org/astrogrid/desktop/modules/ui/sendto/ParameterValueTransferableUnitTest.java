/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.TestCase;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.easymock.MockControl;

/**
 * @author Noel Winstanley
 * @since Jun 21, 20063:40:11 PM
 */
public class ParameterValueTransferableUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		myspaceControl = MockControl.createNiceControl(Myspace.class);
		myspace = (Myspace)myspaceControl.getMock();
		
		local = new ParameterValue();
		local.setIndirect(false);
		local.setName("local");
		local.setValue("42");
		
		
		remote = new ParameterValue();
		remote.setIndirect(true);
		remote.setName("remote");
	//	remote.setValue("http://www.slashdot.org"); - removed - test with a 'local' remote resource.
		URL u = ParameterValueTransferableUnitTest.class.getResource("ParameterValueTransferableUnitTest.class");
		assertNotNull("I don't exist",u);
		remote.setValue(u.toString());
		
		ivo = new ParameterValue();
		ivo.setIndirect(true);
		ivo.setName("ivo");
		ivo.setValue("ivo://comm/user#path/val.vot");	
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		myspaceControl = null;
		myspace = null;
		local = null;
		remote = null;
		ivo = null;
	}
	
	protected MockControl myspaceControl;
	protected Myspace myspace;
	protected ParameterValue local;
	protected ParameterValue remote;
	protected ParameterValue ivo;

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ui.sendto.ParameterValueTransferable.getPreferredDataFlavor()'
	 */
	public void testGetPreferredDataFlavor() {
		ParameterValueTransferable trans = new ParameterValueTransferable(local,myspace);
		assertEquals(trans.getTransferDataFlavors()[0],trans.getPreferredDataFlavor());
		
		trans = new ParameterValueTransferable(remote,myspace);
		assertEquals(trans.getTransferDataFlavors()[0],trans.getPreferredDataFlavor());
		
		trans = new ParameterValueTransferable(ivo,myspace);
		assertEquals(trans.getTransferDataFlavors()[0],trans.getPreferredDataFlavor());	
	}



	/*
	 * Test method for 'org.astrogrid.desktop.modules.ui.sendto.ParameterValueTransferable.getTransferDataFlavors()'
	 */
	public void testGetTransferDataFlavorsLocal() {
		myspaceControl.replay();
		ParameterValueTransferable trans = new ParameterValueTransferable(local,myspace);
		DataFlavor[] expectedArr = ParameterValueTransferable.localResult;
		assertEquals(expectedArr,trans.getTransferDataFlavors());
		
		for (int i = 0; i < expectedArr.length; i++) {
			assertTrue(trans.isDataFlavorSupported(expectedArr[i]));
		}
		
	}
	
	public void testGetTransferDataFlavorsRemote() {
		myspaceControl.replay();
		ParameterValueTransferable trans = new ParameterValueTransferable(remote,myspace);
		DataFlavor[] expectedArr = ParameterValueTransferable.otherRemoteResult;
		assertEquals(expectedArr,trans.getTransferDataFlavors());
		
		for (int i = 0; i < expectedArr.length; i++) {
			assertTrue(trans.isDataFlavorSupported(expectedArr[i]));
		}
	}
	
	public void testGetTransferDataFlavorsIvo() {
		myspaceControl.replay();
		ParameterValueTransferable trans = new ParameterValueTransferable(ivo,myspace);
		DataFlavor[] expectedArr = ParameterValueTransferable.myspaceRemoteResult;
		assertEquals(expectedArr,trans.getTransferDataFlavors());
		
		for (int i = 0; i < expectedArr.length; i++) {
			assertTrue(trans.isDataFlavorSupported(expectedArr[i]));
		}
	}

	public void testGetTransferDataLocal() throws UnsupportedFlavorException, IOException {
		myspaceControl.replay();
		ParameterValueTransferable trans = new ParameterValueTransferable(local,myspace);
		DataFlavor[] flavs = trans.getTransferDataFlavors();
		for (int i = 0; i < flavs.length; i++) {
			Object o = trans.getTransferData(flavs[i]);
			assertNotNull(o);
			if (flavs[i] == VoDataFlavour.URL) {
				assertTrue(o instanceof URL);
			} else {
				assertEquals(o,local.getValue());
			}
		}
	}
	
	public void testGetTransferDataRemote() throws UnsupportedFlavorException, IOException {
		myspaceControl.replay();
		ParameterValueTransferable trans = new ParameterValueTransferable(remote,myspace);
		DataFlavor[] flavs = trans.getTransferDataFlavors();
		for (int i = 0; i < flavs.length; i++) {
			Object o = trans.getTransferData(flavs[i]);
			assertNotNull(o);
			if (flavs[i] == VoDataFlavour.URL) {
				assertTrue(o instanceof URL);
				assertEquals(remote.getValue(),o.toString());
			} else {
				assertTrue(o instanceof InputStream);
	
			}
		}
	}
	
	public void testGetTransferDataIvo() throws UnsupportedFlavorException, IOException, NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException, URISyntaxException {
		myspace.getReadContentURL(new URI(ivo.getValue()));
		myspaceControl.setDefaultReturnValue(new URL(remote.getValue()));
		myspaceControl.replay();
		ParameterValueTransferable trans = new ParameterValueTransferable(ivo,myspace);
		DataFlavor[] flavs = trans.getTransferDataFlavors();
		for (int i = 0; i < flavs.length; i++) {
			Object o = trans.getTransferData(flavs[i]);
			assertNotNull(o);
			if (flavs[i] == VoDataFlavour.IVORN) {
				assertTrue(o instanceof URI);
				assertEquals(ivo.getValue(),o.toString());
			} else if (flavs[i] == VoDataFlavour.URL) {
				assertTrue(o instanceof URL);
				assertEquals(remote.getValue(),o.toString());
			} else {
				assertTrue(o instanceof InputStream);

			}
		}
	}


}
