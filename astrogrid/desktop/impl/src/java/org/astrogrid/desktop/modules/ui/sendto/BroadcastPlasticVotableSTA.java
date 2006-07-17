package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.io.Piper;
import org.votech.plastic.CommonMessageConstants;


/** broadcast a votable to all registered listeners */
public class BroadcastPlasticVotableSTA extends AbstractSTA {
	
	public BroadcastPlasticVotableSTA(String name, String description, String icon,Myspace vos, TupperwareInternal tupp) {
		super(name, description, icon);
		this.vos = vos;
		this.tupp = tupp;
	}
	public BroadcastPlasticVotableSTA(Myspace vos,TupperwareInternal tupp) {
		super("Broadcast as VOTable","Broadcast as VOTABLE to all suitable plastic applications","tx3.gif");
		this.vos = vos;
		this.tupp = tupp;
	}
	
	protected final Myspace vos;
	protected final TupperwareInternal tupp;
	
	public void actionPerformed(ActionEvent e) {
		(new BackgroundWorker(getParent(),"Sending as VOTable") {
			protected Object construct() throws Exception {
            	 if (getAtom().isDataFlavorSupported(VoDataFlavour.VOTABLE_STRING)) { // if is string, must be local...
            		 String vot;
             		if (getAtom().isDataFlavorSupported(VoDataFlavour.VOTABLE_STRING)) {
             			vot = (String)getAtom().getTransferData(VoDataFlavour.VOTABLE_STRING);
             		} else { // it's a stream
             			InputStream is = (InputStream)getAtom().getTransferData(VoDataFlavour.VOTABLE);
             			ByteArrayOutputStream bos = new ByteArrayOutputStream();
             			Piper.pipe(is,bos);
             			is.close();
             			bos.close();
             			vot = bos.toString();
             		}
              		List l = new ArrayList();
             		l.add(vot);
             		l.add(new URI("workbench:votable" + ++count));
             		sendLoadMessage(l);
                  		            		 
            	 } else { 
            		URL url = (URL)getAtom().getTransferData(VoDataFlavour.URL);
            		List l = new ArrayList();
            		l.add(url.toString()); //JDT plastic message arguments need to be xml-rpc types
            		sendLoadURLMessage(l);
            	} 
            	return null;
			}
		}).start();
	}

	
	private static int count = 0;
	protected boolean checkApplicability(PreferredTransferable atom) {
		return (atom.isDataFlavorSupported(VoDataFlavour.VOTABLE)
			|| atom.isDataFlavorSupported(VoDataFlavour.VOTABLE_STRING))
			&&  ! tupp.getRegisteredApplicationsModel().isEmpty(); // only checking for something connected - not whether it accepts votable or not.
	
	}
	/**
	 * @param l
	 */
	protected void sendLoadMessage(List l) {
		tupp.broadcastPlasticMessageAsynch(CommonMessageConstants.VOTABLE_LOAD,l);
	}
	/**
	 * @param l
	 */
	protected void sendLoadURLMessage(List l) {
		tupp.broadcastPlasticMessageAsynch(CommonMessageConstants.VOTABLE_LOAD_FROM_URL,l);
	}
}