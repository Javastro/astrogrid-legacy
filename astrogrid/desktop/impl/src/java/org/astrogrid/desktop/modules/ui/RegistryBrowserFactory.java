/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.net.URI;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import net.sf.ehcache.Ehcache;
import net.sourceforge.hiveutils.service.ObjectBuilder;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.votech.VoMon;

/** @todo add an internal interface to allow displaying of pre-parsed resource objkects.
 * @author Noel Winstanley
 * @since Jun 21, 20067:25:20 PM
 */
public class RegistryBrowserFactory implements RegistryBrowser {

	public RegistryBrowserFactory( HelpServerInternal hs,
			UIInternal ui, Configuration conf, ObjectBuilder regPanelBuilder) 
	throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		this.ui = ui;
		this.conf = conf;
		this.hs = hs;
		this.builder = regPanelBuilder;
	}

	private final UIInternal ui;
	private final Configuration conf;
	private final HelpServerInternal hs;
	private final ObjectBuilder builder;
	public void hide() {
		// ignore.
	}
	public void show() {
		RegistryBrowser r = new RegistryBrowserImpl(hs,ui,conf,createPanel());
		r.show();
	}
	public void search(String arg0) {
		RegistryBrowser r = new RegistryBrowserImpl(hs,ui,conf,createPanel());
		r.search(arg0);
		r.show();
	}
	
	public void open(URI arg0) {
		RegistryBrowser r = new RegistryBrowserImpl(hs,ui,conf,createPanel());
		r.open(arg0);
		r.show();
	}
	
	private RegistryGooglePanel createPanel() {
		return (RegistryGooglePanel)builder.create("registryGooglePanel");
	}

}
