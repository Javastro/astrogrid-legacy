/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.net.URI;
import java.util.List;

import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.scope.SpectrumLoadPlasticButton;

/**
 * @author Noel Winstanley
 * @since Sep 14, 20062:22:22 PM
 */
public class SendPlasticSpectrumSTA extends BroadcastPlasticSpectrumSTA {

	/**
	 * @param name
	 * @param description
	 * @param icon
	 * @param tupp
	 */
	public SendPlasticSpectrumSTA(URI targetApp,TupperwareInternal tupp) {
		super("as Spectrum","Send as spectrum to this plastic application", "plasticeye.gif",tupp);
		this.target = targetApp;
	}
	protected final URI target;
	
	protected void sendLoadMethod(List l) {
		tupp.singleTargetPlasticMessage(SpectrumLoadPlasticButton.SPECTRA_LOAD_FROM_URL,l,target);
	}

}
