/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.net.URI;
import java.util.List;

import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.votech.plastic.CommonMessageConstants;

/** Implementation of send - based on broadcast..
 * @author Noel Winstanley
 * @since Jun 23, 20062:42:44 AM
 */
public class SendPlasticVotableSTA extends BroadcastPlasticVotableSTA {

	/**
	 * @param name
	 * @param description
	 * @param icon
	 * @param vos
	 * @param tupp
	 */
	public SendPlasticVotableSTA(URI targetApp, Myspace vos, TupperwareInternal tupp) {
		super("as VOTable", "Send as VOTABLE to this plastic application", "plasticeye.gif", vos, tupp);
		this.target = targetApp;
	}
	protected final URI target;
	
	protected void sendLoadMessage(List l) {
		tupp.singleTargetPlasticMessage(CommonMessageConstants.VOTABLE_LOAD,l,target);
	}

	protected void sendLoadURLMessage(List l) {
		tupp.singleTargetPlasticMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL,l,target);
	}


}
