/* ADQLToolEditorPanel.java
 * Created on 13-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.adqlEditor;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.im.InputContext;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.UIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.*;
import org.astrogrid.adql.AdqlException;
import org.astrogrid.adql.v1_0.beans.SelectDocument;
import org.astrogrid.adql.v1_0.beans.SelectType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree.EditPromptAction;
import org.astrogrid.desktop.modules.adqlEditor.commands.ColumnInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandFactory;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandInfo;
import org.astrogrid.desktop.modules.adqlEditor.commands.CopyHolder;
import org.astrogrid.desktop.modules.adqlEditor.commands.CutCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.EditCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.EnumeratedElementInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.EnumeratedInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.PasteIntoCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.PasteNextToCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.PasteOverCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.StandardInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.TableInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandFactory.UndoManager;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.TabularMetadataViewer;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.taskrunner.UIComponentWithMenu;

/**
 * @author jl99
 * 
 * 
 * @modified nww - simplifed by refactoring from 'a panel that edits a tool
 *           document' to ' a panel that edits an ADQL parameter'. Also made the
 *           parameter immutable - the editor cannot be switched to edit a
 *           different parameter - instead, construct a new editor.
 * 
 * In the future, might want to go even further - make this 'a panel that edits
 * an adql string' and leave it's clients to insert the adql into the tool
 * document / whatever
 */
