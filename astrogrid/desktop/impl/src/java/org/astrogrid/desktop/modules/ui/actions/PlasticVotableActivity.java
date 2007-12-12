/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.MonitoringInputStream;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;
import org.astrogrid.io.Piper;
import org.votech.plastic.CommonMessageConstants;

import com.l2fprod.common.swing.JLinkButton;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20074:42:35 PM
 */
public class PlasticVotableActivity extends AbstractFileOrResourceActivity {
    
    /** variant which is invokable  on any kind of file not accepted by default implementation, only appears on the main menu */
    public static class Fallback extends PlasticVotableActivity  {

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
	public PlasticVotableActivity(PlasticApplicationDescription plas, PlasticScavenger scav) {
		super();
		this.plas = plas;
        this.scav = scav;
		PlasticScavenger.configureActivity("tables",this,plas);
	}
	
    /// use a hiding item - so that this and the 'fallback' implementation appear to 
    // be the same item - should never see both at once.
    public JMenuItem createMenuItem() {
        return super.createHidingMenuItem();
    }
	
    
	protected boolean invokable(FileObject f) {
		try {
			final FileContent content = f.getContent();
            return VoDataFlavour.MIME_VOTABLE.equals(content.getContentInfo().getContentType());
		} catch (FileSystemException x) {
			return false;
		}
	}
	

    protected boolean invokable(Resource r) {
        return ConeProtocol.isCdsCatalog(r);
    }

	private static final List supportedProtocols = new ArrayList();
	static {
	    supportedProtocols.add("http");
	    supportedProtocols.add("ftp");
	    supportedProtocols.add("file");
	}
	
	public void actionPerformed(ActionEvent e) {
        final List resources = computeInvokableResources();
        final List files = new ArrayList();
        int sz = resources.size() + files.size(); 
        confirmWhenOverThreshold(sz,"Send all " + sz + " files?",new Runnable() {
            public void run() {
                doit(resources,files);
            }
        });
	}
	public void doit(List resources, List files) {
        if (resources.size() > 0) {
            // very CDS-specific at tge moment
            for (Iterator i = resources.iterator(); i.hasNext();) {
                CatalogService vizCatalog = (CatalogService) i.next();
                URI s = SimpleDownloadActivity.findDownloadLinkForCDSResource(vizCatalog);
                if (s != null) {
                    files.add(s);
                }
            }               
        }
        // add in any selected files (it's an and/or thing really, but makes the code clearer)
	    files.addAll(computeInvokableFiles());

	    if (plas.understandsMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL)) {
	        for (Iterator i = files.iterator(); i.hasNext();) {
	            Object o = i.next();
	            if (o instanceof FileObject) {                    
	                FileObject f = (FileObject) o;
	                while (f instanceof DelegateFileObject) { // if we've got a delegate, get to the source here...
	                    f = ((DelegateFileObject)f).getDelegateFile();
	                }
	                (new LoadVotableWorker(f)).start();
	            } else if (o instanceof URI) {
	                (new LoadVotableWorker((URI)o)).start();                  
	            }						    
	        }
	    } else { // fallback
	        for (Iterator i = files.iterator(); i.hasNext();) {			    
	            Object o = i.next();
	            if (o instanceof FileObject) {
	                (new LoadVotableInlineWorker((FileObject)o)).start();
	            } else if (o instanceof URI) {
	                (new LoadVotableInlineWorker((URI)o)).start();				    
	            }
	        }			
	    }

	}

	/** background process that sends an inline votable plastic message */
    private class LoadVotableInlineWorker extends BackgroundWorker {
        protected FileObject fo;
        protected URI uri;
        /**
         * 
         */
        public LoadVotableInlineWorker(FileObject fo) {
            super(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY);
            this.fo = fo;
            setTransient(true);
        }
        public LoadVotableInlineWorker(URI uri) {
            super(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY);
            this.uri = uri;
            setTransient(true);
        }   	

			protected Object construct() throws Exception {
                logger.debug("Sending inline message");			    
				InputStream is = null;
				try {
					List l = new ArrayList();
					String id;
					if (fo != null) {
					    is = MonitoringInputStream.create(this,fo,MonitoringInputStream.ONE_KB * 10);
					    id = fo.getName().getBaseName();
					} else { // must be auri then.
					    is = MonitoringInputStream.create(this,uri.toURL(),MonitoringInputStream.ONE_KB * 10);
					    id = uri.toString();
					}
					reportProgress("Opened file");
					String hopefullyNotVeryBig = IOUtils.toString(is);
					reportProgress("Downloaded file");
					// inline value.
					l.add(hopefullyNotVeryBig);
					//URL url = f.getURL();
					l.add(id); // identifier.					
					scav.getTupp().singleTargetPlasticMessage(CommonMessageConstants.VOTABLE_LOAD,l,plas.getId());
					reportProgress("Sent plastic message");
					return null;
				} finally {
				    IOUtils.closeQuietly(is);
				}
			}
			protected void doFinished(Object result) {
			    parent.showTransientMessage("Message sent","to " + plas.getName());		
			}				
	}

    /** background process that attempts to send a load-votable-bvy-reference message
     * , but will fallback to inlining if it's an odd scheme of url 
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Sep 11, 200711:08:12 AM
     */
	private class LoadVotableWorker extends LoadVotableInlineWorker {

        public LoadVotableWorker(FileObject fo) {
            super(fo);
        }
        public LoadVotableWorker(URI uri) {
            super(uri);
        }        

			protected Object construct() throws Exception {
			    // first check if it's applicable, and if not fallback.
			    URL url;
			    String id;
			    if (fo != null) {				        
			        url = fo.getURL();
			        id = fo.getName().getBaseName();
			    } else { // must be a uri then.
			        url = uri.toURL();
			        id = uri.toString();
			    }
			    reportProgress("Resolved URI");
			    if (! supportedProtocols.contains(url.getProtocol())) {
			        reportProgress("URI is an unsupported protocol - will copy and send");
			        return super.construct(); // fallback.
			    } else {
                    reportProgress("Sending URL message");			        
			        List l = new ArrayList();
			        l.add(url.toString());// url
			        l.add(id);	
			        scav.getTupp().singleTargetPlasticMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL,l,plas.getId());
			        reportProgress("Plastic message sent");
			        return null;
			    }
			}		
	}	
	
	
	
}
