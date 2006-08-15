/*$Id: RegistryGooglePanel.java,v 1.1 2006/08/15 10:14:14 nw Exp $
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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.xml.stream.XMLStreamReader;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.astrogrid.desktop.modules.ivoa.StreamingExternalRegistryImpl.DocumentBuilderStreamProcessor;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceFormatter;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceStreamParser;
import org.astrogrid.desktop.modules.system.CacheFactory;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.ExternalViewerHyperlinkListener;
import org.astrogrid.desktop.modules.ui.RegistryBrowserImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.w3c.dom.Document;

/**
 * Implementation of the registry-google chooser.
 * @todo implement using standard xquery
 * @todo optimize query - no //vor:resouc
 */
public class RegistryGooglePanel extends JPanel implements ActionListener {
    
    /** worker class that does the search.
	 * @author Noel Winstanley
	 * @since Aug 15, 20062:00:41 AM
	 */
	private final class SearchWorker extends BackgroundWorker implements StreamProcessor{
		/**
		 * 
		 */
		private final AbstractQuery q;
	    private final Builder briefXQueryBuilder = new SummaryXQueryVisitor();
	    private final Builder fullTextXQueryBuilder = new FullTextXQueryVisitor();	
		/**
		 * @param parent
		 * @param msg
		 * @param q
		 */
		private SearchWorker(UIComponent parent, String msg, AbstractQuery q) {
			super(parent, msg);
			this.q = q;	
		}

