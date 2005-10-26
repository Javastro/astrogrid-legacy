/*$Id: AstroScopeLauncherImpl.java,v 1.1 2005/10/26 15:53:15 KevinBenson Exp $
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

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.FocusManager;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.ActionMap;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.action.animate.ColorAnimator;
import edu.berkeley.guir.prefuse.action.animate.PolarLocationAnimator;
import edu.berkeley.guir.prefuse.action.assignment.ColorFunction;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.action.filter.GraphFilter;
import edu.berkeley.guir.prefuse.action.filter.TreeFilter;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.activity.SlowInSlowOutPacer;
import edu.berkeley.guir.prefuse.focus.DefaultFocusSet;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.io.XMLGraphReader;
import edu.berkeley.guir.prefuse.render.DefaultEdgeRenderer;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.berkeley.guir.prefuse.render.ShapeRenderer;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
import edu.berkeley.guir.prefuse.util.ColorMap;
import edu.berkeley.guir.prefuse.util.StringAbbreviator;
import edu.berkeley.guir.prefusex.controls.DragControl;
import edu.berkeley.guir.prefusex.controls.FocusControl;
import edu.berkeley.guir.prefusex.controls.NeighborHighlightControl;
import edu.berkeley.guir.prefusex.controls.PanControl;
import edu.berkeley.guir.prefusex.controls.ToolTipControl;
import edu.berkeley.guir.prefusex.controls.ZoomControl;
import edu.berkeley.guir.prefusex.controls.ZoomingPanControl;
import edu.berkeley.guir.prefusex.layout.RadialTreeLayout;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;
import edu.berkeley.guir.prefuse.graph.DefaultGraph;
import edu.berkeley.guir.prefuse.render.DefaultNodeRenderer;
import edu.berkeley.guir.prefusex.controls.MultiSelectFocusControl;
import edu.berkeley.guir.prefusex.layout.SquarifiedTreeMapLayout;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.Edge; 
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.DefaultNode;
import edu.berkeley.guir.prefuse.graph.GraphLib;
import edu.berkeley.guir.prefuse.graph.Node;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.votable.VOElementFactory;
import uk.ac.starlink.votable.VOElement;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.votable.VOStarTable;
import uk.ac.starlink.votable.TableElement;

import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.acr.ui.AstroScopeLauncher;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.nvo.Cone;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.dialogs.editors.AbstractToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.BasicToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.CompositeToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.system.UIInternal;

import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.filemanager.client.FileManagerNode;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.picocontainer.PicoContainer;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
 
import java.net.URI;
import java.util.Arrays;
import java.util.Vector;
import java.util.Comparator;

import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.io.IOException;

import org.apache.axis.utils.XMLUtils;


/** Implementation of the Datascipe launcher
 *
 */
public class AstroScopeLauncherImpl extends UIComponent implements AstroScopeLauncher, ActionListener {

    //components used in astrosope
    
    //myspace for saving data into myspace.
    private final MyspaceInternal myspace;
    
    //currently not used
    private final RegistryChooser rci;
    
    //siap object for querying siap services.
    private final Siap siap;
    
    //cone object for querying cone services.
    private final Cone cone;
    
    private final Component parentComponent;
    private final Registry reg;
    private UIComponent uc;
    private Tree defaultTree;
    
    //The visual graph that holds all the nodes in the display.
    private Graph graph;
    
    //the default sia and cone nodes children of root "Astroscope"
    private DefaultNode siaNode;
    private DefaultNode coneNode;
    
    //checkboxes to determine if you want to query on sia and/or cone.
    private JCheckBox siaCheckBox;
    private JCheckBox coneCheckBox;    
    
    /*
     * RETHINK:
     * variable used as a counter to determine number of threads to start. 
     * A Query Background will add to this counter and the display will decrement.
     * Which is not quite nice.
     */
    int backgroundCounter = 0;

    //hashtable that holds all the id's with values of urls to votable results.
    private Hashtable storageTable;
    
    
    //Various gui components.
    private JTextField posText = null;
    private JTextField regionText = null;
    private JButton advancedButton = null;
    private JButton submitButton = null;            
    private ActionList graphLayout = null;
    private DefaultListModel selectedListModel = null;
    protected final ResourceChooserInternal chooser;
    private JTree selectTree;
    private DefaultMutableTreeNode selectRootNode;
    protected DefaultTreeModel treeModel;
    
