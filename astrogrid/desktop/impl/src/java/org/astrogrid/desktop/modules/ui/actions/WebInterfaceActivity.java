/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/** Display the webpage interface for a resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20075:12:58 PM
 */
public class WebInterfaceActivity extends AbstractResourceActivity {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(WebInterfaceActivity.class);

/**
 * 
 */
public WebInterfaceActivity(final BrowserControl browser) {
    setHelpID("activity.web");
	this.browser = browser;
	setText("Web interface");
	setIcon(IconHelper.loadIcon("browser16.png"));
	setToolTipText("Open the providers's own web interface for this resource");
  //  setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,UIComponentMenuBar.MENU_KEYMASK));	
}
	protected boolean invokable(final Resource r) {
		return hasWebBrowserInterface(r);
	}
	public static boolean hasWebBrowserInterface(final Resource r) {
		if (!( r instanceof Service)) {
			return false;
		}
		final Capability[] capabilities = ((Service)r).getCapabilities();
		for (int i = 0; i < capabilities.length; i++) {
            final Capability capability = capabilities[i];
            final Interface[] interfaces = capability.getInterfaces();
            for (int j = 0; j < interfaces.length; j++) {
                final Interface iface = interfaces[j];
                if (StringUtils.contains(iface.getType(),"WebBrowser")) {
                    return true;
                }
            }
        }
		return false;
	}
	
	public void someSelected(final Resource[] rs) {
		noneSelected();
	}
	private final BrowserControl browser;
public void actionPerformed(final ActionEvent e) {
	final List l = computeInvokable();
	final Resource r = (Resource)l.get(0);
	// now find right interface.
	final Capability[] capabilities = ((Service)r).getCapabilities();
    for (int i = 0; i < capabilities.length; i++) {
        final Capability capability = capabilities[i];
        final Interface[] interfaces = capability.getInterfaces();
        for (int j = 0; j < interfaces.length; j++) {
            final Interface iface = interfaces[j];
            if (iface.getAccessUrls().length > 0 && StringUtils.contains(iface.getType(),"WebBrowser")) {
                try {
                    browser.openURL(iface.getAccessUrls()[0].getValue());
                } catch (final Exception ex) {
                    uiParent.get().showTransientError("Failed to show web interface",ExceptionFormatter.formatException(ex));
                }               
            }
        }
    }	
	
}
}