		public void process(XMLStreamReader reader) throws Exception {
			ResourceStreamParser p = new ResourceStreamParser(reader);
			while (p.hasNext()) {
				final Resource r = (Resource)p.next();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						selectTableModel.addResource(r);
					    parent.setStatusMessage("Found " + selectTableModel.getRowCount() + " resources");             
					}
				});
			}	
		}
		protected Object construct() throws Exception {        
			if (! exhaustiveCheck.isSelected()) {
				// produce a query from the search parse tree.
				String briefXQuery = briefXQueryBuilder.build(this.q,filter);
				// check bulk cache.
				Element el = bulk.get(briefXQuery);
				if (el != null) {
					return el.getValue();
				}
				reg.xquerySearchStream(briefXQuery,this);
				if (selectTableModel.getRowCount() > 0) {
					cacheResult(briefXQuery);
					return null; // halt the search here.
				}

				SwingUtilities.invokeLater(new Runnable() { // put ui-updating code back on the ui thread..
					public void run() {
						exhaustiveCheck.setSelected(true);
						parent.setStatusMessage("Quick search gave no results - trying exhaustive search");
					}
				});

			} // end brief search.

			// do an exhaustive search
			// produce a query from the search parse tree.
			String fullXQuery = fullTextXQueryBuilder.build(this.q,filter);
			// check bulk cache.
			Element el = bulk.get(fullXQuery);
			if (el != null) {
				return el.getValue();
			}
			reg.xquerySearchStream(fullXQuery,this);
			if (selectTableModel.getRowCount() > 0) {
				cacheResult(fullXQuery);
			}

			return null;
		}

		protected void doFinished(Object result) {
			if (result != null) {// cached results passed in here for processing.
				Resource[] arr =  (Resource[]) result;
				for (int i = 0; i< arr.length; i++) {// bit inefficient - but will do for now.
					selectTableModel.addResource(arr[i]);
				}
			} 
		}

		private void cacheResult( final String key) {
			List resultList = selectTableModel.getRows();					
			Resource[] arr = (Resource[])resultList.toArray(new Resource[resultList.size()]);
			Element el = new Element(key,arr);
			bulk.put(el);
			for (int i = 0; i < arr.length; i++) {
				if (resources.get(arr[i].getId()) == null) {
					resources.put(new Element(arr[i].getId(),arr));
				}
			}							
		}

		protected void doAlways() {     
		    // display first result.
		    if (selectTableModel.getRowCount() > 0) {
		    	// rest of this should be done automatically.
		    //	detailsPane.setText(ResourceFormatter.renderResourceAsHTML(selectTableModel.getRow(0)));
		   // 	detailsPane.setCaretPosition(0);
		    	selectTable.changeSelection(0,1,false,false);
		    }               
		    parent.setStatusMessage("Found " + selectTableModel.getRowCount() + " resources");             
		    selectTable.requestFocusInWindow();
		}
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
	/** listens to changes to gui, and fetches and displays XML documents as needed.
     * Also displays the formatted record.
	 * @author Noel Winstanley
	 * @since Aug 5, 20062:57:44 AM
	 */
	private final class XMLDisplayer implements ListSelectionListener, ChangeListener {
		protected int currentRow = -1;
		protected int currentTab = 0;

		// tracks change in tabs.
		public void stateChanged(ChangeEvent e) {
			
			if (tabPane.getSelectedIndex() != 0 && currentTab == 0) { // only catch transitions between 0 and others
				retrieveAndDisplayXML(currentRow);
			}
			currentTab = tabPane.getSelectedIndex();
		}
		/** tracks currently selected row.
		 * if different from currend, display
		 */
		public void valueChanged(ListSelectionEvent e) {
		     ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		     if (lsm.isSelectionEmpty()) {
		         if(selectTableModel.getRowCount() > 0) {
		             xmlPane.setText("");
		             currentRow = -1;
		         }
		     } else if (selectTable.getSelectedColumn() <= 1 && lsm.getMinSelectionIndex() != currentRow) {                                          
		         currentRow = lsm.getMinSelectionIndex();
		    	detailsPane.setText(ResourceFormatter.renderResourceAsHTML(selectTableModel.getRow(currentRow)));
		    	detailsPane.setCaretPosition(0);
		    	if (tabPane.getSelectedIndex() > 0) { // i.e any but the first tab
		    		retrieveAndDisplayXML( currentRow);                     
		    	}
		     }
		 }

		void retrieveAndDisplayXML( final int row) {
		(new BackgroundWorker(parent,"Fetching Record") {
		
		    protected Object construct() throws Exception {
		    	Document doc = reg.getResourceXML(selectTableModel.getRow(row).getId());
		        xmlPane.setText(XMLUtils.DocumentToString(doc));        
		        try {                            
		        	RegistryTree xmltree = new RegistryTree(doc);
		        	tree.setModel(xmltree.getModel());                            	
		        } catch (Exception e) {
		        	RegistryGooglePanel.logger.error("Problem creating registry browser tree: " + e.getMessage());
		        	tree.setModel(null);
		        }                            
		        return null;
		    }
		    protected void doFinished(Object o) {
		        xmlPane.setCaretPosition(0); 
		    }
		 }).start();
		}
	}
    /**
     * @author Noel Winstanley nw@jb.man.ac.uk 07-Sep-2005
     *
     */
    public class ResourceTableModel extends AbstractTableModel {
        private int COLUMN_COUNT = 2;
        private final DefaultListModel listModel;
        private List ri = new ArrayList();
        private final ListSelectionModel selectionModel;
        public ResourceTableModel() {
            listModel = new DefaultListModel() ;
            selectionModel = new DefaultListSelectionModel();
            selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            selectionModel.addListSelectionListener(new ListSelectionListener() { // notify the table model when selection changes.
                public void valueChanged(ListSelectionEvent e) {
                    for (int i = e.getFirstIndex(); i < e.getLastIndex() + 1 && i < getRowCount(); i++) {
                            fireTableCellUpdated(i,0);                            
                            Resource changed = getRow(i);
                            if (selectionModel.isSelectedIndex(i) && !listModel.contains(changed)) {
                                listModel.addElement(changed);
                            } else if (! selectionModel.isSelectedIndex(i) && listModel.contains(changed)) {
                                listModel.removeElement(changed);
                            }
                        }
                    }                                    
            });
        }
        /** set the resouce infroatmion to display */

        public void addResource(Resource r) {
        	ri.add(r);
        	fireTableRowsInserted(ri.size()-1,ri.size()-1);
        }
        /** clear the resource information */
        public void clear() {
            ri.clear();
            selectionModel.clearSelection();
            listModel.clear();
            detailsPane.setText("<html><body></body></html>");
            xmlPane.setText("No entry selected");
            tabPane.setSelectedIndex(0);
          //  keywordField.setText("");
            tree.setModel(null);            
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
        public int getColumnCount() {
            return COLUMN_COUNT;
        }
        public String getColumnName(int column) {
            switch(column) {
                case 0: return "Select";
                case 1: return "Title";
                default: return "";
            }
        }
        /** models the list of selected objects*/
        public ListModel getListModel() {
            return listModel;
        }
        public Resource getRow(int i) {
        	return (Resource)ri.get(i);
        }

        public int getRowCount() {
            return ri.size();
        }

        /** get the resource information */
        public List getRows() {
            return ri;
        }
        /** get a model of the currently selected rowss */
        public ListSelectionModel getSelectionModel() {
            return selectionModel;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return  Boolean.valueOf(selectionModel.isSelectedIndex(rowIndex));
                case 1:
                    
                    return  ri.get(rowIndex) == null ? "" : ((Resource)ri.get(rowIndex)).getTitle();//short name?
                default:
                       return "";
            }
        }
        
        // make the first cell editable
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return (columnIndex == 0 && !(parent.getClass().equals(RegistryBrowserImpl.class)));
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
    } // end resource information table model. 
    private static final int CELL_WRAP_LENGTH = 100;
    
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RegistryGooglePanel.class);
      


    private static final int WRAP_LENGTH = 70;
    
    private JEditorPane detailsPane ;
    private JCheckBox exhaustiveCheck = new JCheckBox("Full-text Search");
   
    private String filter = null;
    private JButton goButton = null;
    private JTextField keywordField = null;
    private JTable selectTable = null;
    private ResourceTableModel selectTableModel= new ResourceTableModel();
    private JSplitPane split = null;
    private JTabbedPane tabPane;
    private JTree tree = null;
    private final XMLDisplayer xmlDisplayer = new XMLDisplayer();
    private JTextArea xmlPane = new JTextArea();
    

    
    public ResourceTableModel getResourceTableModel() {
    	return selectTableModel;
    }
    
    /** parent ui window - used to display progress of background threads, etc 
     * 
     * pass this reference to any {@link org.astrogrid.desktop.modules.ui.BackgroundWorker} objects created
     * */
    protected final UIComponent parent;

	/** registry component - use for queries */
    protected final RegistryInternal reg;
    protected final BrowserControl browser;
    protected final RegistryBrowser regBrowser;
    protected final Ehcache resources ;
    protected final Ehcache bulk;
    /** Construct a new RegistryChooserPanel
     * 
     */
    public RegistryGooglePanel(UIComponent parent,RegistryInternal reg, BrowserControl browser, RegistryBrowser regBrowser, CacheFactory fac) {
        super();    
        this.parent = parent;
        this.reg = reg;
        this.browser = browser;
        this.regBrowser = regBrowser;
        this.resources = fac.getManager().getCache(CacheFactory.RESOURCES_CACHE);
        this.bulk = fac.getManager().getCache(CacheFactory.BULK_CACHE);

        
        initialize();
   }
    
    public void doSearch(String s) {
    	getKeywordField().setText(s);
    	getGoButton().doClick();
    }
    
    public void doOpen(final URI ivorn) {
		(new BackgroundWorker(parent,"Opening " + ivorn) {
			protected Object construct() throws Exception {
				return  reg.getResource(ivorn);
			}
			protected void doFinished(Object result) {
				final ResourceTableModel model = getResourceTableModel();
				model.addResource((Resource)result);
            	selectTable.changeSelection(0,1,false,false);			
			}
		}).start();    	
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        final String searchTerm = keywordField.getText();
        selectTableModel.clear();
        QueryParser qp = new QueryParser(searchTerm);
        final AbstractQuery q = qp.parse();
        (new SearchWorker(parent, "Searching", q)).start();
    }
    
    /** access the resources selected by the user
	    * @return
	    */
	   public Resource[] getSelectedResources() {
	       ListModel m = getSelectedResourcesModel();
	       Resource[] results = new Resource[m.getSize()];
	       for (int i = 0; i < results.length; i++) {
	           results[i] = (Resource)m.getElementAt(i);
	       }
	       return results;                
	   }
    
    // tracks selected row of the table;
    

    /** expose this as a public method - so then interested clients can register listeners on the selection model */
	   public ListModel getSelectedResourcesModel() {
	       return selectTableModel.getListModel();
	   }

	public boolean isMultipleResources() {
	       return selectTableModel.getSelectionModel().getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
	   }
	
    /** set an additional result filter
     * @param filter an adql-like where clause, null indicates 'no filter'
     */
   public void setFilter(String filter) {
       this.filter = filter;       
   }
    
    /** set whether user is permitted to select multiple resources 
	    * @param multiple if true, multiple selection is permitted.*/
	   public void setMultipleResources(boolean multiple) {
	       selectTableModel.getSelectionModel().setSelectionMode(multiple ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);     
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
                     Resource ri = selectTableModel.getRow(rowIndex);
                    StringBuffer result = new StringBuffer();
                     result.append("<html>");
                     result.append("<b>").append(ri.getTitle()).append("</b><br>");
                     result.append("<i>");
                     result.append(ri.getId());
                     result.append("</i></html>");                                          
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
         
         selectTable.getSelectionModel().addListSelectionListener(xmlDisplayer);
         centerPanel.add(new JScrollPane(selectTable));
         centerPanel.setPreferredSize(new Dimension(300,200));
         return centerPanel;
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
        this.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter,"search");
        this.getActionMap().put("search",new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                RegistryGooglePanel.this.actionPerformed(e);
            }
        });
    }
    return goButton;
}
                    
    /**
     * This method initializes jTextField   
     *  
     * @return javax.swing.JTextField   
     */    
    private JTextField getKeywordField() {
        if (keywordField == null) {
            keywordField = new JTextField();
            keywordField.setToolTipText("<html>Enter keywords to search for,<br>a phrase in quotes<br> logical operators - AND, OR, NOT,(,),<br> or the key of a resource - e.g <tt>ivo://org.astrogrid/Galaxev</tt></html>");            
        }
        return keywordField;
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

   
   /** assemble the ui */
private void initialize() {    	
    this.setSize(new Dimension(300,500));
    
    setLayout(new BorderLayout());
	JPanel topPanel = new JPanel();
	topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
	topPanel.add(new JLabel("Find: "), null);
	topPanel.add(getKeywordField(), null);
	topPanel.add(getGoButton(), null);
	exhaustiveCheck.setToolTipText("Search all text in the registry resources");
	topPanel.add(exhaustiveCheck,null);
	exhaustiveCheck.setEnabled(true); 
	add(topPanel,BorderLayout.NORTH);
    
    tabPane = new JTabbedPane();    
    detailsPane= new JEditorPane();
    detailsPane.setContentType("text/html");
    detailsPane.setEditable(false);
    detailsPane.setText("<html><body></body></html>");
    detailsPane.addHyperlinkListener(new ExternalViewerHyperlinkListener(browser, regBrowser));
    tabPane.addTab("Details", null, new JScrollPane(detailsPane), "Details of chosen entry");
	JPanel treePanel = new JPanel(new BorderLayout());
	final JTree tree2 = getTree();
	JScrollPane scrollPane = new JScrollPane(tree2);
	treePanel.add(scrollPane);
	treePanel.setPreferredSize(new Dimension(300,200));    
	tabPane.addTab("Tree View", IconHelper.loadIcon("tree.gif"), treePanel, "Display results in tree form");
    tabPane.addTab("XML entry", IconHelper.loadIcon("document.gif"), getBottomPanel(), "View the XML as entered in the registry");       
    tabPane.addChangeListener(xmlDisplayer);
    split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,getCenterPanel(),tabPane);
    split.setPreferredSize(new Dimension(300,200));
    split.setSize(new Dimension(300,200));
    split.setDividerSize(5);
    split.setDividerLocation(100);       
    add(split,BorderLayout.CENTER);
} 


public void clear() {
	selectTableModel.clear();
}

}

/* 
$Log: RegistryGooglePanel.java,v $
Revision 1.1  2006/08/15 10:14:14  nw
supporting classes for new registry google UI

Revision 1.35  2006/07/18 13:12:08  KevinBenson
placed the action map to WHEN_ANCESTOR_OF_FOCUSED_COMPONENT so the go button is searched when a user hits return/enter

Revision 1.34  2006/06/27 19:12:49  nw
adjusted todo tags.

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