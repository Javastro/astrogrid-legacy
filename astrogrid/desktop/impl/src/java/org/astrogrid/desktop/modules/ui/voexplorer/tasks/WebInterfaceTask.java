/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;

/** Display the webpage interface for a service, if given.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20075:12:58 PM
 */
public class WebInterfaceTask extends ResourceTask {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(WebInterfaceTask.class);

/**
 * 
 */
public WebInterfaceTask(BrowserControl browser) {
	CSH.setHelpIDString(this, "resourceTask.web");		
	this.browser = browser;
	setText("Web interface");
	setIcon(IconHelper.loadIcon("browser16.png"));
	setToolTipText("Open the web interface for this resource in the system browser");
}
	protected boolean invokable(Resource r) {
		if (!( r instanceof Service)) {
			return false;
		}
		Capability[] capabilities = ((Service)r).getCapabilities();
		for (int i = 0; i < capabilities.length; i++) {
			if (capabilities[i].getType().indexOf("WebBrowser") != -1) {
				return true;
			}
		}
		return false;
	}
	
	public void someSelected(List l) {
		noneSelected();
	}
	private final BrowserControl browser;
public void actionPerformed(ActionEvent e) {
	List l = computeInvokable();
	Resource r = (Resource)l.get(0);
	// now find right interface.
	Capability[] capabilities = ((Service)r).getCapabilities();
	for (int i = 0; i < capabilities.length; i++) {
		if (capabilities[i].getType().indexOf("WebBrowser") != -1) {
			Interface[] interfaces = capabilities[i].getInterfaces();
			// assume a single interface.
			try {
			browser.openURL(interfaces[0].getAccessUrls()[0].getValue());
			} catch (Exception ex) {
				logger.error("Failed to displauy web interface",ex);
			}
		}
	}	
}
}