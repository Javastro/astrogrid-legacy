/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.dnd.PreferredTransferable;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.SpectrumLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.SsapRetrieval;

/**
 * @author Noel Winstanley
 * @since Sep 14, 20061:38:58 PM
 */
public class BroadcastPlasticSpectrumSTA extends AbstractSTA {

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public BroadcastPlasticSpectrumSTA(String name, String description, String icon, TupperwareInternal tupp) {
		super(name, description, icon);
		this.tupp = tupp;
	}
	
	public BroadcastPlasticSpectrumSTA(TupperwareInternal tupp) {
		super("Broadcast as Spectrum","Broadcast as Spectrum to all suitable plastic applications","tx3.gif");
		this.tupp = tupp;
	}
	
	protected final TupperwareInternal tupp;


	public void actionPerformed(ActionEvent arg0) {
		(new BackgroundWorker(getParent(),"Sending as Spectrum") {
			protected Object construct() throws Exception {
	        	 if (getAtom().isDataFlavorSupported(VoDataFlavour.URL)) { // other remote
	            		URL url = (URL)getAtom().getTransferData(VoDataFlavour.URL);
	            		List l = new ArrayList();
	            		l.add(url.toString()); 
	            		l.add(url.toString());
	            		l.add(getAtom().getMetaData());
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
	
	protected void sendLoadMethod(List l) {
		tupp.broadcastPlasticMessageAsynch(
				SpectrumLoadPlasticButton.SPECTRA_LOAD_FROM_URL,l);
	}	
	protected boolean checkApplicability(PreferredTransferable atom) {
		// special case.
		if		(atom instanceof TreeNodePreferredTransferable) {
			String format = (String)atom.getMetaData().get(SsapRetrieval.SPECTRA_FORMAT_ATTRIBUTE);
			return format != null && format.startsWith("spectrum");
		} else {
			// general case - don't really know what this is.
		return 
		(atom.isDataFlavorSupported(VoDataFlavour.FITS_IMAGE)
			|| atom.isDataFlavorSupported(VoDataFlavour.FITS_TABLE)
			|| atom.isDataFlavorSupported(VoDataFlavour.VOTABLE)
		)
			&&  tupp.somethingAccepts(SpectrumLoadPlasticButton.SPECTRA_LOAD_FROM_URL);
	}
	}

}
