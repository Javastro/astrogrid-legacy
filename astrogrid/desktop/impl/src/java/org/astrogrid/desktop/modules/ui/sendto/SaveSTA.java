package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.io.Piper;


/** consumer that saves to external space */
public class SaveSTA extends AbstractSTA {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(SaveSTA.class);

	public SaveSTA(ResourceChooserInternal chooser, MyspaceInternal vos) {
		super("Save","Save to local or remote storage","fileexport.png");
		this.chooser = chooser;
		this.vos = vos;
	}

	private final ResourceChooserInternal chooser;
	private final MyspaceInternal vos;
	
	protected boolean checkApplicability(PreferredTransferable atom) {
		return true; // always appliable.
	}

    public void actionPerformed(ActionEvent e) {

            final URI target =  chooser.chooseResourceWithParent("Choose location to save data",true, true, true,getParent().getFrame());
            if (target == null) {
                return;
            }    
            (new BackgroundWorker(getParent(),"Writing Data") {
                
                protected Object construct() throws Exception {
                	DataFlavor preferred = getAtom().getPreferredDataFlavor();
                	OutputStream w=  null;
                	InputStream r = null;
                	try {
                	if (preferred.equals(VoDataFlavour.IVORN)) { // origianl is in myspace
                		URI source = (URI)getAtom().getTransferData(VoDataFlavour.IVORN);
                		if (target.getScheme().equals("ivo")) {
                			vos.copyURLToContent(vos.getReadContentURL(source),target);
                		} else {
                			vos.copyContentToURL(source,target.toURL());
                		}
                	} else if (preferred.equals(VoDataFlavour.URL)) { // original is elsewhere remote
                		URL source = (URL)getAtom().getTransferData(VoDataFlavour.URL);
                		if (target.getScheme().equals("ivo")) {
                			vos.copyURLToContent(source,target);
                		} else {
                			// get the stream - to copy url to url.
                			r = source.openStream();
                		}
                	} else { // original is local.
                		if (getAtom().isDataFlavorSupported(VoDataFlavour.PLAIN_STRING)) {
                			String s= (String)getAtom().getTransferData(VoDataFlavour.PLAIN_STRING);
                			r = new ByteArrayInputStream(s.getBytes());
                		} else {
                			r = (InputStream)getAtom().getTransferData(VoDataFlavour.PLAIN);
                		}
                	}
                	if (r != null) { // we need to do the copy ourselves
                		w=  vos.getOutputStream(target);
                		Piper.pipe(r,w);
                	}
                	} finally {
                        if (w != null) {
                            try {
                                w.close();
                            } catch (IOException e) {
                                 logger.warn("error closing write stream",e);
                            }
                        } 
                        if (r != null) {
                            try {
                                r.close();
                            } catch (IOException e) {
                                 logger.warn("error closing read stream",e);
                            }
                        }                         
                    }
                    return null;     
                }    
            }).start();
            
        }
}