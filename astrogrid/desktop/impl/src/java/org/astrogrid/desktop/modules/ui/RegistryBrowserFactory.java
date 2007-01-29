/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.net.URI;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import net.sf.ehcache.Ehcache;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.RegistryBrowser;
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

	/**
	 * @param reg
	 * @param hs
	 * @param ui
	 * @param conf
	 * @param pref 
	 * @throws TransformerConfigurationException
	 * @throws TransformerFactoryConfigurationError
	 */
	public RegistryBrowserFactory(RegistryInternal reg, HelpServerInternal hs,
			UIInternal ui, Configuration conf, BrowserControl browser,
			Ehcache cache1, Ehcache cache2,VoMon vomon, Preference pref) 
	throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		this.ui = ui;
		this.conf = conf;
		this.hs = hs;
		this.reg = reg;
		this.browser = browser;
		this.cache1 = cache1;
		this.cache2 = cache2;
		this.vomon = vomon;
		this.pref = pref;
	}
	private final Preference pref;
	private final BrowserControl browser;
	private final UIInternal ui;
	private final Configuration conf;
	private final HelpServerInternal hs;
	private final RegistryInternal reg;
	private final Ehcache cache1;
	private final Ehcache cache2;
	private final VoMon vomon;
	public void hide() {
		// ignore.
	}
	public void show() {
		RegistryBrowser r = new RegistryBrowserImpl(reg,hs,ui,conf,browser,this,cache1,cache2,vomon,pref);
		r.show();
	}
	public void search(String arg0) {
		RegistryBrowser r = new RegistryBrowserImpl(reg,hs,ui,conf,browser,this,cache1,cache2,vomon,pref);
		r.search(arg0);
		r.show();
	}
	
	public void open(URI arg0) {
		RegistryBrowser r = new RegistryBrowserImpl(reg,hs,ui,conf,browser,this,cache1,cache2,vomon,pref);
		r.open(arg0);
		r.show();
	}

}