    JButton saveImageButton;
    JButton saveButton;
    JButton selectAllButton;
    
   
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
                                  Siap siap, Cone cone) {
        super(conf,hs,ui);
        this.myspace = myspace;
        this.rci = rci;
        this.siap = siap;
        this.cone = cone;
        this.reg = reg;
        this.uc = this;
        this.chooser = chooser;
        this.parentComponent = ui.getComponent();
        this.storageTable = new Hashtable();
        this.setSize(700, 700);  
        JPanel pane = getJContentPane();
        pane.add(makeSearchPanel(),BorderLayout.WEST);
        pane.add(makeCenterPanel(),BorderLayout.CENTER);
        this.setContentPane(pane);
        this.setTitle("AstroScope");
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
            /*
             *
             //choose a uri to save the data to.             
             final URI u = chooser.chooseResourceWithParent("Save Data",true,true,true,false,this);
             if (u == null) {
                 return;
             }
             */
             (new BackgroundOperation("Saving Data") {
                     protected Object construct() throws Exception {
                         /*
                         //make sure they chose a directory if not then return.
                         if(u.toString().equals("file:")) {
                             File file = new File(u);
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
                         */
                         System.out.println("Save Data");
                         Enumeration depthEnum;
                         Enumeration storageLookUp;
                         Iterator nodeIterator;
                         String lookup;
                         String key;
                         URL url;
                         DefaultMutableTreeNode dmTreeNode;
                         for (Enumeration e = selectRootNode.children() ; e.hasMoreElements() ;) {
                             DefaultMutableTreeNode tn = (DefaultMutableTreeNode)e.nextElement();
                             if(tn.getChildCount() > 0) {
                                 for (depthEnum = selectRootNode.depthFirstEnumeration() ; depthEnum.hasMoreElements() ;) {
                                     dmTreeNode = (DefaultMutableTreeNode)depthEnum.nextElement();
                                     if(!dmTreeNode.isRoot()) {
                                         TreeNodeData treeData = (TreeNodeData)dmTreeNode.getUserObject();
                                         if(treeData.getID() != null  && treeData.getID().trim().length() > 0) {
                                             url = (URL)storageTable.get(treeData.getID());
                                             //TODO
                                             //now copy into the selected directory
                                             //be sure to add an actual filename to that directory.
                                             //should be the name of treeData.toString() with special characters and spaces
                                             //set to "_" or something valid.
                                             System.out.println("the url found = " + url);
                                         }
                                     }//if
                                 }
                             }else {
                                 //they selected everything or sia or cone.
                                 nodeIterator = graph.getNodes();
                                 Node nd = null;
                                 if(tn.toString().equals("All")) {
                                     while(nodeIterator.hasNext()) {
                                         nd = (Node)nodeIterator.next();
                                         key = nd.getAttribute("id");
                                         if(key != null && key.trim().length() > 0) {
                                             url = (URL)storageTable.get(key);
                                             System.out.println("the url = " + url);
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
                                    lookup = tn.toString();                                    
                                    while(nodeIterator.hasNext()) {
                                        nd = (Node)nodeIterator.next();
                                        key = nd.getAttribute("id");
                                        if(key != null && key.trim().length() > 0 &&
                                           key.startsWith(lookup)) {
                                            url = (URL)storageTable.get(key);
                                            System.out.println("the url = " + url);
                                            //TODO
                                            //now copy into the selected directory
                                            //be sure to add an actual filename to that directory.
                                            //Use nd.getAttribute("label") for the filename.
                                            //with special characters and spaces
                                            //set to "_" or something valid.                                    
                                            //copyContentToURL
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
     * method: saveImageData
     * Description: Runs through the selected objects in the JTree and begins saving selected images
     * to myspace or filesystem.  User MUST CHOOSE a directory node.  Does the work in a background
     * thread.  Also allows for selection of all or sub trees like only sia or cone (which looks at graph nodes).
     * Called by the saveImageButton action.  
     *
     */    
    private void saveImageData() {
        /*
        final URI u = chooser.chooseResourceWithParent("Save Data",true,true,true,false,this);
        if (u == null) {
            return;
        }
        */
        (new BackgroundOperation("Saving Images") {
                protected Object construct() throws Exception {
                    /*
                    if(u.toString().equals("file:")) {
                        File file = new File(u);
                        if(!file.isDirectory()) {
                            JOptionPane.showMessageDialog(AstroScopeLauncherImpl.this, 
                                    "You can only save data to a folder or directory",
                                    "No Directory Selected", JOptionPane.OK_OPTION );
                        }                             
                    }else {
                        FileManagerNode fmn = myspace.node(u);
                        if(!fmn.isFolder()) {
                            JOptionPane.showMessageDialog(AstroScopeLauncherImpl.this, 
                                    "You can only save data to a folder or directory",
                                    "No Directory Selected", JOptionPane.OK_OPTION );
                        }                             
                    }//else
                    */
                    System.out.println("Save Images");
                    Enumeration depthEnum;
                    Enumeration storageLookUp;
                    Iterator nodeIterator;
                    String lookup;
                    String key;
                    URL url;
                    DefaultMutableTreeNode dmTreeNode;
                    for (Enumeration e = selectRootNode.children() ; e.hasMoreElements() ;) {
                        DefaultMutableTreeNode tn = (DefaultMutableTreeNode)e.nextElement();
                        if(tn.getChildCount() > 0) {
                            for (depthEnum = selectRootNode.depthFirstEnumeration() ; depthEnum.hasMoreElements() ;) {
                                dmTreeNode = (DefaultMutableTreeNode)depthEnum.nextElement();
                                if(!dmTreeNode.isRoot()) {
                                    TreeNodeData treeData = (TreeNodeData)dmTreeNode.getUserObject();
                                    if(treeData.getURL() != null  && treeData.getURL().trim().length() > 0) {                                    
                                        System.out.println("the url found = " + treeData.getURL());
                                    }//if
                                    if(treeData.getID() != null && dmTreeNode.getChildCount() == 0) {
                                        //Darn they chosen just a service.
                                        //need to look through the nodes now.                                       
                                    }
                                }//if
                            }//for
                        }else {
                            //they selected everything or sia or cone.
                            //At the moment only SIA is available so it is the same as All.
                            //Just grab all the url attributes and start saving.
                            nodeIterator = graph.getNodes();                            
                            while(nodeIterator.hasNext()) {
                                Node nd = (Node)nodeIterator.next();
                                key = nd.getAttribute("url");
                                if(key != null && key.trim().length() > 0) {
                                    url = new URL(key);
                                    System.out.println("the url = " + url);
                                    //TODO
                                    //now copy into the selected directory
                                    //be sure to add an actual filename to that directory.
                                    //Use nd.getAttribute("label") for the filename.
                                    //should be the name of treeData.toString() with special characters and spaces
                                    //set to "_" or something valid.                                    
                                    //copyContentToURL
                                }//if
                            }//while                            
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
        String expression = "\\d+\\.?\\d*,\\d+\\.?\\d*";
        if(pos.matches(expression)) {            
            return pos;            
        }
        System.out.println("does not match expression see if it is an object to be looked up");
        //TODO: Go and look up object.
        throw new IllegalArgumentException("No position found at the moment, expression did not match");
        
    }
    
    /**
     * Extracts out the ra of a particular position in the form of a ra,dec
     * @param position
     * @return
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
     */
    private boolean needsParsedRegion() {
        String region = regionText.getText().trim();
        String expression = "\\d+\\.?\\d*,\\d+\\.?\\d*";
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

        siaCheckBox = new JCheckBox("SIA Services");
        siaCheckBox.setSelected(true);
        coneCheckBox = new JCheckBox("Cone Services");
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
        selectRootNode = new DefaultMutableTreeNode("Selected Data");
        treeModel = new DefaultTreeModel(selectRootNode);
        selectTree = new JTree(treeModel); 
        selectTree.setBackground(bottomPanel.getBackground());
        JScrollPane listScrollPane = new JScrollPane(selectTree);

        wrapPanel.add(listScrollPane);       
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
        
        //Also available for the display.
        //createInitialDisplayForRadial()
        //createInitialDisplayForTreeMap()
        Display display = createInitialDisplayForZoomPan();
        
        JPanel wrapPanel = new JPanel();
        wrapPanel.setLayout(new BoxLayout(wrapPanel,BoxLayout.Y_AXIS));
        wrapPanel.add(display);
        
        
        JPanel southCenterPanel = new JPanel();
        Dimension dim2 = new Dimension((int)wrapPanel.getMaximumSize().getWidth(),30);
        southCenterPanel.setMaximumSize(dim2);
        southCenterPanel.setPreferredSize(dim2); 
        
        selectAllButton = new JButton("Select All");
        selectAllButton.setEnabled(false);
        selectAllButton.addActionListener(this);
                
        saveButton = new JButton("Save Selected Data");
        saveButton.setEnabled(false);
        saveButton.addActionListener(this);
        
        saveImageButton = new JButton("Save Selected Images");
        saveImageButton.setEnabled(false);
        saveImageButton.addActionListener(this);
        
        southCenterPanel.add(selectAllButton);
        southCenterPanel.add(saveButton);
        southCenterPanel.add(saveImageButton);
        wrapPanel.add(southCenterPanel);
        return wrapPanel;        
    }
    
    /**
     * Uses the Prefuse Radial Layout for display along with a little bit of Animation to it. Uses
     * the text item renderer to see text boxes.  Very nice, but for a small display screen we just have
     * to much data.
     * @return
     */
    private Display createInitialDisplayForRadial() {
        
        graph = new DefaultGraph();
        ItemRegistry registry = new ItemRegistry(graph);
        
        //root = new DefaultTreeNode();
        DefaultNode root = new DefaultNode();
        root.setAttribute("label","AstroSope");
        
        siaNode = new DefaultNode();
        siaNode.setAttribute("label","SIA");
        siaNode.setAttribute("tooltip","<html><p>This ia very long tool tip attribute for sia<br> to see how it displays on the screen.<br> Hopefully it displays good</p></html>");
        
        //Edge defaultEdge = new DefaultEdge(root,db);
        //root.addEdge(defaultEdge);
        //defaultTree = new DefaultTree();
        graph.addNode(root);
        graph.addNode(siaNode);
        //graph.addEdge(root,siaNode);
        graph.addEdge(new DefaultEdge(root,siaNode));
        
        Display display = new Display(registry);
        registry.addDisplay(display);
                
        TextItemRenderer nodeRenderer = new TextItemRenderer();
        nodeRenderer.setMaxTextWidth(75);
        nodeRenderer.setRoundedCorner(8,8);
        nodeRenderer.setTextAttributeName("label");
        
        Renderer edgeRenderer = new DefaultEdgeRenderer();
        registry.setRendererFactory(new DefaultRendererFactory(nodeRenderer, edgeRenderer));
        
            graphLayout = new ActionList(registry);
            graphLayout.add(new TreeFilter(true));
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

           
           // initialize display 
           display.setItemRegistry(registry);
           display.setSize(400,400);
           //display.setBackground(Color.WHITE);
           
           //for radial tree
           display.addControlListener(new FocusControl(graphLayout));
           display.addControlListener(new FocusControl(0,FocusManager.HOVER_KEY));
           display.addControlListener(new DragControl());
           display.addControlListener(new PanControl());
           display.addControlListener(new ZoomControl());
           //display.addControlListener(new ToolTipControl("tooltip"));
           //display.addControlListener(new AstroScopeMultiSelectionControl(registry));           
           display.addControlListener(new NeighborHighlightControl(update));
           
           registry.getFocusManager().putFocusSet(
                   FocusManager.HOVER_KEY, new DefaultFocusSet());
           
           coneNode  = new DefaultNode();
           coneNode.setAttribute("label","Cone");
           
           graph.addNode(coneNode);
           //graph.addEdge(root,coneNode);
           graph.addEdge(new DefaultEdge(root,coneNode));
           graphLayout.runNow();
           return display;
    }
    
    private ActionMap actionMap = new ActionMap();
    /**
     * Tends to show the data a little better, also sort of a Grid Radial kind of style.  You need to hold down the
     * mouse button for zooming and panning at the same time.  Problem is it is a little hard to get used to and again
     * so much data.
     * @return
     */
    private Display createInitialDisplayForZoomPan() {
                
        graph = new DefaultGraph();
        ItemRegistry registry = new ItemRegistry(graph);
        
        //root = new DefaultTreeNode();
        DefaultNode root = new DefaultNode();
        root.setAttribute("label","AstroSope");
        
        siaNode = new DefaultNode();
        siaNode.setAttribute("label","SIA");
        siaNode.setAttribute("tooltip","<html><p>This ia very long tool tip attribute for sia<br> to see how it displays on the screen.<br> Hopefully it displays good</p></html>");
        
        coneNode  = new DefaultNode();
        coneNode.setAttribute("label","Cone");
        
        graph.addNode(root);
        graph.addNode(siaNode);
        graph.addNode(coneNode);        
        
        graph.addEdge(new DefaultEdge(root,siaNode));
        graph.addEdge(new DefaultEdge(root,coneNode));        
        
        Display display = new Display(registry);
        registry.addDisplay(display);
                
        TextItemRenderer nodeRenderer = new TextItemRenderer();
        //nodeRenderer.setMaxTextWidth(75);
        nodeRenderer.setRoundedCorner(8,8);
        
        Renderer edgeRenderer = new DefaultEdgeRenderer();
        registry.setRendererFactory(new DefaultRendererFactory(nodeRenderer, edgeRenderer));
        
        graphLayout = new ActionList(registry);
        graphLayout.add(new GraphFilter());
        graphLayout.add(actionMap.put("grid", new PrefuseGridLayout()));
        
        final ActionList update = new ActionList(registry);
        update.add(new ColorFunction());
        update.add(new RepaintAction());
        
        ((Layout)actionMap.get("grid")).setLayoutBounds(
                new Rectangle2D.Double(-1200,-1200,2400,2400));        
        
           // initialize display 
           display.setItemRegistry(registry);
           display.setSize(400,400);
           //display.setBackground(Color.WHITE);
           display.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
           display.addControlListener(new DragControl());
           display.addControlListener(new NeighborHighlightControl());
           display.addControlListener(new FocusControl(0, update));
           display.addControlListener(new ZoomingPanControl());
           display.addControlListener(new ToolTipControl("tooltip"));
           display.addControlListener(new AstroScopeMultiSelectionControl(registry));           
           
           graphLayout.runNow();  update.runNow();
           return display;
    }
    
    /**
     * For the demos this is the best one for showing large amounts of data.  And if we can our data to form similar
     * to what the demo has, then this will be the graph and display to use and should fit fairly nicely with our data.
     * Vision Example: SIA(large box)->Siap Service(medium box)->Various RA,DEC(small box).
     * UNKNOWN: does it allow selection so we can populate the JTree hope so.
     * 
     * @return
     */
    private Display createInitialDisplayForTreeMap() {
        
        graph = new DefaultGraph();
        ItemRegistry registry = new ItemRegistry(graph);
        
        DefaultNode root = new DefaultNode();        
        root.setAttribute("label","AstroSope");
        
        siaNode = new DefaultNode();
        siaNode.setAttribute("label","SIA");
        siaNode.setAttribute("tooltip","<html><p>This ia very long tool tip attribute for sia<br> to see how it displays on the screen.<br> Hopefully it displays good</p></html>");

        coneNode  = new DefaultNode();
        coneNode.setAttribute("label","Cone");
        
        
        //Edge defaultEdge = new DefaultEdge(root,db);
        //root.addEdge(defaultEdge);
        //defaultTree = new DefaultTree();
        graph.addNode(root);
        graph.addNode(siaNode);
        graph.addNode(coneNode);
        
        //graph.addEdge(root,siaNode);
        graph.addEdge(new DefaultEdge(root,siaNode));
        graph.addEdge(new DefaultEdge(root,coneNode));
        
        Display display = new Display(registry);
        //display.setUseCustomTooltips(true);

        registry.addDisplay(display);
                
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
           //graphLayout.add(new SquarifiedTreeMapLayout(4));
           graphLayout.add(new SquarifiedTreeMapLayout(5));
           graphLayout.add(new TreeMapColorFunction());
           graphLayout.add(new RepaintAction());
        
           // initialize display 
           display.setItemRegistry(registry);
        //   display.setSize(400,400);
           display.setBackground(Color.WHITE);
           
           PanControl  pH = new PanControl();
           ZoomControl zH = new ZoomControl();
           display.addMouseListener(pH);
           display.addMouseMotionListener(pH);
           display.addMouseListener(zH);
           display.addMouseMotionListener(zH);
           //TODO probably want to use the "tooltip" text instead of default "label"
           //TODO UNKNOWN can I turn on the selection control for placing data in the selected JTree.
           //display.addControlListener(new AstroScopeMultiSelectionControl(registry));
           display.addControlListener(new ToolTipControl());
           
           //display.addControlListener(new ToolTipControl("tooltip"));           
           graphLayout.runNow();
           return display;
    }

    /**
     * Various action statements for when buttons are clicked.
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        System.out.println("entered actionPerformed");
        if(source == submitButton) {
            System.out.println("submit button clicked");
            query();
        }else if(source == saveButton) {
            saveData();
        }else if(source == saveImageButton) {
            saveImageData();
        }else if(source == selectAllButton) {
            if(storageTable.size() > 0) {
                if(selectRootNode.getChildCount() >= 1) {
                    if(!selectRootNode.getFirstChild().toString().equals("All")) {
                        int result = JOptionPane.showConfirmDialog(AstroScopeLauncherImpl.this, 
                                "You have selected AstroScope (which is everything) this will remove all other selections.  Is this correct?",
                                "Clear Selection", JOptionPane.YES_NO_OPTION );
                        if (result == JOptionPane.YES_OPTION) {
                            selectRootNode.removeAllChildren();
                            selectRootNode.add(new DefaultMutableTreeNode("All"));
                            treeModel.reload();
                        }
                    }//if
                }//if
            }//if
        }
        System.out.println("exit actionPerformed");
    }
    
    
    
    /**
     * Does the work for Querying all SIA services.  Based on Information/URLs from the registry.
     * RETHINK: don't like how I handled bundle's with this backgroundcounter very hacky.  Also suspect
     * I could merge this with BackgroundConeQueryWorker.
     * @author Kevin Benson
     *
     * TODO To change the template for this generated type comment go to
     * Window - Preferences - Java - Code Style - Code Templates
     */
    class BackgroundSIAQueryWorker extends BackgroundWorker {
        private ResourceInformation []ri = null;
        int bundle; 
        public BackgroundSIAQueryWorker(UIComponent parent, String prompt, ResourceInformation []ri, int bundle) {
            super(parent,prompt);
            this.ri = ri;
            this.bundle = bundle;
        }
        
        protected Object construct() throws Exception {
            int j = 0;
            System.out.println("construct of BackgroundSIAQueryWorker");
            String position = getPosition();
            double ra = getRA(position);
            double dec = getDEC(position);
            String region = regionText.getText().trim();
            System.out.println("ra = " + ra + " dec = " + dec + " region = " + region + " regra = " + getRA(region) + " regdec = " + getDEC(region));
            //COMMENTOUT: section to reduce number of queries.
            while(j < ri.length && j < 2) {
                //no point in doing it if they have a registry entry with no accessurl
                //so check and keep moving if it is null.
                if(ri[j].getAccessURL() == null) {
                    j++;
                }else {
                    if(backgroundCounter < bundle) {
                        URI id = ri[j].getId();
                        System.out.println("construct query for id = " + id + " accessurl = " + ri[j].getAccessURL());                        
                        URL siapURL;
                        //check if the region needs to be parsed and call the correct siap url.
                        if(needsParsedRegion()) {
                            siapURL = siap.constructQueryS(new URI(ri[j].getAccessURL().toString()),ra, dec,getRA(region),getDEC(region));
                        } else {
                            siapURL = siap.constructQuery(new URI(ri[j].getAccessURL().toString()),ra, dec,getConeRegion());
                        }
                        //the url to be constructed.
                        System.out.println("the siapURL that was constructed = " + siapURL);
                        
                        //Grap the results.
                        //RETHINK
                        //Wonder this doc is only used for StarTable and display
                        //Startable seems to have a TableSink Sax stuff might be better to use that.
                        Document doc = siap.getResults(siapURL);
                        //place in a hashtable the id and siapurl to the votable.
                        //the SIA prefix is used in case they select all SIA id's.
                        storageTable.put("SIA-" + ri[j].getId().toString(),siapURL);
                        
                        //Add this Service as a node in the graph.
                        DefaultNode riNode = new DefaultNode();
                        riNode.setAttribute("label",ri[j].getTitle());
                        riNode.setAttribute("id", "SIA-" + ri[j].getId());
                        riNode.setAttribute("tooltip","<html><p>Title: " + ri[j].getTitle() + "<br>" + "ID: " + ri[j].getId() + "</p></html>");                        
                        graph.addNode(riNode);
                        graph.addEdge(new DefaultEdge(siaNode,riNode));
                        
                        //Now call the BackgroundDisplayWorker to go through the 
                        //votable and add various nodes to the display/graph.
                        (new BackgroundDisplayWorker(uc,"Query",doc,riNode)).start();
                        backgroundCounter++;
                        j++;
                    }else {
                        System.out.println("in the else for waiting probably need to wait");
                        //backgroundCounter--;
                    }
                }
            }//while
             return null;
        }//construct
        
        protected void doFinished(Object result) {
            System.out.println("doFinished of BackgroundSIAQueryWorker");
            
        }
    }
    
    /**
     * Does the work for Querying all Cone services.  Based on Information/URLs from the registry.
     * RETHINK: don't like how I handled bundle's with this backgroundcounter very hacky.  Also suspect
     * I could merge this with BackgroundSIAQueryWorker.
     */    
    class BackgroundConeQueryWorker extends BackgroundWorker {
        private ResourceInformation []ri = null;
        int bundle; 
        public BackgroundConeQueryWorker(UIComponent parent, String prompt, ResourceInformation []ri, int bundle) {
            super(parent,prompt);
            this.ri = ri;
            this.bundle = bundle;
        }
        
        protected Object construct() throws Exception {
            int j = 0;
            System.out.println("construct of BackgroundQueryWorker");
            String position = getPosition();
            double ra = getRA(position);
            double dec = getDEC(position);
            String region = regionText.getText().trim();
            double reg = 0;
            System.out.println("ra = " + ra + " dec = " + dec + " region = " + region + " regra = " + getRA(region) + " regdec = " + getDEC(region));
            while(j < ri.length) {
                //chek to make sure there is a url if not then keep going.
                if(ri[j].getAccessURL() == null || ri[j].getAccessURL().toString().trim().length() == 0) {
                    j++;
                }else {
                    if(backgroundCounter < bundle) {
                        URI id = ri[j].getId();
                        System.out.println("construct query for id = " + id + " accessurl = " + ri[j].getAccessURL());                        
                        URL coneURL;
                        if(needsParsedRegion()) {
                            if(getRA(region) == getDEC(region)) {
                                reg = getRA(region);
                            }else {
                                System.out.println("will not be able to do cone queries");
                            }
                        } else {
                            reg = getConeRegion();
                        }
                            coneURL = cone.constructQuery(new URI(ri[j].getAccessURL().toString()),ra, dec,reg);
                            System.out.println("the coneURL that was constructed = " + coneURL);
                            //Grap the results.
                            //RETHINK
                            //Wonder this doc is only used for StarTable and display
                            //Startable seems to have a TableSink Sax stuff might be better to use that.                            
                            Document doc = cone.getResults(coneURL);
                            storageTable.put("Cone-" + ri[j].getId().toString(),XMLUtils.DocumentToString(doc));
                            
                            DefaultNode riNode = new DefaultNode();
                            riNode.setAttribute("label",ri[j].getTitle());
                            riNode.setAttribute("id", "Cone-" + ri[j].getId());
                            riNode.setAttribute("tooltip","<html><p>Title: " + ri[j].getTitle() + "<br>" + "ID: " + ri[j].getId() + "</p></html>");
                            graph.addNode(riNode);
                            graph.addEdge(new DefaultEdge(coneNode,riNode));
                            (new BackgroundDisplayWorker(uc,"Query",doc,riNode)).start();
                            backgroundCounter++;
                        j++;
                    }else {
                        System.out.println("in the else for waiting");
                        //backgroundCounter--;
                    }
                }
            }//while
             return null;
        }//construct
        
        protected void doFinished(Object result) {
            System.out.println("doFinished of BackgroundQueryWorker");
            
        }
    }
    
    
    /**
     * Background class that parses a votable and and adds the data as nodes on the graph.  Also sets up
     * various attributes on the nodes that help to determine selections in the tree. 
     */
    class BackgroundDisplayWorker extends BackgroundWorker {
        private Document doc;
        private DefaultNode serviceNode;
        public BackgroundDisplayWorker(UIComponent parent, String prompt, Document doc, DefaultNode serviceNode) {
            super(parent,prompt);
            this.doc = doc;
            this.serviceNode = serviceNode;            
        }
        
        protected Object construct() throws Exception {
            System.out.println("construct of BackgroundDisplayWorker");
            return null;
        }
        
        protected void doFinished(Object result) {
                try {
    //              Create a tree of VOElements from the given XML file.
                    VOElement top = new VOElementFactory().makeVOElement( doc, null );
                    
                    // Find the first RESOURCE element using standard DOM methods.
                    NodeList resources = top.getElementsByTagName( "RESOURCE" );
                    Element resource = (Element) resources.item( 0 );
    //              Locate the third TABLE child of this resource using one of the
                    // VOElement convenience methods.
                    VOElement vResource = (VOElement) resource;
                    VOElement[] tables = vResource.getChildrenByName( "TABLE" );
                    Hashtable starResult = new Hashtable();
                    String temp;
                    Enumeration keyEnum;
                    
                    //COMMENTOUT:  This only goes through 5 tables in the votable, shoudl comment out
                    //the next 4 lines and in the for loop it shoudl go to tables.length
                    int testmodeint = 5;
                    if(tables.length < 5) {
                        testmodeint = tables.length;
                    }
                    for(int j = 0;j < /*tables.length*/ testmodeint;j++) {
                        TableElement tableEl = (TableElement) tables[j];
                        //Turn it into a StarTable so we can access its data.
                        StarTable starTable = new VOStarTable( tableEl );
                        // Write out the column name for each of its columns.
                        int nCol = starTable.getColumnCount();
                
                        //Iterate through its data rows, printing out each element.
                        RowSequence rSeq = starTable.getRowSequence();
                        try {
                            while ( rSeq.hasNext() ) {
                                rSeq.next();
                                Object[] row = rSeq.getRow();
                                //place the columns and the value for that row in a hashtable.
                                //it will be cleared out in a moment after the nodes for the row is added
                                //to the graph.
                                for ( int iCol = 0; iCol < nCol; iCol++ ) {
                                    temp = null;
                                    temp = starTable.getColumnInfo( iCol ).getName();
                                    if(starTable.getColumnInfo( iCol ).getUCD() != null) {
                                        temp += "-" + starTable.getColumnInfo( iCol ).getUCD(); 
                                    }
                                    if(row[iCol] != null) {
                                        starResult.put(temp, row[iCol].toString());
                                    }
                                    //System.out.print("Column name: " + starTable.getColumnInfo( iCol ).getName() + 
                                    //                 " row val: " +  row[ iCol ] + "\t" );                                
                                }
                                //get all the keys and make the attributes for the node in the graph.
                                //such as lable, tooltip, and url(only for sia images.
                                keyEnum = starResult.keys();
                                String key;
                                String toolTip = "";
                                String urlStr = null;
                                temp = null;
                                while(keyEnum.hasMoreElements()) {                                
                                    key = (String)keyEnum.nextElement();
                                    if(key.indexOf("POS_EQ_RA_MAIN") != -1) {
                                        if(temp == null)
                                            temp = (String)starResult.get(key);
                                        else
                                            temp = (String)starResult.get(key) + temp;  //dec must have came first so concat
                                    }else if(key.indexOf("POS_EQ_DEC_MAIN") != -1) {
                                        if(temp == null) {
                                            temp = (String)starResult.get(key);
                                        }else {
                                            temp += ", " + (String)starResult.get(key);
                                        }
                                    }else if(key.indexOf("VOX:Image_AccessReference") != -1) {
                                        urlStr = (String)starResult.get(key);
                                    }else {
                                        toolTip += key + "-" + (String)starResult.get(key) + "<br>";
                                    }
                                }
                                DefaultNode valNode = new DefaultNode();
                                valNode.setAttribute("label",temp);
                                if(urlStr != null) {
                                    valNode.setAttribute("url",urlStr);
                                }
                                valNode.setAttribute("tooltip","<html><p>" + temp + "<br>" + toolTip + "</p></html>");
                                //Add the node and edge to the graph.
                                graph.addNode(valNode);
                                graph.addEdge(new DefaultEdge(serviceNode,valNode));
                                //clear the hashtable for the next row.
                                starResult.clear();
                            }//while
                        }finally{
                            rSeq.close();
                        }
                        //were done lets clear some things.
                        starTable = null;
                    }//for
                    doc = null;
                    //Repaint the graph.
                    //repaintAction.runNow();
                    //RETHINK: I think there is another way to have the graph displayed.
                    //right now this will relayout all the nodes in the graph.  Just think there must be another way.
                    //to save time of it redrawing the layout.
                    graphLayout.runNow();
                    
                }catch(IOException ioe) {
                    //throw this up in a moment.
                    //TODO need to do something else.
                    ioe.printStackTrace();
                }
                finally {
                    //RETHINK again that hacky spot I don't like decprement the background counter.
                    backgroundCounter--;
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
        System.out.println("inside query method)");
        graphLayout.runNow();
        storageTable.clear();
        if(siaCheckBox.isSelected()) {
        (new BackgroundWorker(uc,"Searching SIA") {            
            protected Object construct() throws Exception {
                System.out.println("construct of BackgroundWorker");                
                ResourceInformation []ri = reg.adqlSearchRI(siap.getRegistryQuery());
                System.out.println("just did a full sia query number of results = " + ri.length);
                (new BackgroundSIAQueryWorker(uc,"Searching",ri,5)).start();
                return null;
            }
            protected void doFinished(Object result) {
                System.out.println("doFinished of BackgroundWorker");                
                
            }
        }).start();
        }
        //do the cone searching here as well.

        if(coneCheckBox.isSelected()) {
        (new BackgroundWorker(uc,"Searching Cone") {            
            protected Object construct() throws Exception {
                System.out.println("construct of BackgroundWorker");                
                ResourceInformation []ri = reg.adqlSearchRI(cone.getRegistryQuery());
                System.out.println("just did a full cone query number of results = " + ri.length);
                (new BackgroundConeQueryWorker(uc,"Searching",ri,5)).start();
                return null;
            }
            protected void doFinished(Object result) {
                System.out.println("doFinished of BackgroundWorker");                
                
            }
        }).start();
        }
        
        
        System.out.println("exiting query method");

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
            System.out.println("the itemClicked happened tostring of item = " + item.toString());
            //okay do the stuff to the graph first.
            super.itemClicked(item,e);
            //ge the label.
            String label = item.getAttribute("label");
            TreePath tp;
            DefaultMutableTreeNode temp;            
            DefaultMutableTreeNode child;
            //User Chose AstroScope which is the top level of everything hence "all".
            if(label != null && label.equals("AstroScope")) {
                //Check if there are children already in the tree,
                //if they are and if there is already an All then he is deselecting.
                //if there are other children make sure he wants to clear things and put an "All"
                if(selectRootNode.getChildCount() >= 1) {
                    if(!selectRootNode.getFirstChild().toString().equals("All")) {
                        int result = JOptionPane.showConfirmDialog(AstroScopeLauncherImpl.this, 
                                "You have selected AstroScope (which is everything) this will remove all other selections.  Is this correct?",
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
            
            ItemRegistry registry = item.getItemRegistry();
            Entity entity = registry.getEntity(item);
            //Okay it is not All, they are selecting something unique.
            if(entity instanceof edu.berkeley.guir.prefuse.graph.Node) {
                edu.berkeley.guir.prefuse.graph.Node edgeNode = (edu.berkeley.guir.prefuse.graph.Node)entity;
                Vector treePath = new Vector();
                //Okay put in a vector the nodes starting with selected node then its parents all the way up
                //to AstroScope.  This is talking about nodes in the graph.
                while(!label.equals("AstroSope")) {
                    System.out.println("label not astroscope it was: " + label);
                    treePath.add(new TreeNodeData(edgeNode.getAttribute("label"),
                            edgeNode.getAttribute("id"),
                            edgeNode.getAttribute("url")));                    
                    //treePath.add(label);
                    edgeNode = edgeNode.getEdge(0).getFirstNode();
                    label = edgeNode.getAttribute("label");
                }
                
                System.out.println("done with filling treepath its size = " + treePath.size());
                

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
                    System.out.println("in the treepath for loop i = " + i + " val = " + treePath.get(i));
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
                        System.out.println("parent will be adding: " + treePath.get(i) + " the parent = " + parent.toString());
                        child = new DefaultMutableTreeNode(treePath.get(i));
                        parent.add( child );
                        parent = child;
                    }
                }//for
                if(child != null) {
                    treeModel.reload();
                    selectTree.scrollPathToVisible(new TreePath(child.getPath()));
                }
                System.out.println("hasNode = " + hasNode);
                if(hasNode){                    
                    //if you went through the whole tree and it is still there,
                    //then they must be deselecting it.  So remove it.
                    if(parent.getChildCount() > 0) {
                        int result = JOptionPane.showConfirmDialog(AstroScopeLauncherImpl.this, 
                                "You have chosen to remove an item that has children, all those children will be removed from the selection tree. Is this ok?  Item: " + parent.toString(),
                                "Remove Selection", JOptionPane.YES_NO_OPTION );
                        if (result == JOptionPane.YES_OPTION) {
                            parent.removeFromParent();
                        }
                    }
                    parent.removeFromParent();
                    treeModel.reload();
                }//if
            }//if
            //check there is some node in the tree and enable save buttons.
            enableSaveButtons();
        }
        
        private void enableSaveButtons() {
            if(storageTable.size() > 0 && selectRootNode.getChildCount() > 0) {
                selectAllButton.setEnabled(true);
                saveButton.setEnabled(true);
                saveImageButton.setEnabled(true);                
            }else {
                selectAllButton.setEnabled(false);
                saveButton.setEnabled(false);
                saveImageButton.setEnabled(false);                                
            }
        }
        
        /*
         * User regular mouse click in an empty space before blindly removing things ask the user if there are nodes
         * already in the tree.
         */
        public void mouseClicked(java.awt.event.MouseEvent e) {
            System.out.println("entered mouseClicked");
            //add a JoptionPane to make sure they want to do it if the tree is big.
            if(selectRootNode.getChildCount() > 0) {
                int result = JOptionPane.showConfirmDialog(AstroScopeLauncherImpl.this, 
                        "You have clicked in an empty area on the display, this clears everything.  Confirm Clear Entire Selection Tree? ", 
                        "Clear Selection", JOptionPane.YES_NO_OPTION );
                if (result == JOptionPane.NO_OPTION) {
                    return;
                }                
            }
            selectRootNode.removeAllChildren();
            treeModel.reload();            
            super.mouseClicked(e);
            enableSaveButtons();
        }
    }
    
    /**
     * Rest of classes below come from prefuse.
     */    
    class PrefuseGridLayout extends edu.berkeley.guir.prefuse.action.assignment.Layout {
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
       
    public class DemoColorFunction extends ColorFunction {
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
    
    public class TreeMapSizeFunction extends edu.berkeley.guir.prefuse.action.AbstractAction {
        public void run(ItemRegistry registry, double frac) {
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
            double area = d.width*d.height;
            double divisor = ((double)leafCount)/area;
            iter = registry.getNodeItems();
            while ( iter.hasNext() ) {
                NodeItem n = (NodeItem)iter.next();
                n.setSize(n.getSize()/divisor);
            }
            
            System.out.println("leafCount = " + leafCount);
        } //
    } // end of inner class TreeMapSizeFunction
    
    public class RectangleRenderer extends ShapeRenderer {
        private Rectangle2D bounds = new Rectangle2D.Double();
        protected Shape getRawShape(VisualItem item) {
            Point2D d = (Point2D)item.getVizAttribute("dimension");
            if (d == null)
                System.out.println("uh-oh");
            bounds.setRect(item.getX(),item.getY(),d.getX(),d.getY());
            return bounds;
        } //
    } // end of inner class NodeRenderer
     
  
}

/* 
$Log: AstroScopeLauncherImpl.java,v $
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