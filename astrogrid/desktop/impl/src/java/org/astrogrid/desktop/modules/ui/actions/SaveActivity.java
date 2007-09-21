/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.scope.Retriever;
import org.astrogrid.desktop.modules.ui.scope.SiapRetrieval;
import org.astrogrid.desktop.modules.ui.scope.SsapRetrieval;
import org.astrogrid.io.Piper;

import edu.berkeley.guir.prefuse.graph.TreeNode;

/** Save selection to local disk
 * @author Noel.Winstanley@manchester.ac.uk
 * @todo later rewrite to use VFS
 * @since May 10, 20077:51:45 PM
 */
public class SaveActivity extends AbstractFileActivity {

	
	private final ResourceChooserInternal chooser;
	private final MyspaceInternal myspace;
	
	public SaveActivity(final ResourceChooserInternal chooser, final MyspaceInternal ms) {
		super();
		this.chooser = chooser;
		this.myspace = ms;
		setText("Save");
		setToolTipText("Save the selected objects");
		setIcon(IconHelper.loadIcon("filesave16.png"));		
	}

	protected boolean invokable(FileObject f) {
		return true;
	}
	
	// unimplemented for now.
	
	public void actionPerformed1(ActionEvent e) {
		Component comp = null;
		if (e.getSource() instanceof Component) {
			comp = (Component)e.getSource();
		}	
		final UIComponent ui = uiParent.get();
		final List files = computeInvokable();
        final URI saveLocationRoot = chooser.chooseDirectoryWithParent("Save Data",true,true,true,ui.getFrame());
        if (saveLocationRoot == null) {
            return;
        }
			(new BackgroundWorker(ui,"Saving Data") {
			     protected Object construct() throws Exception {
		                //make sure they chose a directory if not then return.
		                if("file".equals(saveLocationRoot.getScheme()) && !new File(saveLocationRoot).isDirectory()
		                    || "ivo".equals(saveLocationRoot.getScheme()) &&   ! myspace.getNodeInformation(saveLocationRoot).isFolder() ) {
		                    SwingUtilities.invokeLater(
		                            new Runnable() {
		                                public void run() {
		                                    JOptionPane.showMessageDialog(ui.getFrame(), 
		                                            "You can only save data to a folder, nothing was saved.",
		                                            "Selection is not a folder", JOptionPane.OK_OPTION );
		                                }
		                            });                
		                    return null;                                       
		                }                             
		                for (Iterator i = files.iterator(); i.hasNext(); ) {
		                    final FileObject tn = (FileObject)i.next();
		                    //@fixme - implement using vfs.
		                    
		                    /*TreeNode point = tn.getParent();      
		                    TreeNode catalog = point.getParent().getParent();
		                    try {
		                        // ok. got a bit of selected data. if it's an image, save it.
		                        if (tn.getAttribute(SiapRetrieval.IMAGE_URL_ATTRIBUTE) != null) {
		                            saveSiapImage(saveLocationRoot, tn, point, catalog);
		                        }
		                        if (tn.getAttribute(SsapRetrieval.SPECTRA_URL_ATTRIBUTE) != null) {
		                            saveSsapSpectra(saveLocationRoot,tn,point,catalog);
		                        }
		                        
		                        // save the parent document - either a cone, or the siap / ssap response - which describes all the images.                        
		                        // parent holds the url for this catalog. - 
		                        // check we've not processed this catalog already - because the user has selected another child node..
		                        if (! processedServices.contains(catalog)) { 
		                            saveCatalog(saveLocationRoot, catalog);
		                        }
		                        
		                    } catch (final Exception e) {
		                        SwingUtilities.invokeLater(
		                                new Runnable() {
		                                    public void run() {
		                                        ui.setStatusMessage("Failed to save " + tn.getName().getBaseName()+ " : " + e.getMessage());
		                                    }
		                                }
		                        );                                 
		                    }*/
		                }  
		                
		                return null;
		            }
		            
			         protected void doFinished(Object result) {
			            parent.showTransientMessage("Saved files","");
			        }


		            /**
		             * @param saveLocationRoot
		             * @param processedServices
		             * @param catalog
		             * @throws MalformedURLException
		             * @throws URISyntaxException
		             * @throws NotFoundException
		             * @throws InvalidArgumentException
		             * @throws ServiceException
		             * @throws SecurityException
		             * @throws NotApplicableException
		             * @throws IOException
		             */
		            private void saveCatalog(final URI saveLocationRoot, TreeNode catalog) throws MalformedURLException, URISyntaxException, NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException, IOException {
		                InputStream is = null;
		                OutputStream os = null;
		                
		             //   processedServices.add(catalog); // record that we've done this catalog now.
		                URL url = new URL(catalog.getAttribute(Retriever.SERVICE_URL_ATTRIBUTE));
		                final String name = conformToMyspaceName(catalog.getAttribute(Retriever.LABEL_ATTRIBUTE) + ".vot");
		                URI finalURI = new URI(saveLocationRoot.toString() +"/" + name);
		                SwingUtilities.invokeLater(
		                        new Runnable() {
		                            public void run() {
		                                ui.setStatusMessage("Saving Table: " + name);
		                            }
		                        }
		                );                                 
		                if(finalURI.getScheme().startsWith("ivo")) {
		                    myspace.copyURLToContent(url,finalURI);
		                }else {
		                    try {
		                        is = url.openStream();
		                        os = myspace.getOutputStream(finalURI);
		                        Piper.pipe(url.openStream(), os);
		                    }finally {
		                        if(is != null)
		                            is.close();
		                        if(os != null)
		                            os.close();
		                    }//try&finally
		                }//else
		            }           
		            //@fixme move this file-naming code into the prefuse tree instead.
		            /**Save a siap image.
		             * @param saveLocationRoot root location (direcotry) to save results to
		             * @param tn the tree node to save (must be a siap image node).
		             * @param point the parent of tn - node that contains an offset
		             * @param catalog the parent of point - a catalog node.
		             */
		            private void saveSiapImage(final URI saveLocationRoot, final TreeNode tn, TreeNode point, TreeNode catalog) throws Exception {
		                URL url = new URL( tn.getAttribute(SiapRetrieval.IMAGE_URL_ATTRIBUTE));
		                //@todo make a nicer name here.
		                String name = catalog.getAttribute(Retriever.LABEL_ATTRIBUTE) 
		                        + "_" + point.getAttribute(Retriever.LABEL_ATTRIBUTE) 
		                        +"_" + tn.getAttribute(Retriever.LABEL_ATTRIBUTE) 
		                        + "_" + System.currentTimeMillis() 
		                        + "." + StringUtils.substringAfterLast(tn.getAttribute(SiapRetrieval.IMAGE_TYPE_ATTRIBUTE),"/");

		                doSaveResource(saveLocationRoot, url, name);                       
		            }
		 
		            private void saveSsapSpectra(URI saveLocationRoot, TreeNode tn, TreeNode point, TreeNode catalog) throws Exception{
		                URL url = new URL(tn.getAttribute(SsapRetrieval.SPECTRA_URL_ATTRIBUTE));
		                String name = catalog.getAttribute(Retriever.LABEL_ATTRIBUTE)
		                    + "_" + point.getAttribute(Retriever.LABEL_ATTRIBUTE)
		                    + "_" + tn.getAttribute(Retriever.LABEL_ATTRIBUTE)
		                    + "_" + System.currentTimeMillis()
		                    + "." + StringUtils.substringAfterLast( tn.getAttribute(SsapRetrieval.SPECTRA_FORMAT_ATTRIBUTE),"/");
		                doSaveResource(saveLocationRoot,url,name);
		            }  

		            /** primitive operaiton - save a url to the destination
		             * @param saveLocationRoot
		             * @param url url resource to save
		             * @param name name to save it as.   
		             */
		            private void doSaveResource(final URI saveLocationRoot, URL url, String name) throws Exception {
		                InputStream is = null;
		                OutputStream os = null;
		                final String mungedName= conformToMyspaceName(name);
		                URI uri = new URI(saveLocationRoot.toString() + "/" +mungedName);
		                
		                SwingUtilities.invokeLater(
		                        new Runnable() {
		                            public void run() {
		                                ui.setStatusMessage("Saving File: " + mungedName);
		                            }
		                        }
		                );                            
		                if(uri.getScheme().startsWith("ivo")) {
		                    myspace.copyURLToContent(url,uri);
		                }else {
		                    try {
		                        is = url.openStream();
		                        os = myspace.getOutputStream(uri);
		                        Piper.pipe(is, os);
		                    }finally {
		                        if(is != null)
		                            is.close();
		                        if(os != null)
		                            os.close();
		                    }//try&finally
		                }
		            }            
		            
		          

		            private String conformToMyspaceName(String name) {
		                name = name.replaceAll(" ", "_");
		                name = name.replaceAll(":", "_");
		                name = name.replaceAll(",", "_");
		                name = name.replaceAll("/", "_");
		                return name;
		            }
		            
		        }).start();
	}

}
