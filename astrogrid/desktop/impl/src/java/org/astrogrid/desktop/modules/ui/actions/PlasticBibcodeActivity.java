package org.astrogrid.desktop.modules.ui.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.Image;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.lang.WordUtils;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Source;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.VOExplorerFactoryImpl;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** subclass of activity that presents a 'find source paper' plastic button
 *  */
public class PlasticBibcodeActivity extends AbstractResourceActivity {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(PlasticBibcodeActivity.class);

	private final PlasticApplicationDescription plas;

    private final PlasticScavenger scav;
	protected boolean invokable(Resource r) {
	Source source = r.getContent().getSource();
	return source != null 
	    && source.getFormat() != null
	    && source.getValue() != null
	    && source.getFormat().equals("bibcode")
	    ;
	}
	
	// doesn't accept multiple selections.
	public void someSelected(Resource[] list) {
	    noneSelected();
	}
	
	
	public PlasticBibcodeActivity(final PlasticApplicationDescription descr, PlasticScavenger scav) {
		super();
		this.plas = descr;
        this.scav = scav;
		PlasticScavenger.configureActivity("bibcode",this,plas);
	}


	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		for (Iterator i = l.iterator(); i.hasNext();) {
            Resource r = (Resource) i.next();
            sendBibcodeMessage(r);
        }
		
	}
	private void sendBibcodeMessage(final Resource r) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName()) {						
			protected Object construct() throws Exception {
				List l = new ArrayList();
				l.add(r.getContent().getSource().getValue());
				scav.getTupp().singleTargetPlasticMessage(VOExplorerFactoryImpl.BIBCODE_MESSAGE,l,plas.getId());
				return null;
			}
			// indicate when hand-off happended.
			protected void doFinished(Object result) {
				parent.setStatusMessage("Sent bibcode to" + plas.getName());
                scav.getSystray().displayInfoMessage("Message sent","sent bibode to " + plas.getName());				
			}
		}).start();
	}

	
}