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

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;
import org.astrogrid.io.Piper;
import org.votech.plastic.CommonMessageConstants;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20074:42:35 PM
 */
public class PlasticVotableActivity extends AbstractFileOrResourceActivity {
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
        List resources = computeInvokableResources();
        List files = new ArrayList();
        int sz = resources.size() + files.size();
        if (sz > UIConstants.LARGE_SELECTION_THRESHOLD && ! confirm("Send all " + sz + " files?" )) {
            return;         
        }        
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
	                if (f instanceof DelegateFileObject) { // if we've got a delegate, get to the source here...
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
            super(uiParent.get(),"Sending to " + plas.getName());
            this.fo = fo;
            setTransient(true);
        }
        public LoadVotableInlineWorker(URI uri) {
            super(uiParent.get(),"Sending to " + plas.getName());
            this.uri = uri;
            setTransient(true);
        }   	

			protected Object construct() throws Exception {
                logger.debug("Sending inline message");			    
				InputStream is = null;
				ByteArrayOutputStream os = null;
				try {
					List l = new ArrayList();
					String id;
					if (fo != null) {
					    is = fo.getContent().getInputStream();
					    id = fo.getName().getBaseName();
					} else { // must be auri then.
					    is = uri.toURL().openStream();
					    id = uri.toString();
					}
					os = new ByteArrayOutputStream();
					Piper.pipe(is,os);
					// inline value.
					l.add(os.toString());
					//URL url = f.getURL();
					l.add(id); // identifier.
					scav.getTupp().singleTargetPlasticMessage(CommonMessageConstants.VOTABLE_LOAD,l,plas.getId());
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
			    if (! supportedProtocols.contains(url.getProtocol())) {
			        return super.construct(); // fallback.
			    } else {
                    logger.debug("Sending URL message");			        
			        List l = new ArrayList();
			        l.add(url.toString());// url
			        l.add(id);	
			        scav.getTupp().singleTargetPlasticMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL,l,plas.getId());
			        return null;
			    }
			}		
	}	
	
	
	
}
