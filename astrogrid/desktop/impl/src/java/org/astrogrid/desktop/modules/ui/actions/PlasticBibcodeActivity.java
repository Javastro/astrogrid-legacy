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

/** subclass of activity that presents a 'find source paper' plastic button
 *  */
public class PlasticBibcodeActivity extends AbstractResourceActivity {
	private final PlasticApplicationDescription plas;

    private final PlasticScavenger scav;
	@Override
    protected boolean invokable(final Resource r) {
	final Source source = r.getContent().getSource();
	return source != null 
	    && source.getFormat() != null
	    && source.getValue() != null
	    && source.getFormat().equals("bibcode")
	    ;
	}
	
	// doesn't accept multiple selections.
	@Override
    public void someSelected(final Resource[] list) {
	    noneSelected();
	}
	
	
	public PlasticBibcodeActivity(final PlasticApplicationDescription descr, final PlasticScavenger scav) {
		super();
		setHelpID("activity.plastic.bibcode");
		this.plas = descr;
        this.scav = scav;
		PlasticScavenger.configureActivity("bibcode",this,plas);
	}


	@Override
    public void actionPerformed(final ActionEvent e) {
		final List l = computeInvokable();
        final Runnable act= new Runnable() {

            public void run() {
		for (final Iterator i = l.iterator(); i.hasNext();) {
            final Resource r = (Resource) i.next();
            sendBibcodeMessage(r);
        }
            }
        };
        final int sz = l.size();
        confirmWhenOverThreshold(sz,"Send all " + " resources?",act);	
	}
	private void sendBibcodeMessage(final Resource r) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY) {						
			@Override
            protected Object construct() throws Exception {
				final List l = new ArrayList();
				l.add(r.getContent().getSource().getValue());
				scav.getTupp().singleTargetFireAndForgetMessage(VOExplorerFactoryImpl.BIBCODE_MESSAGE,l,plas.getId());
				return null;
			}
			// indicate when hand-off happended.
			@Override
            protected void doFinished(final Object result) {
			    parent.showTransientMessage("Message sent","Sent bibode to " + plas.getName());				
			}
			
		}).start();
	}

	
}