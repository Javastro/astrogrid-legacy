/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.editors;

import net.sf.ehcache.Ehcache;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.votech.VoMon;

/**
 * @author Noel Winstanley
 * @since Apr 11, 200611:37:16 PM
 */
public class ChooseToolEditorPanelFactory implements ToolEditorPanelFactory {

	
	private final RegistryInternal registry;
	private final ApplicationsInternal apps;
	private final BrowserControl browser;
	private final RegistryBrowser regBrowser;
	private final Ehcache cache1;
	private final Ehcache cache2;
	private final VoMon vomon;
	private final Preference pref;
	public AbstractToolEditorPanel create(ToolModel model, UIComponent parent) {
		return new ChooseAToolEditorPanel(model,parent,registry,apps,browser,regBrowser,cache1,cache2,vomon,pref);
	}
	public ChooseToolEditorPanelFactory(final RegistryInternal registry, final ApplicationsInternal apps,BrowserControl browser, RegistryBrowser regBrowser,
			Ehcache cache1, Ehcache cache2, VoMon vomon, Preference pref) {
		super();
		this.registry = registry;
		this.apps = apps;
		this.browser = browser;
		this.regBrowser = regBrowser;
		this.cache1 = cache1;
		this.cache2 = cache2;
		this.vomon = vomon;
		this.pref = pref;
	}
	public String getName() {
		return "Chooser";
	}
	public boolean isAdvanced() {
		return false;
	}

}
