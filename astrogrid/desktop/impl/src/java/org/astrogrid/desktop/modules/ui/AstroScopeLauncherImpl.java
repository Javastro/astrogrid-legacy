/*$Id: AstroScopeLauncherImpl.java,v 1.18 2005/11/10 10:43:13 KevinBenson Exp $
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
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.SiapInformation;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.acr.nvo.ConeInformation;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.AstroScopeLauncher;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.io.Piper;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import flanagan.math.Fmath;

import org.apache.commons.lang.WordUtils;

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
import edu.berkeley.guir.prefuse.activity.ActivityMap;
import edu.berkeley.guir.prefuse.activity.SlowInSlowOutPacer;
import edu.berkeley.guir.prefuse.collections.BreadthFirstTreeIterator;
import edu.berkeley.guir.prefuse.collections.DOIItemComparator;
import edu.berkeley.guir.prefuse.event.ControlAdapter;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.DefaultFocusSet;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;
import edu.berkeley.guir.prefuse.graph.event.GraphEventAdapter;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTranslation;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTranslationEnd;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTreeLayout;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicTreeMapper;
import edu.berkeley.guir.prefuse.hyperbolictree.HyperbolicVisibilityFilter;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultNodeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.ImageFactory;
import edu.berkeley.guir.prefuse.render.NullRenderer;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.berkeley.guir.prefuse.render.RendererFactory;
import edu.berkeley.guir.prefuse.render.ShapeRenderer;
import edu.berkeley.guir.prefuse.render.TextImageItemRenderer;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/** Implementation of the Datascipe launcher
 * 
 * @todo display thumbnails of images - urls are available under 'imgUrl' attribute - use ImageFactory to retrieve them, display in pane under search buttons.
 * @todo hyperbolic doesn't always update to display nodes-to-download as yellow. need to add a redraw in somewhere. don't want to redraw too often though.
 */
