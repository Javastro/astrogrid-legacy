/*$Id: RegistryChooserPanel.java,v 1.33 2006/06/27 10:28:47 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.registry;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.StringWriter;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.transformers.Xml2XhtmlTransformer;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.RegistryBrowserImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.w3c.dom.Document;

/**
 * Implementation of the registry-google chooser.
 *@todo later add bookmark component. - won't affect public inteface, just implementation
 */
public class RegistryChooserPanel extends JPanel implements ActionListener {
    
    private static final int TOOLTIP_WRAP_LENGTH = 50;
    private static final int CELL_WRAP_LENGTH = 100; 
    private static final int WRAP_LENGTH = 70;
    
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RegistryChooserPanel.class);
      
    /** model that maintains resource information objects - rather than deconstructing them and then rebuilding them afterwards
     * 
     * @author Noel Winstanley nw@jb.man.ac.uk 07-Sep-2005
     *
     */
    private class ResourceInformationTableModel extends AbstractTableModel {
        public ResourceInformationTableModel() {
            listModel = new DefaultListModel() ;
            selectionModel = new DefaultListSelectionModel();
            selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            selectionModel.addListSelectionListener(new ListSelectionListener() { // notify the table model when selection changes.
                public void valueChanged(ListSelectionEvent e) {
                    for (int i = e.getFirstIndex(); i < e.getLastIndex() + 1 && i < getRows().length; i++) {
                            fireTableCellUpdated(i,0);                            
                            ResourceInformation changed = getRows()[i];
                            if (selectionModel.isSelectedIndex(i) && !listModel.contains(changed)) {
                                listModel.addElement(changed);
                            } else if (! selectionModel.isSelectedIndex(i) && listModel.contains(changed)) {
                                listModel.removeElement(changed);
                            }
                        }
                    }                                    
            });
        }
        private final ListSelectionModel selectionModel;
        private final DefaultListModel listModel;
        private int COLUMN_COUNT = 2;
        private ResourceInformation[] ri = new ResourceInformation[]{};
        /** get a model of the currently selected rowss */
        public ListSelectionModel getSelectionModel() {
            return selectionModel;
        }
        
        public ListModel getListModel() {
            return listModel;
        }
        /** set the resouce infroatmion to display */
        public void setRows(ResourceInformation[] ri) {
            this.ri = ri;
            fireTableDataChanged();         
        }
        // makes a checkbox appear in col 1 if parent is application launcher or workflow builder, not registry browser
        public Class getColumnClass(int columnIndex) {
            if (columnIndex == 0 && 
            	!(parent.getClass().equals(RegistryBrowserImpl.class))) {
                return Boolean.class;
            } else {
            return super.getColumnClass(columnIndex);
            }            
        }
        /** get the resource information */
        public ResourceInformation[] getRows() {
            return ri;
        }
        /** clear the resource information */
        public void clear() {
            ri = new ResourceInformation[]{};
            selectionModel.clearSelection();
            listModel.clear();
            detailsPane.setText("<html><body><font size='-1'>No entry selected</font></body></html>");
            xmlPane.setText("No entry selected");
            tabPane.setSelectedIndex(0);
            keywordField.setText("");
            tree.setModel(null);            
            fireTableDataChanged();
        }

        public int getRowCount() {
            return ri.length;
        }

        public int getColumnCount() {
            return COLUMN_COUNT;
        }
        // make the first cell editable
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return (columnIndex == 0 && !(parent.getClass().equals(RegistryBrowserImpl.class)));
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return  Boolean.valueOf(selectionModel.isSelectedIndex(rowIndex));
                case 1:
                    
                    return  ri[rowIndex] == null ? "" : ri[rowIndex].getName();
                default:
                       return "";
            }
        }
        
        public void setValueAt(Object aValue,int rowIndex,int columnIndex) {
            switch(columnIndex) {
                case 0:
                    if ( ! selectionModel.isSelectedIndex(rowIndex)) {
                        selectionModel.addSelectionInterval(rowIndex,rowIndex);
                    } else {
                        selectionModel.removeSelectionInterval(rowIndex,rowIndex);
                    }
                default:
                    return;
            }
        }
        
        public String getColumnName(int column) {
            switch(column) {
                case 0: return "Select";
                case 1: return "Title";
                default: return "";
            }
        }
    } // end resource information table model.


    /** Construct a new RegistryChooserPanel
     * 
     */
    public RegistryChooserPanel(UIComponent parent,Registry reg) {
        super();    
        this.parent = parent;
        this.reg = reg;
        initialize();
   }
    
    /** parent ui window - used to display progress of background threads, etc 
     * 
     * pass this reference to any {@link org.astrogrid.desktop.modules.ui.BackgroundWorker} objects created
     * */
    protected final UIComponent parent;
    /** registry component - use for queries */
    protected final Registry reg;
   
    private JTextField keywordField = null;
    private JButton goButton = null;
    private JTabbedPane tabPane;
    private JEditorPane detailsPane = new JEditorPane("text/html","<html><body><font size='-1'>No entry selected</font></body></html>");
    private JTextArea xmlPane = new JTextArea();
    private ResourceInformationTableModel selectTableModel= new ResourceInformationTableModel();
    private JTable selectTable = null;
    private String filter = null;
    private JSplitPane split = null;
    private JLabel noResultsLabel = new JLabel("No Results Found");
    private JCheckBox exhaustiveCheck = new JCheckBox("Full-text Search");
    private JTree tree = null;
    
    /** assemble the ui */
    private void initialize() {    	
        this.setSize(new Dimension(300,500));
        
        setLayout(new BorderLayout());
        add(getTopPanel(),BorderLayout.NORTH);
        
        tabPane = new JTabbedPane();        
        tabPane.addTab("Details", null, new JScrollPane(detailsPane), "Details of chosen entry");        
        tabPane.addTab("Tree View", IconHelper.loadIcon("tree.gif"), getTreePanel(), "Display results in tree form");
        tabPane.addTab("XML entry", IconHelper.loadIcon("document.gif"), getBottomPanel(), "View the XML as entered in the registry");       
        
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,getCenterPanel(),tabPane);
        split.setPreferredSize(new Dimension(300,200));
        split.setSize(new Dimension(300,200));
        split.setDividerSize(5);
        split.setDividerLocation(70);       
        add(split,BorderLayout.CENTER);
    }
    
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(new JLabel("Find: "), null);
        topPanel.add(getKeywordField(), null);
        topPanel.add(getGoButton(), null);
        exhaustiveCheck.setToolTipText("Search all text in the registry resources");
        topPanel.add(exhaustiveCheck,null);
        return topPanel;
    }
    
    private JPanel getCenterPanel() {         
         JPanel centerPanel = new JPanel();
         centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
         
         //set all the tables to 0 rows we want them to be added later after grabbing things from the 
         //registry and selecting things from the "From" table.
         selectTable = new JTable(selectTableModel) { //Implement table cell tool tips.
             public String getToolTipText(MouseEvent e) {
                 String tip = null;
                 java.awt.Point p = e.getPoint();
                 int rowIndex = rowAtPoint(p);
                 int colIndex = columnAtPoint(p);
                 int realColumnIndex = convertColumnIndexToModel(colIndex);               
                 if (realColumnIndex == 1) { //Namecolumn..
                     ResourceInformation ri = selectTableModel.getRows()[rowIndex];
                    StringBuffer result = new StringBuffer();
                     result.append("<html>");
                     result.append("<i>");
                     result.append(ri.getId());
                     result.append("</i><p>");
                     result.append(ri.getDescription()!= null ?   WordUtils.wrap(ri.getDescription(),TOOLTIP_WRAP_LENGTH,"<br>",false) : "");
                     result.append("</p></html>");                                             
                     tip= result.toString(); 
                 } else { 
                     tip = super.getToolTipText(e);
                 }
                 return tip;
             }             
         };
         
         selectTable.getColumnModel().getColumn(0).setPreferredWidth(8);
         selectTable.getColumnModel().getColumn(0).setMaxWidth(10);
         selectTable.setToolTipText("Results");
         
         selectTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
             public void valueChanged(ListSelectionEvent e) {
                 ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                 if (lsm.isSelectionEmpty()) {
                     if(selectTableModel.getRowCount() > 0) {
                         xmlPane.setText("");
                         displayingDocumentForRow = -1;
                     }
                 } else if (selectTable.getSelectedColumn() <= 1 && lsm.getMinSelectionIndex() != displayingDocumentForRow) {                                          
                     displayingDocumentForRow = lsm.getMinSelectionIndex();
                     (new BackgroundWorker(parent,"Rendering Record") {

                        protected Object construct() throws Exception {
                        	// do all document processing on background thead - otherwise ui blocks.
                            Document doc = reg.getRecord(selectTableModel.getRows()[displayingDocumentForRow].getId());
                        	detailsPane.setEditorKit(new XHTMLEditorKit());
                        	detailsPane.setText((String)transform(doc));
                            xmlPane.setText(XMLUtils.DocumentToString(doc));        
                            try {                            
                            	RegistryTree xmltree = new RegistryTree(doc);
                            	tree.setModel(xmltree.getModel());                            	
                            } catch (Exception e) {
                            	logger.error("Problem creating registry browser tree: " + e.getMessage());
                            	tree.setModel(null);
                            }                            
                            return null;
                        }
                        protected void doFinished(Object o) {
                        	detailsPane.setCaretPosition(0);
                            xmlPane.setCaretPosition(0); 
                        }
                     }).start();                     
                 }
             }
         });
         centerPanel.add(new JScrollPane(selectTable));
         centerPanel.setPreferredSize(new Dimension(300,200));
         return centerPanel;
    }
    
    // tracks selected row of the table;
    protected int displayingDocumentForRow = -1;
    
    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        try {
            Source styleSource = getRegistryStyleSource();
            Transformer transformer = TransformerFactory.newInstance().newTransformer(styleSource);
            Source source = new DOMSource((Document)arg0);
            StringWriter sw = new StringWriter();
            Result sink = new StreamResult(sw);        
            transformer.transform(source, sink);
            return sw.toString();
        } catch (TransformerException e) {
            e.printStackTrace();
         //   logger.error("TransformerException",e);
            return XMLUtils.DocumentToString((Document)arg0);
        }
    }
    
    private JComponent getBottomPanel() {
        xmlPane.setEditable(false);
        xmlPane.setText("No entry selected");
        //Put the editor pane in a scroll pane.
        JScrollPane editorScrollPane = new JScrollPane(xmlPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        editorScrollPane.setPreferredSize(new Dimension(300,200));
        return editorScrollPane;
    }

	private JPanel getTreePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(getTree());
		panel.add(scrollPane);
		panel.setPreferredSize(new Dimension(300,200));
		return panel;
	}
	
    /** build / access the tree */
    private JTree getTree() {
        if (tree == null) {
        	tree = new JTree();
        	tree.setModel(null);
        	tree.setCellRenderer(new RegistryTreeRenderer());
        }
        return tree;
    }
    
    /**
     * This method initializes jTextField   
     *  
     * @return javax.swing.JTextField   
     */    
    private JTextField getKeywordField() {
        if (keywordField == null) {
            keywordField = new JTextField();
            keywordField.setToolTipText("<html>Enter keywords to search for,<br> logical connectors (AND, OR),<br> or the key of a resource - e.g <tt>ivo://org.astrogrid/Galaxev</tt></html>");            
            //keywordField.addActionListener(this);
        }
        return keywordField;
    }
    /**
     * This method initializes jButton  
     *  
     * @return javax.swing.JButton  
     */    
    private JButton getGoButton() {
        if (goButton == null) {
            goButton = new JButton("Search");
            goButton.setMnemonic(KeyEvent.VK_S);
            goButton.setToolTipText("Retrieve matching resources from the registry");
            goButton.addActionListener(this);
            //parent.setDefaultButton(goButton);
            KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
            this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(enter,"search");
            this.getActionMap().put("search",new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    RegistryChooserPanel.this.actionPerformed(e);
                }
            });
        }
        return goButton;
    }
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        final String keywords = keywordField.getText();

        (new BackgroundWorker(parent,"Searching") {
            protected Object construct() throws Exception {        
                    if(exhaustiveCheck.isSelected()) {
                        return exhaustiveQuery(keywords); // don't want to try again..
                    }else {
                        ResourceInformation[] ri = query(keywords);                    
                        if(ri.length == 0) { // try harder...   
                            SwingUtilities.invokeLater(new Runnable() { // put ui-updating code back on the ui thread..
                                public void run() {
                                    exhaustiveCheck.setSelected(true);
                                    parent.setStatusMessage("Quick search gave no results - trying exhaustive search");
                                }
                            });
                            ri = exhaustiveQuery(keywords);
                        }
                        return ri;
                    }
            }
            protected void doFinished(Object result) {
                clear();                
                ResourceInformation[] ri = (ResourceInformation[])result;
                selectTableModel.setRows(ri);
                
                // if only 1 matching entry might as well render straight away
                // also select row for consistency
                if (ri.length == 1) {
                    (new BackgroundWorker(parent,"Found 1 record, rendering") {
                    protected Object construct() throws Exception {
                       Document doc = reg.getRecord(selectTableModel.getRows()[0].getId());
                       return doc;
                    }
                    protected void doFinished(Object o) {
                    detailsPane.setEditorKit(new XHTMLEditorKit());
                    detailsPane.setText((String)transform((Document)o));
                    detailsPane.setCaretPosition(0);
                    xmlPane.setText(XMLUtils.DocumentToString((Document)o));
                    xmlPane.setCaretPosition(0);
                    SwingUtilities.invokeLater(new Runnable() {
                    	public void run(){
                    		selectTable.requestFocusInWindow();
                    		selectTable.changeSelection(0,1,false,false);
                    	}
                    });
                    }
                }).start();
                }               
            }
            
            protected void doAlways() {
            	if (selectTableModel.getRowCount() == 1) {
            		parent.setStatusMessage("Found 1 matching resource, rendering...");
            	} else {
            		parent.setStatusMessage("Found " + selectTableModel.getRowCount() + " matching resources");
            	}                
            }

       }).start();
    }
    
   private ResourceInformation[] query(String keywords)  throws NotFoundException, ServiceException {
       String sql = "Select * from Registry where "; 
       boolean shallFilter = filter != null && filter.trim().length() > 0;
       String joinSQL = null;
       String []keyword = StringUtils.split(keywords); // NWW - more robust than String.split() - which takes a regexp 
       if (keyword.length > 0 && shallFilter) {
           sql += "("; //NWW  - added paren here - makes it easier to glue on filter if needed. or can just query on filter if keywords == ""
       }
       for(int j = 0;j < keyword.length;j++) { // NWW - this doesn't handle ( ), and operator precedence at the moment. needs a little syntax tree here eventually.
           //joinSQL = null;
           if(j != (keyword.length - 1) && keyword[(j+1)].trim().toLowerCase().equals("and")) {
               joinSQL = " and ";               
           }else if(j != (keyword.length - 1) && keyword[(j+1)].trim().toLowerCase().equals("or")) {
               joinSQL = " or ";
           }            
           if(!keyword[j].trim().toLowerCase().equals("and") && 
              !keyword[j].trim().toLowerCase().equals("or")) {
               if(joinSQL == null) joinSQL = " or ";
               
                    sql += "(vr:title like '" + keyword[j] + "'" + " or " +
                    "vr:content/vr:description like '" + keyword[j] + "'" + " or " +
                    "vr:identifier like '" + keyword[j] + "'" + " or " +
                    "vr:shortName like '" + keyword[j] + "'" + " or " +
                    "vr:content/vr:subject like '" + keyword[j] + "')";
                    if(j != (keyword.length - 1)) {
                        sql += joinSQL;
                    }//if
                    joinSQL = null;
           }
       }//for
       if (keyword.length > 0 && shallFilter) {
           sql +=") and ";
       }
    if (shallFilter) {
           sql += " (" + filter + ")";
       } 
       return reg.adqlSearchRI(sql);
   }
   
   // NWW - decided not to add filter to exhaustive query - so that is always brings back everything.
   private ResourceInformation[] exhaustiveQuery(String keywords)  throws NotFoundException, ServiceException {
       StringBuffer sql = new StringBuffer("Select * from Registry where ");
       String joinSQL = " or ";
       boolean shallFilter = filter != null && filter.trim().length() > 0;
       String []keyword = StringUtils.split(keywords);
       if (keyword.length > 0 && shallFilter) {
           sql.append( "("); //NWW  - added paren here - makes it easier to glue on filter if needed. or can just query on filter if keywords == ""
       }
       
       for(int j = 0;j < keyword.length;j++) {
           if(j != (keyword.length - 1) && keyword[(j+1)].trim().toLowerCase().equals("and")) {
               joinSQL = " and ";               
           }else if(j != (keyword.length - 1) && keyword[(j+1)].trim().toLowerCase().equals("or")) {
               joinSQL = " or ";
           }            
           if(!keyword[j].trim().toLowerCase().equals("and") && 
              !keyword[j].trim().toLowerCase().equals("or")) {
               if(joinSQL == null) joinSQL = " or ";
               sql.append("(* like '").append(keyword[j]).append("')");
               if(j != (keyword.length - 1)) {
                   sql.append(" ").append(joinSQL).append( " ");
               }//if
               joinSQL = null;
           }
       }//for
       if (keyword.length > 0 && shallFilter) {
           sql.append(") and ");
       }
    if (shallFilter) {
           sql.append(" (").append( filter ).append(")");
       }
       return reg.adqlSearchRI(sql.toString());
   }
                    
    /** set an additional result filter
     * @param filter an adql-like where clause, null indicates 'no filter'
     */
   public void setFilter(String filter) {
       this.filter = filter;       
   }

   
   /** expose this as a public method - so then interested clients can register listeners on the selection model */
   public ListModel getSelectedResourcesModel() {
       return selectTableModel.getListModel();
   }
   
   /** set whether user is permitted to select multiple resources 
    * @param multiple if true, multiple selection is permitted.*/
   public void setMultipleResources(boolean multiple) {
       selectTableModel.getSelectionModel().setSelectionMode(multiple ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);     
   }

   
   public boolean isMultipleResources() {
       return selectTableModel.getSelectionModel().getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
   }
   
   /** clear display, set selectedResources == null 
    *
    */
   public void clear() { 
       selectTableModel.clear();    
       displayingDocumentForRow = -1;
   }
   

   /** access the resources selected by the user
    * @return
    */
   public ResourceInformation[] getSelectedResources() {
       ListModel m = getSelectedResourcesModel();
       ResourceInformation[] results = new ResourceInformation[m.getSize()];
       for (int i = 0; i < results.length; i++) {
           results[i] = (ResourceInformation)m.getElementAt(i);
       }
       return results;                
   } 
     
 private Source getRegistryStyleSource() {
    return new StreamSource(Xml2XhtmlTransformer.class.getResourceAsStream("registryResults.xsl"));
}
   
    /**
     * Simple cell renderer used to wrap cell contents to 
     * sensible length (WRAP_LENGTH)
     */
    private static class RegistryTreeRenderer extends DefaultTreeCellRenderer {
		public Component getTreeCellRendererComponent(JTree tree, 
	            Object value,
				  boolean isSelected,
				  boolean expanded, 
				  boolean leaf, 
				  int row, 
				  boolean hasFocus) {
			tree.setRowHeight(0);
			JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, 
                        											  value, 
																	  isSelected, 
																	  expanded, 
																	  leaf, 
																	  row, 
																	  hasFocus);
			if (value.toString().length() > CELL_WRAP_LENGTH) {
				StringBuffer sb = new StringBuffer();
				sb.append("<html>");
				sb.append(value.toString() != null ?   WordUtils.wrap(value.toString(),WRAP_LENGTH,"<br>",false) : "");
				sb.append("</html>");
				label.setText(sb.toString());
			} else {
				label.setText("" + value);
			}			
			return(label);
		}
	}
}


