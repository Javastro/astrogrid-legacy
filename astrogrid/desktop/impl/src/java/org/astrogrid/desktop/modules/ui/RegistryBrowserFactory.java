/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;

/**
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
	public RegistryBrowserFactory(Registry reg, HelpServerInternal hs, UIInternal ui, Configuration conf) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		this.ui = ui;
		this.conf = conf;
		this.hs = hs;
		this.reg = reg;
	}
	
	private final UIInternal ui;
	private final Configuration conf;
	private final HelpServerInternal hs;
	private final Registry reg;
	public void hide() {
		// ignore.
	}
	public void show() {
		RegistryBrowser r = new RegistryBrowserImpl(reg,hs,ui,conf);
		r.show();
	}

}