public class AstroScopeLauncherImpl extends UIComponent implements AstroScopeLauncher, ActionListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AstroScopeLauncherImpl.class);

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
    //private Hashtable storageTable;
    
    
    //Various gui components.
    private JTextField posText = null;
    private JTextField regionText = null;
    private JButton submitButton = null;           
    protected final ResourceChooserInternal chooser;
    
    //JButton saveImageButton;
    JButton saveButton;
  
    
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
    public AstroScopeLauncherImpl(UIInternal ui, Configuration conf, HelpServerInternal hs,  
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
        
        // configure vizualizations.
        vizualizations = new Vizualization[]{
                new WindowedRadial()
                , new Hyperbolic()
             , new ConventionalTree()  // not working yet.
        };
        // register each vizualization as a listener
        for (int i = 0; i < vizualizations.length; i++) {
            getTree().addGraphEventListener(vizualizations[i]);
        }
        
        // configure execution system
        nodeSizingMap = Collections.synchronizedMap(new java.util.TreeMap());
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
        final URI u = chooser.chooseResourceWithParent("Save Data",true,true,true,false,AstroScopeLauncherImpl.this);
        if (u == null) {
            return;
        }
        
        logger.debug("the uri chosen = " + u.toString());
        (new BackgroundOperation("Saving Data") {
            protected Object construct() throws Exception {
                //@todo KEVIN - this parameter checking and showing message dialogs musn't be done in a background operation
                // do it before entering the background op.
                File file = null;
                //make sure they chose a directory if not then return.
                if(u.toString().startsWith("file:")) {
                    file = new File(u);
                    if(!file.isDirectory()) {
                        JOptionPane.showMessageDialog(AstroScopeLauncherImpl.this, 
                                "You can only save data to a folder or directory, nothing was saved.",
                                "No Directory Selected", JOptionPane.OK_OPTION );
                        return null;
                    }                             
                }else {
                    FileManagerNode fmn = myspace.node(u);
                    if(!fmn.isFolder()) {
                        JOptionPane.showMessageDialog(AstroScopeLauncherImpl.this, 
                                "You can only save data to a folder or directory, nothing was saved",
                                "No Directory Selected", JOptionPane.OK_OPTION );
                        return null;
                    }                             
                }//else
                
                Set processedServices = new HashSet();
                String []urls;
                InputStream is = null;
                OutputStream os = null;
                for (Iterator i = getSelectionFocusSet().iterator(); i.hasNext(); ) {
                    final TreeNode tn = (TreeNode)i.next();
                    // find each leaf node - these are the data points.
                    if (tn.getChildCount() > 0) {
                        continue;
                    }
                    TreeNode parent = tn.getParent(); // @todo with clustering, need grandparent here instead.                                
                    try {
                    // ok. got a bit of selected data. if it's an image, save it.
                        if (tn.getAttribute("imgURL") != null) { // its siap - this is a reference to the image  
                            urls = tn.getAttribute("imgURL").split("\\|");
                            for(int j = 0;j < urls.length;j++) {
                                //System.out.println("the image urls = " + urls[j]);
                                URL url = new URL(urls[j]); // url to the image.
                                String name = conformToMyspaceName(parent.getParent().getAttribute("label") + "_" + parent.getAttribute("label") +"_" + tn.getAttribute("label") + "_" + System.currentTimeMillis() + "." + tn.getAttribute("type"));
                                final String finalName = name;
                                // @todo this will be different when clustering is introduced.
                                // @todo not sure whether the label of the image node is enough to uniquely identify it - maybe we should use the row number of it within the votable siap response,
                                // and save the siap response too - as it's
                                URI finalURI = new URI(u.toString() + "/" + name);
                                
                                SwingUtilities.invokeLater(
                                        new Runnable() {
                                            public void run() {
                                                AstroScopeLauncherImpl.this.setStatusMessage("Saving Image: " + finalName);
                                            }
                                        }
                                );                            
                                if(finalURI.getScheme().startsWith("ivo")) {
                                    myspace.copyURLToContent(url,finalURI);
                                }else {
                                    //@todo close streams afterwards.
                                    try {
                                        is = url.openStream();
                                        os = myspace.getOutputStream(finalURI);
                                        Piper.pipe(is, os);
                                    }finally {
                                        if(is != null)
                                            is.close();
                                        if(os != null)
                                            os.close();
                                    }//try&finally
                                }//else
                            }//for
                        } 
                        
                        // save the parent document - either a cone, or the siap response - which describes all the images.                        
                        // parent holds the url for this catalog. - @todo - this will be different when clustering is introduced. url will probably be with grandparent node then.
                        // check we've not processed this catalog already - because the user has selected another child node..
                        if (processedServices.contains(parent)) {
                            continue;
                        }
                        processedServices.add(parent); // record that we've done this catalog now.
                        if(parent.getAttribute("url") == null) {
                            parent = parent.getParent();
                            if (processedServices.contains(parent)) {
                                continue;
                            }
                            processedServices.add(parent); // record that we've done this catalog now.                            
                        }
                        URL url = new URL(parent.getAttribute("url"));
                        final String name = conformToMyspaceName(parent.getAttribute("label") + ".vot");
                        URI finalURI = new URI(u.toString() +"/" + name);
                        SwingUtilities.invokeLater(
                                new Runnable() {
                                    public void run() {
                                        AstroScopeLauncherImpl.this.setStatusMessage("Saving Table: " + name);
                                    }
                                }
                        );                                 
                        if(finalURI.getScheme().startsWith("ivo")) {
                            myspace.copyURLToContent(url,finalURI);
                        }else {
                            //KEVIN - need to close streams in a finally block.
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
                    } catch (Exception e) {
                        //e.printStackTrace();
                        // @todo should it show an error, or just report??
                        SwingUtilities.invokeLater(
                                new Runnable() {
                                    public void run() {
                                        AstroScopeLauncherImpl.this.setStatusMessage("Failed to save " + tn.getAttribute("label"));
                                    }
                                }
                        );                                 
                    }
                }                
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
            String result =  getPosition(pos);
            if (result == null) {
                return getPosition(getPositionFromObject());
            } else {
                return result;
            }
    }
    
    // better to return null if we're just going to catch it straght away.
    private String getPosition(String pos)  {
        if(pos == null || pos.trim().length() == 0) {
            return null;
        }
        String expression = "\\+?-?\\d+\\.?\\d*,\\+?-?\\d+\\.?\\d*";
        if(pos.matches(expression)) {            
            return pos;            
        } else {
            return null;
        }
    }
    
    /**
     * method: getPositionFromObject
     * Description: Queries CDS-Simbad service for a position in the sky based on a object name.  This is typically 
     * called if the user enters an invalid position then it will attempt a lookup.
     * @return position in the sky based on a object name.
     */
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
            //e.printStackTrace();
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
     * @return JPanel consisting of the query gui and custom controls typically placedo on the WEST side of the main panel.
     * @todo probably needs to use a gridbaglayout instead of all these other panels within panels.
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
        
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(this);

        scopeMain.add(new JLabel("Position/Object: "));
        scopeMain.add(posText);
        scopeMain.add(new JLabel("Region: "));
        scopeMain.add(regionText);
        scopeMain.add(siaCheckBox);
        scopeMain.add(coneCheckBox);
        scopeMain.add(submitButton);
        scopeMain.add(saveButton);
        
        wrapPanel.add(scopeMain);
        
    
        JPanel graphControlsPanel = new JPanel();
        graphControlsPanel.setMaximumSize(dim2);
        graphControlsPanel.setPreferredSize(dim2);
        Dimension buttonDim = new Dimension(80,20);
        reFocusTopButton = new JButton("Go to Top");
        reFocusTopButton.setPreferredSize(buttonDim);
        reFocusTopButton.setMaximumSize(buttonDim);
        graphControlsPanel.setLayout(new GridLayout(3,1));
        //graphControlsPanel.add(new JLabel("<html><body><b>Custom Graph Controls:</b></body></html>"), BorderLayout.NORTH);
        graphControlsPanel.add(new JLabel("<html><body><b>Custom Graph Controls:</b></body></html>"));
        graphControlsPanel.add(reFocusTopButton);
        graphControlsPanel.add(new JPanel());
        reFocusTopButton.addActionListener(this);
        reFocusTopButton.setEnabled(false);        
        wrapPanel.add(graphControlsPanel);
        
        wrapPanel.add(new JPanel());

        return wrapPanel;
    }
    
    private JButton reFocusTopButton;

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
                      
            siaNode = new DefaultTreeNode();
            siaNode.setAttribute("label","Images");
            siaNode.setAttribute("ra","0");
            siaNode.setAttribute("dec","0");
       
            coneNode  = new DefaultTreeNode();
            coneNode.setAttribute("label","Catalogues");
            coneNode.setAttribute("ra","0");
            coneNode.setAttribute("dec","0");
            
            DefaultEdge siaEdge = new DefaultEdge(rootNode,siaNode);
            siaEdge.setAttribute("weight","3");
            rootNode.addChild(siaEdge);
            DefaultEdge coneEdge = new DefaultEdge(rootNode,coneNode);
            coneEdge.setAttribute("weight","3");
            rootNode.addChild(coneEdge);
            
               
            tree = new DefaultTree(rootNode);            
        }
        return tree;
    }
    
    /** removes previous results, just leaving the skeleton */
    private void clearTree() {
        // reset selection too.
        nodeSizingMap.clear();
        getSelectionFocusSet().clear();
        siaNode.removeAllChildren();
        coneNode.removeAllChildren();
        
        //just for sanity go make sure the remaining nodes which are really just
        //siaNode, rootNode, coneNode are set to false for the selected attribute.
        for (Iterator i = getTree().getNodes(); i.hasNext(); ) {
            Node n = (Node)i.next();
            n.setAttribute("selected","false");
        }        
        // register each vizualization as a listener
        //getSelectionFocusSet().set(rootNode);
        //
        refocusMainNodes();
  
    }
    
    /**
     * method: refocusMainNodes()
     * description: Refocuses the graph on the main root node.  Typically called right before a query or user
     * hits a particular button.  Seems to focus better if it focuses on cone and sia tree nodes first before root.
     *
     */
    private void refocusMainNodes() {
        for (int j =0; j < vizualizations.length; j++) {
            vizualizations[j].getItemRegistry().getFocusManager().getDefaultFocusSet().set(coneNode);
            vizualizations[j].getItemRegistry().getFocusManager().getDefaultFocusSet().set(siaNode);
            vizualizations[j].getItemRegistry().getFocusManager().getDefaultFocusSet().set(rootNode);
        }    
    }
    
    /**
     * method: reDrawGraphs
     * description: Calls Redraw on all the displays/visualizations typically called after a refocus and needs to
     * draw the new focus on the graph.  
     * Note: clearTree does not call it because nodes are typically added from a query causing the graph to be 
     * redrawn anyways.
     *
     */
    private void reDrawGraphs() {
        for (int j =0; j < vizualizations.length; j++) {
            vizualizations[j].reDraw();
        }
    }
    
    /** strategy class - subclasses encapsulate one vizualization - private item registry
     * (as this contains the dispplay-specific info), but a shared tree of data objects - so 
     * each viz provides a different 'view' of the same data.
     * 
     *
     */
    
    private ImageFactory imageFactory;
    //shared instance - used in both vizualizations.
    private ImageFactory getImageFactory() {
        if (imageFactory == null) {
            imageFactory = new ImageFactory(75,75); // small thumbnails.
            
        }
        return imageFactory;
    }
    
    private abstract class Vizualization extends GraphEventAdapter {
        public Vizualization(String name) {
            this.name = name;           
        }
        public final String getName() {
            return name;
        }
        final private String name;
    private ItemRegistry itemRegistry;
   /** create default node renderer */
    private TextImageItemSizeRenderer nodeRenderer;
    protected final TextImageItemRenderer getTextRenderer() {
        if (nodeRenderer == null) {
            nodeRenderer = new TextImageItemSizeRenderer() ;
            nodeRenderer.setMaxTextWidth(75);
            nodeRenderer.setRoundedCorner(8,8);
            nodeRenderer.setTextAttributeName("label");
            nodeRenderer.setImageAttributeName("img");
            nodeRenderer.setMaxImageDimensions(50,50);
            nodeRenderer.setAbbrevType(StringAbbreviator.TRUNCATE);
            nodeRenderer.setImageFactory(getImageFactory());
        }
        return nodeRenderer;
    }
    
    protected final ItemRegistry getItemRegistry() {
        if (itemRegistry == null) {
            itemRegistry = new ItemRegistry(getTree());
            itemRegistry.getFocusManager().putFocusSet(FocusManager.SELECTION_KEY,getSelectionFocusSet());
            
            DefaultEdgeRenderer edgeRenderer = new DefaultEdgeRenderer();
            edgeRenderer.setWeightAttributeName("weight");
            edgeRenderer.setWeightType(DefaultEdgeRenderer.WEIGHT_TYPE_LINEAR);
            itemRegistry.setRendererFactory(new DefaultRendererFactory(getTextRenderer(), edgeRenderer));            
        }
        return itemRegistry;
    }
    /** access the display for this vizualization */
    public abstract Display getDisplay();
    
    /** access the display for this vizualization */
    public abstract void reDraw();
    
    }
        
    /** focus set used to maintain list of nodes selected for download.
     * focus set is shared between vizualizaitons- so changes in one will be seen in others.
     */
    private FocusSet selectionFocusSet;

    private TreeNode rootNode;
    private FocusSet getSelectionFocusSet() {
        if (selectionFocusSet == null) {
            selectionFocusSet = new DefaultFocusSet();
            selectionFocusSet.addFocusListener(new FocusListener() {                
                public void focusChanged(FocusEvent arg0) {                    
                   saveButton.setEnabled(selectionFocusSet.size() > 0);
                }
            });
        }
        return selectionFocusSet;
    }
    
         
    /** vizualization that hides some of the data some of the time 
     * based on edu.berkeley.guir.prefuse.action.filter.WindowedTreeFilter
     * 
     * needs to have it's nodes spaced out more.
     * 
     * */
    public class Hyperbolic extends Vizualization {
            
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
        
        public void reDraw() {
            actmap.runNow("filter");
        }
        
        public Display getDisplay() {
        
            if(display == null) {
                actmap = new ActivityMap();
                ItemRegistry registry = getItemRegistry();
                display = new Display();


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
               edgeRenderer.setWeightAttributeName("weight");
               edgeRenderer.setWeightType(DefaultEdgeRenderer.WEIGHT_TYPE_LINEAR);               
                
                // set the renderer factory
               registry.setRendererFactory(new DemoRendererFactory(
                    getTextRenderer(), nodeRenderer2, edgeRenderer));
                
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
               display.addControlListener(new DoubleClickMultiSelectFocusControl());
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
            private Color selectedColor = Color.YELLOW;
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
                    String attr=  item.getAttribute("selected");                
                    if (attr != null && attr.equals("true")) {
                        return selectedColor;
                    } else {               
                        return Color.WHITE;
                    }
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
               
               // add jitter to layout nodes better. could maybe make the jitter larger - makes the nodes less
               // likely to overlap.
               
               ForceSimulator fsim = new ForceSimulator();
               fsim.addForce(new NBodyForce(-0.1f,15f,0.5f));
               fsim.addForce(new DragForce());
               
               ActionList forces = new ActionList(registry,1000);
               forces.add(new ForceDirectedLayout(fsim,true));
               forces.add(new RepaintAction());
               forces.alwaysRunAfter(animate);
               
               
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
               display.addControlListener(new DoubleClickMultiSelectFocusControl());
               
               registry.getFocusManager().putFocusSet(
                       FocusManager.HOVER_KEY, new DefaultFocusSet());
                }
               return display;
        }

        // refresh display when new item added
        public void nodeAdded(Graph arg0, Node arg1) {
            graphLayout.runNow();
        }
        
        // refresh display when new item added
        public void reDraw() {
            graphLayout.runNow();
        }
    } /// end windowed radial vizualization
    
    
    
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
        
        public void reDraw() {
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
        }else if(source == reFocusTopButton) {
            refocusMainNodes();
            reDrawGraphs();
        }

        logger.debug("actionPerformed(ActionEvent) - exit actionPerformed");
    }
    
    private static final int TOOLTIP_WRAP_LENGTH = 50;
    

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
                          
                final TreeNode riNode = new DefaultTreeNode();
                riNode.setAttribute("label",information.getTitle());
                riNode.setAttribute("weight","2");
                riNode.setAttribute("id", information.getId().toString());
                riNode.setAttribute("url",siapURL.toString());
                riNode.setAttribute("ra","0");
                riNode.setAttribute("dec","0");
                if (information.getLogoURL() != null) {
                    riNode.setAttribute("img",information.getLogoURL().toString());
                }
                StringBuffer sb = new StringBuffer();
                sb.append("<html>Title: ").append(information.getTitle())
                    .append("<br>ID: ").append(information.getId())
                    .append("<br>Description: <p>")
                    .append(information.getDescription()!= null ?   WordUtils.wrap(information.getDescription(),TOOLTIP_WRAP_LENGTH,"<br>",false) : "")                
                    .append("</p><br>Service Type: ").append(information.getImageServiceType())
                    .append("</html>");                        
                riNode.setAttribute("tooltip",sb.toString());
                // build subtree for this service
                buildNodes(top, riNode);
                // splice our subtree into the main tree.. do on the event dispatch thread, as this will otherwise cause 
                // concurrent modification exceptions
                if (riNode.getChildCount() > 0) { // found some results..
                SwingUtilities.invokeLater(new Runnable() { // has to add nodes on event dispatch thread.
                    public void run() {
                        AstroScopeLauncherImpl.this.setStatusMessage("Adding results from " + information.getName());
                        DefaultEdge siaEdge = new DefaultEdge(siaNode,riNode);
                        siaEdge.setAttribute("weight","2");
                        getTree().addChild(siaEdge);                                               
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

                final TreeNode riNode = new DefaultTreeNode();
                riNode.setAttribute("label",information.getTitle());              
                riNode.setAttribute("id", information.getId().toString());
                riNode.setAttribute("url",coneURL.toString());
                riNode.setAttribute("ra","0"); // dummies for the plot viualization.
                riNode.setAttribute("dec","0");     
                if (information.getLogoURL() != null) {
                    //System.out.println("found an image!!");
                    riNode.setAttribute("img",information.getLogoURL().toString());
                }
                StringBuffer sb = new StringBuffer();
                sb.append("<html>Title: ").append(information.getTitle())
                    .append("<br>ID: ").append(information.getId())
                    .append("<br>Description: <p>")
                    .append(information.getDescription()!= null ?   WordUtils.wrap(information.getDescription(),TOOLTIP_WRAP_LENGTH,"<br>",false) : "")
                    .append("</p></html>");                        
                riNode.setAttribute("tooltip",sb.toString());
                                
                buildNodes(top, riNode);
                // splice our subtree into the main tree.. do on the event dispatch thread, as this will otherwise cause 
                // concurrent modification exceptions
                if (riNode.getChildCount() > 0) { // i.e. found some results.
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        AstroScopeLauncherImpl.this.setStatusMessage("Adding results from " + information.getName());
                        DefaultEdge coneEdge = new DefaultEdge(coneNode,riNode);
                        coneEdge.setAttribute("weight","2");
                        getTree().addChild(coneEdge);
                     }
                });
                }
            } catch (Exception e) {
                logger.warn("Failed to process " + information.getId(),e);
            }
        }
    }
    

    private double hav(double val) {
     return Math.pow((Math.sin(0.5D * val)),2);    
    }
    
    private double ahav(double val) {
        return 2.0D * Math.asin(Math.sqrt(val));
    }
    

    /**
     * Method getOffset
     * Description: method to calculate a distance offset between two points in the sky using the 
     * haversine formula.  Uses the library from Dr Michael Thomas Flanagan at www.ee.ucl.ac.uk/~mflanaga
     * @param queryra known query ra.
     * @param querydec known query dec
     * @param objectra objects ra from results of a service/votable
     * @param objectdec objects dec from results of a service/votable
     * @return distance between two points.
     */
    private double getOffset(double queryra, double querydec, double objectra, double objectdec) {
        // gcdist = ahav( hav(dec1-dec2) + cos(dec1)*cos(dec2)*hav(ra1-ra2) )
        queryra = Math.toRadians(queryra);  
        querydec = Math.toRadians(querydec);
        //from the look of the formula I suspect this to be ra1 and dec1 since it should be the greater distance
        objectra = Math.toRadians(objectra); 
        objectdec = Math.toRadians(objectdec);
        //System.out.println("about to run haversine formula with " + queryra + ", " + querydec + ", " + objectra + ", " + objectdec);
        double result = ahav( hav(objectdec-querydec) + Math.cos(objectdec)*Math.cos(querydec)*hav(objectra-queryra) );
        //System.out.println("the haversine result = " + result + " throwing it toDegrees = " + Math.toDegrees(result));
        return Math.toDegrees(result);
    }  
    
    private String chopValue(String doubleValue, int scale) {
        int decIndex = doubleValue.indexOf(".");
        //we use the scale during the substring process
        //and to go to scale we need to increment by one character
        //to include the "." decimal point.
        scale++;
        if(decIndex != -1 && doubleValue.length() > (decIndex + scale)) {
            return doubleValue.substring(0,(decIndex + scale));
        }
        return doubleValue;
    }
    
    private static final int SMALL_NODE = 1;
    private static final int MEDIUM_NODE = 2;
    private static final int LARGE_NODE = 3;
    private static final int []NODE_SIZING_ARRAY = {SMALL_NODE, MEDIUM_NODE, LARGE_NODE};
    
    class NodeSizing {
        private Color color;
        private double extraSize;
        private Font font;
        public NodeSizing(int constraint) {
            switch(constraint) {
               case 1:
                   this.color = Color.RED;
                   font = new Font(null,Font.BOLD,16);
                   extraSize = 15;
               break;
               case 2:
                   this.color = Color.GREEN;
                   this.font = new Font(null,Font.ITALIC, 14);
                   this.extraSize = 10;
               break;
               case 3:
                   this.color = Color.BLUE;
                   this.font = new Font(null,Font.PLAIN, 10);
                   this.extraSize = 5;
               break;
               default:
                   //hmmm not sure should be throw an illegalargumentexception?
                   this.color = Color.BLACK;
                   this.font = new Font(null,Font.PLAIN,10);
                   this.extraSize = 0;
               break;
            }//switch            
        }        
        public Color getColor() { return this.color;}
        public Font getFont() { return this.font;}
        public double getSize() { return this.extraSize;}        
    }
    
    private Map nodeSizingMap;    
    
    /** base class for something that fetches a resource
     *  - extensible for siap, cone, and later ssap, etc.
     * @author Noel Winstanley nw@jb.man.ac.uk 28-Oct-2005
     *
     */
    private abstract class AbstractRetreiver implements Runnable {
        private static final int MAX_INLINE_IMAGE_SIZE = 100000;
        double ra;
        double dec;
        
        public AbstractRetreiver(double ra, double dec) {
            this.ra = ra;
            this.dec = dec;
        }        
        
        private void setNodeSizing() {
            int size = nodeSizingMap.size();
            int breakdown = size/3;
            int increment = 0;
            Object []keys = nodeSizingMap.keySet().toArray();
            for(int i = 0;i < 3;i++) {
                for(int j = increment;j < breakdown + increment;j++) {
                    nodeSizingMap.put(keys[j],new NodeSizing(NODE_SIZING_ARRAY[i]));                    
                }
                increment += breakdown;                
            }
            if(keys.length > 0)
                nodeSizingMap.put(keys[keys.length - 1], new NodeSizing(LARGE_NODE));
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
            
            for(int j = 0;j < tables.length;j++) {
                TableElement tableEl = (TableElement) tables[j];
                //Turn it into a StarTable so we can access its data.
                StarTable starTable = new VOStarTable( tableEl );   
                // first locate the column indexes for the columns we're interested in.
                int raCol = -1;
                int decCol = -1;
                int imgCol = -1;
                int formatCol = -1;
                int sizeCol = -1;
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
                        } else if (ucd.equals("VOX:Image_Format")) {
                            formatCol = col;
                        } else if (ucd.equals("VOX:Image_FileSize")) {
                            sizeCol = col;
                        }
                    }
                    titles[col] = columnInfo.getName() + " (" + columnInfo.getUCD() + ")";
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
                        String rowRa = row[raCol].toString();
                        String rowDec = row[decCol].toString();                                 
                        DefaultTreeNode valNode = new DefaultTreeNode();
                        valNode.setAttribute("label",chopValue(String.valueOf(rowRa),2) + "," + chopValue(String.valueOf(rowDec),2) );
                        valNode.setAttribute("ra",rowRa); // these might come in handy for searching later.
                        valNode.setAttribute("dec",rowDec);
                        if (imgCol >= 0) {
                            String imgURL = row[imgCol].toString();
                            long size;
                            try {
                                size = Long.parseLong(row[sizeCol].toString());
                            } catch (Throwable t) { // not found, or can't parse
                                size = Long.MAX_VALUE; // assume the worse
                            }
                            valNode.setAttribute("imgURL",imgURL);
                            String format = row[formatCol].toString();
                            valNode.setAttribute("type" , StringUtils.substringAfterLast(format,"/"));
                            if (size < MAX_INLINE_IMAGE_SIZE && (format.equals("image/gif") || format.equals("image/png") || format.equals("image/jpeg"))) {
                                valNode.setAttribute("preview",imgURL); // its a small image, of a suitable format for viewing.
                            }
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
                        //System.out.println("about to call offset with vals " + ra + ", " + dec + ", " + Double.valueOf(rowRa).doubleValue() + ", " +  Double.valueOf(rowDec).doubleValue() + " for service = " + riNode.getAttribute("label"));
                        double offset = getOffset(ra, dec, Double.valueOf(rowRa).doubleValue(), Double.valueOf(rowDec).doubleValue());
                        String offsetVal = chopValue(String.valueOf(offset),2);
                        TreeNode discoverNode = findNode(offsetVal, riNode);
                        String tempAttr;
                        TreeNode clusterNode;
                        if(discoverNode == null) {
                            clusterNode = new DefaultTreeNode();
                            clusterNode.setAttribute("label",offsetVal);
                            clusterNode.setAttribute("offset",offsetVal);
                            clusterNode.setAttribute("tooltip",String.valueOf(offset));
                            nodeSizingMap.put(offsetVal,null);
                            clusterNode.addChild(new DefaultEdge(clusterNode, valNode));
                            riNode.addChild(new DefaultEdge(riNode,clusterNode));
                        }else {
                            clusterNode = findNode(valNode.getAttribute("label"), discoverNode);
                            if(clusterNode == null) {
                                discoverNode.addChild(new DefaultEdge(discoverNode, valNode));
                            }else {
                                //okay it already has this ra,dec child of the cluster
                                //so add more urls to this imgURL attribute delimeted by "|".
                                if(valNode.getAttribute("imgURL") != null) {
                                    tempAttr = clusterNode.getAttribute("imgURL");
                                    if(tempAttr == null)
                                        tempAttr = valNode.getAttribute("imgURL");
                                    else
                                        tempAttr += "|" + valNode.getAttribute("imgURL");
                                    clusterNode.setAttribute("imgURL",tempAttr);
                                }
                            }
                        }
                    }//while rows in table.
                }finally{
                    rSeq.close();
                }
            }//for
            setNodeSizing();
        }//buildNodes
    }

                       
    
    /**
     * method: query
     * description: Queries the registry for sia and conesearch types and begins working on them.
     *
     *
     */
    private void query() {
        logger.debug("query() - inside query method)");
        final String position = getPosition();
        if (position == null) {
            showError("Could not parse position\nYou must enter RA,DEC or name of object known to SIMBAD");
            return;
        }
        clearTree();
        reFocusTopButton.setEnabled(true);
        
        // @todo refactor this string-munging methods.
                       
        final double ra = getRA(position);
        final double dec = getDEC(position);
        final String region = regionText.getText().trim();
        final double size = needsParsedRegion() ? getRA(region) : getConeRegion();
        final double raSize = needsParsedRegion() ?  getRA(region) : Double.parseDouble(region);
        final double decSize= needsParsedRegion() ? getDEC(region) : raSize;
                    
        (new BackgroundWorker(AstroScopeLauncherImpl.this,"Searching For Services") {
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
                                AstroScopeLauncherImpl.this.setStatusMessage("Completed querying servers");
                            }
                        });
                    }
                });
                return null;
            }
            protected void doFinished(Object result) {
                // just report what we found - querying will have already started.
               AstroScopeLauncherImpl.this.setStatusMessage("Found " + siaps.length + " image servers, "+ cones.length + " catalogue servers");                                     
            }
        }).start();        
    }
    
    
    class TextImageItemSizeRenderer extends TextImageItemRenderer {
        
        public TextImageItemSizeRenderer() {
            super();
        }
        
        protected Shape getRawShape(VisualItem item) {
            //System.out.println("getRawShape called and nodsizing size = " + nodeSizingMap.size());
            String offset = item.getAttribute("offset");            
            if(offset != null) {
                Object obj = nodeSizingMap.get(offset);
                if(obj != null) {
                    //Font currentFont = item.getFont();
                    //Font currentColor = item.getColor();
                    
                    item.setFont(((NodeSizing)obj).getFont());
                    item.setColor(((NodeSizing)obj).getColor());
                    double extraSize = ((NodeSizing)obj).getSize();
                    super.getRawShape(item);                    
                    if ( m_imageBox instanceof RoundRectangle2D ) {
                        ((RoundRectangle2D)m_imageBox)
                            .setRoundRect(((RoundRectangle2D)m_imageBox).getX(),((RoundRectangle2D)m_imageBox).getY(),
                            ((RoundRectangle2D)m_imageBox).getWidth() + extraSize ,((RoundRectangle2D)m_imageBox).getHeight()  + extraSize,
                            ((RoundRectangle2D)m_imageBox).getArcWidth(),((RoundRectangle2D)m_imageBox).getArcHeight());
                    } else {
                        m_imageBox.setFrame(m_imageBox.getX(),m_imageBox.getY(),m_imageBox.getWidth() + extraSize ,m_imageBox.getHeight()  + extraSize);
                    }                    
                }else {
                    super.getRawShape(item);
                }
            }else {
                super.getRawShape(item);
            }
            return m_imageBox;            
        }
    }    


    /** focus control - on double click, adds / removes node and children from focus set. */
    class DoubleClickMultiSelectFocusControl extends ControlAdapter {
        
        public DoubleClickMultiSelectFocusControl() {
            set = getSelectionFocusSet();
        }
        private final FocusSet set;
        public void itemClicked(VisualItem item, MouseEvent e) {
            if ( e.getClickCount() ==2 && 
                    item instanceof NodeItem && 
                    SwingUtilities.isLeftMouseButton(e)) {               
                TreeNode node = (TreeNode)item.getEntity();
                
                if (set.contains(node)) {// do a remove of this, and all children, and any parents.
                    for (Iterator i = new BreadthFirstTreeIterator(node); i.hasNext(); ) {
                        
                        TreeNode n = (TreeNode)i.next();
                        set.remove(n);
                        n.setAttribute("selected","false"); // attribute used to speed up coloring function.
                    }
                    while (node.getParent() != null) {
                        
                        node = node.getParent();
                        if (set.contains(node)) {
                            node.setAttribute("selected","false");
                            set.remove(node);
                        }
                    }
                } else { // an add of this, and all children
                    for (Iterator i = new BreadthFirstTreeIterator(node); i.hasNext(); ) {
                        
                        TreeNode n = (TreeNode)i.next();
                        n.setAttribute("selected","true"); // yechh.
                        if (! set.contains(n)) {
                            set.add(n);
                        }
                    }
                }
            }
        }
    } // end inner class.
    


    public class DemoColorFunction extends ColorFunction {
        private Color graphEdgeColor = Color.LIGHT_GRAY;
         private Color highlightColor = new Color(50,50,255);
         private Color focusColor = new Color(255,50,50);
         private Color selectedColor = Color.YELLOW;
         private ColorMap colorMap;
       
        public DemoColorFunction(int thresh) {
            colorMap = new ColorMap(
                ColorMap.getInterpolatedMap(thresh+1, Color.RED, Color.BLACK),
                0, thresh);
        } //
       
        public Paint getFillColor(VisualItem item) {
            if ( item instanceof NodeItem ) {
                //NodeItem n = (NodeItem)item;
                String attr=  item.getAttribute("selected");                
                if (attr != null && attr.equals("true")) {
                    return selectedColor;
                } else {               
                    return Color.WHITE;
                }
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
$Log: AstroScopeLauncherImpl.java,v $
Revision 1.18  2005/11/10 10:43:13  KevinBenson
minor change on the haversine formula

Revision 1.17  2005/11/09 16:05:55  KevinBenson
minor change to add a "Go to Top" button.

Revision 1.16  2005/11/09 14:10:44  KevinBenson
removed some statemetns that were not needed

Revision 1.15  2005/11/09 14:06:52  KevinBenson
minor changes for clearTree to refocus in the center.  And fix an expression on the position

Revision 1.14  2005/11/08 15:03:56  KevinBenson
minor changes on sizing

Revision 1.13  2005/11/07 16:25:05  KevinBenson
added some clustering to it. so there is an offset and some clustered child nodes as well.

Revision 1.12  2005/11/04 17:49:52  nw
reworked selection and save datastructures.

Revision 1.11  2005/11/04 14:09:12  nw
improved error handling in getPosition,
started looking at image preview.

Revision 1.10  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.9  2005/11/03 11:56:49  KevinBenson
added a new astroscope cluster

Revision 1.8  2005/11/02 17:29:56  KevinBenson
fixed scrollpane

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