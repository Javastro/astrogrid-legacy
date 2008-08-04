package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.VOExplorerFactoryImpl;

import com.l2fprod.common.swing.JLinkButton;

/** subclass of activity that presents a 'load resource' plastic button
 * I've modified this so that it only appears on the main menu - as it's advanced, and because of poor resource typeing, not universally applicable.
 *  */
public class PlasticRegistryActivity extends AbstractResourceActivity {

	private final PlasticApplicationDescription plas;

    private final PlasticScavenger scav;
	@Override
    protected boolean invokable(final Resource r) {
		return true;
	}
	public PlasticRegistryActivity(final PlasticApplicationDescription descr, final PlasticScavenger scav) {
		super();
		setHelpID("activity.plastic.resource");
		this.plas = descr;
        this.scav = scav;
		PlasticScavenger.configureActivity("resource descriptions",this,plas);
	}

	  // create components but keep them invisible.
    @Override
    public JLinkButton createLinkButton() {
        final JLinkButton b = new JLinkButton(this);
        b.setVisible(false);
        return b;
    }
    @Override
    public JMenuItem createHidingMenuItem() {
        final JMenuItem i = new JMenuItem(this);
        i.setVisible(false);
        return i;
    }
//	public JMenuItem createMenuItem() {
//	    return super.createHidingMenuItem();
//	}

	@Override
    public void actionPerformed(final ActionEvent e) {
		final List l = computeInvokable();
		final int sz = l.size();
		final Runnable r = new Runnable() {

            public void run() {
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
                            for (final Iterator i = l.iterator(); i.hasNext();) {
                                final Resource r = (Resource) i.next();					
                                sendLoadMessage(r);
                            }
                        }			
                }
            }
		};
		confirmWhenOverThreshold(sz,"Send all " + sz + " resources?",r);
	}
	private void sendLoadMessage(final Resource r) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY) {		
//		    {
//		        setTransient(true);
//		    }
			@Override
            protected Object construct() throws Exception {
				final List l = new ArrayList();
				l.add(r.getId().toString());
				scav.getTupp().singleTargetFireAndForgetMessage(VOExplorerFactoryImpl.VORESOURCE_LOAD,l,plas.getId());
				return null;
			}
			// indicate when hand-off happended.
			@Override
            protected void doFinished(final Object result) {
                parent.showTransientMessage("Message sent to " + plas.getName(),"");		
			}
		}).start();
	}

	private void sendLoadListMessage(final List resources) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY) {
		    {
		        setTransient(true);
		    }
			@Override
            protected Object construct() throws Exception {
				final List l = new ArrayList();
				// marshall the args..
				final List us = new ArrayList(resources.size());
				l.add(us);
				for (int i = 0; i < resources.size(); i++) {
					final Resource r = (Resource)resources.get(i);
					us.add(r.getId().toString());
				}
				scav.getTupp().singleTargetFireAndForgetMessage(VOExplorerFactoryImpl.VORESOURCE_LOADLIST,l,plas.getId());
				return null;
			}
			@Override
            protected void doFinished(final Object result) {
                parent.showTransientMessage("Message sent to " + plas.getName(),"");	
			}			
		}).start();					
	}

	
}