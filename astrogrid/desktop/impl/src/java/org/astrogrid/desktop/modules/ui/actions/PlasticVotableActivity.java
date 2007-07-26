/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.io.Piper;
import org.votech.plastic.CommonMessageConstants;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20074:42:35 PM
 */
public class PlasticVotableActivity extends AbstractFileActivity {
	private final PlasticApplicationDescription plas;
	private final TupperwareInternal tupp;
	/**
	 * @param plas
	 * @param tupp
	 */
	public PlasticVotableActivity(PlasticApplicationDescription plas, TupperwareInternal tupp) {
		super();
		this.plas = plas;
		this.tupp = tupp;
		PlasticScavenger.configureActivity("tables",this,plas);
	}
	protected boolean invokable(FileObject f) {
		try {
			final FileContent content = f.getContent();
            return VoDataFlavour.MIME_VOTABLE.equals(content.getContentInfo().getContentType())
                || content.getAttribute(VoDataFlavour.TABULAR_HINT) != null;
		} catch (FileSystemException x) {
			return false;
		}
	}

	private static final List supportedProtocols = new ArrayList();
	static {
	    supportedProtocols.add("http");
	    supportedProtocols.add("ftp");
	    supportedProtocols.add("file");
	}
	
	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		if (plas.understandsMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL)) {
			for (Iterator i = l.iterator(); i.hasNext();) {
			    try {
				FileObject f = (FileObject) i.next();
				if (supportedProtocols.contains(f.getURL().getProtocol())) {
				    sendLoadVotableURLMessage(f);
				} else {
				    sendLoadVotableInlineMessage(f);
				}
			    } catch (FileSystemException ex) {
			        // oh well. skip that one.
			        //@todo report what went wrong.
			    }
			}
		} else { // fallback
			for (Iterator i = l.iterator(); i.hasNext();) {
				FileObject f = (FileObject) i.next();
				sendLoadVotableInlineMessage(f);
			}			
		}
	}

	private void sendLoadVotableInlineMessage(final FileObject f) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName()) {
			protected Object construct() throws Exception {
				InputStream is = null;
				ByteArrayOutputStream os = null;
				try {
					List l = new ArrayList();
					is = f.getContent().getInputStream();
					os = new ByteArrayOutputStream();
					Piper.pipe(is,os);
					// inline value.
					l.add(os.toString());
					URL url = f.getURL();
					l.add(url.toString()); // identifier.
					tupp.singleTargetPlasticMessage(CommonMessageConstants.VOTABLE_LOAD,l,plas.getId());
					return null;
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
						}
					}
				}
			}
			protected void doFinished(Object result) {
				parent.setStatusMessage("Message sent to " + plas.getName());
			}			
		}).start();		
	}

	private void sendLoadVotableURLMessage(final FileObject f) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName()) {
			protected Object construct() throws Exception {
				List l = new ArrayList();
				URL url = f.getURL();
				l.add(url.toString());// url
				l.add(url.toString()); // identifier - have nothing else to use really.			
				tupp.singleTargetPlasticMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL,l,plas.getId());
				return null;
			}
			protected void doFinished(Object result) {
				parent.setStatusMessage("Message sent to " + plas.getName());
			}			
		}).start();		
	}
	/*
	 * from original send-to
    public void actionPerformed(ActionEvent e) {
        (new BackgroundWorker(ui,super.getText()) {
            private Set processedServices = new HashSet();
            protected Object construct() throws Exception {
                for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
                    final TreeNode tn = (TreeNode)i.next();
                    // find each leaf node
                    if (tn.getChildCount() > 0 ) {
                        continue;
                    }
                    TreeNode service = tn.getParent().getParent().getParent();
                    if (! processedServices.contains(service)) {
                        processedServices.add(service);                       
						URL url = new URL(service.getAttribute(Retriever.SERVICE_URL_ATTRIBUTE));
                        List args = new ArrayList();  
                        args.add(url.toString()); // plastic spec expects parameter types that are strings - but still parse into a url first, to check it's valid.
                        args.add(url.toString()); // identifier.
                        tupperware.singleTargetPlasticMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL,args,targetId);
                    }// end if new catalog
                    //next send plastic messages to highlight selected rows.
                    // hard, as we're not maintining row ids at the moment - might be easier with prefuse beta.
                }// end for each child node
                return null;
            }
        }).start();
    }
	 */

	/* code-snippets for how to do equivalent broadcast.
	 *  enabling condition - 
	 *  tupp.somethingAccepts(CommonMessageConstants.VOTABLE_LOAD)
	 *   check for this along with what the current selection is.
	 * 
	 * sending the message.
	 * 	tupp.broadcastPlasticMessageAsynch(CommonMessageConstants.VOTABLE_LOAD,l);

	 * 
	 */
	
}
