package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.votech.plastic.CommonMessageConstants;


/** broadcast a fits file to all registered listeners. */
public class BroadcastPlasticFitsSTA extends AbstractSTA {
	
	public BroadcastPlasticFitsSTA(String name,String descr, String icon, TupperwareInternal tupp) {
		super(name,descr,icon);
		this.tupp = tupp;
	}
	public BroadcastPlasticFitsSTA(TupperwareInternal tupp) {
		super("Broadcast as FITS","Broadcast as FITS to all suitable plastic applications","tx3.gif");
		this.tupp = tupp;
	}
	
	protected final TupperwareInternal tupp;
	
	public void actionPerformed(ActionEvent e) {
		(new BackgroundWorker(getParent(),"Sending as FITS") {
			protected Object construct() throws Exception {
	        	 if (getAtom().isDataFlavorSupported(VoDataFlavour.URL)) { // other remote
	            		URL url = (URL)getAtom().getTransferData(VoDataFlavour.URL);
	            		List l = new ArrayList();
	            		l.add(url.toString()); //JDT - Plastic args need to be xml-rpc types
	            		sendLoadMethod(l);
	            	} else { // retrieve data, then send the message.
	            		// no message exists - ho hum.
                        // JDT - but it could if you wanted...just need to decide how to send the binary
	            		// @future look into this.
	            	}
	            	return null;
			}

		}).start();
	}
	
	/**
	 * @param l
	 */
	protected void sendLoadMethod(List l) {
		tupp.broadcastPlasticMessageAsynch(CommonMessageConstants.FITS_LOAD_FROM_URL,l);
	}	
	protected boolean checkApplicability(PreferredTransferable atom) {
		return (atom.isDataFlavorSupported(VoDataFlavour.FITS_IMAGE)
			|| atom.isDataFlavorSupported(VoDataFlavour.FITS_TABLE))
			&&  ! tupp.getRegisteredApplicationsModel().isEmpty(); // only checking for something connected - not whether it accepts votable or not.
			
	}
}