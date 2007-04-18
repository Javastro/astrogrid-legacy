/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.net.URI;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import net.sourceforge.hiveutils.service.ObjectBuilder;

import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/** @todo add an internal interface to allow displaying of pre-parsed resource objkects.
 * @author Noel Winstanley
 * @since Jun 21, 20067:25:20 PM
 */
public class RegistryBrowserFactory implements RegistryBrowser {

	public RegistryBrowserFactory( UIContext context,
			UIComponent ui, ObjectBuilder regPanelBuilder) 
	throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		this.ui = ui;
		this.context = context;
		this.builder = regPanelBuilder;
	}

	private final UIComponent ui;
	private final UIContext context;
	private final ObjectBuilder builder;
	public void hide() {
		// ignore.
	}
	public void show() {
		RegistryBrowser r = new RegistryBrowserImpl(context,createPanel());
		r.show();
	}
	public void search(String arg0) {
		RegistryBrowser r = new RegistryBrowserImpl(context,createPanel());
		r.search(arg0);
		r.show();
	}
	
	public void open(URI arg0) {
		RegistryBrowser r = new RegistryBrowserImpl(context,createPanel());
		r.open(arg0);
		r.show();
	}
	
	private RegistryGooglePanel createPanel() {
		return (RegistryGooglePanel)builder.create("registryGooglePanel");
	}

}
