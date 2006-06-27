/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.net.URI;
import java.util.List;

import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.votech.plastic.CommonMessageConstants;

/** Implementation based on Broadcast.
 * @author Noel Winstanley
 * @since Jun 23, 20062:47:57 AM
 */
public class SendPlasticFitsSTA extends BroadcastPlasticFitsSTA {

	public SendPlasticFitsSTA(URI targetApp,TupperwareInternal tupp) {
		super("as FITS","Send as FITS to this plastic application","plasticeye.gif",tupp);
		this.target = targetApp;
	}
	protected final URI target;
protected void sendLoadMethod(List l) {
	tupp.singleTargetPlasticMessage(CommonMessageConstants.FITS_LOAD_FROM_URL,l,target);
}


}
