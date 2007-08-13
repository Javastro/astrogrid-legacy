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

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.SsapRetrieval;
import org.votech.plastic.CommonMessageConstants;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20074:42:56 PM
 */
public class PlasticSpectrumActivity extends AbstractFileActivity {
	private final PlasticApplicationDescription plas;
	private final TupperwareInternal tupp;
	/**
	 * @param plas
	 * @param tupp
	 */
	public PlasticSpectrumActivity(PlasticApplicationDescription plas, TupperwareInternal tupp) {
		super();
		this.plas = plas;
		this.tupp = tupp;
		PlasticScavenger.configureActivity("spectra",this,plas);
	}
	protected boolean invokable(FileObject f) {
		try {
			return VoDataFlavour.MIME_FITS_SPECTRUM.equals(f.getContent().getContentInfo().getContentType());
		} catch (FileSystemException x) {
			return false;
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();		
		for (Iterator i = l.iterator(); i.hasNext();) {
			FileObject f = (FileObject) i.next();
            if (f instanceof DelegateFileObject) { // if we've got a delegate, get to the source here...
                f = ((DelegateFileObject)f).getDelegateFile();
            }			
			sendLoadSpectrumMessage(f);
		}	
	}

	private void sendLoadSpectrumMessage(final FileObject f) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName()) {
			protected Object construct() throws Exception {
				List l = new ArrayList();
				URL url = f.getURL();
				l.add(url.toString());// url
				l.add(f.getName().getBaseName());
				Hashtable t = new Hashtable(f.getContent().getAttributes());
				l.add(t);// some kind of map here.
				tupp.singleTargetPlasticMessage(PlasticScavenger.SPECTRA_LOAD_FROM_URL,l,plas.getId());
				return null;
			}
			protected void doFinished(Object result) {
				parent.setStatusMessage("Message sent to " + plas.getName());
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
