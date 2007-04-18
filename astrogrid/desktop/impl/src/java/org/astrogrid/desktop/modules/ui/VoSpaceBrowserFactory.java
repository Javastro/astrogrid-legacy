/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.ui.MyspaceBrowser;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;

/**
 * @author Noel Winstanley
 * @since Jun 21, 20067:32:27 PM
 */
public class VoSpaceBrowserFactory implements	MyspaceBrowser {

	/**
	 * @param conf
	 * @param hs
	 * @param ui
	 * @param vos
	 * @param browser
	 * @param chooser
	 * @param pref 
	 */
	public VoSpaceBrowserFactory(UIContext context, MyspaceInternal vos,SendToMenu sendTo,ResourceChooserInternal chooser, Community comm, Preference pref) {
		this.context = context;
		this.myspace = vos;
		this.chooser= chooser;
		this.comm = comm;
		this.sendTo = sendTo;
		this.pref = pref;
	}

	private final SendToMenu sendTo;
	private final UIContext context;
	private final MyspaceInternal myspace;
	private final ResourceChooserInternal chooser;
	private final Community comm;
	private final Preference pref;
	public void hide() {
		// ignored.
	}
	public void show() {
		VospaceBrowserImpl mb = new VospaceBrowserImpl(context, myspace, sendTo,chooser,pref);
		comm.addUserLoginListener(mb); // @todo - should this be a weak refernece?
		mb.show();
	}
}
