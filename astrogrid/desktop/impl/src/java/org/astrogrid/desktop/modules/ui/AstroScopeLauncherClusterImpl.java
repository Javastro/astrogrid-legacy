/*$Id: AstroScopeLauncherClusterImpl.java,v 1.2 2005/11/03 15:59:19 KevinBenson Exp $
 * Created on 12-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.SiapInformation;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.acr.nvo.ConeInformation;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.AstroScopeLauncherCluster;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.io.Piper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.votable.TableElement;
import uk.ac.starlink.votable.VOElement;
import uk.ac.starlink.votable.VOElementFactory;
import uk.ac.starlink.votable.VOStarTable;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.FocusManager;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.activity.ActivityMap;
import edu.berkeley.guir.prefuse.action.ActionMap;
import edu.berkeley.guir.prefuse.action.ActionSwitch;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.animate.ColorAnimator;
import edu.berkeley.guir.prefuse.action.animate.LocationAnimator;
import edu.berkeley.guir.prefuse.action.animate.PolarLocationAnimator;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.action.filter.Filter;
import edu.berkeley.guir.prefuse.action.filter.GraphFilter;
import edu.berkeley.guir.prefuse.action.filter.TreeFilter;
import edu.berkeley.guir.prefuse.action.filter.WindowedTreeFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.activity.SlowInSlowOutPacer;
import edu.berkeley.guir.prefuse.collections.DOIItemComparator;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.DefaultFocusSet;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;
import edu.berkeley.guir.prefuse.graph.event.GraphEventAdapter;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultNodeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.berkeley.guir.prefuse.render.RendererFactory;
import edu.berkeley.guir.prefuse.render.ShapeRenderer;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
import edu.berkeley.guir.prefuse.render.NullRenderer;
import edu.berkeley.guir.prefuse.util.ColorLib;
import edu.berkeley.guir.prefuse.util.ColorMap;
import edu.berkeley.guir.prefuse.util.StringAbbreviator;
import edu.berkeley.guir.prefusex.controls.AnchorUpdateControl;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.FocusControl;
import edu.berkeley.guir.prefusex.controls.MultiSelectFocusControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.SubtreeDragControl;
import edu.berkeley.guir.prefusex.controls.ToolTipControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;
import edu.berkeley.guir.prefusex.controls.ZoomingPanControl;
import edu.berkeley.guir.prefusex.distortion.Distortion;
import edu.berkeley.guir.prefusex.distortion.FisheyeDistortion;
import edu.berkeley.guir.prefusex.force.DragForce;
import edu.berkeley.guir.prefusex.force.ForceSimulator;
import edu.berkeley.guir.prefusex.force.NBodyForce;
import edu.berkeley.guir.prefusex.force.SpringForce;
import edu.berkeley.guir.prefusex.layout.BalloonTreeLayout;
import edu.berkeley.guir.prefusex.layout.ForceDirectedLayout;
import edu.berkeley.guir.prefusex.layout.IndentedTreeLayout;
import edu.berkeley.guir.prefusex.layout.RadialTreeLayout;
import edu.berkeley.guir.prefusex.layout.ScatterplotLayout;
import edu.berkeley.guir.prefusex.layout.SquarifiedTreeMapLayout;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTranslation;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTreeMapper;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTreeLayout;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicVisibilityFilter;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTranslationEnd;
import edu.berkeley.guir.prefuse.event.ControlAdapter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Cursor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


/** Implementation of the Datascipe launcher
 * 
 * @todo replace JTree with prefuse Display of selection site? - and vizual clue to selected items in viz(color, or something). 
 * @todo debug and refine vizualizaitons.
 * @todo display thumbnails - there's a TextImageNodeRenderer thingie that sounds the job for this. most responses from siap services have the 
 * same coords anyhow - so a thumbnail would be better.
 * @todo saving of results.
  # @todo fix selecton - as the moment the selection model doesn't seem to be doing anything - maye go back to AstroScopeMultiSelectionControl
 */
