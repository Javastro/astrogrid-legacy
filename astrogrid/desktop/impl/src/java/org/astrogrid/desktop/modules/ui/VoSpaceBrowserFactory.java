/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.MyspaceBrowser;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
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
	 */
	public VoSpaceBrowserFactory(Configuration conf, HelpServerInternal hs, UIInternal ui, MyspaceInternal vos,SendToMenu sendTo, BrowserControl browser, ResourceChooserInternal chooser, Community comm) {
		this.ui = ui;
		this.conf = conf;
		this.hs = hs;
		this.myspace = vos;
		this.chooser= chooser;
		this.browser = browser;
		this.comm = comm;
		this.sendTo = sendTo;
	}

	private final SendToMenu sendTo;
	private final UIInternal ui;
	private final Configuration conf;
	private final HelpServerInternal hs;
	private final MyspaceInternal myspace;
	private final ResourceChooserInternal chooser;
	private final BrowserControl browser;
	private final Community comm;
	public void hide() {
		// ignored.
	}
	public void show() {
		VospaceBrowserImpl mb = new VospaceBrowserImpl(conf, hs, ui, myspace, sendTo,browser, chooser);
		comm.addUserLoginListener(mb); // @todo - should this be a weak refernece?
		mb.show();
	}
}
