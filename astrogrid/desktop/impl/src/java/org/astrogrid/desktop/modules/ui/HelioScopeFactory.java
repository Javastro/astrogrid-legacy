/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.net.URISyntaxException;
import java.util.List;

import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ui.HelioScope;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;

/**
 * @author Noel Winstanley
 * @since Jun 21, 20067:16:58 PM
 */
public class HelioScopeFactory implements HelioScopeInternal {

	/**
	 * @param ui
	 * @param conf
	 * @param hs
	 * @param myspace
	 * @param chooser
	 * @param reg
	 * @param stap
	 * @param tupp
	 * @throws URISyntaxException
	 */
	public HelioScopeFactory(UIContext context, MyspaceInternal myspace, ResourceChooserInternal chooser, Registry reg, Stap stap, TupperwareInternal tupp, SendToMenu sendTo, SnitchInternal snitch, RegistryBrowser browser) throws URISyntaxException {
		this.context= context;
		this.myspace = myspace;
		this.chooser= chooser;
		this.reg = reg;
		this.stap = stap;
		this.tupp = tupp;
		this.sendTo = sendTo;
		this.snitch = snitch;
		this.browser = browser;
	}
	
	private final UIContext context;
	private final MyspaceInternal myspace;
	private final ResourceChooserInternal chooser;
	private final Registry reg;
	private final Stap stap;
	private final TupperwareInternal tupp;
	private final SendToMenu sendTo;
	private final SnitchInternal snitch;
	private final RegistryBrowser browser;
	public void show() {
		HelioScope i = new HelioScopeLauncherImpl(context,myspace, chooser, reg, stap, tupp, sendTo,snitch, browser);

		i.show();		
	}
	public void runSubset(List resources) {
		HelioScopeInternal i = new HelioScopeLauncherImpl(context,myspace, chooser, reg, stap, tupp, sendTo,snitch, browser);
		i.runSubset(resources); 
		i.show();			
	}

}
