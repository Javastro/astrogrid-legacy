package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.dnd.PreferredTransferable;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.io.Piper;


/** consumer that displays result in a popup
 * 
 * .*/
public class ShowSTA extends AbstractSTA {
	public ShowSTA() {
		super("Show","Display value","show16.png");
	}
	public void actionPerformed(ActionEvent e) {
		if (getAtom().isDataFlavorSupported(VoDataFlavour.STRING)) {
			try { // in thread
			String data = (String)getAtom().getTransferData(VoDataFlavour.STRING);
			ResultDialog d = new ResultDialog(getParent().getFrame(),data);
			d.show();
			} catch (Exception ex) {
				getParent().showError("Unable to display result",ex);
			}
		} else { // in background.
			(new BackgroundWorker(getParent(),"Reading remote value") {

				protected Object construct() throws Exception {
					InputStream is = (InputStream)getAtom().getTransferData(VoDataFlavour.PLAIN);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					Piper.pipe(is,bos);
					is.close();
					bos.close();
					return bos.toString();
				}
				protected void doFinished(Object result) {
					ResultDialog d = new ResultDialog(getParent().getFrame(),result);
					d.show();
				}
			}).start();

		}

	}

	protected boolean checkApplicability(PreferredTransferable atom) {
		return
			 atom.isDataFlavorSupported(VoDataFlavour.PLAIN)
			|| atom.isDataFlavorSupported(VoDataFlavour.STRING);
			
	}
}