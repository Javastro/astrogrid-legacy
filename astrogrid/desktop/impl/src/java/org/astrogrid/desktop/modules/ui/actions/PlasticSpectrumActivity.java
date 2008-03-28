/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

import com.l2fprod.common.swing.JLinkButton;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20074:42:56 PM
 */
public class PlasticSpectrumActivity extends AbstractFileActivity {
	/**
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 12, 20074:38:31 PM
     */
    public static class Fallback extends PlasticSpectrumActivity {

        public Fallback(PlasticApplicationDescription plas,
                PlasticScavenger scav) {
            super(plas, scav);
            String title = getText();
            setText("Attempt to " + Character.toLowerCase(title.charAt(0)) + title.substring(1));
        }
        protected boolean invokable(FileObject f) {
            try {
                return ! super.invokable(f) && f.getType().hasContent();
            } catch (FileSystemException x) {
                return false;
            }
        }
        protected boolean invokable(Resource r) {
            return false ; // only ever for files.
        }
        //don't allow invokcation on multiple resources though - too dodgy.
        public void manySelected(FileObject[] list) {
            noneSelected();
        }
        
        // create components but keep them invisible.
        public JLinkButton createLinkButton() {
            JLinkButton b = new JLinkButton(this);
            b.setVisible(false);
            return b;
        }
        public JMenuItem createHidingMenuItem() {
            JMenuItem i = new JMenuItem(this);
            i.setVisible(false);
            return i;
        }

    }

    private final PlasticApplicationDescription plas;
    private final PlasticScavenger scav;
	/**
	 * @param plas
	 * @param tupp
	 */
	public PlasticSpectrumActivity(PlasticApplicationDescription plas,PlasticScavenger scav) {
		super();
		setHelpID("activity.plastic.spectrum");
		this.plas = plas;
        this.scav = scav;
		PlasticScavenger.configureActivity("spectra",this,plas);
	}
	
    /// use a hiding item - so that this and the 'fallback' implementation appear to 
    // be the same item - should never see both at once.
    public JMenuItem createMenuItem() {
        return super.createHidingMenuItem();
    }
	protected boolean invokable(FileObject f) {
		try {
			return VoDataFlavour.MIME_FITS_SPECTRUM.equals(f.getContent().getContentInfo().getContentType());

		} catch (FileSystemException x) {
			return false;
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		final List l = computeInvokable();	
		final int sz = l.size();	
		Runnable r= new Runnable() {

		    public void run() {
		        for (Iterator i = l.iterator(); i.hasNext();) {
		            FileObject f = (FileObject) i.next();
		            while (f instanceof DelegateFileObject) { // if we've got a delegate, get to the source here...
		                f = ((DelegateFileObject)f).getDelegateFile();
		            }			
		            sendLoadSpectrumMessage(f);
		        }	
		    }
		};
        confirmWhenOverThreshold(sz,"Send all " + sz + " files?",r);
	}

	private void sendLoadSpectrumMessage(final FileObject f) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY) {
		    {
		        setTransient(true);
		    }
			protected Object construct() throws Exception {
				List l = new ArrayList();
				URL url = f.getURL();
				l.add(url.toString());// url
				l.add(f.getName().getBaseName());
				Hashtable t = new Hashtable(f.getContent().getAttributes());
				l.add(t);// some kind of map here.
				scav.getTupp().singleTargetPlasticMessage(PlasticScavenger.SPECTRA_LOAD_FROM_URL,l,plas.getId());
				return null;
			}
			protected void doFinished(Object result) {
                parent.showTransientMessage("Message sent to " + plas.getName(),"");	
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
