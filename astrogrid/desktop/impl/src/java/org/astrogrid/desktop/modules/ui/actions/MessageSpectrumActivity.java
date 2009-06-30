/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.SpectrumMessageSender;
import org.astrogrid.desktop.modules.system.messaging.SpectrumMessageType;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;
import org.astrogrid.samp.Response;

import com.l2fprod.common.swing.JLinkButton;

/** Send a loadSpectrum message for a file (either over SAMP or PLASTIC).
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20074:42:56 PM
 */
public class MessageSpectrumActivity extends AbstractFileActivity {
    /** variant which is invokable on any kind of file <b>not</b> acceptable to the {@code PlasticSpectrumActivity}.
     * only appears on the main menu */
    public static class Fallback extends MessageSpectrumActivity {

        public Fallback(final ExternalMessageTarget plas) {
            super(plas);
            final String title = getText();
            setText("Attempt to " + Character.toLowerCase(title.charAt(0)) + title.substring(1));
        }
        @Override
        protected boolean invokable(final FileObjectView f) {
                return ! super.invokable(f) && f.getType().hasContent();
        }
        protected boolean invokable(final Resource r) {
            return false ; // only ever for files.
        }
        //don't allow invokcation on multiple resources though - too dodgy.
        @Override
        public void manySelected(final FileObjectView[] list) {
            noneSelected();
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

    }

    private final ExternalMessageTarget plas;
	/**
	 * @param plas
	 * @param tupp
	 */
	public MessageSpectrumActivity(final ExternalMessageTarget plas) {
		super();
		setHelpID("activity.plastic.spectrum");
		this.plas = plas;
		MessagingScavenger.configureActivity("spectra",this,plas);
	}
	
    /// use a hiding item - so that this and the 'fallback' implementation appear to 
    // be the same item - should never see both at once.
    @Override
    public JMenuItem createMenuItem() {
        return super.createHidingMenuItem();
    }
	@Override
    protected boolean invokable(final FileObjectView f) {
			return VoDataFlavour.MIME_FITS_SPECTRUM.equals(f.getContentType());

	}
	
	@Override
    public void actionPerformed(final ActionEvent e) {
		final List<FileObjectView> l = computeInvokable();	
		final int sz = l.size();	
		final Runnable r= new Runnable() {

		    public void run() {
		        for (final FileObjectView f: l) {
		            sendLoadSpectrumMessage(f);		            
		        }	
		    }
		};
        confirmWhenOverThreshold(sz,"Send all " + sz + " files?",r);
	}
	/** extended verion, when additional metadata is available */
	private void sendLoadSpectrumMessage(final FileObjectView fv) {
		(new BackgroundWorker<Response>(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY) {
//		    {
//		        setTransient(true);
//		    }
			@Override
            protected Response construct() throws Exception {
				// see if an astroscopeFileObject is present - if so, use this as a source of richer metadata.
			    final FileObject f = fv.getFileObject();
                final AstroscopeFileObject astroscopeFileObject = AstroscopeFileObject.findAstroscopeFileObject(f);
                final FileObject innermost = AstroscopeFileObject.findInnermostFileObject(f);
				final URL url = innermost.getURL();
				final Map t;
				if (astroscopeFileObject != null) {
				    t = new Hashtable(astroscopeFileObject.getNode().getAttributes());
				    t.remove("tooltip"); // load of noise.
				} else {
				    t = new Hashtable(f.getContent().getAttributes());
				}
				final String name = f.getName().getBaseName();
				final SpectrumMessageSender sender = plas.createMessageSender(SpectrumMessageType.instance);
				return sender.sendSpectrum(url,t,null,name);
			}
			@Override
            protected void doFinished(final Response response) {
                if (response.isOK()) {    
                    parent.showTransientMessage(plas.getIcon(),plas.getName() + " received spectra", "");          
            } else {
                parent.showTransientWarning(plas.getName() + ": " + response.getStatus(),response.getErrInfo().getErrortxt());
                logger.warn(response);
            }       			    
			}			
		}).start();		
	}
	

	
	/**
	 * 
    public void focusChanged(FocusEvent arg0) {
        for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
            TreeNode t= (TreeNode)i.next();
            if (t.getAttribute(SsapRetrieval.SPECTRA_URL_ATTRIBUTE) != null) {
                setEnabled(true);
                return;
            }
        }
        setEnabled(false);
    }

	public void actionPerformed(ActionEvent arg0) {
		   (new BackgroundWorker(ui,super.getText()) {        
	            protected Object construct() throws Exception {        
	                for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
	                    final TreeNode tn = (TreeNode)i.next();
	                    // find each leaf node
	                    if (tn.getChildCount() > 0 ) {
	                        continue;
	                    }
	                        URL url = new URL(tn.getAttribute(SsapRetrieval.SPECTRA_URL_ATTRIBUTE));
	                        List args = new ArrayList();
	                        args.add(url.toString()); // plastic expects a string, but we first construct a url to ensure it's valid.
	                        args.add(url.toString()); // use this as the identifier too.
	                        args.add(tn.getAttributes());
	                        tupperware.singleTargetPlasticMessage(
	                                SPECTRA_LOAD_FROM_URL
	                                ,args,targetId);                 
	                }// end for each child node.
	                return null;
	            }
	        }).start();		
	}

	 */
}