public class ADQLEditorPanel extends JPanel implements TreeModelListener,
        ChangeListener, PropertyChangeListener {

    private static final Log log = LogFactory.getLog(ADQLEditorPanel.class);

    private StringBuffer logIndent;

    public static final String DIAGNOSTICS_ICON = "flag16.png";

    public static final String GOOD_COMPILE_ICON = "tick16.png";

    public static final String BAD_COMPILE_ICON = "no16.png";

    public static final String INFO_ICON = "info16.png";

    public static final String METADATA_ICON = "table16.png";

    public static final String XML_ICON = "xml.gif";

    public static final String EDIT_ICON = "edit16.png";

    public static final String LASTEDITS_ICON = "multiplefile16.png";

    private final ParameterValue queryParam;

    /**
     * registry resource that describes the application this query is targeted
     * at probably always going to be a CeaApplication at the moment, but use
     * the most general type 'Resource', to allow us to target TAP when it
     * appears
     */
    protected final Resource targetApplication;

    protected final UIComponent parent;

    protected final RegistryGoogle regChooser;

    protected final Registry registry;

    protected final Preference showDebugPanePreference;

    private URI ceaApplicationURI;

    private URI tabulaDataURI;

    // LHS Editor tabs...
    private JTabbedPane tabbedEditorPane;

    private AdqlTree adqlTree;

    private AdqlXmlView adqlXmlView;

    private AdqlMainView adqlMainView;

    private CommentView adqlCommentView;

    // RHS Metadata tabs...
    private JTabbedPane tabbedMetadataPane;

    // Bottom View Tabs...
    private JTabbedPane tabbedBottomPane;

    // This is the diagnostics tab...
    private JTextPane diagnostics;

    private JTextPane history;

    // private JButton chooseResourceButton ;

    private JButton validateEditButton;

    private SizedStack clipBoard = new SizedStack();

    private String editWindowOldImage;

    private boolean bEditWindowUpdatedByFocusGained = false;

    private SizedStack historyStack = new SizedStack();

    final JLabel historyStackCount = new JLabel();

    final JButton historyTopButton = new JButton("Top");

    final JButton historyUpButton = new JButton("Up");

    final JButton historyDownButton = new JButton("Down");

    final JButton historyBottomButton = new JButton("Bottom");

    private class SizedStack extends Stack {

        private int maxSize;

        private int currentPosition = 0;

        public SizedStack() {
            this(16);
        }

        public SizedStack(int maxSize) {
            this.maxSize = maxSize;
        }

        public Object push(Object item) {
            if (size() == maxSize) {
                removeElementAt(0);
            }
            currentPosition = 1;
            return super.push(item);
        }

        public Object peek(int position) {
            if (isEmpty())
                throw new java.util.EmptyStackException();
            return get(vectorDisplacement());
        }

        public Object down() {
            if (currentPosition >= size())
                return null;
            return peek(currentPosition++);
        }

        public Object up() {
            if (currentPosition <= 1)
                return null;
            return peek(currentPosition--);
        }

        public Object top() {
            if (isEmpty())
                return null;
            currentPosition = 1;
            return peek();
        }

        public Object bottom() {
            if (isEmpty())
                return null;
            currentPosition = size();
            return peek(currentPosition);
        }

        public boolean isAtBottom() {
            if (isEmpty())
                return false;
            return (currentPosition == size());
        }

        public boolean isAtTop() {
            if (isEmpty())
                return false;
            return (currentPosition == 1);
        }

        public int getCurrentPosition() {
            return currentPosition;
        }

        public void setCurrentPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        private int vectorDisplacement() {
            if (isEmpty())
                return -1;
            return size() - currentPosition;
        }

    }

    // private BranchExpansionListener branchExpansionListener = null ;
    private AdqlTransformer transformer;

    public ADQLEditorPanel(ParameterValue queryParam,
            Resource targetApplication, UIComponentWithMenu parent,
            RegistryGoogle regChooser, Registry registry,
            Preference showDebugPanePref) {
        if (log.isTraceEnabled())
            enterTrace("ADQLEditorPanel()");
        this.queryParam = queryParam;
        this.targetApplication = targetApplication;
        this.parent = parent;
        this.regChooser = regChooser;
        this.registry = registry;
        this.transformer = new AdqlTransformer();
        this.showDebugPanePreference = showDebugPanePref;
        this.showDebugPanePreference.addPropertyChangeListener(this);
        this.init();
        new ContextMenuAssistant(this, parent.getContextMenu());
        if (log.isTraceEnabled())
            exitTrace("ADQLEditorPanel()");
    }

    /**
     * class to manage showing / hiding of context menu items.
     * 
     * populated with example menu items at the moment - in real use, the menu
     * items would probably be replaced with Action classes defined separately,
     * so this class would just populate / depopulate the menu and control the
     * visibility of the items.
     */
    private class ContextMenuAssistant implements HierarchyListener {

        private final JMenu contextMenu;

        private MenuListener menuListener;

        public ContextMenuAssistant(JPanel p, JMenu contextMenu) {
            if (log.isTraceEnabled())
                enterTrace("ContextMenuAssistant()");
            this.contextMenu = contextMenu;
            p.addHierarchyListener(this);
            if (log.isTraceEnabled())
                exitTrace("ContextMenuAssistant()");
        }

        public void hierarchyChanged(HierarchyEvent e) {
            long flags = e.getChangeFlags();
            if ((flags & HierarchyEvent.SHOWING_CHANGED) != 0) {
                contextMenu.setEnabled(isShowing());
            } else if ((flags & HierarchyEvent.PARENT_CHANGED) != 0) {
                if (getParent() == null) {
                    // remove the menu entries for non ADQL contexts...
                    contextMenu.removeMenuListener(menuListener);
                } else {
                    // "add" the menu entries, or at least a menu listener
                    // that manages adding or removing...

                    menuListener = new MenuListener() {

                        public void menuCanceled(MenuEvent e) {
                        }

                        public void menuDeselected(MenuEvent e) {
                        }

                        public void menuSelected(MenuEvent e) {
                            if (isShowing()) {
                                AdqlNode entry = (AdqlNode) adqlTree
                                        .getLastSelectedPathComponent();
                                if (entry != null) {
                                    buildEditContextMenu(contextMenu, entry);
                                }
                            }
                        }

                    };
                    contextMenu.addMenuListener(menuListener);
                }
            }
        }

    }

    private AdqlTree setAdqlTree() {
        if (log.isTraceEnabled())
            enterTrace("setAdqlTree()");
        URI toolIvorn = targetApplication.getId();
        String query = null;
        this.adqlTree = null;
        if (queryParam == null) {
            if (this.adqlTree == null) {
                this.adqlTree = new AdqlTree(parent, registry, toolIvorn,
                        tabulaDataURI);
            }
        }
        //
        // Not quite sure of the reason for the hasIndirect() method call.
        // However, I will test for the presence of the indirection flag AND
        // whether it is set to true....
        else if (queryParam.hasIndirect() == true
                && queryParam.getIndirect() == true) {
            if (log.isDebugEnabled())
                log.debug("Query is a remote reference.");
            query = readQuery();
            if (query == null || query.length() < 5) {
                this.adqlTree = new AdqlTree(parent, registry, toolIvorn,
                        tabulaDataURI);
            } else if (query.startsWith("<")) {
                query = adaptToVersion(query);
                try {
                    this.adqlTree = new AdqlTree(parent, query, registry,
                            toolIvorn, tabulaDataURI);
                } catch (Exception ex) {
                    this.adqlTree = new AdqlTree(parent, registry, toolIvorn,
                            tabulaDataURI);
                }
            }
            if (log.isDebugEnabled())
                log.debug("...setting indirect to false");
            queryParam.setIndirect(false);
            // try {
            // URI fileLocation = new URI( queryParam.getValue().trim() ) ;
            // InputStream is = myspace.getInputStream( fileLocation );
            //           
            // if( this.adqlTree == null ) {
            // this.adqlTree = new AdqlTree( is );
            // }
            // else {
            // this.adqlTree.setTree( AdqlTree.parseXml( is ) ) ;
            // }
            //                
            // } catch (Exception e) {
            // this.adqlTree = new AdqlTree() ;
            // e.printStackTrace() ;
            // }

        } else {
            if (log.isDebugEnabled())
                log.debug("Query is inline...");
            query = queryParam.getValue();
            if (query == null || query.length() < 5) {
                this.adqlTree = new AdqlTree(parent, registry, toolIvorn,
                        tabulaDataURI);
            } else if (query.startsWith("<")) {
                // Assume this is an instream adql/x query...
                query = adaptToVersion(query);
                try {
                    this.adqlTree = new AdqlTree(parent, query, registry,
                            toolIvorn, tabulaDataURI);
                } catch (Exception ex) {
                    this.adqlTree = new AdqlTree(parent, registry, toolIvorn,
                            tabulaDataURI);
                }
            } else {
                // Assume this is an instream adql/s query...

                // JL Note. I'm not supporting this for the moment
                // because there is no easy way to see how the metadata
                // can be recovered from the adql/s in a simple way
                // (maybe some specialized comment?)
                // So for the moment...
                this.adqlTree = new AdqlTree(parent, registry, toolIvorn,
                        tabulaDataURI);
                //                      
                // Document doc = null ;
                // try {
                // doc = validator.s2x( query ) ;
                // this.adqlTree = new AdqlTree( doc ) ;
                // }
                // // this needs to be refined...
                // catch ( Exception se ) {
                // log.debug( "Failed parsing by validator...", se ) ;
                // this.adqlTree = new AdqlTree() ;
                // }
            }
        }
        this.adqlTree.setEnabled(true);
        this.adqlTree.addTreeExpansionListener(new BranchExpansionListener());
        this.adqlTree.addMouseListener(new Popup());
        this.adqlTree.addChangeListener(this);
        if (log.isDebugEnabled()) {
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);
            log.debug("setAdqlTree() pretty print from root:\n"
                    + getRoot().toString());
        }
        this.setAdqlParameter();
        this.adqlTree.getModel().addTreeModelListener(this);

        //
        // setup the actionmap...
        ActionMap map = this.adqlTree.getActionMap();
        map.put(UIComponentMenuBar.EditMenuBuilder.COPY, new CopyAction());
        map.put(UIComponentMenuBar.EditMenuBuilder.CUT, new CutAction());
        map
                .put(UIComponentMenuBar.EditMenuBuilder.PASTE,
                        new PasteOverAction());

        this.adqlTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                if (e.isAddedPath()) {
                    TreePath path = e.getPath();
                    ActionMap map = adqlTree.getActionMap();
                    CopyAction copya = (CopyAction) map
                            .get(UIComponentMenuBar.EditMenuBuilder.COPY);
                    copya.setEnabled(copya.wouldBeEnabled(path));
                    CutAction cuta = (CutAction) map
                            .get(UIComponentMenuBar.EditMenuBuilder.CUT);
                    cuta.setEnabled(cuta.wouldBeEnabled(path));
                    PasteOverAction pasta = (PasteOverAction) map
                            .get(UIComponentMenuBar.EditMenuBuilder.PASTE);
                    pasta.setEnabled(pasta.wouldBeEnabled(path));
                }
            }

        });

        if (log.isTraceEnabled())
            exitTrace("setAdqlTree()");
        return this.adqlTree;
    }

    private String adaptToVersion(String query) {
        String returnQuery = query;
        StringBuffer buffer = new StringBuffer(returnQuery);
        int index = buffer.indexOf(AdqlData.NAMESPACE_0_74);
        if (index != -1) {
            if (log.isDebugEnabled())
                log.debug("Namespace replaced");
            buffer.replace(index, index + AdqlData.NAMESPACE_0_74.length(),
                    AdqlData.NAMESPACE_1_0);
            returnQuery = buffer.toString();
        } else if (buffer.indexOf(AdqlData.NAMESPACE_1_0) == -1) {
            log.error("Unrecognized namespace in query:\n" + query);
        }
        return returnQuery;
    }

    private String readQuery() {
        String retQuery = null;
        BufferedInputStream bis = null;
        URL fileLocation = null;
        try {
            fileLocation = new URL(queryParam.getValue().trim());
            bis = new BufferedInputStream(fileLocation.openStream(), 2000);
            StringBuffer buffer = new StringBuffer(2000);
            int c;
            while ((c = bis.read()) != -1) {
                buffer.append((char) c);
            }
            retQuery = buffer.toString();
        } catch (Exception exception) {
            log.error("Failed to read adql file: " + fileLocation, exception);
        } finally {
            try {
                bis.close();
            } catch (Exception ex) {
                ;
            }
        }
        return retQuery;
    }

    private void findTabulaData() {
        if (targetApplication != null) {
            ceaApplicationURI = targetApplication.getId();
            Content content = targetApplication.getContent();
            Relationship[] rels = content.getRelationships();
            ResourceName[] rns = rels[0].getRelatedResources();
            try {
                tabulaDataURI = new URI(rns[0].getValue());
            } catch (URISyntaxException uex) {
                tabulaDataURI = null;
            }
        }
        if (log.isDebugEnabled()) {

            if (targetApplication == null) {
                log.debug("targetApplication is null");
            } else {
                StringBuffer b = new StringBuffer();
                b.append(targetApplication.getId()).append('\n').append(
                        targetApplication.getShortName()).append('\n').append(
                        targetApplication.getType()).append('\n');

                Content content = targetApplication.getContent();
                Relationship[] rels = content.getRelationships();
                for (int i = 0; i < rels.length; i++) {
                    b.append(rels[i].getRelationshipType()).append('\n');
                    ResourceName[] rns = rels[i].getRelatedResources();
                    for (int j = 0; j < rns.length; j++) {
                        b.append(rns[j].getId()).append('\n');
                        b.append(rns[j].getValue()).append('\n');
                    }
                    b.append("===");
                }
                log.debug(b.toString());
            }

        }
    }

    private void init() {
        findTabulaData();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        JSplitPane splAdqlToolEditor = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splAdqlToolEditor.setBorder(null);
        // Put the editing panels plus the metadata panels in the top half of
        // the split pane.
        splAdqlToolEditor.setTopComponent(initTopView());

        // Put the diagnostics panel at the bottom of the split pane.
        splAdqlToolEditor.setBottomComponent(initBottomView());

        // Set the rest of the split pane's properties,
        splAdqlToolEditor.setDividerLocation(0.80);
        splAdqlToolEditor.setResizeWeight(0.80);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        add(splAdqlToolEditor, gbc);
    } // end of init()

    private JSplitPane initTopView() {
        JSplitPane topView = new JSplitPane();
        topView.setBorder(null);
        // topView.setPreferredSize(new Dimension(600,350));
        topView.setMaximumSize(null);
        // Put the editor panels in the left side of the split pane.
        topView.setLeftComponent(initLeftHandView());

        // Put the metadata/details panels in the right side of the split pane.
        topView.setRightComponent(initRightHandView());

        // Set the rest of the split pane's properties,
        // apparantly float doesn't work unless splut pane is already visible
        topView.setDividerLocation(250);
        topView.setResizeWeight(0.60);
        topView.setOneTouchExpandable(true);
        return topView;
    }

    private JPanel initLeftHandView() {

        // LHS top pane...
        JPanel lhsPanel = new JPanel();
        lhsPanel.setLayout(new GridBagLayout());
        lhsPanel.setBorder(BorderFactory.createEmptyBorder());

        // Create the components for the left side of the split pane:
        // the panel, scrolling panel, and the XML tree it will contain.
        tabbedEditorPane = new JTabbedPane();
        tabbedEditorPane.setBorder(BorderFactory.createEmptyBorder());
        tabbedEditorPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedEditorPane.setTabPlacement(SwingConstants.TOP);

        // Experiment with combined views (tree/adql/s)...
        this.setAdqlTree();
        this.adqlMainView = new AdqlMainView(tabbedEditorPane);

        //
        // Place any top comment in the Comment View...
        this.adqlCommentView = new CommentView(tabbedEditorPane);

        // Adql/x panel ...
        // This is really only here for debug purposes...
        boolean bDebugView = showDebugPanePreference.asBoolean();
        if (bDebugView) {
            this.adqlXmlView = new AdqlXmlView(tabbedEditorPane);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        lhsPanel.add(tabbedEditorPane, gbc);

        // gbc.weighty = 0;
        // gbc.gridheight = 1 ;
        // gbc.gridwidth = 8 ;
        // gbc.gridx = 0 ;
        // gbc.gridy = 7 ;
        // // gbc.fill = GridBagConstraints.HORIZONTAL ;
        // gbc.fill = GridBagConstraints.NONE ;
        //        
        // lhsPanel.add( getValidateEditButton(), gbc) ;

        return lhsPanel;
    }

    private JTabbedPane initBottomView() {
        //
        // Create the components for the bottom view:

        tabbedBottomPane = new JTabbedPane();
        tabbedBottomPane.setBorder(null);
        Dimension dim = new Dimension(400, 80);
        tabbedBottomPane.setPreferredSize(dim);
        tabbedBottomPane.setMaximumSize(dim);
        tabbedBottomPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedBottomPane.setTabPlacement(SwingConstants.TOP);

        formatDiagnosticsTab();

        formatHistoryTab();

        return tabbedBottomPane;
    }

    private void formatDiagnosticsTab() {        
        JPanel dp = new JPanel() ;
        dp.setLayout( new BorderLayout() ) ;        
        diagnostics = new JTextPane();
        diagnostics.setBorder(BorderFactory.createEmptyBorder());
        JScrollPane scrContent = new JScrollPane(diagnostics);
        scrContent.setBorder(BorderFactory.createEmptyBorder());
        dp.add( scrContent, BorderLayout.CENTER ) ;
        tabbedBottomPane.addTab("Diagnostics", IconHelper
                .loadIcon(DIAGNOSTICS_ICON), dp, "View errors");
    }

    private void formatHistoryTab() {

        JScrollPane hscrContent = new JScrollPane();
        hscrContent.setBorder(BorderFactory.createEmptyBorder());

        historyStack = new SizedStack();
        historyStackCount.setText("0 of 0");
        historyTopButton.setEnabled(false);
        historyUpButton.setEnabled(false);
        historyDownButton.setEnabled(false);
        historyBottomButton.setEnabled(false);

        if (historyTopButton.getActionListeners().length == 0) {
            historyStackCount.setBorder(BorderFactory
                    .createLineBorder(Color.BLACK));
            historyTopButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setHistoryText((String) getHistoryStack().top());
                }
            });
            historyUpButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setHistoryText((String) getHistoryStack().up());
                }
            });
            historyDownButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setHistoryText((String) getHistoryStack().down());
                }
            });
            historyBottomButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setHistoryText((String) getHistoryStack().bottom());
                }
            });
        }

        Box buttons = Box.createHorizontalBox();
        buttons.add(historyStackCount);
        buttons.add(historyTopButton);
        buttons.add(historyDownButton);
        buttons.add(historyUpButton);
        buttons.add(historyBottomButton);
        buttons.add(Box.createHorizontalGlue());
        JViewport buttonView = new JViewport();
        buttonView.setView(buttons);
        hscrContent.setColumnHeaderView(buttonView);

        history = new JTextPane();
        history.setBorder(BorderFactory.createEmptyBorder());
        hscrContent.setViewportView(history);
        tabbedBottomPane.addTab("History stack", IconHelper
                .loadIcon(LASTEDITS_ICON), hscrContent, "View last 16 edits");
    }

    private SizedStack getHistoryStack() {
        return historyStack;
    }

    private void setHistoryText(String text) {
        if (text != null) {
            historyStackCount.setText(historyStack.getCurrentPosition()
                    + " of " + historyStack.size());
            history.setText(text);
        }
        enableHistoryButtons();
    }

    private void enableHistoryButtons() {
        SizedStack stack = getHistoryStack();
        if (stack.size() <= 1) {
            historyTopButton.setEnabled(false);
            historyUpButton.setEnabled(false);
            historyDownButton.setEnabled(false);
            historyBottomButton.setEnabled(false);
        } else if (stack.isAtBottom()) {
            historyTopButton.setEnabled(true);
            historyUpButton.setEnabled(true);
            historyDownButton.setEnabled(false);
            historyBottomButton.setEnabled(false);
        } else if (stack.isAtTop()) {
            historyTopButton.setEnabled(false);
            historyUpButton.setEnabled(false);
            historyDownButton.setEnabled(true);
            historyBottomButton.setEnabled(true);
        } else {
            historyTopButton.setEnabled(true);
            historyUpButton.setEnabled(true);
            historyDownButton.setEnabled(true);
            historyBottomButton.setEnabled(true);
        }
    }

    private void setDiagnosticsIcon(boolean errorFree) {
        ImageIcon icon = null;
        if (errorFree) {
            icon = IconHelper.loadIcon(GOOD_COMPILE_ICON);
        } else {
            icon = IconHelper.loadIcon(BAD_COMPILE_ICON);
        }
        int i = tabbedBottomPane.indexOfTab("Diagnostics");
        tabbedBottomPane.setIconAt(i, icon);
    }

    private JPanel initRightHandView() {

        // RHS top pane...
        JPanel rhsPanel = new JPanel();
        // rhsPanel.setPreferredSize(new Dimension(400,350));
        rhsPanel.setLayout(new GridBagLayout());

        // Top bit will be a series of metadata tabs...
        tabbedMetadataPane = new JTabbedPane();
        tabbedMetadataPane.setBorder(BorderFactory.createEmptyBorder());
        tabbedMetadataPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedMetadataPane.setTabPlacement(SwingConstants.TOP);

        // First catalogue panel...
        JPanel catalog1 = new JPanel();
        tabbedMetadataPane.addTab("Archive",
                IconHelper.loadIcon(METADATA_ICON), catalog1, "View metadata");

        // @todo replace this with a flip panel.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = 7;
        gbc.gridwidth = 8;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        rhsPanel.add(tabbedMetadataPane, gbc);
        
        return rhsPanel;
    }

    public void treeNodesChanged(TreeModelEvent e) {
        if (e != null /* && e.getSource() != ADQLToolEditorPanel.this */) {
            this.setAdqlParameter();
            refreshCommentAndDebugViews();
        }
    }

    public void treeNodesInserted(TreeModelEvent e) {
        if (e != null /* && e.getSource() != ADQLToolEditorPanel.this */) {
            this.update();
            refreshCommentAndDebugViews();
        }
    }

    public void treeNodesRemoved(TreeModelEvent e) {
        if (e != null /* && e.getSource() != ADQLToolEditorPanel.this */) {
            this.update();
            refreshCommentAndDebugViews();
        }
    }

    public void treeStructureChanged(TreeModelEvent e) {
        if (e != null /* && e.getSource() != ADQLToolEditorPanel.this */) {
            this.update();
            refreshCommentAndDebugViews();
        }
    }

    private void refreshCommentAndDebugViews() {
        if (this.adqlXmlView != null)
            this.adqlXmlView.refreshFromModel();
        this.adqlCommentView.refreshDisplay();
    }

    private void update() {
        this.setAdqlParameter();
        this.validateAdql();
    }

    private void validateAdql() {
        if (log.isTraceEnabled())
            enterTrace("validateAdql()");
        String[] messages = validateUponAdqlsFromRoot();
        if (messages.length != 0) {
            setDiagnosticsIcon(false);
            StringBuffer buffer = new StringBuffer(1024);
            for (int i = 0; i < messages.length; i++) {
                buffer.append("[" + (i + 1) + "] ").append(messages[i]).append(
                        '\n');
            }
            this.diagnostics.setText(buffer.toString());
        } else {
            this.diagnostics.setText("");
            setDiagnosticsIcon(true);
        }
        if (log.isTraceEnabled())
            exitTrace("validateAdql()");
    }

    private String[] validateUponAdqlsFromRoot() {
        String[] messages;
        XmlObject userObject = adqlTree.getRoot();
        userObject = AdqlUtils.modifyQuotedIdentifiers(userObject);
        XmlCursor nodeCursor = userObject.newCursor();
        String text = nodeCursor.xmlText();
        nodeCursor.dispose();
        userObject = AdqlUtils.unModifyQuotedIdentifiers(userObject);
        String adqls = transformer.transformToAdqls(text, " ");

        try {
            adqlTree.getCommandFactory().getAdqlCompiler(adqls)
                    .compileToXmlText();
            messages = new String[0];
        } catch (AdqlException adqlex) {
            messages = adqlex.getMessages();
            if (messages == null) {
                messages = (new String[] { "Internal compiler error. See log." });
                log.debug("Internal compiler error.", adqlex);
            } else if (messages.length == 0) {
                messages = (new String[] { "Internal compiler error. See log." });
                log.debug("Internal compiler error.", adqlex);
            }
        } catch (Exception ex) {
            messages = (new String[] { "Internal compiler error. See log." });
            log.debug("Internal compiler error.", ex);
        }
        return messages;
    }

    private class BranchExpansionListener implements TreeExpansionListener {

        public void treeCollapsed(TreeExpansionEvent event) {
            AdqlNode entry = (AdqlNode) event.getPath().getLastPathComponent();
            entry.setExpanded(false);
        }

        public void treeExpanded(TreeExpansionEvent event) {
            AdqlNode entry = (AdqlNode) event.getPath().getLastPathComponent();
            entry.setExpanded(true);
        }

    } // end of class BranchExpansionListener

    private class Popup extends MouseAdapter {

        // fix for mac. for portability, need to check on mousePressed
        // and mouseReleased whether it's the 'popupTrigger' event.
        // onlny way to do it - as a mac CTRL-Cick gives a different event type
        // to a Button-3 click.
        // complicated, eh?
        // http://developer.apple.com/documentation/Java/Conceptual/Java14Development/07-NativePlatformIntegration/NativePlatformIntegration.html
        public void mouseReleased(MouseEvent event) {
            if (event.isPopupTrigger()) {
                processPopupClick(event);
            }
        }

        public void mousePressed(MouseEvent event) {
            if (event.isPopupTrigger()) {
                processPopupClick(event);
            }
        }

        /**
         * @param event
         */
        private void processPopupClick(MouseEvent event) {
            int x = event.getX();
            int y = event.getY();
            TreePath path = adqlTree.getPathForLocation(x, y);
            if (path != null) {
                adqlTree.setSelectionPath(path);
                adqlTree.scrollPathToVisible(path);
                showPopup(path, x, y);
            }
        }

        private void showPopup(TreePath path, int x, int y) {
            AdqlNode entry = (AdqlNode) adqlTree.getLastSelectedPathComponent();
            getPopupMenu(entry).show(adqlTree, x, y);
        }

        private JPopupMenu getPopupMenu(AdqlNode entry) {
            JPopupMenu popup = new JPopupMenu("AdqlTreeContextMenu");
            buildPopupEditMenu(popup, entry);
            return popup;
        }

    } // end of class PopupMenu

    private class CutAction extends AbstractAction {

        public CutAction() {
            super("Cut");
        }

        public boolean isEnabled() {
            TreePath path = adqlTree.getSelectionPath();
            return wouldBeEnabled(path);
        }

        public boolean wouldBeEnabled(TreePath path) {
            //
            // If the path is null or there is no parent ( count < 2 )
            // (ie: we should not remove the SelectDocument)
            // Then we cannot cut...
            if (path == null) {
                return false;
            }
            int pathCount = path.getPathCount();
            if (pathCount < 2) {
                return false;
            } else {
                //
                // OK. We now need to be sure we don't cut the top
                // SelectType (ie: the whole query)...
                AdqlNode node = (AdqlNode) path.getLastPathComponent();
                AdqlNode root = adqlTree.getRootNode();
                //
                // Not sure the first will work in all situations...
                if (node == root) {
                    return false;
                }
                if (pathCount < 3
                        && AdqlUtils.areTypesEqual(node.getXmlObject(),
                                AdqlData.SELECT_TYPE)) {
                    return false;
                }
            }
            return true;
        }

        public void actionPerformed(ActionEvent e) {
            if (log.isTraceEnabled()) {
                enterTrace("CutAction.actionPerformed()");
            }
            try {
                TreePath path = adqlTree.getSelectionPath();
                if (path == null)
                    return;
                AdqlNode node = (AdqlNode) path.getLastPathComponent();
                CutCommand command = adqlTree.getCommandFactory()
                        .newCutCommand(adqlTree,
                                adqlTree.getCommandFactory().getUndoManager(),
                                node);
                CopyHolder copy = command.getCopy();
                clipBoard.push(copy);
                copyToSystemClipboard(node);
                if (command.execute() != CommandExec.FAILED) {
                    DefaultTreeModel model = (DefaultTreeModel) adqlTree
                            .getModel();
                    model.nodeStructureChanged(command.getParentEntry());
                    adqlTree.ensureSomeNodeSelected(command);
                    adqlTree.repaint();
                }
            } finally {
                if (log.isTraceEnabled()) {
                    exitTrace("CutAction.actionPerformed()");
                }
            }
        }
    }

    private class CopyAction extends AbstractAction {

        public boolean isEnabled() {
            TreePath path = adqlTree.getSelectionPath();
            return wouldBeEnabled(path);
        }

        public boolean wouldBeEnabled(TreePath path) {
            //
            // If the path is null or there is no parent ( count < 2 )
            // Then we cannot copy...
            if (path == null) {
                return false;
            }
            if (path.getPathCount() < 2) {
                return false;
            }
            return true;
        }

        public CopyAction() {
            super("Copy");
        }

        public void actionPerformed(ActionEvent e) {
            TreePath path = adqlTree.getSelectionPath();
            if (path == null)
                return;
            AdqlNode node = (AdqlNode) path.getLastPathComponent();
            CopyHolder copy = CopyHolder.holderForCopyPurposes(node);
            clipBoard.push(copy);
            copyToSystemClipboard(node);
        }
    }

    private void copyToSystemClipboard(AdqlNode node) {
        XmlCursor nodeCursor = null;
        try {
            XmlObject userObject = node.getXmlObject();
            userObject = AdqlUtils.modifyQuotedIdentifiers(userObject);
            nodeCursor = userObject.newCursor();
            String text = nodeCursor.xmlText();
            userObject = AdqlUtils.unModifyQuotedIdentifiers(userObject);
            String adqls = transformer.transformToAdqls(text, " ");
            StringSelection contents = new StringSelection(adqls);
            Clipboard clipboard = Toolkit.getDefaultToolkit()
                    .getSystemClipboard();
            clipboard.setContents(contents, null);
        } catch (Exception ex) {
            ;
        } finally {
            if (nodeCursor != null)
                nodeCursor.dispose();
        }
    }

    private class PasteOverAction extends AbstractAction {

        public PasteOverAction() {
            super("Paste");
        }

        public void actionPerformed(ActionEvent e) {
            if (log.isTraceEnabled()) {
                enterTrace("PasteOverAction.actionPerformed()");
            }
            try {
                AdqlNode node = (AdqlNode) adqlTree.getSelectionPath()
                        .getLastPathComponent();
                PasteOverCommand command = adqlTree.getCommandFactory()
                        .newPasteOverCommand(node,
                                (CopyHolder) clipBoard.peek());
                if (command.execute() != CommandExec.FAILED) {
                    DefaultTreeModel model = (DefaultTreeModel) adqlTree
                            .getModel();
                    model.nodeStructureChanged(command.getParentEntry());
                    adqlTree.ensureSomeNodeSelected(command);
                    adqlTree.repaint();
                }
            } finally {
                if (log.isTraceEnabled()) {
                    exitTrace("PasteOverAction.actionPerformed()");
                }
            }
        }

        public boolean isEnabled() {
            TreePath path = adqlTree.getSelectionPath();
            return wouldBeEnabled(path);
        }

        public boolean wouldBeEnabled(TreePath path) {
            if (preConditionsForPaste(path) == true) {
                CopyHolder copy = (CopyHolder) clipBoard.peek();
                if (AdqlUtils.isCopyHolderIdenticalToSystemClipboard(copy,
                        transformer)) {
                    //
                    // If we get this far, then we know we can safely use the
                    // local copy...
                    AdqlNode targetOfPasteOver = (AdqlNode) path
                            .getLastPathComponent();
                    if (AdqlUtils.isSuitablePasteOverTarget(targetOfPasteOver,
                            copy.getSource())) {
                        return true;
                    }
                }
            }
            return false;
        }

    }

    private class PasteNextToAction extends AbstractAction {
        private PasteNextToCommand command;

        public PasteNextToAction(AdqlNode entry, boolean before) {
            super();
            if (before) {
                super.putValue(Action.NAME, "Paste before");
            } else {
                super.putValue(Action.NAME, "Paste after");
            }
            TreePath path = adqlTree.getSelectionPath();
            if (preConditionsForPaste(path) == true) {
                this.command = adqlTree.getCommandFactory()
                        .newPasteNextToCommand(entry,
                                (CopyHolder) clipBoard.peek(), before);
            }
            setEnabled(command != null && command.isChildEnabled());
        }

        public void actionPerformed(ActionEvent e) {
            if (log.isTraceEnabled()) {
                enterTrace("PasteNextToAction.actionPerformed()");
            }
            try {
                if (command.execute() != CommandExec.FAILED) {
                    DefaultTreeModel model = (DefaultTreeModel) adqlTree
                            .getModel();
                    model.nodeStructureChanged(command.getParentEntry());
                    adqlTree.ensureSomeNodeSelected(this.command);
                    adqlTree.repaint();
                }
            } finally {
                if (log.isTraceEnabled()) {
                    exitTrace("PasteNextToAction.actionPerformed()");
                }
            }
        }

    }

    private class PasteIntoAction extends AbstractAction {
        private PasteIntoCommand command = null;

        public PasteIntoAction(AdqlNode entry) {
            super("Paste into");
            TreePath path = adqlTree.getSelectionPath();
            if (preConditionsForPaste(path) == true) {
                this.command = adqlTree.getCommandFactory()
                        .newPasteIntoCommand(entry,
                                (CopyHolder) clipBoard.peek());
            }
            setEnabled(command != null && command.isChildEnabled());
        }

        public void actionPerformed(ActionEvent e) {
            if (log.isTraceEnabled()) {
                enterTrace("PasteIntoAction.actionPerformed()");
            }
            try {
                if (command.execute() != CommandExec.FAILED) {
                    DefaultTreeModel model = (DefaultTreeModel) adqlTree
                            .getModel();
                    model.nodeStructureChanged(command.getParentEntry());
                    adqlTree.ensureSomeNodeSelected(this.command);
                    adqlTree.repaint();
                }
            } finally {
                if (log.isTraceEnabled()) {
                    exitTrace("PasteIntoAction.actionPerformed()");
                }
            }
        }
    }

    private class UndoAction extends AbstractAction {

        CommandFactory.UndoManager undoManager;

        public UndoAction() {
            super();
            undoManager = adqlTree.getCommandFactory().getUndoManager();
            super.putValue(Action.NAME, undoManager.getUndoPresentationName());
            setEnabled(undoManager.canUndo());
        }

        public void actionPerformed(ActionEvent e) {
            if (log.isTraceEnabled()) {
                enterTrace("UndoAction.actionPerformed()");
            }
            try {
                CommandInfo ci = (CommandInfo) undoManager
                        .getCommandToBeUndone();
                undoManager.undo();
                DefaultTreeModel model = (DefaultTreeModel) adqlTree.getModel();
                model.nodeStructureChanged(ci.getParentEntry());
                adqlTree.ensureSomeNodeSelected(ci);
                adqlTree.repaint();
            } finally {
                if (log.isTraceEnabled()) {
                    exitTrace("UndoAction.actionPerformed()");
                }
            }
        }

    }

    private class RedoAction extends AbstractAction {

        CommandFactory.UndoManager undoManager;

        public RedoAction() {
            super();
            undoManager = adqlTree.getCommandFactory().getUndoManager();
            super.putValue(Action.NAME, undoManager.getRedoPresentationName());
            setEnabled(undoManager.canRedo());
        }

        public void actionPerformed(ActionEvent e) {
            if (log.isTraceEnabled()) {
                enterTrace("RedoAction.actionPerformed()");
            }
            try {
                CommandInfo ci = (CommandInfo) undoManager
                        .getCommandToBeRedone();
                undoManager.redo();
                DefaultTreeModel model = (DefaultTreeModel) adqlTree.getModel();
                model.nodeStructureChanged(ci.getParentEntry());
                adqlTree.ensureSomeNodeSelected(ci);
                adqlTree.repaint();
            } finally {
                if (log.isTraceEnabled()) {
                    exitTrace("RedoAction.actionPerformed()");
                }
            }
        }

    }

    private class UndoRedoAction extends AbstractAction {

        CommandFactory.UndoManager undoManager;

        public UndoRedoAction() {
            super();
            undoManager = adqlTree.getCommandFactory().getUndoManager();
            super.putValue(Action.NAME, undoManager
                    .getUndoOrRedoPresentationName());
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            if (log.isTraceEnabled()) {
                enterTrace("UndoRedoAction.actionPerformed()");
            }
            try {
                if (undoManager.canRedo()) {
                    CommandInfo ci = (CommandInfo) undoManager
                            .getCommandToBeRedone();
                    undoManager.redo();
                    DefaultTreeModel model = (DefaultTreeModel) adqlTree
                            .getModel();
                    model.nodeStructureChanged(ci.getParentEntry());
                    adqlTree.ensureSomeNodeSelected(ci);
                    adqlTree.repaint();
                } else if (undoManager.canUndo()) {
                    CommandInfo ci = (CommandInfo) undoManager
                            .getCommandToBeUndone();
                    undoManager.undo();
                    DefaultTreeModel model = (DefaultTreeModel) adqlTree
                            .getModel();
                    model.nodeStructureChanged(ci.getParentEntry());
                    adqlTree.ensureSomeNodeSelected(ci);
                    adqlTree.repaint();
                }
            } finally {
                if (log.isTraceEnabled()) {
                    exitTrace("UndoRedoAction.actionPerformed()");
                }
            }
        }

    }

    private class EditAction extends AbstractAction {

        public EditAction(AdqlNode entry) {
            super("Micro edit");
            TreePath path = adqlTree.getSelectionPath();
            if (path != null) {
                AdqlNode selectedEntry = (AdqlNode) adqlTree
                        .getLastSelectedPathComponent();
                if (selectedEntry.isBottomLeafEditable()) {
                    this.setEnabled(true);
                }
            }
        }

        public void actionPerformed(ActionEvent e) {
            AdqlTree.EditPromptAction action = adqlTree.new EditPromptAction(
                    "Micro Edit");
            action.actionPerformed(e);
        }
    }

    private class InsertAction extends AbstractAction {

        protected StandardInsertCommand command;

        public InsertAction(String name, StandardInsertCommand command) {
            super(name);
            this.command = command;
            this.setEnabled(command.isChildEnabled());
        }

        public void actionPerformed(ActionEvent e) {
            if (log.isTraceEnabled()) {
                enterTrace("InsertAction.actionPerformed()");
            }
            try {
                TreePath path = adqlTree.getSelectionPath();
                if (path == null)
                    return;

                command.setSelectedValue(e.getActionCommand());
                CommandExec.Result result = command.execute();

                // if( newEntry.isBottomLeafEditable() ) {
                // String typeName =
                // newEntry.getSchemaType().getName().getLocalPart() ;
                // String attributeName = (String)AdqlData.EDITABLE.get(
                // typeName ) ;
                // if( attributeName != null && attributeName.length() > 0 ) {
                // this.setDefaultValueForAttributeDrivenElement() ;
                // }
                // else {
                // this.setDefaultValueForElement() ;
                // }
                // }

                if (result != CommandExec.FAILED) {
                    adqlTree.getCommandFactory()
                            .retireOutstandingMultipleInsertCommands();
                    DefaultTreeModel model = (DefaultTreeModel) adqlTree
                            .getModel();
                    model.nodeStructureChanged(command.getParentEntry());
                    // path = path.pathByAddingChild( command.getChildEntry() )
                    // ;
                    // adqlTree.scrollPathToVisible( path ) ;
                    adqlTree.ensureSomeNodeSelected(this.command);
                    adqlTree.repaint();

                }

                // Switch to edit for a newly created bottom leaf...
                // This needs reviewing where multiple inserts are required...
                // if( newEntry.isBottomLeafEditable() ) {
                // adqlTree.setEditingActive( true ); // is this required?
                // adqlTree.startEditingAtPath( path ) ;
                // adqlTree.setEditingActive( false ); // is this required?
                // model.nodeChanged( newEntry ) ;
                // }

                // setAdqlParameter() ;
            } finally {
                if (log.isTraceEnabled()) {
                    exitTrace("InsertAction.actionPerformed()");
                }
            }

        } // end of actionPerformed()

    } // end of class InsertAction

    // protected JButton getChooseResourceButton() {
    // if (chooseResourceButton == null) {
    // chooseResourceButton = new JButton("Set Archive Definition..");
    // chooseResourceButton.addActionListener(new ActionListener() {
    // public void actionPerformed(ActionEvent e) {
    // Resource[] selection = regChooser.selectResourcesXQueryFilter( "Select
    // Catalogue description for "
    // + targetApplication.getTitle()
    // ,false
    // , "(@xsi:type &= '*TabularDB')"
    // ) ;
    // if( selection != null && selection.length > 0 ) {
    // if( log.isDebugEnabled() ) log.debug(
    // "regChooser.chooseResourceWithFilter() returned object of type: " +
    // selection[0].getClass().getName() ) ;
    // adqlTree.setCatalogueResource( (DataCollection)selection[0]) ;
    // chooseResourceButton.setEnabled( false ) ;
    // formatCatalogTab() ;
    // }
    // }
    // });
    // }
    // return chooseResourceButton;
    // }

    protected JButton getValidateEditButton() {
        if (validateEditButton == null) {
            validateEditButton = new JButton("Validate Edit");
            validateEditButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // validateEditButton.setEnabled( false ) ;
                    ADQLEditorPanel.this.adqlMainView.executeEditCommand();
                }
            });
        }
        return validateEditButton;
    }

    private void formatCatalogTab() {
        if (tabbedMetadataPane.getTabCount() == 1) {
            CatalogService catalogService = adqlTree.getCatalogService();

            tabbedMetadataPane.setTitleAt(0, adqlTree.getTitle());

            // First catalogue panel...
            TabularMetadataViewer tmv = new AdqlEditorTabularMetadataViewer(
                    this, adqlTree);
            tmv.display(catalogService);
            tabbedMetadataPane.setComponentAt(0, tmv);
        }
    }

    private JMenu getEnumeratedMenus(StandardInsertCommand command) {
        JMenu menu = new JMenu(command.getChildDisplayName());
        String[] attrValues = ((EnumeratedInsertCommand) command)
                .getEnumeratedValues();
        for (int i = 0; i < attrValues.length; i++) {
            menu.add(new JMenuItem(new InsertAction(attrValues[i], command)));
        }
        return menu;
    }

    private JMenu getEnumeratedElementMenus(StandardInsertCommand command) {
        JMenu menu = new JMenu(command.getChildDisplayName());
        String[] elementValues = ((EnumeratedElementInsertCommand) command)
                .getEnumeratedValues();
        for (int i = 0; i < elementValues.length; i++) {
            menu
                    .add(new JMenuItem(new InsertAction(elementValues[i],
                            command)));
        }
        return menu;
    }

    private JMenu getInsertColumnMenu(StandardInsertCommand command) {
        ColumnInsertCommand cic = (ColumnInsertCommand) command;
        JMenu insertMenu = new JMenu(command.getChildDisplayName());
        insertMenu.setEnabled(false);
        Object[] tables = adqlTree.getFromTables().values().toArray();
        Arrays.sort(tables, new TableDataComparator());
        ColumnBeanComparator colComparator = new ColumnBeanComparator();
        ColumnInsertCommand cicForTable = null;
        for (int i = 0; i < tables.length; i++) {
            if (cicForTable == null) {
                cicForTable = cic;
                insertMenu.setEnabled(true);
            } else {
                // Shallow copy of the command...
                cicForTable = new ColumnInsertCommand(cic);
            }
            AdqlTree.TableData tableData = (AdqlTree.TableData) tables[i];
            try {
                TableBean table = tableData.getTable();
                cicForTable.setTable(table);
                cicForTable.setTableAlias(tableData.alias);
                JMenu tableMenu = new JMenu(table.getName());
                insertMenu.add(tableMenu);
                ColumnBean[] columns = table.getColumns();
                Arrays.sort(columns, colComparator);
                for (int j = 0; j < columns.length; j++) {
                    tableMenu.add(new InsertAction(columns[j].getName(),
                            cicForTable));
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                continue;
            }
        }
        return insertMenu;
    }

    private JMenu getInsertTableMenu(StandardInsertCommand command) {
        JMenu insertMenu = new JMenu(command.getChildDisplayName());
        TableBean[] tables = adqlTree.getCatalogService().getTables();
        Arrays.sort(tables,
                new org.astrogrid.acr.astrogrid.TableBeanComparator());
        for (int i = 0; i < tables.length; i++) {
            insertMenu.add(new InsertAction(tables[i].getName(), command));
        }
        return insertMenu;
    }

    private JMenu getInsertJoinTableMenu(StandardInsertCommand command) {
        JMenu insertMenu = new JMenu(command.getChildDisplayName());
        TableBean[] tables = adqlTree.getCatalogService().getTables();
        Arrays.sort(tables,
                new org.astrogrid.acr.astrogrid.TableBeanComparator());
        for (int i = 0; i < tables.length; i++) {
            insertMenu.add(new InsertAction(tables[i].getName(), command));
        }
        return insertMenu;
    }

    private void setAdqlParameter() {
        if (queryParam.hasIndirect() == true)
            queryParam.setIndirect(false);
        AdqlNode rootEntry = ((AdqlNode) adqlTree.getModel().getRoot());
        XmlObject xmlRoot = (XmlObject) rootEntry.getUserObject();
        // NOte. I'm not sure the following is adequate.
        XmlOptions options = new XmlOptions();
        options.setSaveOuter();
        String xmlText = xmlRoot.xmlText(options);
        queryParam.setValue(xmlText);
    }

    private class AdqlXmlView extends JPanel {

        JTextPane textPane = new JTextPane();

        public AdqlXmlView(JTabbedPane owner) {
            super();
            JScrollPane xmlScrollContent = new JScrollPane();
            xmlScrollContent.setViewportView(textPane);
            textPane.setEditable(false);
            this.setLayout(new BorderLayout());
            this.add(xmlScrollContent, BorderLayout.CENTER);
            owner.addTab("Debug", IconHelper.loadIcon(XML_ICON), this,
                    "View the query in XML");
            this.refreshFromModel();
        }

        protected void refreshFromModel() {
            if (log.isTraceEnabled())
                enterTrace("AdqlXmlView.refreshFromModel()");
            XmlCursor nodeCursor = adqlTree.getRoot().newCursor();
            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            options.setSavePrettyPrintIndent(4);
            textPane.setText(nodeCursor.xmlText(options));
            nodeCursor.dispose();
            if (log.isTraceEnabled())
                exitTrace("AdqlXmlView.refreshFromModel()");
        }

    } // end of class AdqlXmlView

    private class CommentView extends JPanel implements FocusListener {

        JTextPane textPane = new JTextPane();

        public CommentView(JTabbedPane owner) {
            super();
            JScrollPane scrollContent = new JScrollPane();
            scrollContent.setViewportView(textPane);
            textPane.setEditable(true);
            SelectDocument selectDoc = (SelectDocument) adqlTree.getRoot();
            SelectType select = selectDoc.getSelect();
            if (select.isSetStartComment()) {
                textPane.setText(select.getStartComment());
            }
            this.setLayout(new BorderLayout());
            this.add(scrollContent, BorderLayout.CENTER);
            owner.addTab("Description", IconHelper.loadIcon(INFO_ICON), this,
                    "Enter a description for the query");
            textPane.addFocusListener(this);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
         */
        public void focusLost(FocusEvent e) {
            if (log.isTraceEnabled())
                enterTrace("CommentView.focusLost()");
            SelectDocument selectDoc = (SelectDocument) adqlTree.getRoot();
            SelectType select = selectDoc.getSelect();
            String comment = textPane.getText().trim();
            if (comment == null) {
                if (select.isSetStartComment()) {
                    select.unsetStartComment();
                }
            } else if (comment.length() == 0) {
                if (select.isSetStartComment()) {
                    select.unsetStartComment();
                }
            } else {
                select.setStartComment(comment);
            }
            refreshDebugView();
            if (log.isTraceEnabled())
                exitTrace("CommentView.focusLost()");
        }

        public void focusGained(FocusEvent e) {
        }

        protected void refreshDisplay() {
            SelectDocument selectDoc = (SelectDocument) adqlTree.getRoot();
            SelectType select = selectDoc.getSelect();
            if (select.isSetStartComment()) {
                textPane.setText(select.getStartComment());
            }
        }

        private void refreshDebugView() {
            if (adqlXmlView != null)
                adqlXmlView.refreshFromModel();
        }

    } // end of class CommentView

    private class AdqlMainView extends JPanel {

        private final JTabbedPane owner;

        private JSplitPane splitPane;

        private final AdqlsView textPane = new AdqlsView();

        public AdqlMainView( JTabbedPane owner ) {
            super( new GridBagLayout() ) ;
// super(JSplitPane.VERTICAL_SPLIT ) ;
            this.setBorder( BorderFactory.createLineBorder( Color.black) ) ;
            this.owner = owner ;
            this.owner.addTab( "Edit", IconHelper.loadIcon( EDIT_ICON ), this, "Edit and validate query" ) ;            
            //
            // Format the split pane containing
            // tree and text views first...
            this.splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT ) ;  
            this.splitPane.setBorder( BorderFactory.createEmptyBorder() ) ;
            //
            // First the tree view part...
            // On creation, default to top Select node.
            adqlTree.setSelectionToTopSelectNode();
            adqlTree.setBorder(BorderFactory.createEmptyBorder());
            adqlTree.openBranches() ;
            
            final JScrollPane scrTree = new JScrollPane(adqlTree,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);            
            scrTree.addComponentListener ( 
                    new ComponentAdapter() {
                        public void componentResized( ComponentEvent e ) {
                            adqlTree.setAvailableWidth( scrTree.getViewportBorderBounds().width ) ;
                        }
                    } 
            ) ;
            scrTree.setBorder(BorderFactory.createEmptyBorder());
                                         
            //
            // Next, the main text pane where users type in ADQL.
            // Set up the text pane in a scrolling panel etc...
            JScrollPane textScrollContent = new JScrollPane(textPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);            
            textPane.setBorder(BorderFactory.createEmptyBorder());
            textScrollContent.setBorder(BorderFactory.createEmptyBorder());
            //
            // Positioning information...
            this.splitPane.setTopComponent( textScrollContent ) ;  
            this.splitPane.setBottomComponent( scrTree ) ;
            this.splitPane.setDividerLocation(200);
            this.splitPane.setOneTouchExpandable(false); // folk find this
                                                            // irritating.
                                  
            GridBagConstraints gbc = new GridBagConstraints() ;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.gridheight = 7 ;
            gbc.gridwidth = 8 ;
            gbc.gridx = 0 ;
            gbc.gridy = 0 ;   
            gbc.fill = GridBagConstraints.BOTH ;
            this.add( splitPane, gbc ) ;
            
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.gridheight = 1 ;
            gbc.gridwidth = 8 ;
            gbc.gridx = 0 ;
            gbc.gridy = 7 ;        
            gbc.fill = GridBagConstraints.NONE ;
            
            this.setBackground( adqlTree.getBackground() ) ;
            this.add( getValidateEditButton(), gbc) ;  
                      
        }

        protected void executeEditCommand() {
            textPane.executeEditCommand();
        }

    }

    /** Extractef from AdqlMainView - the adql display panel */
    public class AdqlsView extends JTextPane implements TreeSelectionListener,
            FocusListener, TreeModelListener {

        // tree selection listener interface.
        public void valueChanged(TreeSelectionEvent e) {
            displayText();
        }

        // focus listener interface
        public void focusGained(FocusEvent e) {
            editWindowOldImage = getText();
            bEditWindowUpdatedByFocusGained = true;
        }

        public void focusLost(FocusEvent e) {
            if (bEditWindowUpdatedByFocusGained) {
                maintainHistory(editWindowOldImage, getText());
                bEditWindowUpdatedByFocusGained = false;
            }
        }

        // TreeModelListener interface
        public void treeNodesChanged(TreeModelEvent e) {
            displayText();
        }

        public void treeNodesInserted(TreeModelEvent e) {
            displayText();
        }

        public void treeNodesRemoved(TreeModelEvent e) {
            displayText();
        }

        public void treeStructureChanged(TreeModelEvent e) {
            displayText();
        }

        public AdqlsView() {
            this(true);
        }

        /**
         * 
         * @param followPath
         *            if true, this pane should show just the subset of the
         *            query selected in the tree view.
         */
        public AdqlsView(boolean followPath) {
            super();
            this.followPath = followPath;
            if (followPath) {
                // no point listening to tree selections if we still display the
                // same thing.
                adqlTree.addTreeSelectionListener(this);
            }
            adqlTree.getModel().addTreeModelListener(this);
            addFocusListener(this);
//            initKeyProcessing();
            displayText();

            this.originalBorder = getBorder();
            this.warnBorder = BorderFactory.createLineBorder(Color.RED);
        }

        final private Border originalBorder;

        final private Border warnBorder;

        private final boolean followPath;

        private void maintainHistory(String beforeImage, String afterImage) {
            if (afterImage == null)
                return;
            afterImage = afterImage.trim();
            if (afterImage.length() == 0)
                return;
            if (beforeImage != null) {
                beforeImage = beforeImage.trim();
                if (afterImage.equals(beforeImage)) {
                    return;
                }
            }

            if (!historyStack.isEmpty()
                    && afterImage.equals((String) historyStack.peek()))
                return;
            historyStack.push(afterImage);
            historyStack.setCurrentPosition(1);
            setHistoryText(afterImage);
        }

        private void refreshFromModel() {
            setAdqlParameter();
        }

        public void displayText() {
            if (bEditWindowUpdatedByFocusGained) {
                maintainHistory(editWindowOldImage, getText());
                bEditWindowUpdatedByFocusGained = false;
            }

            AdqlNode node;
            if (followPath) {
                node = (AdqlNode) adqlTree.getLastSelectedPathComponent();
            } else {
                node = (AdqlNode) adqlTree.getModel().getRoot();
            }
            // AdqlNode node = adqlTree.getCommandFactory().getEditStore().get(
            // selectedNodeToken ) ;

            try {
                if (node != null) {
                    XmlObject userObject = node.getXmlObject();
                    userObject = AdqlUtils.modifyQuotedIdentifiers(userObject);
                    XmlCursor nodeCursor = userObject.newCursor();
                    String text = nodeCursor.xmlText();
                    nodeCursor.dispose();
                    userObject = AdqlUtils
                            .unModifyQuotedIdentifiers(userObject);
                    setText(transformer.transformToAdqls(text, " "));
                } else {
                    setText("");
                }
            } catch (Exception ex) {
                setText("");
            }

        }

        private void initKeyProcessing() {
            // First we need to find the default action for the Enter key and
            // preserve this.
            // We will use it to invoke the default action after having
            // processed the Enter key.
            KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
            Object key = getInputMap(JComponent.WHEN_FOCUSED).get(keyStroke);
            final Action action = getActionMap().get(key);
            // Set up our own key for the Enter key...
            getInputMap(JComponent.WHEN_FOCUSED).put(keyStroke, "ValidateAdql");
            // adqlText.getInputMap(
            // JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT )
            // .put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ),
            // "ValidateAdqlString" ) ;

            //
            // Now our substitute action. Note how we invoke the default action
            // at the end...
            // (JL Note: not sure whether I should invoke the default process
            // first!!!)
            getActionMap().put("ValidateAdql", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (log.isTraceEnabled())
                        enterTrace("AdqlMainView.VK_ENTER.actionPerformed()");
                    executeEditCommand();
                    action.actionPerformed(e);
                    if (log.isTraceEnabled())
                        exitTrace("AdqlMainView.VK_ENTER.actionPerformed()");
                }
            });
        }

        public void executeEditCommand() {
            if (log.isTraceEnabled()) {
                enterTrace("AdqlMainView.executeEditCommand()");
            }
            try {
                String image = getText();
                if (!followPath) { // set selection to top selection node
                                    // first, as this edit comes from an editor
                                    // containing all the adqls.
                    adqlTree.setSelectionToTopSelectNode();
                }
                AdqlNode node = (AdqlNode) adqlTree
                        .getLastSelectedPathComponent();

                if (node == null) {
                    maintainHistory(null, image);
                    return;
                }

                //
                // There appears still to be an occasional problem of losing
                // visual selection
                // yet having a selectedNodeToken. The situation is accompanied
                // by having no parent node,
                // which will produce a NullPointerException in initializing the
                // command. This is caught
                // and logged when the AbstractCommand is initialized.
                // So, safety first, maintain the edit history so no editing is
                // lost, then return
                // to give the user another try.
                EditCommand editCommand = adqlTree.getCommandFactory()
                        .newEditCommand(adqlTree, node, image);
                if (!editCommand.isInitializedStatusGood()) {
                    maintainHistory(null, image);
                    return;
                }

                CommandExec.Result result = editCommand.execute();
                if (result == CommandExec.FAILED) {
                    setDiagnosticsIcon(false);
                    setBorder(warnBorder);
                    String[] messages = editCommand.getMessages();
                    StringBuffer buffer = new StringBuffer(messages.length * 64);
                    for (int i = 0; i < messages.length; i++) {
                        buffer.append("[" + (i + 1) + "] ").append(messages[i])
                                .append('\n');
                    }
                    diagnostics.setText(buffer.toString());
                } else {
                    setDiagnosticsIcon(true);
                    diagnostics.setText("");
                    setBorder(originalBorder);
                    // Refresh tree...
                    // NB: Cosmetically this requires improvement.
                    // Refreshing using the child entry is cosmetically better,
                    // but does not properly update
                    // the model, leading to exceptions being thrown when a menu
                    // is built (particularly on
                    // a delete action). Using the parent entry gives no errors,
                    // but tends to collapse
                    // branches in the parent. Some method of using the parent
                    // but ensuring branches remain
                    // open is called for.
                    // ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged(
                    // editCommand.getChildEntry() ) ;
                    ((DefaultTreeModel) adqlTree.getModel())
                            .nodeStructureChanged(editCommand.getParentEntry());

                    editCommand.adjustBranches();

                    //
                    // Highlight the edited node...
                    // TreeSelectionModel selectionModel =
                    // (TreeSelectionModel)adqlTree.getSelectionModel() ;
                    // selectionModel.setSelectionPath( new TreePath(
                    // editCommand.getChildEntry().getPath() ) ) ;
                    adqlTree.clearSelection();
                    adqlTree.ensureSomeNodeSelected(editCommand);
                    adqlTree.repaint();
                }
                maintainHistory(null, image);
            } finally {
                if (log.isTraceEnabled()) {
                    exitTrace("AdqlMainView.executeEditCommand()");
                }
            }
        }

    }

    private Point[] elastic;

    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (elastic != null && elastic.length == 2) {
            g.drawLine(elastic[0].x, elastic[0].y, elastic[1].x, elastic[1].y);
        }
    }

    public void updateDisplay() {
        repaint();
    }

    protected Point[] getElastic() {
        return elastic;
    }

    public void setElastic(Point[] elastic) {
        this.elastic = elastic;
    }

    public void unsetElastic() {
        this.elastic = null;
    }

    private XmlObject getRoot() {
        return adqlTree.getRoot();
    }

    private boolean preConditionsForPaste(TreePath path) {
        // If the path is null or there is no parent
        // Then we cannot paste into this entry...
        if (path == null)
            return false;
        if (path.getPathCount() < 2)
            return false;
        // If the clipboard is empty then there is nothing to paste...
        if (clipBoard.size() == 0)
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == adqlTree) {
            if (adqlTree.isCatalogServiceSet()) {
                formatCatalogTab();
            }
        }
    }

    /** called when value of prefernece changes */
    public void propertyChange(PropertyChangeEvent evt) {
        // test which preference has changed (in future, might be listening to
        // more than one prefernece
        if (evt.getSource() == this.showDebugPanePreference) {
            boolean bState = this.showDebugPanePreference.asBoolean();
            if (bState) {
                ADQLEditorPanel.this.adqlXmlView = new AdqlXmlView(
                        tabbedEditorPane);
            } else {
                tabbedEditorPane.removeTabAt(tabbedEditorPane
                        .indexOfComponent(adqlXmlView));
                adqlXmlView = null;
            }
        }
    }

    private void buildPopupEditMenu(JPopupMenu popupMenu, AdqlNode entry) {
        //
        // Safety first...
        if (entry == null) {
            return;
        }
        ActionMap map = this.adqlTree.getActionMap();
        
        if (entry.isBottomLeafEditable()) {
            JMenuItem mix = popupMenu.add(new EditAction(entry));
            mix.setAccelerator( adqlTree.getMicroEditAccelerator() ) ;
        }

        CopyAction copyAction = (CopyAction) map
                .get(UIComponentMenuBar.EditMenuBuilder.COPY);
        copyAction.setEnabled(copyAction.isEnabled());
        JMenuItem mi = (JMenuItem) popupMenu.add(copyAction);
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                UIComponentMenuBar.MENU_KEYMASK));

        CutAction cutAction = (CutAction) map
                .get(UIComponentMenuBar.EditMenuBuilder.CUT);
        cutAction.setEnabled(cutAction.isEnabled()); // redundant?
        mi = (JMenuItem) popupMenu.add(cutAction);
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                UIComponentMenuBar.MENU_KEYMASK));

        PasteOverAction pasteOverAction = (PasteOverAction) map
                .get(UIComponentMenuBar.EditMenuBuilder.PASTE);
        pasteOverAction.setEnabled(pasteOverAction.isEnabled());
        mi = (JMenuItem) popupMenu.add(pasteOverAction);
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                UIComponentMenuBar.MENU_KEYMASK));

        popupMenu.addSeparator();
        popupMenu.add(new PasteIntoAction(entry));
        popupMenu.add(new PasteNextToAction(entry, true)); // Before
        popupMenu.add(new PasteNextToAction(entry, false)); // After

        UndoManager undoManager = adqlTree.getCommandFactory().getUndoManager();
        if (undoManager.canUndoOrRedo()) {
            popupMenu.addSeparator();
            popupMenu.add(new UndoRedoAction());
        }

        List commandArray = adqlTree.getCommandFactory().newInsertCommands(
                entry);
        if (commandArray != null && commandArray.size() > 0) {
            popupMenu.addSeparator();
            popupMenu.add("Insert into " + entry.getDisplayName() + "...");
            ListIterator iterator = commandArray.listIterator();
            while (iterator.hasNext()) {
                StandardInsertCommand command = (StandardInsertCommand) iterator
                        .next();
                if (!command.isChildSupportedType())
                    continue;
                if (command.isChildCascadeable()) {
                    popupMenu.add(getCascadeableMenu(command));
                } else {
                    popupMenu.add(new InsertAction(command
                            .getChildDisplayName(), command));
                }
            }
        }

    }

    private void buildEditContextMenu(JMenu contextMenu, AdqlNode entry) {
        contextMenu.removeAll();
        //
        // Safety first...
        if (entry == null) {
            return;
        }

        if (entry.isBottomLeafEditable()) {
            contextMenu.add(new EditAction(entry));
        }

        contextMenu.add(new PasteIntoAction(entry));
        contextMenu.add(new PasteNextToAction(entry, true)); // Before
        contextMenu.add(new PasteNextToAction(entry, false)); // After

        UndoManager undoManager = adqlTree.getCommandFactory().getUndoManager();
        if (undoManager.canUndoOrRedo()) {
            contextMenu.addSeparator();
            contextMenu.add(new UndoRedoAction());
        }

        List commandArray = adqlTree.getCommandFactory().newInsertCommands(
                entry);
        if (commandArray != null && commandArray.size() > 0) {
            contextMenu.addSeparator();
            contextMenu.add("Insert into " + entry.getDisplayName() + "...");
            ListIterator iterator = commandArray.listIterator();
            while (iterator.hasNext()) {
                StandardInsertCommand command = (StandardInsertCommand) iterator
                        .next();
                if (!command.isChildSupportedType())
                    continue;
                if (command.isChildCascadeable()) {
                    contextMenu.add(getCascadeableMenu(command));
                } else {
                    contextMenu.add(new InsertAction(command
                            .getChildDisplayName(), command));
                }
            }
        }

    }

    private JMenu getCascadeableMenu(StandardInsertCommand command) {
        JMenu menu = new JMenu(command.getChildDisplayName());

        if (command.isChildColumnLinked()) {
            if (adqlTree.getFromTables().isEmpty() == false) {
                menu = getInsertColumnMenu(command);
                menu.setEnabled(command.isChildEnabled());
            } else {
                menu.setEnabled(false);
            }

        } else if (command.isChildTableLinked()) {
            if (adqlTree.isCatalogServiceSet()) {
                menu = getInsertTableMenu(command);
                menu.setEnabled(command.isChildEnabled());
            } else {
                menu.setEnabled(false);
            }
        } else if (command.isChildDrivenByEnumeratedAttribute()) {
            menu = getEnumeratedMenus(command);
            menu.setEnabled(command.isChildEnabled());
        } else if (command.isChildDrivenByEnumeratedElement()) {
            menu = getEnumeratedElementMenus(command);
            menu.setEnabled(command.isChildEnabled());
        }
        return menu;
    }

    private void enterTrace(String entry) {
        log.trace(getLogIndent().toString() + "enter: " + entry);
        indentPlus();
    }

    private void exitTrace(String entry) {
        indentMinus();
        log.trace(getLogIndent().toString() + "exit : " + entry);
    }

    private void indentPlus() {
        logIndent.append(' ');
    }

    private void indentMinus() {
        logIndent.deleteCharAt(logIndent.length() - 1);
    }

    private StringBuffer getLogIndent() {
        if (logIndent == null) {
            logIndent = new StringBuffer();
        }
        return logIndent;
    }

    private class TableDataComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
            AdqlTree.TableData t0 = (AdqlTree.TableData) arg0;
            AdqlTree.TableData t1 = (AdqlTree.TableData) arg1;
            return t0.getTable().getName().compareTo(t1.getTable().getName());
        }

    }

    private class ColumnBeanComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
            ColumnBean c0 = (ColumnBean) arg0;
            ColumnBean c1 = (ColumnBean) arg1;
            return c0.getName().compareTo(c1.getName());
        }

    }

} // end of ADQLToolEditor
