/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ivoa.CacheFactory;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley
 * @since Apr 11, 200611:37:16 PM
 */
public class ChooseToolEditorPanelFactory implements ToolEditorPanelFactory {

	
	private final RegistryInternal registry;
	private final ApplicationsInternal apps;
	private final BrowserControl browser;
	private final RegistryBrowser regBrowser;
	private final CacheFactory cache;
	public AbstractToolEditorPanel create(ToolModel model, UIComponent parent) {
		return new ChooseAToolEditorPanel(model,parent,registry,apps,browser,regBrowser,cache);
	}
	public ChooseToolEditorPanelFactory(final RegistryInternal registry, final ApplicationsInternal apps,BrowserControl browser, RegistryBrowser regBrowser, CacheFactory cache) {
		super();
		this.registry = registry;
		this.apps = apps;
		this.browser = browser;
		this.regBrowser = regBrowser;
		this.cache =cache;
	}
	public String getName() {
		return "Chooser";
	}

}
