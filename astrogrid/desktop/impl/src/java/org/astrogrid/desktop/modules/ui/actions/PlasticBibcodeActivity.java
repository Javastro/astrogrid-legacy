package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Source;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.VOExplorerFactoryImpl;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;

/** subclass of activity that presents a 'find source paper' plastic button
 *  */
public class PlasticBibcodeActivity extends AbstractResourceActivity {
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
		final List l = computeInvokable();
        Runnable act= new Runnable() {

            public void run() {
		for (Iterator i = l.iterator(); i.hasNext();) {
            Resource r = (Resource) i.next();
            sendBibcodeMessage(r);
        }
            }
        };
        int sz = l.size();
        confirmWhenOverThreshold(sz,"Send all " + " resources?",act);	
	}
	private void sendBibcodeMessage(final Resource r) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY) {						
			protected Object construct() throws Exception {
				List l = new ArrayList();
				l.add(r.getContent().getSource().getValue());
				scav.getTupp().singleTargetPlasticMessage(VOExplorerFactoryImpl.BIBCODE_MESSAGE,l,plas.getId());
				return null;
			}
			// indicate when hand-off happended.
			protected void doFinished(Object result) {
			    parent.showTransientMessage("Message sent","Sent bibode to " + plas.getName());				
			}
			
		}).start();
	}

	
}