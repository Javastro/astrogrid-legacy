/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.VotableMessageSender;
import org.astrogrid.desktop.modules.system.messaging.VotableMessageType;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;
import org.astrogrid.samp.Response;

import com.l2fprod.common.swing.JLinkButton;

/** Send a loadVOTable message for a file, or a suitable registry resource (either over SAMP or PLASTIC).
 * @todo rename this class to 'MessageVotableActivity' to fit with other messaging activities.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20074:42:35 PM
 */
public class PlasticVotableActivity extends AbstractFileOrResourceActivity {
    
    /** variant which is invokable on any kind of file <b>not</b> acceptable to the {@PlasticVotableActivity}.
     * only appears on the main menu */
    public static class Fallback extends PlasticVotableActivity  {

        public Fallback(final ExternalMessageTarget plas) {
            super(plas);
            final String title = getText();
            setText("Attempt to " + Character.toLowerCase(title.charAt(0)) + title.substring(1));
        }
        @Override
        protected boolean invokable(final FileObjectView f) {
                return ! super.invokable(f) && f.getType().hasContent();
        }
        @Override
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
    
	private final ExternalMessageTarget target;
	/**
	 * @param plas
	 * @param tupp
	 */
	public PlasticVotableActivity(final ExternalMessageTarget plas) {
		super();
		setHelpID("activity.plastic.votable");
		this.target = plas;
		MessagingScavenger.configureActivity("tables",this, target);
	}
	
    /// use a hiding item - so that this and the 'fallback' implementation appear to 
    // be the same item - should never see both at once.
    @Override
    public JMenuItem createMenuItem() {
        return super.createHidingMenuItem();
    }
	
    
	@Override
    protected boolean invokable(final FileObjectView f) {
            return VoDataFlavour.MIME_VOTABLE.equals(f.getContentType());
	}
	

    @Override
    protected boolean invokable(final Resource r) {
        return ConeProtocol.isCdsCatalog(r);
    }

//	private static final List supportedProtocols = new ArrayList();
//	static {
//	    supportedProtocols.add("http");
//	    supportedProtocols.add("ftp");
//	    supportedProtocols.add("file");
//	}
	
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


		        for (final Iterator i = sources.iterator(); i.hasNext();) {
	            final Object o = i.next();
	            if (o instanceof FileObjectView) {                    
	                final FileObjectView f = (FileObjectView) o;
	                (new LoadVotableWorker(f,f.getBasename())).start();
	                
	            } else if (o instanceof CatalogService) {
	                // very CDS specific
	                final CatalogService vizCatalog = (CatalogService) o;
	                final URI s = SimpleDownloadActivity.findDownloadLinkForCDSResource(vizCatalog);
	                final TableBean[] tables = vizCatalog.getTables();
	                if (tables == null || tables.length == 1) {
	                    (new LoadVotableWorker(s,vizCatalog.getTitle())).start();
	                } else { // more than 1 table.
	                    for (int t = 0; t < tables.length; t++) {
	                        final String tableName = StringUtils.substringAfterLast(tables[t].getName(),"/"); // get trailing part of tablename.
                            try {
                            final URI tURI = new URI(s.toString() + "/" + tableName); // download url is the main url plus the tablename.
                            final LoadVotableWorker worker = new LoadVotableWorker(tURI,vizCatalog.getTitle() + " - " + tableName);
                            if (t < tables.length -1) { // if thre's more than 1 table, only report success for the last one
                                // otherwise we get a flood of popup messages.
                                worker.setReportSuccess(false);
                            }
                            worker.start();                      
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

	}


    /** background process that attempts to send a load-votable-bvy-reference message
     * , but will fallback to inlining if it's an odd scheme of url 
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Sep 11, 200711:08:12 AM
     */
	private class LoadVotableWorker extends BackgroundWorker<Response>{
        protected final FileObjectView fv;
        protected final URI uri;
        protected final String id;
        private boolean reportSuccess = true;
        public LoadVotableWorker(final FileObjectView fv, final String name) {
            super(uiParent.get(),"Sending table " + name +  " to " + target.getName(),Thread.MAX_PRIORITY);
            this.fv = fv;
            this.id = name;
            this.uri = null;
            //setTransient(true);
        }
        /**
         * @param b
         */
        public void setReportSuccess(final boolean b) {
            reportSuccess = b;
        }
        public LoadVotableWorker(final URI uri, final String id) {
            super(uiParent.get(),"Sending table to " + target.getName(),Thread.MAX_PRIORITY);
            this.uri = uri;
            this.id = id;
            this.fv = null;
            //setTransient(true);
        }             

			@Override
            protected Response construct() throws Exception {
			    // first check if it's applicable, and if not fallback.
			    URL url;
			    if (fv != null) {				        
			        url = AstroscopeFileObject.findInnermostFileObject(fv.getFileObject()).getURL();
			    } else { // must be a uri then.
			        url = uri.toURL();
			    }
			    //reportProgress("Resolved URI");
                    reportProgress("Sending message");	
                    final VotableMessageSender sender = target.createMessageSender(VotableMessageType.instance);                    
                    return sender.sendVotable(url,null,id);
			    
			}
            @Override
            protected void doFinished(final Response response) {
                if (response.isOK()) {     
                    if (reportSuccess) {
                        parent.showTransientMessage(target.getIcon(),target.getName() + " received table", "");
                    }
                } else {
                    parent.showTransientWarning(target.getName() + ": " + response.getStatus(),response.getErrInfo().getErrortxt());
                    logger.warn(response);
                }
            }
	}	
	
	
	
}