public class AstroScopeLauncherClusterImpl extends UIComponent implements AstroScopeLauncherCluster, ActionListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AstroScopeLauncherClusterImpl.class);

    //components used in astrosope
    
    //myspace for saving data into myspace.
    private final MyspaceInternal myspace;
    
    //currently not used
    private final RegistryChooser rci;
    
    //siap object for querying siap services.
    private final Siap siap;
    
    private final Sesame ses;
    
    //cone object for querying cone services.
    private final Cone cone;
    
    private final Community comm;
    
    private final Registry reg;
    
    //The visual graph that holds all the nodes in the display.
    private Tree tree;

    
 //the default sia and cone nodes children of root "Astroscope"
    private TreeNode siaNode;
    private TreeNode coneNode; 
    
    //checkboxes to determine if you want to query on sia and/or cone.
    private JCheckBox siaCheckBox;
    private JCheckBox coneCheckBox;    
    
    // array of vizualizations we're using.
    private final Vizualization[] vizualizations;

    //hashtable that holds all the id's with values of urls to votable results.
    private Hashtable storageTable;
    
    
    //Various gui components.
    private JTextField posText = null;
    private JTextField regionText = null;
    private JButton advancedButton = null;
    private JButton submitButton = null;           
    private DefaultListModel selectedListModel = null;
    protected final ResourceChooserInternal chooser;
    private JTree selectTree;
    private DefaultMutableTreeNode selectRootNode;
    protected DefaultTreeModel treeModel;
    
    //JButton saveImageButton;
    JButton saveButton;
    //JButton selectAllButton;
    JButton clearTreeButton;
    
    /** configurable thread-pool - used to perform the queries and background updates. */
    private PooledExecutor executor;
   
    /**
     * 
     * @param ui
     * @param conf
     * @param hs
     * @param myspace
     * @param chooser
     * @param reg
     * @param rci
     * @param siap
     * @param cone
     */
    public AstroScopeLauncherClusterImpl(UIInternal ui, Configuration conf, HelpServerInternal hs,  
                                  MyspaceInternal myspace, ResourceChooserInternal chooser, Registry reg, RegistryChooser rci, 
                                  Siap siap, Cone cone, Sesame ses, Community comm) {
        super(conf,hs,ui);
        this.myspace = myspace;
        this.rci = rci;
        this.siap = siap;
        this.cone = cone;
        this.comm = comm;
        this.reg = reg;
        this.ses = ses;
        this.chooser = chooser;
        this.storageTable = new Hashtable();
        
        // configure vizualizations.
        vizualizations = new Vizualization[]{
                new WindowedRadial()
                , new FisheyeWindowedRadial()
                , new Hyperbolic()
                , new Balloon()
       //         ,new TreeMap() //-- seems to throw exceptions.
              // ,new ZoomPan()// - dodgy and ugly
             , new ConventionalTree()  // not working yet.
          //  , new Plot() // doesn't seem to layout quite right. - evertything in same place. (as coords are always the same)
             //,  new Force() //- takes too long to settle.               
        };
        // register each vizualization as a listener
        for (int i = 0; i < vizualizations.length; i++) {
            getTree().addGraphEventListener(vizualizations[i]);
        }
        
        // configure execution system
        this.executor = new PooledExecutor(new LinkedQueue()); // infinite task buffer
        this.executor.setMinimumPoolSize(5); // always have 5 threads ready to go.
        this.setSize(700, 700);  
        JPanel pane = getJContentPane();
        JPanel searchPanel = makeSearchPanel();
        searchPanel.setMaximumSize(searchPanel.getSize());
        pane.add(searchPanel,BorderLayout.WEST);
        pane.add(makeCenterPanel(),BorderLayout.CENTER);
        this.setContentPane(pane);
        this.setTitle("AstroScope");
    }
       
    private String conformToMyspaceName(String name) {
        name = name.replaceAll(" ", "_");
        name = name.replaceAll(":", "_");
        name = name.replaceAll(",", "_");
        name = name.replaceAll("/", "_");
        return name;
    }
       
    /**
     * method: saveData
     * Description: Runs through the selected objects in the JTree and begins saving data
     * to myspace or filesystem.  User MUST CHOOSE a directory node.  Does the work in a background
     * thread.  Allows for selection of All or pieces such as only sia and/or cone (which looks at graph nodes).
     * Called by the saveButton action.
     *
     */
    private void saveData() {
             comm.getUserInformation();
             //choose a uri to save the data to.             
             final URI u = chooser.chooseResourceWithParent("Save Data",true,true,true,false,this);
             if (u == null) {
                 return;
             }
             
             logger.debug("the uri chosen = " + u.toString());
             (new BackgroundOperation("Saving Data") {
                     protected Object construct() throws Exception {
                         File file = null;
                         //make sure they chose a directory if not then return.
                         if(u.toString().startsWith("file:")) {
                             file = new File(u);
                             if(!file.isDirectory()) {
                                 JOptionPane.showMessageDialog(AstroScopeLauncherClusterImpl.this, 
                                         "You can only save data to a folder or directory, nothing was saved.",
                                         "No Directory Selected", JOptionPane.OK_OPTION );
                                 return null;
                             }                             
                         }else {
                             FileManagerNode fmn = myspace.node(u);
                             if(!fmn.isFolder()) {
                                 JOptionPane.showMessageDialog(AstroScopeLauncherClusterImpl.this, 
                                         "You can only save data to a folder or directory, nothing was saved",
                                         "No Directory Selected", JOptionPane.OK_OPTION );
                                 return null;
                             }                             
                         }//else
                         Enumeration depthEnum;
                         Enumeration storageLookUp;
                         Iterator nodeIterator;
                         String lookup;
                         String key;
                         String name;
                         URL url;
                         URI finalURI;
                         DefaultMutableTreeNode dmTreeNode;
                         String []vals;
                         TreeNodeData treeData;
                         for (Enumeration e = selectRootNode.children() ; e.hasMoreElements() ;) {
                             DefaultMutableTreeNode tn = (DefaultMutableTreeNode)e.nextElement();
                             if(tn.getChildCount() > 0) {
                                 for (depthEnum = selectRootNode.depthFirstEnumeration() ; depthEnum.hasMoreElements() ;) {
                                     dmTreeNode = (DefaultMutableTreeNode)depthEnum.nextElement();
                                     if(!dmTreeNode.isRoot()) {
                                         treeData = (TreeNodeData)dmTreeNode.getUserObject();
                                         if(treeData.getID() != null  && treeData.getID().trim().length() > 0) {
                                             vals = treeData.getID().split("\\|");
                                             //System.out.println("vals length = " + vals.length + " and id = " + treeData.getID());
                                             for(int i = 0;i < vals.length;i++) {
                                                 //System.out.println("id" + vals[i] + "for i = " + i + " and votable url = " + storageTable.get(vals[i]));
                                                 url = (URL)storageTable.get(vals[i]);
                                                 //TODO
                                                 //now copy into the selected directory
                                                 //be sure to add an actual filename to that directory.
                                                 //should be the name of treeData.toString() with special characters and spaces
                                                 //set to "_" or something valid.
                                                 name = conformToMyspaceName("Votable_" + treeData.toString() + vals[i]);
                                                 finalURI = new URI(u.toString() + "/" + name);
                                                 
                                                 logger.debug("the url found = " + url);
                                                 logger.debug("the finaluri = " + finalURI);
                                                 if(finalURI.getScheme().startsWith("ivo")) {
                                                     myspace.copyURLToContent(url,finalURI);
                                                 }else {
                                                     Piper.pipe(url.openStream(), myspace.getOutputStream(finalURI));
                                                 }
                                             }
                                         }
                                     }//if
                                 }
                             }else {
                                 //they selected everything or sia or cone.
                                 nodeIterator = tree.getNodes();
                                 Node nd = null;
                                 if(tn.toString().equals("All")) {
                                     while(nodeIterator.hasNext()) {
                                         nd = (Node)nodeIterator.next();
                                         key = nd.getAttribute("id");
                                         if(key != null && key.trim().length() > 0) {
                                             vals = key.split("\\|");
                                             //System.out.println("vals length = " + vals.length + " and key = " + key);
                                             for(int i = 0;i < vals.length;i++) {  
                                                 //System.out.println("id" + vals[i] + "for i = " + i + " and votable url = " + storageTable.get(vals[i]));
                                                 url = (URL)storageTable.get(vals[i]);
                                                 //System.out.println("the url = " + url);
                                                 name = conformToMyspaceName("Votable_" + nd.getAttribute("label"));
                                                 finalURI = new URI(u.toString() + "/" + name);
                                                 //myspace.copyURLToContent(url,finalURI);
                                                 if(finalURI.getScheme().startsWith("ivo")) {
                                                     myspace.copyURLToContent(url,finalURI);
                                                 }else {
                                                     Piper.pipe(url.openStream(), myspace.getOutputStream(finalURI));
                                                 }
                                             }
                                             //TODO
                                             //now copy into the selected directory
                                             //be sure to add an actual filename to that directory.
                                             //Use nd.getAttribute("label") for the filename.
                                             //with special characters and spaces
                                             //set to "_" or something valid.                                    
                                             //copyContentToURL
                                         }//if
                                     }//while
                                 }else {                                     
                                     treeData = (TreeNodeData)tn.getUserObject();
                                     if(treeData.getID() != null  && treeData.getID().trim().length() > 0) {
                                         vals = treeData.getID().split("\\|");
                                         //System.out.println("vals length = " + vals.length + " and id = " + treeData.getID());
                                         for(int i = 0;i < vals.length;i++) {
                                             //System.out.println("id" + vals[i] + "for i = " + i + " and votable url = " + storageTable.get(vals[i]));
                                             url = (URL)storageTable.get(vals[i]);
                                             name = conformToMyspaceName("Votable_" + treeData.toString() + vals[i]);
                                             finalURI = new URI(u.toString() + "/" + name);
                                             
                                             logger.debug("the url found = " + url);
                                             logger.debug("the finaluri = " + finalURI);
                                             if(finalURI.getScheme().startsWith("ivo")) {
                                                 myspace.copyURLToContent(url,finalURI);
                                             }else {
                                                 Piper.pipe(url.openStream(), myspace.getOutputStream(finalURI));
                                             }
                                         }//for
                                     }//if
                                 }//else
                             }//else
                         }//for
                         return null;
                      }
                 }).start();
             saveImageData(u);
    }

    /**
     * method: saveImageData
     * Description: Runs through the selected objects in the JTree and begins saving selected images
     * to myspace or filesystem.  User MUST CHOOSE a directory node.  Does the work in a background
     * thread.  Also allows for selection of all or sub trees like only sia or cone (which looks at graph nodes).
     * Called by the saveImageButton action.  
     *
     */    
    private void saveImageData(URI saveURI) {
        comm.getUserInformation();
        /*
        final URI u = chooser.chooseResourceWithParent("Save Data",true,true,true,false,this);
        if (u == null) {
            return;
        }
        */
        final URI u = saveURI;
        

        (new BackgroundOperation("Saving Images") {
                protected Object construct() throws Exception {

                    if(u.toString().startsWith("file:")) {
                        File file = new File(u);
                        if(!file.isDirectory()) {
                            JOptionPane.showMessageDialog(AstroScopeLauncherClusterImpl.this, 
                                    "You can only save data to a folder or directory",
                                    "No Directory Selected", JOptionPane.OK_OPTION );
                        }                             
                    }else {
                        FileManagerNode fmn = myspace.node(u);
                        if(!fmn.isFolder()) {
                            JOptionPane.showMessageDialog(AstroScopeLauncherClusterImpl.this, 
                                    "You can only save data to a folder or directory",
                                    "No Directory Selected", JOptionPane.OK_OPTION );
                        }                             
                    }//else

                    Enumeration depthEnum;
                    Enumeration storageLookUp;
                    Iterator nodeIterator;
                    String lookup;
                    String key;
                    URL url;
                    String name;
                    int randInc = 0;
                    URI finalURI;
                    DefaultMutableTreeNode dmTreeNode;
                    Node nd;
                    String []vals;
                    TreeNodeData treeData;
                    for (Enumeration e = selectRootNode.children() ; e.hasMoreElements() ;) {
                        DefaultMutableTreeNode tn = (DefaultMutableTreeNode)e.nextElement();
                        if(tn.getChildCount() > 0) {
                            for (depthEnum = selectRootNode.depthFirstEnumeration() ; depthEnum.hasMoreElements() ;) {
                                dmTreeNode = (DefaultMutableTreeNode)depthEnum.nextElement();
                                if(!dmTreeNode.isRoot()) {
                                    treeData = (TreeNodeData)dmTreeNode.getUserObject();
                                    if(treeData.getURL() != null  && treeData.getURL().trim().length() > 0) {
                                        vals = treeData.getURL().split("\\|");
                                        //System.out.println("vals length" + vals.length + "treedata urls = " + treeData.getURL());
                                        for(int i = 0;i < vals.length;i++) {
                                            //System.out.println("url for images" + vals[i] + "for i = " + i);
                                            url = new URL(vals[i]);
                                            name = conformToMyspaceName(((DefaultMutableTreeNode)dmTreeNode.getParent()).getUserObject().toString());
                                            name += "_" + conformToMyspaceName(treeData.toString());
                                            randInc++;
                                            finalURI = new URI(u.toString() + "/" + name + "-uniquenum_" + randInc);
                                            //myspace.copyURLToContent(url,finalURI);
                                            if(finalURI.getScheme().startsWith("ivo")) {
                                                myspace.copyURLToContent(url,finalURI);
                                            }else {
                                                Piper.pipe(url.openStream(), myspace.getOutputStream(finalURI));
                                            }                                        
                                            logger.debug("the url found = " + treeData.getURL());
                                        }
                                    }//if
                                }//if
                            }//for
                        }else {
                            
                            if(!tn.getUserObject().toString().equals("All")) {
                                treeData = (TreeNodeData)tn.getUserObject();
                                if(treeData.getID() != null) {
                                    //Darn they chosen just a offset "only".
                                    //need to look through the nodes now.
                                    nodeIterator = tree.getNodes(); 
                                    while(nodeIterator.hasNext()) {                                            
                                        nd = (Node)nodeIterator.next();
                                        key = nd.getAttribute("id");
                                        if(key != null && key.equals(treeData.getID())) {
                                            //okay we found it id's matched go through the 
                                            //neighbor nodes and see if there is a url attribute in which we 
                                            //will start saving.                                                
                                            nodeIterator = nd.getNeighbors();
                                            while(nodeIterator.hasNext()) {
                                                nd = (Node)nodeIterator.next();
                                                if(nd.getAttribute("url") != null) {
                                                    vals = nd.getAttribute("url").split("\\|");
                                                    //System.out.println("vals length" + vals.length + "treedata urls = " + nd.getAttribute("url"));
                                                    for(int i = 0;i < vals.length;i++) { 
                                                        //System.out.println("url for images" + vals[i] + "for i = " + i);
                                                        url = new URL(vals[i]);
                                                        name = conformToMyspaceName(treeData.toString() + "/" + nd.getAttribute("label"));
                                                        randInc++;                                                        
                                                        finalURI = new URI(u.toString() + "/" + name  + "-uniquenum_" + randInc);
                                                        //myspace.copyURLToContent(url,finalURI);
                                                        if(finalURI.getScheme().startsWith("ivo")) {
                                                            myspace.copyURLToContent(url,finalURI);
                                                        }else {
                                                              Piper.pipe(url.openStream(), myspace.getOutputStream(finalURI));
                                                        }
                                                    }
                                                }//if
                                            }//while
                                        }//if
                                    }//while
                                }//if
                            }else {                           
                                nodeIterator = tree.getNodes();                            
                                while(nodeIterator.hasNext()) {
                                    nd = (Node)nodeIterator.next();
                                    key = nd.getAttribute("url");
                                    if(key != null && key.trim().length() > 0) {
                                        vals = key.split("\\|");
                                        //System.out.println("vals length" + vals.length + "treedata urls = " + nd.getAttribute("url"));
                                        for(int i = 0;i < vals.length;i++) { 
                                            //System.out.println("url for images" + vals[i] + "for i = " + i);                                            
                                            url = new URL(vals[i]);                                        
                                            //all nodes with urls only have 1 neighbor which is the
                                            //actual service so gets label as part of the name.
                                            name = conformToMyspaceName(nd.getNeighbor(0).getAttribute("label"));
                                            name += "_" + conformToMyspaceName(nd.getAttribute("label"));
                                            randInc++;
                                            finalURI = new URI(u.toString() + "/" + name  + "-uniquenum_" + randInc);
                                            //myspace.copyURLToContent(url,finalURI);
                                            if(finalURI.getScheme().startsWith("ivo")) {
                                                myspace.copyURLToContent(url,finalURI);
                                            }else {
                                                Piper.pipe(url.openStream(), myspace.getOutputStream(finalURI));
                                            }
                                        }//for
                                    }//if
                                }//while
                            }//else
                        }//else
                    }//for
                    return null;
                 }
            }).start();
    }
    
    /**
     * Verify the entry in the position text box is a position.  If not it should try to look it up. 
     * @return
     */
    private String getPosition() {
        String pos = posText.getText().trim();
        try {
            return getPosition(pos);
        }catch(IllegalArgumentException iae) {
            return getPosition(getPositionFromObject());  
        }
    }
    
    private String getPosition(String pos) throws IllegalArgumentException {
        if(pos == null || pos.trim().length() == 0) {
            throw new IllegalArgumentException("No position given");
        }
        String expression = "-?\\d+\\.?\\d*,-?\\d+\\.?\\d*";
        if(pos.matches(expression)) {            
            return pos;            
        }
        throw new IllegalArgumentException("No position found at the moment, expression did not match");
    }
    
    private String getPositionFromObject() {
        String pos = null;    
        try {
            String temp = ses.sesame(posText.getText().trim(),"x");
            logger.debug("here is the xml response from sesame = " + temp);
            pos = temp.substring(temp.indexOf("<jradeg>")+ 8, temp.indexOf("</jradeg>"));
            pos += "," + temp.substring(temp.indexOf("<jdedeg>")+ 8, temp.indexOf("</jdedeg>"));
            logger.debug("here is the position extracted from sesame = " + pos);
        }catch(Exception e) {
            //hmmm I think glueservice is throwing an exception but things seem to be okay.
				logger.debug("error from sesame - ho hum",e);
        }
        return pos;
    }

    
    /**
     * Extracts out the ra of a particular position in the form of a ra,dec
     * @param position
     * @return
     * @todo refactor -report error to user - or prevent invalid input in the first place.
     */
    private double getRA(String position) {
        String []val = position.split(",");
        if(val.length != 2) {
            throw new IllegalArgumentException("Invalid position was given for determinging 'ra', position: " + position);
        }
        return Double.parseDouble(val[0]);        
    }

    /**
     * Extracts out the dec of a particular position in the form of a ra,dec
     * @param position
     * @return
     */    
    private double getDEC(String position) {
        String []val = position.split(",");
        if(val.length != 2) {
            throw new IllegalArgumentException("Invalid position was given for determinging 'dec', position: " + position);
        }
        return Double.parseDouble(val[1]);
    }
    
    /**
     * Checks to see if the regiion is in the form of ra,dec which will need to be parsed.
     * @return
     * @todo refactor
     */
    private boolean needsParsedRegion() {
        String region = regionText.getText().trim();
        String expression = "-?\\d+\\.?\\d*,-?\\d+\\.?\\d*";
        return region.matches(expression);
    }
    
    /**
     * Returns the text in the region box as a double.
     * @return
     */
    private double getConeRegion() {
        return Double.parseDouble(regionText.getText().trim());
    }
    

    /**
     * Creates the left/WEST side of the GUI.  By creating a small search panel at the top(north-west).  Then
     * let the rest of the panel be a JTree for the selected data.
     * @return
     */
    public JPanel makeSearchPanel() {
        JPanel wrapPanel = new JPanel();
        wrapPanel.setLayout(new BoxLayout(wrapPanel,BoxLayout.Y_AXIS));
        JPanel scopeMain = new JPanel();
        scopeMain.setLayout(new GridLayout(4,2));

        siaCheckBox = new JCheckBox("Images");
        siaCheckBox.setSelected(true);
        coneCheckBox = new JCheckBox("Catalogues");
        coneCheckBox.setSelected(true);
        
        
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        posText = new JTextField();
        regionText = new JTextField();
       
        Dimension dim2 = new Dimension(200,70);
        scopeMain.setMaximumSize(dim2);
        scopeMain.setPreferredSize(dim2); 
        
        scopeMain.add(new JLabel("Position/Object: "));
        scopeMain.add(posText);
        scopeMain.add(new JLabel("Region: "));
        scopeMain.add(regionText);
        scopeMain.add(submitButton);
        scopeMain.add(new JLabel(""));
        scopeMain.add(siaCheckBox);
        scopeMain.add(coneCheckBox);
        
        wrapPanel.add(scopeMain);
        
        //Dimension dim3 = new Dimension(200,500);
        
        JPanel bottomPanel = new JPanel();
        //selectedList.setBackground(bottomPanel.getBackground());
        JTextArea selectTextArea = new JTextArea();
        selectTextArea.setBackground(bottomPanel.getBackground());
        selectTextArea.setEditable(false);
        //JScrollPane listScrollPane = new JScrollPane(selectTextArea);
        //@todo replace this tree with a prefuse tree display.
        selectRootNode = new DefaultMutableTreeNode("Selected Data");
        /*
         * 
         DefaultMutableTreeNode testNode = new DefaultMutableTreeNode("This is a very long node name to see the scrollbar work");
        DefaultMutableTreeNode testNode2 = new DefaultMutableTreeNode("Another long node; This is a very long node name to see the scrollbar work");
        testNode.add(testNode2);
        selectRootNode.add(testNode);
        */
        treeModel = new DefaultTreeModel(selectRootNode);
        selectTree = new JTree(treeModel); 
        selectTree.setBackground(bottomPanel.getBackground());
        JScrollPane listScrollPane = new JScrollPane(selectTree,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);        
        wrapPanel.add(listScrollPane);
        selectTree.setMaximumSize(selectTree.getSize());
        
        //getPathForRow        
        JPanel southCenterPanel = new JPanel();
        dim2 = new Dimension(200,70);
        southCenterPanel.setMaximumSize(dim2);
        southCenterPanel.setPreferredSize(dim2);
                
        /*
        selectAllButton = new JButton("Select All");
        selectAllButton.setEnabled(false);
        selectAllButton.addActionListener(this);
        */      
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(this);
        
        /*
        saveImageButton = new JButton("Save Selected Images");
        saveImageButton.setEnabled(false);
        saveImageButton.addActionListener(this);
        */
        
        clearTreeButton = new JButton("Clear");
        clearTreeButton.setEnabled(false);
        clearTreeButton.addActionListener(this);
                
        //southCenterPanel.add(selectAllButton);
        southCenterPanel.add(saveButton);
        //southCenterPanel.add(saveImageButton);
        southCenterPanel.add(clearTreeButton);
        wrapPanel.add(southCenterPanel);
        
        treeModel.reload();
        wrapPanel.setPreferredSize(new Dimension(200,selectTree.getSize().height));
        return wrapPanel;
    }

    /**
     * Makes the Center Panel for the GUI.  The main look is is the display graph which is the 
     * majority of the center, and a series of save buttons below it.  A series of 
     * createInitialDisplay type methods are here to try and switch to see the best display.
     * 
     * @return
     */
    private JPanel makeCenterPanel() {
        
        
        JTabbedPane alternatives = new JTabbedPane();
        for (int i = 0; i < vizualizations.length; i++) {
            alternatives.addTab(vizualizations[i].getName(),vizualizations[i].getDisplay());
        }
 
        JPanel wrapPanel = new JPanel();
        wrapPanel.setLayout(new BoxLayout(wrapPanel,BoxLayout.Y_AXIS));
        wrapPanel.add(alternatives);                
        //wrapPanel.add(southCenterPanel);
        return wrapPanel;        
    }
    
    
    /** builds basic structure of the graph */
    private Tree getTree() {
        if (tree == null) {
            rootNode = new DefaultTreeNode();
            rootNode.setAttribute("label","Search Results");
            rootNode.setAttribute("ra","0");
            rootNode.setAttribute("dec","0");
            
            /*
                      
            siaNode = new DefaultTreeNode();
            siaNode.setAttribute("label","Images");
            siaNode.setAttribute("ra","0");
            siaNode.setAttribute("dec","0");
       
            coneNode  = new DefaultTreeNode();
            coneNode.setAttribute("label","Catalogs");
            coneNode.setAttribute("ra","0");
            coneNode.setAttribute("dec","0");
            
            rootNode.addChild(new DefaultEdge(rootNode,siaNode));
            rootNode.addChild(new DefaultEdge(rootNode,coneNode));
            */
            tree = new DefaultTree(rootNode);
        }
        return tree;
    }
    
    /** removes previous results, just leaving the skeleton */
    private void clearTree() {
        // reset selection too.
        getSelectionFocusSet().clear();
        for (Iterator i = getTree().getNodes(); i.hasNext(); ) {
            Node n = (Node)i.next();
            if (n != siaNode && n != coneNode && n != rootNode) {
                getTree().removeNode(n);
            }
        }
    }
    
    /** strategy class - subclasses encapsulate one vizualization - private item registry
     * (as this contains the dispplay-specific info), but a shared tree of data objects - so 
     * each viz provides a different 'view' of the same data.
     * 

     *
     */
    
    private abstract class Vizualization extends GraphEventAdapter {
        public Vizualization(String name) {
            this.name = name;
        }
        public final String getName() {
            return name;
        }
        final private String name;
    private ItemRegistry itemRegistry;
    /** creates the default registry 
     * 
     * pre-initializes a selection called 'selection'
     * */
    protected final ItemRegistry getItemRegistry() {
        if (itemRegistry == null) {
            itemRegistry = new ItemRegistry(getTree());
            itemRegistry.getFocusManager().putFocusSet(FocusManager.SELECTION_KEY,getSelectionFocusSet());
            
            TextItemRenderer nodeRenderer = new TextItemRenderer();
            nodeRenderer.setMaxTextWidth(75);
            nodeRenderer.setRoundedCorner(8,8);
            nodeRenderer.setTextAttributeName("label");
            
            Renderer edgeRenderer = new DefaultEdgeRenderer();
            itemRegistry.setRendererFactory(new DefaultRendererFactory(nodeRenderer, edgeRenderer));            
        }
        return itemRegistry;
    }
    /** access the display for this vizualization */
    public abstract Display getDisplay();
    
    }
    
    /** focus set used to maintain list of nodes selected for download.
     * focus set is shared between vizualizaitons- so changes in one will be seen in others.
     */
    private FocusSet selectionFocusSet;

    private TreeNode rootNode;
    private FocusSet getSelectionFocusSet() {
        if (selectionFocusSet == null) {
            selectionFocusSet = new DefaultFocusSet();
            //@todo add a listener to this, to oupdate tree.
            selectionFocusSet.addFocusListener(new FocusListener() {                
                public void focusChanged(FocusEvent arg0) {
                    System.err.println(selectionFocusSet.size());
                }
            });
        }
        return selectionFocusSet;
    }
    
  
    
    /*
    private AstroScopeMultiSelectionControl asms;
    private ControlAdapter getSelectionControl() {
        if (asms == null) {
            asms = new AstroScopeMultiSelectionControl(getItemRegistry());
        }
        return asms;
    }*/
    
    
    /** vizualization that hides some of the data some of the time 
     * based on edu.berkeley.guir.prefuse.action.filter.WindowedTreeFilter
     * 
     * needs to have it's nodes spaced out more.
     * 
     * */
    class Hyperbolic extends Vizualization {
            
        public Hyperbolic() {
                super("Hyperbolic");    
                
        }
        private Display display;
        
        public ActivityMap actmap; 
        public HyperbolicTranslation translation;   
        
        // refresh display when new item added to results.
        public void nodeAdded(Graph arg0, Node arg1) {
            actmap.runNow("filter");
        }        
        
        public Display getDisplay() {
        
            if(display == null) {
                actmap = new ActivityMap();
                ItemRegistry registry = getItemRegistry();
                display = new Display();

            //initialize renderers
                // create a text renderer with rounded corners and labels
               // with a maximum length of 75 pixels, abbreviated as names.
               TextItemRenderer nodeRenderer = new TextItemRenderer();
               nodeRenderer.setRoundedCorner(8,8);
               nodeRenderer.setMaxTextWidth(75);
               //nodeRenderer.setAbbrevType(StringAbbreviator.NAME);
               // create a null renderer for use when no label should be shown
               NullRenderer nodeRenderer2 = new NullRenderer();
               // create an edge renderer with custom curved edges
               DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer() {
                   protected void getCurveControlPoints(EdgeItem eitem, 
                       Point2D[] cp, double x1, double y1, double x2, double y2) 
                   {
                       Point2D c = eitem.getLocation();      
                       cp[0].setLocation(c);
                       cp[1].setLocation(c);
                   } //
                };
               edgeRenderer.setEdgeType(DefaultEdgeRenderer.EDGE_TYPE_CURVE);
               edgeRenderer.setRenderType(ShapeRenderer.RENDER_TYPE_DRAW);
                
                // set the renderer factory
               registry.setRendererFactory(new DemoRendererFactory(
                    nodeRenderer, nodeRenderer2, edgeRenderer));
                
                // initialize the display
                //display.setSize(500,460);
               display.setSize(500,350);
               display.setItemRegistry(registry);
               display.setBackground(Color.WHITE);
               display.addControlListener(new DemoControl());          
               TranslateControl dragger = new TranslateControl();
               display.addMouseListener(dragger);
               display.addMouseMotionListener(dragger);
               display.addControlListener(new ZoomControl());
               display.addControlListener(new AstroScopeMultiSelectionControl(registry));
               display.addControlListener(new ToolTipControl("tooltip"));
                
               // initialize repaint list
               ActionList repaint = new ActionList(registry);
               repaint.add(new HyperbolicTreeMapper());
               repaint.add(new HyperbolicVisibilityFilter());
               repaint.add(new RepaintAction());
               actmap.put("repaint", repaint);
               
                // initialize filter
               ActionList filter  = new ActionList(registry);
               filter.add(new TreeFilter());
               filter.add(new HyperbolicTreeLayout());
               filter.add(new HyperbolicDemoColorFunction());
               filter.add(repaint);
               actmap.put("filter", filter);
               //graphLayout = filter;
      
               // intialize hyperbolic translation
               ActionList translate = new ActionList(registry);
               translation = new HyperbolicTranslation();
               translate.add(translation);
               translate.add(repaint);
               actmap.put("translate", translate);
               
               // intialize animated hyperbolic translation
               ActionList animate = new ActionList(registry, 1000, 20);
               animate.setPacingFunction(new SlowInSlowOutPacer());
               animate.add(translate);
               actmap.put("animate", animate);
               
               // intialize the end translation list
               ActionList endTranslate = new ActionList(registry);
               endTranslate.add(new HyperbolicTranslationEnd());
               actmap.put("endTranslate", endTranslate);                              
            }               
               return display;
        }
        
        
        public class TranslateControl extends MouseAdapter implements MouseMotionListener {
            boolean drag = false;
            public void mousePressed(java.awt.event.MouseEvent e) {
                translation.setStartPoint(e.getX(), e.getY());
            } //
            public void mouseDragged(java.awt.event.MouseEvent e) {
                drag = true;
                translation.setEndPoint(e.getX(), e.getY());
                actmap.runNow("translate");
            } //
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if ( drag ) {
                    actmap.runNow("endTranslate");
                    drag = false;
                }
            } //
            public void mouseMoved(java.awt.event.MouseEvent e) {
            } //
        } // end of inner class TranslateControl
            
        public class DemoControl extends ControlAdapter {
            public void itemEntered(VisualItem item, java.awt.event.MouseEvent e) {
                e.getComponent().setCursor(
                        Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } //
            public void itemExited(VisualItem item, java.awt.event.MouseEvent e) {
                e.getComponent().setCursor(Cursor.getDefaultCursor());
            } //        
            public void itemClicked(VisualItem item, java.awt.event.MouseEvent e) {
                ItemRegistry registry = getItemRegistry();
                // animate a translation when a node is clicked
                int cc = e.getClickCount();
                if ( item instanceof NodeItem ) {
                    if ( cc == 1 ) {
                        TreeNode node = (TreeNode)registry.getEntity(item);
                        if ( node != null ) {                           
                            translation.setStartPoint(e.getX(), e.getY());
                            translation.setEndPoint(e.getX(), e.getY());
                            actmap.runNow("animate");
                            actmap.runAfter("animate", "endTranslate");
                        }
                    }
                }
            } //
        } // end of inner class DemoController
        
        public class DemoRendererFactory implements RendererFactory {
            private Renderer nodeRenderer1;
            private Renderer nodeRenderer2;
            private Renderer edgeRenderer;
            public DemoRendererFactory(Renderer nr1, Renderer nr2, Renderer er) {
                nodeRenderer1 = nr1;
                nodeRenderer2 = nr2;
                edgeRenderer = er;
            } //
            public Renderer getRenderer(VisualItem item) {
                if ( item instanceof NodeItem ) {
                    NodeItem n = (NodeItem)item;
                    NodeItem p = (NodeItem)n.getParent();
                    
                    double d = Double.MAX_VALUE;
                    
                    Point2D nl = n.getLocation();
                    if ( p != null) {
                        d = Math.min(d,nl.distance(p.getLocation()));
                        int idx = p.getChildIndex(n);
                        NodeItem b;
                        if ( idx > 0 ) {
                            b = (NodeItem)p.getChild(idx-1);
                            d = Math.min(d,nl.distance(b.getLocation()));
                        }
                        if ( idx < p.getChildCount()-1 ) {
                            b = (NodeItem)p.getChild(idx+1);
                            d = Math.min(d,nl.distance(b.getLocation()));
                        }
                    }
                    if ( n.getChildCount() > 0 ) {
                        NodeItem c = (NodeItem)n.getChild(0);
                        d = Math.min(d,nl.distance(c.getLocation()));
                    }
                    
                    if ( d > 15 ) {
                        return nodeRenderer1;
                    } else {
                        return nodeRenderer2;
                    }
                } else if ( item instanceof EdgeItem ) {
                    return edgeRenderer;
                } else {
                    return null;
                }
            } //
        } // end of inner class DemoRendererFactory
        
        public class HyperbolicDemoColorFunction extends ColorFunction {
            private int  thresh = 5;
            private Color graphEdgeColor = Color.LIGHT_GRAY;
            private Color nodeColors[];
            private Color edgeColors[];
           
            public HyperbolicDemoColorFunction() {
                nodeColors = new Color[thresh];
                edgeColors = new Color[thresh];
                for ( int i = 0; i < thresh; i++ ) {
                    double frac = i / ((double)thresh);
                    nodeColors[i] = ColorLib.getIntermediateColor(Color.RED, Color.BLACK, frac);
                    edgeColors[i] = ColorLib.getIntermediateColor(Color.RED, Color.BLACK, frac);
                }
            } //
           
            public Paint getFillColor(VisualItem item) {
                if ( item instanceof NodeItem ) {
                    return Color.WHITE;
                } else if ( item instanceof AggregateItem ) {
                    return Color.LIGHT_GRAY;
                } else if ( item instanceof EdgeItem ) {
                    return getColor(item);
                } else {
                    return Color.BLACK;
                }
            } //
           
            public Paint getColor(VisualItem item) {
                if (item instanceof NodeItem) {
                     int d = ((NodeItem)item).getDepth();
                    return nodeColors[Math.min(d,thresh-1)];
                } else if (item instanceof EdgeItem) {
                    EdgeItem e = (EdgeItem) item;
                    if ( e.isTreeEdge() ) {
                        int d, d1, d2;
                         d1 = ((NodeItem)e.getFirstNode()).getDepth();
                         d2 = ((NodeItem)e.getSecondNode()).getDepth();
                         d = Math.max(d1, d2);
                        return edgeColors[Math.min(d,thresh-1)];
                    } else {
                        return graphEdgeColor;
                    }
                } else {
                    return Color.BLACK;
                }
            } //
        } // end of inner class DemoColorFunction
        
    }

        
    
