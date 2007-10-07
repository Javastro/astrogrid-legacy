package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.VOExplorerFactoryImpl;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;

/** subclass of activity that presents a 'load resource' plastic button
 *  */
public class PlasticRegistryActivity extends AbstractResourceActivity {

	private final PlasticApplicationDescription plas;

    private final PlasticScavenger scav;
	protected boolean invokable(Resource r) {
		return true;
	}
	public PlasticRegistryActivity(final PlasticApplicationDescription descr, PlasticScavenger scav) {
		super();
		this.plas = descr;
        this.scav = scav;
		PlasticScavenger.configureActivity(null,this,plas);
	}


	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
        if (l.size() > UIConstants.LARGE_SELECTION_THRESHOLD && ! confirm("Send all " + l.size() + " resources?" )) {
            return;         
        }		
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
		    {
		        setTransient(true);
		    }
			protected Object construct() throws Exception {
				List l = new ArrayList();
				l.add(r.getId().toString());
				scav.getTupp().singleTargetPlasticMessage(VOExplorerFactoryImpl.VORESOURCE_LOAD,l,plas.getId());
				return null;
			}
			// indicate when hand-off happended.
			protected void doFinished(Object result) {
                parent.showTransientMessage("Message sent to " + plas.getName(),"");		
			}
		}).start();
	}

	private void sendLoadListMessage(final List resources) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName()) {
		    {
		        setTransient(true);
		    }
			protected Object construct() throws Exception {
				List l = new ArrayList();
				// marshall the args..
				List us = new ArrayList(resources.size());
				l.add(us);
				for (int i = 0; i < resources.size(); i++) {
					Resource r = (Resource)resources.get(i);
					us.add(r.getId().toString());
				}
				scav.getTupp().singleTargetPlasticMessage(VOExplorerFactoryImpl.VORESOURCE_LOADLIST,l,plas.getId());
				return null;
			}
			protected void doFinished(Object result) {
                parent.showTransientMessage("Message sent to " + plas.getName(),"");	
			}			
		}).start();					
	}

	
}