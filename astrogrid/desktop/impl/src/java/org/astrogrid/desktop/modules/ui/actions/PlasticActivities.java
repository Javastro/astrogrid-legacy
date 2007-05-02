/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.VOExplorerFactoryImpl;
import org.astrogrid.desktop.modules.ui.comp.PlasticButtons;
import org.astrogrid.desktop.modules.ui.comp.PlasticButtons.ButtonBuilder;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.SpectrumLoadPlasticButton;
import org.votech.plastic.CommonMessageConstants;

/**@todo mix together this, and the other plastic-buttons libraries - make it a single routine.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20071:29:37 PM
 */
public final class PlasticActivities extends AbstractActivityScavenger implements ButtonBuilder {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(PlasticActivities.class);

public PlasticActivities(TupperwareInternal tupp) {
	super("Plastic Applications");
	this.tupp = tupp;   
}
private JPanel plasticPanel;
private final TupperwareInternal tupp;

private static final List plasticMessages = new ArrayList();
static {
	plasticMessages.addAll(VOExplorerFactoryImpl.REGISTRY_MESSAGES);
	plasticMessages.add(CommonMessageConstants.FITS_LOAD_FROM_URL);
	plasticMessages.add(SpectrumLoadPlasticButton.SPECTRA_LOAD_FROM_URL);
	plasticMessages.add(CommonMessageConstants.VOTABLE_LOAD);
	plasticMessages.add(CommonMessageConstants.VOTABLE_LOAD_FROM_URL);
	
}

public JButton[] buildPlasticButtons(PlasticApplicationDescription plas) {
	List results = new ArrayList();
	for (Iterator i = plasticMessages.iterator(); i.hasNext(); ) {
		URI u = (URI)i.next();
		if (plas.understandsMessage(u)) {
			results.add(
					new PlasticRegistryButton(plas)
					);
			break;
		}
	}
	return (JButton[])results.toArray(new JButton[results.size()]);
}
	// necessary to lazily-initialize plastic panel,
	// as if we make an eager call to tupperware in the constructor, 
	// we get a deadlock, as voexplorer and tupperware both come up at the start
	// and voexporer contributes to tupperware, while depending (indirectly) on it.
	private JPanel getPlasticPanel() {
		if (plasticPanel == null) {
		    PlasticButtons pbs = new PlasticButtons(tupp.getRegisteredApplications(),this);
		    plasticPanel = pbs.getPanel();
		    plasticPanel.setPreferredSize(new Dimension(75,100));
		    //FIXMEadd(plasticPanel);
		}
		return plasticPanel;
	}
	private Transferable t;
	
	public void noneSelected() {
		getPlasticPanel().setVisible(false);
		t = null;
		super.noneSelected();
	}

	public void selected(Transferable r) {
		if (r.isDataFlavorSupported(VoDataFlavour.LOCAL_URI) 
				|| r.isDataFlavorSupported(VoDataFlavour.LOCAL_URI_ARRAY))  {
			getPlasticPanel().setVisible(true);
			t = r;
		} else {
			noneSelected();
		}
		super.selected(r);
	
	}

	public class PlasticRegistryButton extends JButton implements ActionListener{

		/**
		 * @param plas
		 * @param aggregate
		 * @param tupp
		 */
		public PlasticRegistryButton(PlasticApplicationDescription plas) {
			if (plas.getIcon() != null) {
		        ImageIcon scaled = new ImageIcon((plas.getIcon()).getImage().getScaledInstance(-1,32,Image.SCALE_SMOOTH));
				setIcon(scaled);
			}
			this.targetId = plas.getId();
			setPreferredSize(new Dimension(75,50)); // @todo work out way of fixing size of buttons
			setText("<html><center>Send to<br>" + plas.getName());
			setToolTipText("Send selection to another application using PLASTIC");
			addActionListener(this);
		}
		protected URI targetId;
		public void actionPerformed(ActionEvent e) {
			if (t == null) {
				return;
			}
			try {
				if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI)) {
					final URI u = (URI) t.getTransferData(VoDataFlavour.LOCAL_URI);
					(new BackgroundWorker(uiParent.get(),getText()) {						
						protected Object construct() throws Exception {
							List l = new ArrayList();
							l.add(u.toString());
							tupp.singleTargetPlasticMessage(VOExplorerFactoryImpl.VORESOURCE_LOAD,l,targetId);
							return null;
						}
					}).start();
				} else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI_ARRAY)) {
					final URI[] u = (URI[]) t.getTransferData(VoDataFlavour.LOCAL_URI_ARRAY);
					(new BackgroundWorker(uiParent.get(),getText()) {
						protected Object construct() throws Exception {
							List l = new ArrayList();
							// marshall the args..
							List us = new ArrayList(u.length);
							l.add(us);
							for (int i = 0; i < u.length; i++) {
								us.add(u[i].toString());
							}
							tupp.singleTargetPlasticMessage(VOExplorerFactoryImpl.VORESOURCE_LOADLIST,l,targetId);
							return null;
						}
					}).start();					
				}
			} catch (UnsupportedFlavorException x) {
				logger.error("UnsupportedFlavorException",x);
			} catch (IOException x) {
				logger.error("IOException",x);
			}
		}
		
		

	}

}
