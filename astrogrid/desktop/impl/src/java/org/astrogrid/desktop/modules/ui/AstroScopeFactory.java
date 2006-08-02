/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.net.URISyntaxException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;

/** Factory that builds instances of astroscope.
 * used to enable more than one instance under hivemind.
 * also can be used later to pre-fetch shared resources (e.g. registry queries)
 * and as a point to hang 'SendTo' actions,  Tupperware processors, and AR methods
 * that imply creating a new instance of astroscope.
 * @author Noel Winstanley
 * @since Jun 21, 20066:49:30 PM
 */
public class AstroScopeFactory implements AstroScope {

	/**
	 * @param ui
	 * @param conf
	 * @param hs
	 * @param myspace
	 * @param chooser
	 * @param reg
	 * @param siap
	 * @param cone
	 * @param ssap
	 * @param ses
	 * @param tupp
	 * @throws URISyntaxException
	 */
	public AstroScopeFactory(UIInternal ui, Configuration conf, HelpServerInternal hs, MyspaceInternal myspace, ResourceChooserInternal chooser, Registry reg, Siap siap, Cone cone, Ssap ssap, Sesame ses, TupperwareInternal tupp, SendToMenu sendTo, SnitchInternal snitch) throws URISyntaxException {
		this.ui = ui;
		this.conf = conf;
		this.hs = hs;
		this.myspace = myspace;
		this.chooser= chooser;
		this.reg = reg;
		this.siap = siap;
		this.cone = cone;
		this.ssap = ssap;
		this.ses = ses;
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
	private final Siap siap;
	private final Cone cone;
	private final Ssap ssap;
	private final Sesame ses;
	private final TupperwareInternal tupp;
	private final SendToMenu sendTo;
	private final SnitchInternal snitch;
	
	public void show() {
		AstroScope	i = new AstroScopeLauncherImpl(ui, conf, hs, myspace, chooser, reg, siap, cone, ssap, ses, tupp, sendTo,snitch);
		i.show();

	}

}
