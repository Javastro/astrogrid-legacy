package org.astrogrid.desktop.modules.plastic;

import org.astrogrid.acr.system.SystemTray;
import org.votech.plastic.PlasticHubListener;

public class SysTrayUnitTest extends AbstractPlasticTestBase {
	
	private final class TestSystemTray implements SystemTray {
		private String caption;

		private String message;

		public void displayErrorMessage(String arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		public void displayInfoMessage(String arg0, String arg1) {
			this.caption = arg0;
			this.message = arg1;
			
		}

		public void displayWarningMessage(String arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		public void startThrobbing() {
			// TODO Auto-generated method stub
			
		}

		public void stopThrobbing() {
			// TODO Auto-generated method stub
			
		}

		/**
		 * @return Returns the caption.
		 */
		public String getCaption() {
			return caption;
		}

		/**
		 * @return Returns the message.
		 */
		public String getMessage() {
			return message;
		}
	}

	private TestSystemTray tray;
	private PlasticHubListener hub;

	public void setUp() {
		super.setUp();
		tray = new TestSystemTray();
		hub = getNewHub();
		
	}

	private PlasticHubListener getNewHub() {
		return new PlasticHubImpl(version, ui, executor, idGenerator,  rmi, web, tray, new PrettyPrinterImpl(browser),config);
	}
	
	public void testPreferenceNotSet() {
		// just to make sure
		config.removeKey(PlasticHubImpl.PLASTIC_NOTIFICATIONS_ENABLED);
		hub.registerNoCallBack("test");
		assertEquals("Plastic", tray.getCaption());
		assertNotNull(tray.getMessage());
	}
	
	public void testNotificationsDisabled() {
		((PlasticHubListenerInternal) hub).setNotificationsEnabled(false);
		//check it persists
		hub = null;
		hub = getNewHub();
		hub.registerNoCallBack("test");
		
		assertNull(tray.getCaption());
		assertNull(tray.getMessage());		
	}
	
	public void testNotificationsEnabled() {
		((PlasticHubListenerInternal) hub).setNotificationsEnabled(true);
		//check it persists
		hub = null;
		hub = getNewHub();
		hub.registerNoCallBack("test");
		
		assertEquals("Plastic", tray.getCaption());
		assertNotNull(tray.getMessage());		
	}

}