/** vizualization that hides some of the data some of the time 
 * based on edu.berkeley.guir.prefuse.action.filter.WindowedTreeFilter
 * 
 * needs to have it's nodes spaced out more.
 * 
 * */
    public class Balloon extends Vizualization {
        public Balloon() {
            super("Balloon");            
    }
        private Display display;
        private ActionList filter;
        public Display getDisplay() {
            if (display == null) {
                // create display and filter
                ItemRegistry registry =getItemRegistry();
                registry.setItemComparator(new DOIItemComparator());
                display = new Display();

                // initialize renderers
                TextItemRenderer nodeRenderer = new TextItemRenderer();
                //nodeRenderer.setMaxTextWidth(75);
                nodeRenderer.setAbbrevType(StringAbbreviator.NAME);
                nodeRenderer.setRoundedCorner(8,8);
                nodeRenderer.setTextAttributeName("label");
                
                Renderer nodeRenderer2 = new DefaultNodeRenderer();
                Renderer edgeRenderer = new DefaultEdgeRenderer();
                
                registry.setRendererFactory(new SizeVaryingRenderFactory(
                    nodeRenderer, nodeRenderer2, edgeRenderer));
                
                // initialize action lists
                filter = new ActionList(registry);
                filter.add(new WindowedTreeFilter(-4,true));
                filter.add(new BalloonTreeLayout());
                filter.add(new DemoColorFunction(4));
                
                ActionList update = new ActionList(registry);
                update.add(new DemoColorFunction(4));
                update.add(new RepaintAction());
                
                ActionList animate = new ActionList(registry, 1500, 20);
                animate.setPacingFunction(new SlowInSlowOutPacer());
                animate.add(new LocationAnimator());
                animate.add(new ColorAnimator());
                animate.add(new RepaintAction());
                animate.alwaysRunAfter(filter);
                
                // initialize display
                display.setItemRegistry(registry);
                display.setSize(400,400);
                //display.setBackground(Color.WHITE);
                display.addControlListener(new FocusControl(filter));
                display.addControlListener(new SubtreeDragControl());
                display.addControlListener(new PanControl());
                display.addControlListener(new ZoomControl());
                display.addControlListener(new NeighborHighlightControl(update));
                display.addControlListener(new ToolTipControl("tooltip"));
                display.addControlListener(new MultiSelectFocusControl(registry,FocusManager.SELECTION_KEY));
            }
           return display;
        }
        // refresh display when new item added to results.
        public void nodeAdded(Graph arg0, Node arg1) {
            filter.runNow();
        }
        /**
         * A RendererFactory instance that assigns node renderers of varying size
         * in response to a node's depth in the tree.
         */        
        public class SizeVaryingRenderFactory implements RendererFactory {
            private Renderer nodeRenderer1;
            private Renderer nodeRenderer2;
            private Renderer edgeRenderer;
            public SizeVaryingRenderFactory(Renderer nr1, Renderer nr2, Renderer er) {
                nodeRenderer1 = nr1;
                nodeRenderer2 = nr2;
                edgeRenderer = er;
            } //
            public Renderer getRenderer(VisualItem item) {
                if ( item instanceof NodeItem ) {
                    int d = ((NodeItem)item).getDepth();
                    if ( d > 1 ) {
                        int r = (d == 2 ? 5 : 1);
                        ((DefaultNodeRenderer)nodeRenderer2).setRadius(r);
                        return nodeRenderer2;
                    } else {
                        return nodeRenderer1;
                    }
                } else if ( item instanceof EdgeItem ) {
                    return edgeRenderer;
                } else {
                    return null;
                }
            } //
        } // end of inner class DemoRendererFactory
                
    }// end bubble;
   
    

    /** variation of the windowed radial, using fisheye distortion. also shows an overview in the corner 
     * 
     * bit jerky. fisheye is proabbly overkill.
     * 
     * */
    public class FisheyeWindowedRadial extends WindowedRadial {
        public FisheyeWindowedRadial() {
            super("Fisheye radial");
        }
    public Display getDisplay() {
        if (display == null) {
            display = super.getDisplay();
            Display overview = new Display(getItemRegistry());
            overview.setBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1));
            overview.setSize(50,50);
            overview.zoom(new Point2D.Float(0,0),0.1);
            display.add(overview);
            
            Distortion feye = new FisheyeDistortion();
            ActionList distort = new ActionList(getItemRegistry());
            distort.add(feye);
            distort.add(new RepaintAction());
            AnchorUpdateControl auc = new AnchorUpdateControl(feye,distort);
            display.addMouseListener(auc);
            display.addMouseMotionListener(auc);       
        }
        return display;
    }  
    }
    
    /**
     * Uses the Prefuse Radial Layout for display along with a little bit of Animation to it. Uses
     *windowing filter - displays nearer neighbours, not entire graph.
     *based on node type, adjusts the 'window' size - so we don't get an explosion of nodes on the display
     * based on edu.berkeley.guir.prefuse.demos.RadialGraphDemo
     * @return
     */

    public class WindowedRadial extends Vizualization {
    /** Construct a new Radial
         * @param name
         */
        public WindowedRadial() {
            super("Windowed Radial");
        }
        public WindowedRadial(String name) {
            super(name);
        }
        protected Display display;
        protected ActionList graphLayout;
        
    public Display getDisplay() {
            if (display == null) {
        ItemRegistry registry = getItemRegistry();
         display = new Display(registry);
        
            graphLayout = new ActionList(registry);
            
            // two different types of filter - only vary in window size.
            Filter[] filters = new Filter[] {
                    new WindowedTreeFilter(-2,true)
                    ,new WindowedTreeFilter(-1,true)
            };            
                    
            // switch between the two filters
            final ActionSwitch filterSwitch = new ActionSwitch(filters,0);
            
            // add a listener, that selects the correct filter based on the current node.
            registry.getDefaultFocusSet().addFocusListener(new FocusListener() {
                public void focusChanged(FocusEvent event) {
                    if (event.getEventType() == FocusEvent.FOCUS_SET || event.getEventType() == FocusEvent.FOCUS_ADDED) {
                        if (event.getFirstAdded() == siaNode || event.getFirstAdded() == coneNode) {
                            filterSwitch.setSwitchValue(1);
                        } else {
                            filterSwitch.setSwitchValue(0);
                        }
                    }
                }
            });
            
            graphLayout.add(filterSwitch);
            graphLayout.add(new RadialTreeLayout());
            graphLayout.add(new DemoColorFunction(3));
        
           ActionList update = new ActionList(registry);
           update.add(new DemoColorFunction(3));
           update.add(new RepaintAction());

           
           ActionList animate = new ActionList(registry, 1500, 20);
           animate.setPacingFunction(new SlowInSlowOutPacer());
           animate.add(new PolarLocationAnimator());
           animate.add(new ColorAnimator());
           animate.add(new RepaintAction());
           animate.alwaysRunAfter(graphLayout);
           /*
           // add jitter to layout nodes better. could maybe make the jitter larger - makes the nodes less
           // likely to overlap.
           ForceSimulator fsim = new ForceSimulator();
           fsim.addForce(new NBodyForce(-0.1f,15f,0.5f));
           fsim.addForce(new DragForce());
           
           
           ActionList forces = new ActionList(registry,1000);
           forces.add(new ForceDirectedLayout(fsim,true));
           forces.add(new RepaintAction());
           forces.alwaysRunAfter(animate);
           */

           display.setItemRegistry(registry);
           display.setSize(400,400);
           
           //for radial tree
           display.addControlListener(new FocusControl(graphLayout));
           display.addControlListener(new FocusControl(0,FocusManager.HOVER_KEY));
           display.addControlListener(new DragControl(false,true));
           display.addControlListener(new PanControl(false));
           display.addControlListener(new ZoomControl(false));
           display.addControlListener(new ToolTipControl("tooltip"));   
           display.addControlListener(new NeighborHighlightControl(update));
           //display.addControlListener(new MultiSelectFocusControl(registry,FocusManager.SELECTION_KEY));
           display.addControlListener(new AstroScopeMultiSelectionControl(registry));
           
           registry.getFocusManager().putFocusSet(
                   FocusManager.HOVER_KEY, new DefaultFocusSet());
            }
           return display;
    }

        // refresh display when new item added
        public void nodeAdded(Graph arg0, Node arg1) {
         
            /*
            String offset = arg1.getAttribute("offset");
            System.out.println("in nodeadded offset = " + offset + " and label = " + arg1.getAttribute("label"));
            if(offset != null) {
                NodeItem ni = getItemRegistry().getNodeItem(arg1);
                if(ni == null) {
                    System.out.println("darn it is null the ni");
                }else {
                    System.out.println("setting sizes orig = " + ni.getSize() + " adding = " + (360/Double.parseDouble(offset)));
                    ni.setSize(ni.getSize() + (360/Double.parseDouble(offset)));
                }
            } 
            */
        
            
            graphLayout.runNow();
        }
    } /// end windowed radial vizualization
    
    
    /** force-based layout
     * based on edu.berkeley.guir.prefuse.demos.ForceDemo
     * 
     * interersting to zoom out and watch, but takes forever to settle and eats the CPU.
     * maybe useable if I could understand what params to set in the force sim to make it settle faster.
     * @author Noel Winstanley nw@jb.man.ac.uk 31-Oct-2005
     *
     */
    public class Force extends Vizualization {
        public Force() {
            super("Force");
        }
        private Display display;
        private ActionList graphLayout;        

        // refresh display when new item added
        public void nodeAdded(Graph arg0, Node arg1) {
            graphLayout.runNow();
        }

        public Display getDisplay() {
            if (display == null) {
                ItemRegistry registry = getItemRegistry();
                display = new Display(registry);
        
                
                graphLayout = new ActionList(registry,-1,20);
                graphLayout.add(new TreeFilter());
                ForceSimulator fsim = new ForceSimulator();
                fsim.addForce(new NBodyForce(-0.4f,-1f,0.9f));
                fsim.addForce(new SpringForce(4E-5f,75f));
                fsim.addForce(new DragForce(-0.0005f)); // shrunk by a factor of 10 - hopefully things will settle faster.
                
                graphLayout.add(new ForceDirectedLayout(fsim,false,false));
                graphLayout.add(new DemoColorFunction(3));
                graphLayout.add(new RepaintAction());
                display.addControlListener(new NeighborHighlightControl());
                display.addControlListener(new DragControl(false,true));
                display.addControlListener(new FocusControl(0));
                display.addControlListener(new PanControl(false));
                display.addControlListener(new ZoomControl(false));
                display.addControlListener(new ToolTipControl("tooltip"));   
                display.addControlListener(new MultiSelectFocusControl(registry,FocusManager.SELECTION_KEY));                
            }
            return display;
        }        
    }// end force vizualization.
    
    /** normal tree layout.
     * 
     * having a bit of trouble getting this to show anything.
     * pity - would like to use this on the left-hand-side instead of the current JTree - would be
     * nice to have nodes looking the same, and then no need for a separate datamodel for the JTree
     * 
     * 
     *  */
    public class ConventionalTree extends Vizualization {

        public ConventionalTree() {
            super("Tree");
        }
        protected Display display;
        protected ActionList graphLayout;
        public void nodeAdded(Graph arg0, Node arg1) {
            graphLayout.runNow();
        }
        
        public Display getDisplay() {
            if (display == null) {
                ItemRegistry registry = getItemRegistry();
                display = new Display(registry);
                
                graphLayout = new ActionList(registry);
                graphLayout.add(new TreeFilter());
                graphLayout.add(new IndentedTreeLayout());
                graphLayout.add(new DemoColorFunction(4));
                display.setItemRegistry(registry);
                display.setSize(400,400);
                
                display.addControlListener(new FocusControl(graphLayout));
                display.addControlListener(new PanControl());
                display.addControlListener(new ZoomControl());
                display.addControlListener(new ToolTipControl("tooltip"));
                display.addControlListener(new MultiSelectFocusControl(registry,FocusManager.SELECTION_KEY));
            }
            return display;
        }
    } // end conventional tree layout.
    
    /** x-y plot layout, using ra and dec. 
     * 
     * doesn't seem to spread thngs out correctly.
     * uses attributes called ra and dec of the nodes - think I need to provide dummy values for 
     * nodes with no ra and dec.
     * */
    public class Plot extends Vizualization {

        public Plot() {
            super("Plot");
        }
        protected Display display;
        protected ActionList graphLayout;
        public void nodeAdded(Graph arg0, Node arg1) {
            graphLayout.runNow();
        }
        
        public Display getDisplay() {
            if (display == null) {
                ItemRegistry registry = getItemRegistry();
                display = new Display(registry);
                
                graphLayout = new ActionList(registry);
                graphLayout.add(new TreeFilter());
                graphLayout.add(new ScatterplotLayout("ra","dec"));
                graphLayout.add(new DemoColorFunction(4));
                display.setItemRegistry(registry);
                display.setSize(400,400);
                
                display.addControlListener(new FocusControl(graphLayout));
                display.addControlListener(new PanControl());
                display.addControlListener(new ZoomControl());
                display.addControlListener(new ToolTipControl("tooltip"));
                display.addControlListener(new MultiSelectFocusControl(registry,FocusManager.SELECTION_KEY));         
            }
            return display;
        }
    }
       
        
    
    /**
     * Tends to show the data a little better, also sort of a Grid Radial kind of style.  You need to hold down the
     * mouse button for zooming and panning at the same time.  Problem is it is a little hard to get used to and again
     * so much data.
     * 
     * NWW - found the data was badly laid out. - not much used
     * 
     * based on edu.berkeley.guir.prefuse.demos.ZoomingPanDemo
     * @return
     */
    public class ZoomPan extends Vizualization {
    /** Construct a new ZoomPan
         * @param name
         */
        public ZoomPan() {
            super("Zoom Pan");
        }

        private Display display;
        private ActionList graphLayout;
        private ActionList update;
    public Display getDisplay() {
       if (display == null) {           
        ItemRegistry registry = getItemRegistry();  
        
        display = new Display(registry);
        
        graphLayout = new ActionList(registry);
        graphLayout.add(new GraphFilter());
        ActionMap actionMap = new ActionMap();
        graphLayout.add(actionMap.put("grid", new PrefuseGridLayout()));
        
        update = new ActionList(registry);
        update.add(new ColorFunction());
        update.add(new RepaintAction());
        
        ((Layout)actionMap.get("grid")).setLayoutBounds(
                new Rectangle2D.Double(-1200,-1200,2400,2400));        
        
           // initialize display 
           display.setSize(400,400);
           display.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
           display.addControlListener(new DragControl());
           display.addControlListener(new NeighborHighlightControl());
           display.addControlListener(new FocusControl(0, update));
           display.addControlListener(new ZoomingPanControl());
           display.addControlListener(new ToolTipControl("tooltip"));
           display.addControlListener(new MultiSelectFocusControl(registry,FocusManager.SELECTION_KEY));
    }
           return display;
    }
    // refresh display when new item added
    public void nodeAdded(Graph arg0, Node arg1) {
        graphLayout.runNow();
        update.runNow();
    }
    
    public class PrefuseGridLayout extends edu.berkeley.guir.prefuse.action.assignment.Layout {
        public void run(ItemRegistry registry, double frac) {
            Rectangle2D b = getLayoutBounds(registry);
            double bx = b.getMinX(), by = b.getMinY();
            double w = b.getWidth(), h = b.getHeight();
            int m, n;
            Graph g = (Graph)registry.getGraph();
            Iterator iter = g.getNodes(); iter.next();
            for ( n=2; iter.hasNext(); n++ ) {
                Node nd = (Node)iter.next();
                if ( nd.getEdgeCount() == 2 )
                    break;
            }
            m = g.getNodeCount() / n;
            iter = g.getNodes();
            for ( int i=0; iter.hasNext(); i++ ) {
                Node nd = (Node)iter.next();
                NodeItem ni = registry.getNodeItem(nd);
                double x = bx + w*((i%n)/(double)(n-1));
                double y = by + h*((i/n)/(double)(m-1));
                
                // add some jitter, just for fun
                x += (Math.random()-0.5)*(w/n);
                y += (Math.random()-0.5)*(h/m);
                
                setLocation(ni,null,x,y);
            }
        } //
    } // end of inner class GridLayout        
    }// end zoom pan vizualizaiton.
    
    /**
     * For the demos this is the best one for showing large amounts of data.  And if we can our data to form similar
     * to what the demo has, then this will be the graph and display to use and should fit fairly nicely with our data.
     * Vision Example: SIA(large box)->Siap Service(medium box)->Various RA,DEC(small box).
     * UNKNOWN: does it allow selection so we can populate the JTree hope so.
     *@todo NWW: can't get this one working. 
     * based on edu.berkeley.guir.prefuse.demos.TreeMapDemo
     * @return
     */
    public class TreeMap extends Vizualization {
        
    /** Construct a new TreeMap
         * @param name
         */
        public TreeMap() {
            super("TreeMap");
        }
        public void nodeAdded(Graph arg0, Node arg1) {
            graphLayout.runNow();
        }
        private Display display;
        private ActionList graphLayout;
        
    public Display getDisplay() {
        if (display == null) {
        ItemRegistry registry = getItemRegistry();
        
        display = new Display(registry);
        display.setSize(500,350);
        // causes probs.        
        registry.setRendererFactory(new DefaultRendererFactory(new RectangleRenderer()));

        // make sure we draw from larger->smaller to prevent
        // occlusion from parent node boxes        
        registry.setItemComparator(new Comparator() {
            public int compare(Object o1, Object o2) {
                double s1 = ((VisualItem)o1).getSize();
                double s2 = ((VisualItem)o2).getSize();
                return ( s1>s2 ? -1 : (s1<s2 ? 1 : 0));
            } //
        });
        
        
           // create the single filtering and layout action list
           graphLayout = new ActionList(registry);
           graphLayout.add(new TreeFilter(false, false));
           graphLayout.add(new TreeMapSizeFunction());
           graphLayout.add(new SquarifiedTreeMapLayout(15));
           //graphLayout.add(new TreeMapColorFunction());
           graphLayout.add(new RepaintAction());
 
           display.setSize(500,350);           
           PanControl  pH = new PanControl();
           ZoomControl zH = new ZoomControl();
           display.addMouseListener(pH);
           display.addMouseMotionListener(pH);
           display.addMouseListener(zH);
           display.addMouseMotionListener(zH);        
           display.addControlListener(new ToolTipControl("tooltip"));
           //display.addControlListener(new MultiSelectFocusControl(registry,FocusManager.SELECTION_KEY));
        }
        return display;
    }

    public class TreeMapColorFunction extends ColorFunction {
        Color c1 = new Color(0.5f,0.5f,0.f);
        Color c2 = new Color(0.5f,0.5f,1.f);
        ColorMap cmap = new ColorMap(ColorMap.getInterpolatedMap(10,c1,c2),0,9);
        public Paint getColor(VisualItem item) {
            return Color.WHITE;
        } //
        public Paint getFillColor(VisualItem item) {
            double v = (item instanceof NodeItem ? ((NodeItem)item).getDepth():0);
            return cmap.getColor(v);
        } //
    } // end of inner class TreeMapColorFunction
    
    public  class TreeMapSizeFunction extends edu.berkeley.guir.prefuse.action.AbstractAction {
        public void run(ItemRegistry registry, double frac) {
            System.out.println("in run of treemapsize frac = " + frac);
            int leafCount = 0;
            Iterator iter = registry.getNodeItems();
            while ( iter.hasNext() ) {
                NodeItem n = (NodeItem)iter.next();
                if ( n.getChildCount() == 0 ) {
                    n.setSize(1.0);
                    NodeItem p = (NodeItem)n.getParent();
                    for (; p!=null; p=(NodeItem)p.getParent())
                        p.setSize(1.0+p.getSize());
                    leafCount++;
                }
            }
            
            Dimension d = registry.getDisplay(0).getSize();
            logger.debug("leafCount = " + leafCount + " display size, width = " + d.width + " height = " + d.height);
            System.out.println("leafCount = " + leafCount + " display size, width = " + d.width + " height = " + d.height);
            double area = d.width*d.height;
            double divisor = ((double)leafCount)/area;
            logger.debug("the area = " + area + " divisor = " + divisor);
            System.out.println("the area = " + area + " divisor = " + divisor);
            iter = registry.getNodeItems();
            while ( iter.hasNext() ) {
                NodeItem n = (NodeItem)iter.next();
                System.out.println("the setsize for node = " + n.getAttribute("label") + " is " + n.getSize()/divisor + " the getsize = " + n.getSize());
                n.setSize(n.getSize()/divisor);
            }
        } //
    } // end of inner class TreeMapSizeFunction
    
    public class RectangleRenderer extends ShapeRenderer {
        private Rectangle2D bounds = new Rectangle2D.Double();
        protected Shape getRawShape(VisualItem item) {
            Point2D d = (Point2D)item.getVizAttribute("dimension");
            if (d == null)
                System.out.println("uh-oh");
            //System.out.println("in rectrender rect1x = " + item.getX() + " recty = " + item.getY() + " and dx = " + d.getX() + " and dy = " + d.getY());
            bounds.setRect(item.getX(),item.getY(),d.getX(),d.getY());
            return bounds;
        } //
    } // end of inner class NodeRenderer
     
    
    } // end of tree map layout viz class.

    /**
     * Various action statements for when buttons are clicked.
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        logger.debug("actionPerformed(ActionEvent) - entered actionPerformed");
        if(source == submitButton) {
            logger.debug("actionPerformed(ActionEvent) - submit button clicked");
            query();
        }else if(source == saveButton) {
            saveData();
        }else if(source == clearTreeButton) {
            selectRootNode.removeAllChildren();
            treeModel.reload();
        }
        /*
        else if(source == selectAllButton) {
            if(storageTable.size() > 0) {
                if(selectRootNode.getChildCount() >= 1) {
                    if(!selectRootNode.getFirstChild().toString().equals("All")) {
                        int result = JOptionPane.showConfirmDialog(AstroScopeLauncherClusterImpl.this, 
                                "You have chosen to 'select all' this will remove all other selections.  Is this correct?",
                                "Clear Selection", JOptionPane.YES_NO_OPTION );
                        if (result == JOptionPane.YES_OPTION) {
                            selectRootNode.removeAllChildren();
                            selectRootNode.add(new DefaultMutableTreeNode("All"));
                            treeModel.reload();
                        }
                    }//if
                }//if
            }//if
        }*/
        logger.debug("actionPerformed(ActionEvent) - exit actionPerformed");
    }
    

    /** task that retrives, parses and adds to the display results of one siap service 
     * 
     * @todo refactor more of the commonality of Siap and Cone into the base class.*/
    private class SiapRetrieval extends AbstractRetreiver {

        public SiapRetrieval(ResourceInformation information, double ra, double dec, double raSize,double decSize) throws InvalidArgumentException, NotFoundException, URISyntaxException {
            super(ra,dec);
            this.information = (SiapInformation)information;
            siapURL = siap.constructQueryS(new URI(information.getAccessURL().toString()),ra, dec,raSize,decSize);
        }
        private final URL siapURL;
        private final SiapInformation information;
 
        public void run() {
            try {
                //              Create a tree of VOElements from the given request url.
                VOElement top = new VOElementFactory().makeVOElement( siapURL);
                // managed to fetch the resource ok, so le's create the service node.
                
                //place in a hashtable the id and siapurl to the votable.
                //the SIA prefix is used in case they select all SIA id's.
                storageTable.put("Images-" + information.getId().toString(),siapURL);                            
                final TreeNode riNode = new DefaultTreeNode();
                riNode.setAttribute("label",information.getTitle());
                riNode.setAttribute("id", "Images-" + information.getId());
                riNode.setAttribute("ra","0");
                riNode.setAttribute("dec","0");
                StringBuffer sb = new StringBuffer();
                sb.append("<html><p>Title: ").append(information.getTitle())
                    .append("<br>ID: ").append(information.getId())
                    .append("<br>Description: ").append(information.getDescription())                
                    .append("<br>Service Type: ").append(information.getImageServiceType())
                    .append("</p></html>");                        
                riNode.setAttribute("tooltip",sb.toString());
                // build subtree for this service
                buildNodes(top, riNode);
                // splice our subtree into the main tree.. do on the event dispatch thread, as this will otherwise cause 
                // concurrent modification exceptions
                if (riNode.getChildCount() > 0) { // found some results..
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        AstroScopeLauncherClusterImpl.this.setStatusMessage("Adding results from " + information.getName());
                        //getTree().addChild(new DefaultEdge(siaNode,riNode));                        
                    }
                });  
                }
            } catch (Exception e) {
                logger.warn("Failed to process " + information.getId(),e);
            }
        }
    } // end siap retriever
    
    /** taks that retreives, parses and adds to the display the results of one cone service */
    private class ConeRetrieval extends AbstractRetreiver {

        public ConeRetrieval(ResourceInformation information, double ra, double dec, double sz) throws InvalidArgumentException, NotFoundException, URISyntaxException {
            super(ra,dec);
            this.information = (ConeInformation)information;
            coneURL = cone.constructQuery(new URI(information.getAccessURL().toString()),ra,dec,sz);
        } 
        private final ConeInformation information;
        private final URL coneURL;

        public void run() {
            try {
                //                 Create a tree of VOElements from the given request url.
                VOElement top = new VOElementFactory().makeVOElement( coneURL);            
                storageTable.put("Catalogs-" + information.getId().toString(),coneURL);
                final TreeNode riNode = new DefaultTreeNode();
                riNode.setAttribute("label",information.getTitle());
                riNode.setAttribute("id", "Catalogs-" + information.getId());
                riNode.setAttribute("ra","0"); // dummies for the plot viualization.
                riNode.setAttribute("dec","0");                
                StringBuffer sb = new StringBuffer();
                sb.append("<html><p>Title: ").append(information.getTitle())
                    .append("<br>ID: ").append(information.getId())
                    .append("<br>Description: ").append(information.getDescription())
                    .append("</p></html>");                        
                riNode.setAttribute("tooltip",sb.toString());
                
                
                buildNodes(top, riNode);
                // splice our subtree into the main tree.. do on the event dispatch thread, as this will otherwise cause 
                // concurrent modification exceptions
                if (riNode.getChildCount() > 0) { // i.e. found some results.
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        AstroScopeLauncherClusterImpl.this.setStatusMessage("Adding results from " + information.getName());
                        //getTree().addChild(new DefaultEdge(coneNode,riNode));
                     }
                });
                }
            } catch (Exception e) {
                logger.warn("Failed to process " + information.getId(),e);
            }
        }
    }
    

    
    private double getOffset(double queryra, double querydec, double objectra, double objectdec) {
        //r = sqrt[ {X*cos(Y) - x*cos(x)}^2 + {Y - y}^2)
        return Math.sqrt(( 
                Math.pow( ((queryra * Math.cos(Math.toRadians(querydec))) - (objectra * Math.cos(Math.toRadians(objectra)))),2) + 
                Math.pow((querydec - objectdec),2) ));
    }
    
    /** base class for something that fetches a resource
     *  - extensible for siap, cone, and later ssap, etc.
     * @author Noel Winstanley nw@jb.man.ac.uk 28-Oct-2005
     *
     */
    private abstract class AbstractRetreiver implements Runnable {
        double ra;
        double dec;
        public AbstractRetreiver(double ra, double dec) {
            this.ra = ra;
            this.dec = dec;
        }
        
        protected TreeNode findNode(String label, TreeNode startNode) {
            if(startNode == null)
                startNode = rootNode;
            Iterator iter = startNode.getChildren();
            while(iter.hasNext()) {
                TreeNode n = (TreeNode)iter.next();
                if(n.getAttribute("label").equals(label)) {
                    return n;
                }
            }
            return null;
        }

        /** build a node for each result in the votable.
         * set them up as children of the <tt>riNode</tt> element - which is the node ofor the service.
         */
        protected void buildNodes(VOElement top, TreeNode riNode) throws IOException {
            // Find the first RESOURCE element using standard DOM methods.
            NodeList resources = top.getElementsByTagName( "RESOURCE" );
            Element resource = (Element) resources.item( 0 );
            //              Locate the TABLE children of this resource using one of the
            // VOElement convenience methods.
            VOElement vResource = (VOElement) resource;
            VOElement[] tables = vResource.getChildrenByName( "TABLE" );
            String tempAttr = null;
            
            for(int j = 0;j < tables.length;j++) {
                TableElement tableEl = (TableElement) tables[j];
                //Turn it into a StarTable so we can access its data.
                StarTable starTable = new VOStarTable( tableEl );   
                // first locate the column indexes for the columns we're interested in.
                int raCol = -1;
                int decCol = -1;
                int imgCol = -1;
                String[] titles = new String[starTable.getColumnCount()];
                for (int col = 0; col < starTable.getColumnCount(); col++) {
                    ColumnInfo columnInfo = starTable.getColumnInfo(col);
                    String ucd = columnInfo.getUCD();
                    if (ucd != null) {
                        if (ucd.equals("POS_EQ_RA_MAIN")) {
                            raCol = col;
                        } else if (ucd.equals("POS_EQ_DEC_MAIN")) {
                            decCol = col;
                        } else if (ucd.equals("VOX:Image_AccessReference")) {
                            imgCol = col;
                        }                                        
                    }
                    titles[col] = columnInfo.getName() + "(" + columnInfo.getUCD() + ")";
                }
                // check we've got enough to proceed.
                if (raCol < 0 || decCol < 0) {
                    continue; // on to the next table
                }                                
                //make a node for each row of the table.
                RowSequence rSeq = starTable.getRowSequence();
                try {
                    while (rSeq.hasNext()) {
                        rSeq.next();
                        Object[] row = rSeq.getRow();
                        String rowRa =row[raCol].toString();
                        String rowDec = row[decCol].toString();                                 
                        DefaultTreeNode valNode = new DefaultTreeNode();
                        valNode.setAttribute("label",rowRa + "," + rowDec);
                        valNode.setAttribute("ra",rowRa); // these might come in handy for searching later.
                        valNode.setAttribute("dec",rowDec); 
                        if (imgCol >= 0) {
                            valNode.setAttribute("url",row[imgCol].toString());
                        } 
                        StringBuffer tooltip = new StringBuffer();
                        tooltip.append("<html><p>").append(rowRa).append(", ").append(rowDec);
                        for (int v = 0; v < row.length; v++) {
                            tooltip.append("<br>")
                            .append(titles[v])
                            .append( ": ")
                            .append(row[v] == null ? "" : row[v].toString());
                        }
                        tooltip.append("</p></html>");
                        valNode.setAttribute("tooltip",tooltip.toString());
                        
                        double offset = getOffset(ra, dec, Double.valueOf(rowRa).doubleValue(), Double.valueOf(rowDec).doubleValue());
                        
                        String offsetVal = String.valueOf(offset);
                        offsetVal = offsetVal.substring(0,offsetVal.indexOf(".") + 3);
                        TreeNode discoverNode = findNode(offsetVal, null);
                        if(discoverNode == null) {
                            DefaultTreeNode offsetNode = new DefaultTreeNode();
                            offsetNode.setAttribute("label",offsetVal);
                            offsetNode.setAttribute("offset",offsetVal);
                            if(riNode.getAttribute("id") != null) {
                                offsetNode.setAttribute("id",riNode.getAttribute("id"));
                            }
                            //((NodeItem)offsetNode).setSize(offsetNode.getSize() + (360/offset));
                            
                            getTree().addChild(new DefaultEdge(rootNode,offsetNode));
                            
                            //ra,dec not found it must be new.
                            //valNode.addChild(new DefaultEdge(valNode,riNode));
                            offsetNode.addChild(new DefaultEdge(offsetNode, valNode));
                            
                        }else {
                            //System.out.println("found discovernode");                            
                            if(riNode.getAttribute("id") != null) {
                                tempAttr = discoverNode.getAttribute("id");
                                if(tempAttr == null)
                                    tempAttr = riNode.getAttribute("id");
                                else {
                                    if(!tempAttr.endsWith(riNode.getAttribute("id")))
                                        tempAttr += "|" + riNode.getAttribute("id");
                                }
                                discoverNode.setAttribute("id",tempAttr);
                            }
                            TreeNode checkNode = findNode(rowRa + "," + rowDec, discoverNode);
                            if(checkNode == null) {
                                getTree().addChild(new DefaultEdge(discoverNode,valNode));
                            }else {
                                if(valNode.getAttribute("url") != null) {
                                    tempAttr = checkNode.getAttribute("url");
                                    if(tempAttr == null)
                                        tempAttr = valNode.getAttribute("url");
                                    else
                                        tempAttr += "|" + valNode.getAttribute("url");
                                    checkNode.setAttribute("url",tempAttr);
                                }
                            }//else
                        }
                    }//while rows in table.
                }finally{
                    rSeq.close();
                }
            }
        }
    }

                       
    
    /**
     * method: query
     * description: Queries the registry for sia and conesearch types and begins working on them.
     *
     *
     */
    private void query() {
        logger.debug("query() - inside query method)");
        storageTable.clear();
        clearTree();
        
        // @todo refactor this string-munging methods.
        final String position = getPosition();
        final double ra = getRA(position);
        final double dec = getDEC(position);
        final String region = regionText.getText().trim();
        final double size = needsParsedRegion() ? getRA(region) : getConeRegion();
        final double raSize = needsParsedRegion() ?  getRA(region) : Double.parseDouble(region);
        final double decSize= needsParsedRegion() ? getDEC(region) : raSize;
                    
        (new BackgroundWorker(AstroScopeLauncherClusterImpl.this,"Searching For Services") {
            private ResourceInformation[] siaps = new ResourceInformation[]{};
            private ResourceInformation[] cones = new ResourceInformation[]{};
            protected Object construct() throws Exception {
                // query registry for resources.
                // for each resource found, create a new task to retrieve, parse and add it to the display.
                // each task is sent to the executor - which queues them and then executes them on 5 threads.
                // this is better than writing a single thread that loops over all tasks, as a network-block on one service
                // only holds up it's own thread - the others continue regardless.
                if (siaCheckBox.isSelected()) {
                    siaps = reg.adqlSearchRI(siap.getRegistryQuery());                    
                    for (int i = 0; i < siaps.length; i++) {
                        if (siaps[i].getAccessURL() != null) {
                            try {
                            executor.execute(new SiapRetrieval(siaps[i],ra,dec,raSize,decSize));
                            } catch (Exception e) {
                                // carry on
                                logger.info("Failed to start task",e);
                            }
                        }
                    }
                } 
                if (coneCheckBox.isSelected()) {
                    cones = reg.adqlSearchRI(cone.getRegistryQuery());
                    for (int i = 0; i < cones.length; i++) {
                        if (cones[i].getAccessURL() != null) {
                            try {
                            executor.execute(new ConeRetrieval(cones[i],ra,dec,size));
                            } catch (Exception e) {
                                logger.info("Failed to start task",e);
                            }
                        }
                    }                        
                }
                // finally add another task to the executor queue - all it does iis put a status message 'completed'
                // as there's 5 background threads, it might be run while there's still 4 other queries running,
                // but it's better than nothing..
                executor.execute(new Runnable() {
                    public void run() {
                        // meh. got to put it on the event thread.
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                AstroScopeLauncherClusterImpl.this.setStatusMessage("Completed querying servers");
                            }
                        });
                    }
                });
                return null;
            }
            protected void doFinished(Object result) {
                // just report what we found - querying will have already started.
               AstroScopeLauncherClusterImpl.this.setStatusMessage("Found " + siaps.length + " image servers, "+ cones.length + " catalogue servers");                                     
            }
        }).start();        
    }

    
    /**
     * Small class used as TreeNodes in the JTREE not the graph.
     * 
     */
    class TreeNodeData {
        String label = null;
        String id = null;
        String url = null;
        public TreeNodeData(String label, String id, String url) {
            this.label = label;
            this.id = id;
            this.url = url;
        }
        
        public String getLabel() {
            return this.label;
        }
        
        public String getID() {
            return this.id;
        }
        
        public String getURL() {
            return this.url;
        }
        
        public String toString() {
            return this.label;
        }
        
    }
    
    
    /**
     * Small MultiSelectionControl that extends the prefuse one.  In general MultiSelectFocusControl from
     * prefuse allows a user to hold shift and select multiple nodes.  But in general I don't care about that to 
     * much, even though it is probably a nice feature.  The main purpose of this class is to detect when a node
     * is selected/deselected and do the appropriate action to a JTree that handles what the user selected.  So it 
     * will add to the Jtree on new nodes and take them out on nodes they clicked a 2nd time(deselect).  Also clear
     * the tree if need be if the user clicked on an empty space.
     * @author Kevin Benson
     *
     * TODO To change the template for this generated type comment go to
     * Window - Preferences - Java - Code Style - Code Templates
     */
    class AstroScopeMultiSelectionControl extends MultiSelectFocusControl {
        
        public AstroScopeMultiSelectionControl(ItemRegistry registry) {
            super(registry);
        }
        
        public AstroScopeMultiSelectionControl(ItemRegistry registry, java.lang.Object focusKey) {
            super(registry,focusKey);
        }
        
        public void itemClicked(VisualItem item, java.awt.event.MouseEvent e) {
            logger.debug("the itemClicked happened tostring of item = " + item.toString());
            //okay do the stuff to the graph first.
            super.itemClicked(item,e);
            
            if(e.getClickCount() < 2) {
                return;                
            }
            
            //ge the label.
            String label = item.getAttribute("label");
            TreePath tp;
            DefaultMutableTreeNode temp;            
            DefaultMutableTreeNode child;
            //User Chose AstroScope which is the top level of everything hence "all".
            
            ItemRegistry registry = item.getItemRegistry();
            Entity entity = registry.getEntity(item);
            //Okay it is not All, they are selecting something unique.
            if(entity instanceof edu.berkeley.guir.prefuse.graph.TreeNode) {
                //System.out.println("yes it was a defaulttreenode");
                edu.berkeley.guir.prefuse.graph.TreeNode edgeNode = (edu.berkeley.guir.prefuse.graph.TreeNode)entity;
                Vector treePath = new Vector();
                
                //check if we are the root (hence clicked on the root of the tree.
                if(edgeNode.getParent() == null) {
                    //Check if there are children already in the tree,
                    //if they are and if there is already an All then he is deselecting.
                    //if there are other children make sure he wants to clear things and put an "All"
                    if(selectRootNode.getChildCount() >= 1) {
                        if(!selectRootNode.getFirstChild().toString().equals("All")) {
                            int result = JOptionPane.showConfirmDialog(AstroScopeLauncherClusterImpl.this, 
                                    "You have selected the top/root of the tree; (which is everything) this will remove all other selections.  Is this correct?",
                                    "Clear Selection", JOptionPane.YES_NO_OPTION );
                            if (result == JOptionPane.YES_OPTION) {
                                selectRootNode.removeAllChildren();
                                selectRootNode.add(new DefaultMutableTreeNode("All"));
                                treeModel.reload();
                            }
                        }else {
                            //user is deselecting.
                            selectRootNode.removeAllChildren();
                            treeModel.reload();
                        }//else
                    }else {
                        //before we blindly put "All" in. lets make sure they have done some kind of query
                        //hence there is data on the graph.
                        if(storageTable.size() > 0) {
                            selectRootNode.removeAllChildren();
                            selectRootNode.add(new DefaultMutableTreeNode("All"));
                            treeModel.reload();                        
                        }
                    }
                    return;
                }//if
                
                //double check if "all" is already in the tree and they obviosly clicked
                //on something else then remove it.  
                if(selectRootNode.getChildCount() >= 1 &&
                   selectRootNode.getFirstChild().toString().equals("All")) {
                    selectRootNode.removeAllChildren();
                }
                
                
                
                //Okay put in a vector the nodes starting with selected node then its parents all the way up
                //to AstroScope.  This is talking about nodes in the graph.
                while(edgeNode.getParent() != null) {
                    treePath.add(new TreeNodeData(edgeNode.getAttribute("label"),
                            edgeNode.getAttribute("id"),
                            edgeNode.getAttribute("url")));                    
                    //treePath.add(label);
                    //edgeNode = edgeNode.getEdge(0).getFirstNode();
                    //edgeNode = (Node)edgeNode.getNeighbors().next();
                    edgeNode = edgeNode.getParent();
                    label = edgeNode.getAttribute("label");
                }
                
                //System.out.println("done with filling treepath its size = " + treePath.size());
                

                //TODO  (maybe rethink) seems to work but looks confusing.
                //below is a loop that will add nodes to the JTree.
                //looks at the vector and goes through it once it finds something not
                //already recognized in a tree it starts adding all the nodes.
                //if it goes through the whole vector and everything matches then
                //they are deslecting remove that node.                  
                boolean hasNode = true;
                boolean foundNode = false;
                DefaultMutableTreeNode parent = selectRootNode;
                child = null;
                int index = 0;
                for(int i = treePath.size() - 1;i >= 0;i--) {
                    //System.out.println("in the treepath for loop i = " + i + " val = " + treePath.get(i));
                    if(hasNode) {
                        hasNode = false;
                        //parent.
                        for (Enumeration childEnum = parent.children() ; childEnum.hasMoreElements() ;) {
                            child = (DefaultMutableTreeNode)childEnum.nextElement();
                            if(treePath.get(i).toString().equals(child.toString())) {
                                parent = child;
                                hasNode = true;
                            }
                        }
                    }
                    if(!hasNode) {
                        //System.out.println("parent will be adding: " + treePath.get(i) + " the parent = " + parent.toString());
                        child = new DefaultMutableTreeNode(treePath.get(i));
                        parent.add( child );
                        parent = child;
                    }
                }//for
                if(child != null) {
                    treeModel.reload();
                    selectTree.scrollPathToVisible(new TreePath(child.getPath()));
                }
                //System.out.println("hasNode = " + hasNode);
                if(hasNode){                    
                    //if you went through the whole tree and it is still there,
                    //then they must be deselecting it.  So remove it.
                    //temp = parent.getParent();
                    if(parent.getChildCount() > 0) {
                        int result = JOptionPane.showConfirmDialog(AstroScopeLauncherClusterImpl.this, 
                                "You have chosen to remove an item that has children, all those children will be removed from the selection tree. Is this ok?  Item: " + parent.toString(),
                                "Remove Selection", JOptionPane.YES_NO_OPTION );
                        if (result == JOptionPane.YES_OPTION) {
                            parent.removeFromParent();
                        }
                    }
                    parent.removeFromParent();
                    /*
                    while(temp != null && temp.getChildCount == 0) {
                        child = temp;
                        temp = temp.getParent();
                        child.removeFromParent();
                    }
                    */
                    treeModel.reload();
                }//if
            }//if
            //check there is some node in the tree and enable save buttons.
            enableSaveButtons();
        }
        
        private void enableSaveButtons() {
            if(storageTable.size() > 0 && selectRootNode.getChildCount() > 0) {
                //selectAllButton.setEnabled(true);
                saveButton.setEnabled(true);
                //saveImageButton.setEnabled(true);                
                clearTreeButton.setEnabled(true);
            }else {
                //selectAllButton.setEnabled(false);
                saveButton.setEnabled(false);
                //saveImageButton.setEnabled(false);
                clearTreeButton.setEnabled(false);
            }
        }
        
        /*
         * User regular mouse click in an empty space before blindly removing things ask the user if there are nodes
         * already in the tree.
         */
        public void mouseClicked(java.awt.event.MouseEvent e) {
            logger.debug("entered mouseClicked");
            //add a JoptionPane to make sure they want to do it if the tree is big.
            if(selectRootNode.getChildCount() > 0) {
                int result = JOptionPane.showConfirmDialog(AstroScopeLauncherClusterImpl.this, 
                        "You have clicked in an empty area on the display, this clears everything.  Confirm Clear Entire Selection Tree? ", 
                        "Clear Selection", JOptionPane.YES_NO_OPTION );
                if (result == JOptionPane.NO_OPTION) {
                    return;
                }                
            }
            super.mouseClicked(e);
            selectRootNode.removeAllChildren();
            treeModel.reload();
            enableSaveButtons();
        }
    }
    
    /**
     * Rest of classes below come from prefuse.
     */    

       
    
    /** @todo maybe later we could color notdes based on some quality of the node - e.g.
     * maybe waveband of the result? or something like this that could be set as an attribute of the node 
     * when it's created, and then is used by this function to determine the colour.
     *
     */
    public static class DemoColorFunction extends ColorFunction {
        private Color graphEdgeColor = Color.LIGHT_GRAY;
         private Color highlightColor = new Color(50,50,255);
         private Color focusColor = new Color(255,50,50);
         private ColorMap colorMap;
       
        public DemoColorFunction(int thresh) {
            colorMap = new ColorMap(
                ColorMap.getInterpolatedMap(thresh+1, Color.RED, Color.BLACK),
                0, thresh);
        } //
       
        public Paint getFillColor(VisualItem item) {
            if ( item instanceof NodeItem ) {
                return Color.WHITE;
            } else if ( item instanceof AggregateItem ) {
                return Color.LIGHT_GRAY;
            } else if ( item instanceof EdgeItem ) {
                return getColor(item);
            } else {
                return Color.BLACK;
            }
        } //
       
        public Paint getColor(VisualItem item) {
            if ( item.isFocus() ) {
                return focusColor;
            } else if ( item.isHighlighted() ) {
                 return highlightColor;
             } else if (item instanceof NodeItem) {
                 int d = ((NodeItem)item).getDepth();
                 return colorMap.getColor(d);
            } else if (item instanceof EdgeItem) {
                EdgeItem e = (EdgeItem) item;
                if ( e.isTreeEdge() ) {
                    int d, d1, d2;
                     d1 = ((NodeItem)e.getFirstNode()).getDepth();
                     d2 = ((NodeItem)e.getSecondNode()).getDepth();
                     d = Math.max(d1, d2);
                     return colorMap.getColor(d);
                } else {
                    return graphEdgeColor;
                }
            } else {
                return Color.BLACK;
            }
        } //
    } // end of inner class DemoColorFunction
    

  
}

