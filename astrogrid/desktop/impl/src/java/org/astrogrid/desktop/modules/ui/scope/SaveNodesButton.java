/*$Id: SaveNodesButton.java,v 1.2 2006/04/18 23:25:44 nw Exp $
 * Created on 03-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.astrogrid.io.Piper;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** Action that takes a set of nodes and saves to myspace / localspace.
 * @todo improve code.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Feb-2006
 *
 */
public class SaveNodesButton extends NodeConsumerButton {
    
    
    private static final Log logger = LogFactory.getLog(SaveNodesButton.class);
    
    public SaveNodesButton( FocusSet selection, UIComponent ui, ResourceChooserInternal chooser, MyspaceInternal myspace ) {
        super("Save", "Save the selected nodes to Myspace or local disk",selection);
        this.setIcon(IconHelper.loadIcon("filesave.png"));
        this.chooser = chooser;
        this.ui = ui;
        this.myspace = myspace;
    }
    
    
    //private final Community comm;
    private final ResourceChooserInternal chooser;
    private final UIComponent ui;
    private final MyspaceInternal myspace;
    
    
    /**
     * Runs through the selected objects in the JTree and begins saving data
     * to myspace or filesystem.  User MUST CHOOSE a directory node.  Does the work in a background
     * thread.  Allows for selection of All or pieces such as only sia and/or cone (which looks at graph nodes).
     * Called by the saveButton action.
     *
     */
    public void actionPerformed(ActionEvent e) {       

        //choose a uri to save the data to.             
        final URI saveLocationRoot = chooser.chooseResourceWithParent("Save Data",true,true,true,false,ui.getFrame());
        if (saveLocationRoot == null) {
            return;
        }
                
        (new BackgroundWorker(ui,"Saving Data") {
            private Set processedServices = new HashSet();            
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
                for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
                    final TreeNode tn = (TreeNode)i.next();
                    // find each leaf node - these are the data points.
                    if (tn.getChildCount() > 0) {
                        continue;
                    }
                    TreeNode point = tn.getParent();      
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
                                        ui.setStatusMessage("Failed to save " + tn.getAttribute("label") + " : " + e.getMessage());
                                    }
                                }
                        );                                 
                    }
                }  
                
                return null;
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
                
                processedServices.add(catalog); // record that we've done this catalog now.
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
                    + "." + StringUtils.substringAfterLast( tn.getAttribute(SsapRetrieval.SPECTRA_TYPE_ATTRIBUTE),"/");
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
    
    

    
    /** only enable this action if more than one node is selected. */
    public void focusChanged(FocusEvent arg0) {
        setEnabled(selectedNodes.size() > 0);
    }
    
    
 
}


/* 
 $Log: SaveNodesButton.java,v $
 Revision 1.2  2006/04/18 23:25:44  nw
 merged asr development.

 Revision 1.1.2.1  2006/04/14 02:45:00  nw
 finished code.extruded plastic hub.

 Revision 1.1  2006/02/24 15:26:53  nw
 build framework for dynamically adding buttons

 Revision 1.1  2006/02/09 15:40:01  nw
 finished refactoring of astroscope.
 added vospec viewer
 
 */