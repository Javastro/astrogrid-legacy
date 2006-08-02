/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.net.URISyntaxException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.acr.ui.HelioScope;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;

/**
 * @author Noel Winstanley
 * @since Jun 21, 20067:16:58 PM
 */
public class HelioScopeFactory implements HelioScope {

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
	public HelioScopeFactory(UIInternal ui, Configuration conf, HelpServerInternal hs, MyspaceInternal myspace, ResourceChooserInternal chooser, Registry reg, Stap stap, TupperwareInternal tupp, SendToMenu sendTo, SnitchInternal snitch) throws URISyntaxException {
		this.ui = ui;
		this.conf = conf;
		this.hs = hs;
		this.myspace = myspace;
		this.chooser= chooser;
		this.reg = reg;
		this.stap = stap;
		this.tupp = tupp;
		this.sendTo = sendTo;
		this.snitch = snitch;
	}
	
	private final UIInternal ui;
	private final Configuration conf;
	private final HelpServerInternal hs;
	private final MyspaceInternal myspace;
	private final ResourceChooserInternal chooser;
	private final Registry reg;
	private final Stap stap;
	private final TupperwareInternal tupp;
	private final SendToMenu sendTo;
	private final SnitchInternal snitch;
	public void show() {
		HelioScope i = new HelioScopeLauncherImpl(ui, conf, hs, myspace, chooser, reg, stap, tupp, sendTo,snitch);

		i.show();		
	}

}
