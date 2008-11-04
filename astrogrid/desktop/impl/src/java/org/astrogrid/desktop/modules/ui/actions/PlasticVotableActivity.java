/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.MonitoringInputStream;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;
import org.votech.plastic.CommonMessageConstants;

import com.l2fprod.common.swing.JLinkButton;

/** Send PLASTIC loadVOTable message for a file, or a suitable registry resource
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20074:42:35 PM
 */
public class PlasticVotableActivity extends AbstractFileOrResourceActivity {
    
    /** variant which is invokable on any kind of file <b>not</b> acceptable to the {@PlasticVotableActivity}.
     * only appears on the main menu */
    public static class Fallback extends PlasticVotableActivity  {

        public Fallback(final PlasticApplicationDescription plas, final PlasticScavenger scav) {
            super(plas, scav);
            final String title = getText();
            setText("Attempt to " + Character.toLowerCase(title.charAt(0)) + title.substring(1));
        }
        @Override
        protected boolean invokable(final FileObject f) {
            try {
                return ! super.invokable(f) && f.getType().hasContent();
            } catch (final FileSystemException x) {
                return false;
            }
        }
        @Override
        protected boolean invokable(final Resource r) {
            return false ; // only ever for files.
        }
        //don't allow invokcation on multiple resources though - too dodgy.
        @Override
        public void manySelected(final FileObject[] list) {
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
    
	private final PlasticApplicationDescription plas;
    private final PlasticScavenger scav;
	/**
	 * @param plas
	 * @param tupp
	 */
	public PlasticVotableActivity(final PlasticApplicationDescription plas, final PlasticScavenger scav) {
		super();
		setHelpID("activity.plastic.votable");
		this.plas = plas;
        this.scav = scav;
		PlasticScavenger.configureActivity("tables",this,plas);
	}
	
    /// use a hiding item - so that this and the 'fallback' implementation appear to 
    // be the same item - should never see both at once.
    @Override
    public JMenuItem createMenuItem() {
        return super.createHidingMenuItem();
    }
	
    
	@Override
    protected boolean invokable(final FileObject f) {
		try {
			final FileContent content = f.getContent();
            return VoDataFlavour.MIME_VOTABLE.equals(content.getContentInfo().getContentType());
		} catch (final FileSystemException x) {
			return false;
		}
	}
	

    @Override
    protected boolean invokable(final Resource r) {
        return ConeProtocol.isCdsCatalog(r);
    }

	private static final List supportedProtocols = new ArrayList();
	static {
	    supportedProtocols.add("http");
	    supportedProtocols.add("ftp");
	    supportedProtocols.add("file");
	}
	
	@Override
    public void actionPerformed(final ActionEvent e) {
        final List sources = new ArrayList();
        sources.addAll(computeInvokableResources());
        sources.addAll( computeInvokableFiles());
        confirmWhenOverThreshold(sources.size(),"Send all " + sources.size() + " files?",new Runnable() {
            public void run() {
                doit(sources);
            }
        });
	}
	public void doit(final List sources) {


	    if (plas.understandsMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL)) {
	        for (final Iterator i = sources.iterator(); i.hasNext();) {
	            final Object o = i.next();
	            if (o instanceof FileObject) {                    
	                FileObject f = (FileObject) o;
	                f = AstroscopeFileObject.findInnermostFileObject(f);
	                (new LoadVotableWorker(f)).start();
	                
	            } else if (o instanceof CatalogService) {
	                // very CDS specific
	                final CatalogService vizCatalog = (CatalogService) o;
	                final URI s = SimpleDownloadActivity.findDownloadLinkForCDSResource(vizCatalog);
	                final TableBean[] tables = vizCatalog.getTables();
	                if (tables == null || tables.length == 1) {
	                    (new LoadVotableWorker(s,vizCatalog.getTitle())).start();
	                } else { // more
	                    for (int t = 0; t < tables.length; t++) {
	                        final String tableName = StringUtils.substringAfterLast(tables[t].getName(),"/"); // get trailing part of tablename.
                            try {
                            final URI tURI = new URI(s.toString() + "/" + tableName); // download url is the main url plus the tablename.
                            (new LoadVotableWorker(tURI,vizCatalog.getTitle() + " - " + tableName)).start();                      
                            } catch (final URISyntaxException e) {
                                logger.warn("Failed to construct download link",e);
                            }	                        
	                    }
	                }
	            } else if (o instanceof URI) {
	                final URI u= (URI)o;
	                (new LoadVotableWorker(u,u.toString())).start();                  
	            }						    
	        }
	    } else { // fallback
	        for (final Iterator i = sources.iterator(); i.hasNext();) {			    
	            final Object o = i.next();
	            if (o instanceof FileObject) {
	                (new LoadVotableInlineWorker((FileObject)o)).start();
	                
                } else if (o instanceof CatalogService) {
                    final CatalogService vizCatalog = (CatalogService)o;
                    final URI s = SimpleDownloadActivity.findDownloadLinkForCDSResource(vizCatalog);
                    final TableBean[] tables = vizCatalog.getTables();
                    if (tables == null || tables.length == 1) {
                        (new LoadVotableInlineWorker(s,vizCatalog.getTitle())).start();
                    } else { // more
                        for (int t = 0; t < tables.length; t++) {
                            final String tableName = StringUtils.substringAfterLast(tables[t].getName(),"/"); // get trailing part of tablename.
                            try {
                            final URI tURI = new URI(s.toString() + "/" + tableName); // download url is the main url plus the tablename.
                            (new LoadVotableInlineWorker(tURI,vizCatalog.getTitle() + " - " + tableName)).start();                      
                            } catch (final URISyntaxException e) {
                                logger.warn("Failed to construct download link",e);
                            }                           
                        }
                    }
                    
	            } else if (o instanceof URI) {
	                final URI u = (URI)o;
	                (new LoadVotableInlineWorker(u,u.toString())).start();				    
	            }
	        }			
	    }

	}

