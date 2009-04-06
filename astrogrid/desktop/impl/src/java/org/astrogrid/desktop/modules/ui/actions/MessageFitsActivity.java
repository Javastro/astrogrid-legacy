/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;

import javax.swing.JMenuItem;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.FitsImageMessageSender;
import org.astrogrid.desktop.modules.system.messaging.FitsImageMessageType;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;
import org.astrogrid.samp.Response;

import com.l2fprod.common.swing.JLinkButton;

/** Send a PLASTIC loadFits message for a file.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20074:42:47 PM
 */
public class MessageFitsActivity extends AbstractFileActivity {
    /** variant which is invokable on any kind of file <b>not</b> acceptable to {@code PlasticFitsActivity}.
     * only appears on the main menu */
    public static class Fallback extends MessageFitsActivity {

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
	public MessageFitsActivity(final ExternalMessageTarget plas) {
		super();
		setHelpID("activity.plastic.fits");
		this.plas = plas;
		MessagingScavenger.configureActivity("FITS",this,plas);
	}

    /// use a hiding item - so that this and the 'fallback' implementation appear to 
    // be the same item - should never see both at once.
    @Override
    public JMenuItem createMenuItem() {
        return super.createHidingMenuItem();
    }
	@Override
    protected boolean invokable(final FileObjectView f) {
            final String contentType = f.getContentType();
            return VoDataFlavour.MIME_FITS_IMAGE.equals(contentType)
                || VoDataFlavour.MIME_FITS_TABLE.equals(contentType)
                || VoDataFlavour.MIME_FITS_SPECTRUM.equals(contentType);

	}

	@Override
    public void actionPerformed(final ActionEvent e) {
	    final List<FileObjectView> l = computeInvokable();
	    final Runnable r = new Runnable() {

	        public void run() {
	            for (final FileObjectView f : l) {	                
	                sendLoadImageMessage(f,f.getBasename());
	            }
	        }
	    };
	    final int sz = l.size();
	    confirmWhenOverThreshold(sz,"Sent all " + sz + " files?",r);
	}

	private void sendLoadImageMessage(final FileObjectView fv,final String name) {
		(new BackgroundWorker<Response>(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY) {
//		    {
//		        setTransient(true);
//		    }
			@Override
            protected Response construct() throws Exception {
			    final FileObject f = AstroscopeFileObject.findInnermostFileObject(fv.getFileObject());
				final URL url = f.getURL();
				final FitsImageMessageSender sender = plas.createMessageSender(FitsImageMessageType.instance);
				return sender.sendFitsImage(url,null,name);
			}
			@Override
            protected void doFinished(final Response response) {
                if (response.isOK()) {    
                        parent.showTransientMessage(plas.getIcon(),plas.getName() + " received image", "");          
                } else {
                    parent.showTransientWarning(plas.getName() + ": " + response.getStatus(),response.getErrInfo().getErrortxt());
                    logger.warn(response);
                }			    
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
