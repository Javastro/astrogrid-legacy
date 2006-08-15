/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.net.URI;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.CacheFactory;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;

/** @todo add an internal interface to allow displaying of pre-parsed resource objkects.
 * @author Noel Winstanley
 * @since Jun 21, 20067:25:20 PM
 */
public class RegistryBrowserFactory implements RegistryBrowser {

	/**
	 * @param reg
	 * @param hs
	 * @param ui
	 * @param conf
	 * @throws TransformerConfigurationException
	 * @throws TransformerFactoryConfigurationError
	 */
	public RegistryBrowserFactory(RegistryInternal reg, HelpServerInternal hs, UIInternal ui, Configuration conf, BrowserControl browser, CacheFactory cache) 
	throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		this.ui = ui;
		this.conf = conf;
		this.hs = hs;
		this.reg = reg;
		this.browser = browser;
		this.cache = cache;
	}
	private final BrowserControl browser;
	private final UIInternal ui;
	private final Configuration conf;
	private final HelpServerInternal hs;
	private final RegistryInternal reg;
	private final CacheFactory cache;
	public void hide() {
		// ignore.
	}
	public void show() {
		RegistryBrowser r = new RegistryBrowserImpl(reg,hs,ui,conf,browser,this,cache);
		r.show();
	}
	public void search(String arg0) {
		RegistryBrowser r = new RegistryBrowserImpl(reg,hs,ui,conf,browser,this,cache);
		r.search(arg0);
		r.show();
	}
	
	public void open(URI arg0) {
		RegistryBrowser r = new RegistryBrowserImpl(reg,hs,ui,conf,browser,this,cache);
		r.open(arg0);
		r.show();
	}

}