	/** background process that sends an inline votable plastic message */
    private class LoadVotableInlineWorker extends BackgroundWorker {
        protected final FileObject fo;
        protected final URI uri;
        protected final String id;
        /**
         * 
         */
        public LoadVotableInlineWorker(final FileObject fo) {
            super(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY);
            this.fo = fo;
            this.id = fo.getName().getBaseName();
            this.uri = null;
            //setTransient(true);
        }
        public LoadVotableInlineWorker(final URI uri, final String id) {
            super(uiParent.get(),"Sending to " + plas.getName(),Thread.MAX_PRIORITY);
            this.uri = uri;
            this.id = id;
            this.fo = null;
            //setTransient(true);
        }   	

			@Override
            protected Object construct() throws Exception {
                logger.debug("Sending inline message");			    
				InputStream is = null;
				try {
					final List l = new ArrayList();
					if (fo != null) {
					    is = MonitoringInputStream.create(this,fo,MonitoringInputStream.ONE_KB * 10);
					} else { // must be auri then.
					    is = MonitoringInputStream.create(this,uri.toURL(),MonitoringInputStream.ONE_KB * 10);
					}
					reportProgress("Opened file");
					final String hopefullyNotVeryBig = IOUtils.toString(is);
					reportProgress("Downloaded file");
					// inline value.
					l.add(hopefullyNotVeryBig);
					//URL url = f.getURL();
					l.add(id); // identifier.					
					scav.getTupp().singleTargetFireAndForgetMessage(CommonMessageConstants.VOTABLE_LOAD,l,plas.getId());
					reportProgress("Sent plastic message");
					return null;
				} finally {
				    closeQuietly(is);
				}
			}
			@Override
            protected void doFinished(final Object result) {
			    parent.showTransientMessage("Message sent","to " + plas.getName());		
			}				
	}

    /** background process that attempts to send a load-votable-bvy-reference message
     * , but will fallback to inlining if it's an odd scheme of url 
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Sep 11, 200711:08:12 AM
     */
	private class LoadVotableWorker extends LoadVotableInlineWorker {

        public LoadVotableWorker(final FileObject fo) {
            super(fo);
        }
        public LoadVotableWorker(final URI uri, final String id) {
            super(uri,id);
        }        

			@Override
            protected Object construct() throws Exception {
			    // first check if it's applicable, and if not fallback.
			    URL url;
			    if (fo != null) {				        
			        url = fo.getURL();
			    } else { // must be a uri then.
			        url = uri.toURL();
			    }
			    reportProgress("Resolved URI");
			    if (! supportedProtocols.contains(url.getProtocol())) {
			        reportProgress("URI is an unsupported protocol - will copy and send");
			        return super.construct(); // fallback.
			    } else {
                    reportProgress("Sending URL message");			        
			        final List l = new ArrayList();
			        l.add(url.toString());// url
			        l.add(id);	
			        scav.getTupp().singleTargetFireAndForgetMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL,l,plas.getId());
			        reportProgress("Plastic message sent");
			        return null;
			    }
			}		
	}	
	
	
	
}
