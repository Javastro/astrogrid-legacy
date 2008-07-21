/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;
import org.votech.plastic.CommonMessageConstants;

import com.l2fprod.common.swing.JLinkButton;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20074:42:47 PM
 */
public class PlasticFitsActivity extends AbstractFileActivity {
    /** variant which is invokable  on any kind of file not accepted by default implementation, only appears on the main menu */
    
    public static class Fallback extends PlasticFitsActivity {

    public Fallback(PlasticApplicationDescription plas, PlasticScavenger scav) {
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
	public PlasticFitsActivity(PlasticApplicationDescription plas, PlasticScavenger scav) {
		super();
		setHelpID("activity.plastic.fits");
		this.plas = plas;
        this.scav = scav;
		PlasticScavenger.configureActivity("FITS",this,plas);
	}

    /// use a hiding item - so that this and the 'fallback' implementation appear to 
    // be the same item - should never see both at once.
    public JMenuItem createMenuItem() {
        return super.createHidingMenuItem();
    }
	protected boolean invokable(FileObject f) {
		try {
			final FileContent content = f.getContent();
            final String contentType = content.getContentInfo().getContentType();
            return VoDataFlavour.MIME_FITS_IMAGE.equals(contentType)
                || VoDataFlavour.MIME_FITS_TABLE.equals(contentType)
                || VoDataFlavour.MIME_FITS_SPECTRUM.equals(contentType);
		} catch (FileSystemException x) {
			return false;
		}
	}

	public void actionPerformed(ActionEvent e) {
	    final List l = computeInvokable();
	    Runnable r = new Runnable() {

	        public void run() {
	            for (Iterator i = l.iterator(); i.hasNext();) {
	                FileObject f = (FileObject) i.next();
	                f = AstroscopeFileObject.findInnermostFileObject(f);
	                sendLoadImageMessage(f);
	            }
	        }
	    };
	    int sz = l.size();
	    confirmWhenOverThreshold(sz,"Sent all " + sz + " files?",r);
	}

	private void sendLoadImageMessage(final FileObject f) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY) {
//		    {
//		        setTransient(true);
//		    }
			protected Object construct() throws Exception {
				List l = new ArrayList();
				
				URL url = f.getURL();
				l.add(url.toString());
				scav.getTupp().singleTargetPlasticMessage(CommonMessageConstants.FITS_LOAD_FROM_URL,l,plas.getId());
				return null;
			}
			protected void doFinished(Object result) {
			    parent.showTransientMessage("Message sent to " + plas.getName(),"");
			}			
		}).start();		
	}

	/*
	 * 
    public void focusChanged(FocusEvent arg0) {
        for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
            TreeNode t= (TreeNode)i.next();
            if (t.getAttribute(SiapRetrieval.IMAGE_URL_ATTRIBUTE) != null) {
                setEnabled(true);
                return;
            }
        }
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        (new BackgroundWorker(ui,super.getText()) {        
            protected Object construct() throws Exception {        
                for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
                    final TreeNode tn = (TreeNode)i.next();
                    // find each leaf node
                    if (tn.getChildCount() > 0 ) {
                        continue;
                    }
                        URL url = new URL(tn.getAttribute(SiapRetrieval.IMAGE_URL_ATTRIBUTE));
                        List args = new ArrayList();
                        args.add(url.toString()); // plastic expects a string, but we first construct a url to ensure it's valid.
                        tupperware.singleTargetPlasticMessage(
                                CommonMessageConstants.FITS_LOAD_FROM_URL
                                ,args,targetId);
                   }// end for each child node.
                return null;
            }
        }).start();
    }        
    

	 */
}
