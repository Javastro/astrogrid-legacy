package org.astrogrid.desktop.modules.plastic;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.system.BrowserControl;
import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;
import org.jdesktop.jdic.desktop.Message;
import org.picocontainer.Startable;
import org.votech.ds6.plastlets.BrowserPlastlet;
import org.votech.ds6.plastlets.EmailPlastlet;
import org.votech.ds6.plastlets.Plastlet;
import org.votech.ds6.plastlets.PrintPlastlet;
import org.votech.ds6.plastlets.SaveToMySpacePlastlet;

import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.StandardHandler;
import org.votech.plastic.managers.PlasticManager;
import org.votech.plastic.managers.PlasticManager.PlasticHubObserver;
/**
 * Manages creation and registration of Plastlets.
 * @author jdt
 *
 */
public class PlastletsImpl implements PlastletsInternal, Startable, PlasticHubObserver {
	private static final String NAME = "ACR Plastlets Manager";

	private static final String DESCRIPTION = "The Plastlets manager is part of the ACR resposible for (surprise, surprise) managing Plastlets.  Plastlets are small Plastic applications.  These are disabled by default - go to the plastlets menu in the Workbench to enable them.  More soon...";

	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(PlastletsImpl.class);

	private BrowserControl browser;

	private PlasticManager plasticManager;

	private Collection plastlets = new HashSet();

	private boolean enabled = false;
	
	public void setAllEnabled(boolean enabled) {
		this.enabled  = enabled;
		if (!enabled) {
			unregisterPlastlets();
		} else  {
			registerPlastlets();
		}
		
	}

	public PlastletsImpl(PlasticHubListener hub, BrowserControl browser) {
		//putting the hub in the ctor should force that component to start up
		//but we're not going to use it, in case there's another one running
		//from another vendor.
		this.browser = browser;
	}

	public void start() {
		logger.info("PlastletManager starting");
		logger.debug("Creating PlasticManager");
		StandardHandler handler = new StandardHandler(NAME, DESCRIPTION, "", "http://logo", PlasticListener.CURRENT_VERSION); 
		plasticManager = new PlasticManager(NAME,handler,false,30000);
		plasticManager.addObserver(plasticManager.new ObserverAdaptor(this));
		logger.debug("Manager will connect to hub when ready");
		plasticManager.connectWhenReady();
	}

	public void stop() {
		//TODO think about unregistering
		logger.info("PlastletManager stopping");
	}

	

	private void unregisterPlastlets() {
		logger.debug("Unregistering plastlets");
		Iterator it = plastlets.iterator();
		while (it.hasNext()) {
			Plastlet plastlet = (Plastlet) it.next();
			plastlet.unregister();//TODO this might not work if the hub is going down
		}
		plastlets.clear();
	}

	private void registerPlastlets() {
		logger.debug("Registering Plastlets");
		if (!plasticManager.isConnected()) {
			logger.warn("PlastletManager's PlasticManager isn't connected.  Cannot register plastlets.");
			return; //just to be on the safe side
		}
		
		//Construct them////////////////////////////////////////////////////
		
		//Browser
		Plastlet browserPlastlet = new BrowserPlastlet(new BrowserPlastlet.BrowserControl() {

			public boolean openURL(URL url) {
				try {
					browser.openURL(url);
				} catch (ACRException e) {
					logger.error("Error trying to control browser ",e);
					return false;
				}
				return true;
			}
			
		}, plasticManager.getId()); 
		plastlets.add(browserPlastlet);
		
		//Save2MySpace		
		try {
			Plastlet savePlastlet = new SaveToMySpacePlastlet(plasticManager.getId(), new Finder().find());
			plastlets.add(savePlastlet);
		} catch (ACRException e) {
			// Unlikely to happen in-process
			logger.error("Problem instantiating ACR for save2myspace plastlet",e);
		}
		
		//Email - currently doesn't work so well due to problems
		//with jdic
		Plastlet emailPlastlet = new EmailPlastlet(plasticManager.getId(), new EmailPlastlet.Mailer() {

			public void mail(EmailPlastlet.Message message) throws DesktopException  {
				//Convert between jdics message format, and our own.
				Message jdicMessage = new Message();
				jdicMessage.setSubject(message.getSubject());
				jdicMessage.setBody(message.getBody());
				try {
					jdicMessage.setAttachments(message.getAttachments());
				} catch (IOException e) {
					logger.warn("One of the attached files was not readable, ignoring",e);
					jdicMessage.setBody("Unable to attach file");
				}
					Desktop.mail(jdicMessage);

				
			}
			
		});
		
		plastlets.add(emailPlastlet);
		
		//Print
		Plastlet printPlastlet = new PrintPlastlet(plasticManager.getId(), new PrintPlastlet.Printer() {

			public void print(File file) throws Exception {
				Desktop.print(file);
			}
		});		
		plastlets.add(printPlastlet);
		
		//End////////////////////////////////////////////////////////////////
		
		
		Collection toRemove = new HashSet();
		//register them
		Iterator it = plastlets.iterator();
		while (it.hasNext()) {
			Plastlet plastlet = (Plastlet) it.next();
			boolean success = plastlet.register(plasticManager.getHub());
			if (!success) {
				//must be already registered by someone else
				toRemove.add(plastlet);
			}
		}
		plastlets.remove(toRemove);
	}

	public void hubUp(PlasticHubListener hub) {
		logger.info("Hub started");
		if (enabled) registerPlastlets();
	}

	public void hubDown() {
		logger.info("Hub stopped");
		unregisterPlastlets();
	}
	
}