/* 
$Log: RegistryChooserPanel.java,v $
Revision 1.33  2006/06/27 10:28:47  nw
findbugs tweaks

Revision 1.32  2006/05/17 15:45:17  nw
factored common base class out of astroscope and helioscope.improved error-handline on astroscope input.

Revision 1.31  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.30  2006/03/14 15:46:21  pjn3
Scrollpane replaced

Revision 1.29  2006/03/14 15:06:40  pjn3
reduced WRAP_LENGTH slightly

Revision 1.28  2006/03/14 13:58:46  pjn3
Added basic cell renderer to wrap cell text e.g. descriptions

Revision 1.27  2006/03/10 17:33:19  pjn3
set tree model to null when no entry available to prevent default 'color' tree appearing

Revision 1.26  2006/03/09 14:48:33  pjn3
Initial work to add JTree to registry browser

Revision 1.25  2006/03/06 17:05:50  pjn3
Correctly clear selection

Revision 1.24  2006/02/24 12:41:43  KevinBenson
added the filter on exhaustiveQuery

Revision 1.23  2006/02/24 11:06:46  KevinBenson
the joins were not working right for and's/or's

Revision 1.22  2006/01/23 14:38:52  KevinBenson
reverting back to 1.20 version

Revision 1.20  2005/12/16 12:30:01  pjn3
*** empty log message ***

Revision 1.19  2005/12/16 12:03:12  pjn3
render immediately if only 1 match

Revision 1.18  2005/12/16 10:35:00  pjn3
*** empty log message ***

Revision 1.17  2005/12/15 15:19:07  pjn3
corrected when checkbox appear

Revision 1.16  2005/12/08 13:01:12  pjn3
Merge of pjn_wprkbench_1_12_05

Revision 1.15.4.2  2005/12/08 11:35:14  pjn3
Prevent checkbox appearing when not req'd

Revision 1.15.4.1  2005/12/08 10:37:46  pjn3
Moved xml to seperate tab to improve display

Revision 1.15  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.14  2005/11/22 18:58:19  pjn3
added XHTMLEditorKit to improve how xhtml is displayed in JEditorPane

Revision 1.13  2005/11/17 13:34:59  pjn3
Query sql string alterred to include vr:content as per Kevin's email

Revision 1.12  2005/11/11 15:25:08  pjn3
caret added

Revision 1.11.24.1  2005/11/11 15:13:23  pjn3
caret added

Revision 1.11  2005/09/09 10:19:39  nw
implemented filtering

Revision 1.10  2005/09/09 09:59:14  nw
fixed status bar messages.

Revision 1.9  2005/09/09 08:45:40  KevinBenson
added "and"/"or" ability to be placed in the text field so it will be ignored and be used for the join operations between keywords

Revision 1.8  2005/09/09 08:09:51  KevinBenson
small changes on the query side to put them in methods to better do exhuastivequeries

Revision 1.7  2005/09/08 13:53:30  KevinBenson
small change to wordwrap text

Revision 1.6  2005/09/08 11:14:08  nw
adjusted visible model object.

Revision 1.5  2005/09/08 10:45:28  KevinBenson
small fixes on the sizing and arrangement of components

Revision 1.4  2005/09/07 14:27:24  nw
added background workers.
implemented a custom table model to maintain state.

Revision 1.3  2005/09/07 11:34:10  KevinBenson
changes to reg chooser to use jtable and jeditorpane.

Revision 1.1  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs
 
*/