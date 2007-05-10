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
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.VOExplorerFactoryImpl;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** subclass of activity that presents a 'load resource' plastic button
 *  */
public class PlasticRegistryActivity extends AbstractResourceActivity {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(PlasticRegistryActivity.class);

	private final PlasticApplicationDescription plas;
	private final TupperwareInternal tupp;
	protected boolean invokable(Resource r) {
		return true;
	}
	public PlasticRegistryActivity(final PlasticApplicationDescription descr, TupperwareInternal tupp) {
		super();
		this.plas = descr;
		this.tupp = tupp;
		PlasticScavenger.configureActivity(null,this,plas);
	}


	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		switch(l.size()) {
		case 0:
			return;
		case 1:
			// handle case of only supporting one message. type
			if (plas.understandsMessage(VOExplorerFactoryImpl.VORESOURCE_LOAD)) {
				sendLoadMessage((Resource)l.get(0));
			} else {
				sendLoadListMessage(l);
			}
			return;
		default:
			// likewise - use the best message supported.
			if (plas.understandsMessage(VOExplorerFactoryImpl.VORESOURCE_LOADLIST)) {
				sendLoadListMessage(l);
			} else {
				for (Iterator i = l.iterator(); i.hasNext();) {
					Resource r = (Resource) i.next();					
					sendLoadMessage(r);
				}
			}			
		}

	}
	private void sendLoadMessage(final Resource r) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName()) {						
			protected Object construct() throws Exception {
				List l = new ArrayList();
				l.add(r.getId().toString());
				tupp.singleTargetPlasticMessage(VOExplorerFactoryImpl.VORESOURCE_LOAD,l,plas.getId());
				return null;
			}
			// indicate when hand-off happended.
			protected void doFinished(Object result) {
				parent.setStatusMessage("Message sent to " + plas.getName());
			}
		}).start();
	}

	private void sendLoadListMessage(final List resources) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName()) {
			protected Object construct() throws Exception {
				List l = new ArrayList();
				// marshall the args..
				List us = new ArrayList(resources.size());
				l.add(us);
				for (int i = 0; i < resources.size(); i++) {
					Resource r = (Resource)resources.get(i);
					us.add(r.getId().toString());
				}
				tupp.singleTargetPlasticMessage(VOExplorerFactoryImpl.VORESOURCE_LOADLIST,l,plas.getId());
				return null;
			}
			protected void doFinished(Object result) {
				parent.setStatusMessage("Message sent to " + plas.getName());
			}			
		}).start();					
	}

	
}