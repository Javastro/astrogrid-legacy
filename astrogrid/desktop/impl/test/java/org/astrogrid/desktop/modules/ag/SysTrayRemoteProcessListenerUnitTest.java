/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.io.IOException;
import java.net.URI;

import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;
import org.easymock.AbstractMatcher;
import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley
 * @since Jun 13, 20067:14:51 PM
 */
public class SysTrayRemoteProcessListenerUnitTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		recorderControl = MockControl.createControl(MessageRecorderInternal.class);
		recorder = (MessageRecorderInternal)recorderControl.getMock();
		trayControl = MockControl.createControl(SystemTray.class);
		tray = (SystemTray)trayControl.getMock();
		listener = new SysTrayRemoteProcessListener(recorder,tray);
		uri = new URI("ivo://wibble/pling");
		folderControl = MockControl.createControl(Folder.class);
		folder =(Folder)folderControl.getMock(); 
		information = new ExecutionInformation(uri,"name",null,null,null,null);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		listener = null;
		recorder = null;
		recorderControl = null;
		tray = null;
		trayControl = null;
		uri = null;
		folderControl = null;
		folder = null;
		information = null;
	}
	protected SysTrayRemoteProcessListener listener;
	protected MessageRecorderInternal recorder;
	protected MockControl recorderControl;
	protected SystemTray tray;
	protected MockControl trayControl;
	protected URI uri;
	protected MockControl folderControl;
	protected Folder folder;
	protected ExecutionInformation information;

	/*
	 * Test method for 'org.astrogrid.desktop.modules.ag.SysTrayRemoteProcessListener.statusChanged(URI, String)'
	 */
	public void testStatusChangedToError() throws IOException {
		recorder.getFolder(uri);
		recorderControl.setReturnValue(folder);
		recorderControl.replay();
		
		folder.getInformation();
		folderControl.setReturnValue(information);
		folderControl.replay();
		
		tray.displayWarningMessage("name ended in error","See VO Lookout for details");
		trayControl.replay();
		listener.statusChanged(uri,"ERROR");
		
		recorderControl.verify();
		folderControl.verify();
		trayControl.verify();
		
	}
	
	public void testStatusChangedToCompleted() throws IOException {
		recorder.getFolder(uri);
		recorderControl.setReturnValue(folder);
		recorderControl.replay();
		
		folder.getInformation();
		folderControl.setReturnValue(information);
		folderControl.replay();
		
		tray.displayInfoMessage("name completed successfully","See VO Lookout for results");
		trayControl.replay();
		listener.statusChanged(uri,"COMPLETED");
		
		recorderControl.verify();
		folderControl.verify();
		trayControl.verify();
		
	}
	
	public void testStatusChangedToSomethingElse() throws IOException {

		recorderControl.replay();

		folderControl.replay();

		trayControl.replay();
		listener.statusChanged(uri,"Wibble");
		
		recorderControl.verify();
		folderControl.verify();
		trayControl.verify();
		
	}
	
	
	


}