/* 
$Log: AstroScopeLauncherClusterImpl.java,v $
Revision 1.2  2005/11/03 15:59:19  KevinBenson
small changes for tying this cluster stuff with myspace

Revision 1.1  2005/11/03 11:56:49  KevinBenson
added a new astroscope cluster

Revision 1.7  2005/11/02 09:50:11  KevinBenson
should have Noel's 2 minor fixes.  Plus a couple of additions for buttons and node selections

Revision 1.5  2005/11/01 14:40:20  KevinBenson
Some small changes to have hyperbolic working with selections and saving to myspace

Revision 1.3  2005/10/31 16:13:51  KevinBenson
added hyperbolic in there, plus the saving to myspace area.

Revision 1.2  2005/10/31 12:49:38  nw
rehashed downloading mechanism,
put in a bunch of sample vizualizations.

Revision 1.1  2005/10/26 15:53:15  KevinBenson
new astroscope being added into the workbench.

Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.2.2.2  2005/10/10 18:12:36  nw
merged kev's datascope lite.

Revision 1.2  2005/10/10 12:09:45  KevinBenson
small change to the astroscope to show browser and vospace when the user hits okay

Revision 1.1  2005/10/04 20:46:48  KevinBenson
new datascope launcher and change to module.xml for it.  Vospacebrowserimpl changes to handle file copies to directories on import and export

Revision 1.4  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.3  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:33  nw
first release of application launcher
 
*/