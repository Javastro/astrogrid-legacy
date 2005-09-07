/*$Id: RegistryChooserPanel.java,v 1.3 2005/09/07 11:34:10 KevinBenson Exp $
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

import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.desktop.modules.ui.UIComponent;

import java.awt.LayoutManager;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.DefaultCellEditor;
import javax.swing.JEditorPane;
import javax.swing.BoxLayout;
import javax.swing.ListSelectionModel;


import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import java.net.URI;
import java.net.URL;
import java.util.Hashtable;

import org.w3c.dom.Document;


import org.astrogrid.desktop.modules.system.transformers.Xml2XhtmlTransformer;
import org.apache.axis.utils.XMLUtils;
import javax.xml.transform.Transformer;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.StringWriter;



/**
 * Implementation of the registry-google chooser.
 *@todo later add bookmark component. - won't affect public inteface, just implementation
 */
public class RegistryChooserPanel extends JPanel implements ActionListener {



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
    /** boolean flag to indicate multiple resources can be selected */
    protected boolean multiple;
    
    private JTextField keywordField = null;
    private JButton goButton = null;
    JButton lookEveryWhere = new JButton("Search");
    
    private JCheckBox anyKeywords = new JCheckBox("Any");
    
    
    /** assemble the ui 
     * @todo implement */
    private void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(getTopPanel());
        add(getCenterPanel());
        add(getBottomPanel());
    }
    
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(new JLabel("Keywords: "), null);
        topPanel.add(getKeywordField(), null);
        topPanel.add(getGoButton(), null);
        return topPanel;
    }
    
    private DefaultTableModel selectTableModel = null;
    private Object []selectColumns = {"Select", "Identifier", "Title",  "URL", "Description"};
    
    JCheckBox selectedColumn = new JCheckBox();
    JTable selectTable = null;
    Hashtable resInfoHash = new Hashtable();
    
    protected ResourceInformation makeResourceInformationFromTableRow(int row) {
        URL url = null;
        try {
            if(selectTableModel.getValueAt(row,3) != null && selectTableModel.getValueAt(row,3).toString().trim().length() > 0) {
                url = new URL(selectTableModel.getValueAt(row,3).toString());
            }
            return new ResourceInformation(new URI(selectTableModel.getValueAt(row,1).toString()),
                                           selectTableModel.getValueAt(row,2).toString(),
                                           selectTableModel.getValueAt(row,4).toString(), 
                                           url);
        }catch(Exception e) {
            //need to do something this is for URISyntaxException and MalformedURLException
            e.printStackTrace();
        }
        return null;
    }
    
    private void printResInfo(ResourceInformation []ri) {
        for(int i = 0;i < resInfo.length;i++) {
            System.out.println(ri[i].toString());
        }
    }
    
    private void printHash(Hashtable ht) {
        Object []test = resInfoHash.values().toArray();
        for(int i = 0;i < test.length;i++) {
            System.out.println("hash print i = " + i + " val = " + test[i]);
        }
        
    }
    
    private void makeResourceInformationFromHash() {        
        Object []vals = resInfoHash.values().toArray();
        resInfo = null;
        resInfo = new ResourceInformation[vals.length];
        for(int i = 0;i < vals.length;i++) {
            resInfo[i] = (ResourceInformation)vals[i];            
        }
    }
    
    

    
    private JPanel getCenterPanel() {         
         JPanel centerPanel = new JPanel();
         centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
         
         //set all the tables to 0 rows we want them to be added later after grabbing things from the 
         //registry and selecting things from the "From" table.
         selectTableModel = new DefaultTableModel(selectColumns,0);
         selectTable = new JTable(selectTableModel);
         selectTable.setPreferredScrollableViewportSize(new Dimension(300, 70));
         JScrollPane selectScrollPane = new JScrollPane(selectTable);
         setupCheckColumn(selectTable, selectTable.getColumnModel().getColumn(0),selectedColumn);
         selectTableModel.addTableModelListener(new TableModelListener() {
             public void tableChanged(TableModelEvent e) {             
                 int editRow = selectTable.getEditingRow();
                 ResourceInformation temp = null;
                 if(e.getColumn() == 0) {
                     System.out.println("Col 0 editrow = " + editRow + " val = " + selectTableModel.getValueAt(editRow,0).toString());
                     if(selectTableModel.getValueAt(editRow,0).toString().equals("true")) {
                         temp = makeResourceInformationFromTableRow(editRow);
                         System.out.println("Made RI for row = " + editRow + " the toString = " + temp.toString());
                         resInfoHash.put(String.valueOf(editRow),temp);
                         printHash(resInfoHash);
                         //resInfo = (ResourceInformation [])resInfoHash.values().toArray();
                         //The above did not work need to work out something else.
                         makeResourceInformationFromHash();
                         //printResInfo(resInfo);
                     }else if(selectTableModel.getValueAt(editRow,0).toString().equals("false")) {
                         if(resInfoHash.containsKey(String.valueOf(editRow))) {
                             resInfoHash.remove(String.valueOf(editRow));
                             if(resInfoHash.size() > 0) {                                 
                                 //resInfo = (ResourceInformation [])resInfoHash.values().toArray();
                                 //The above did not work need to work out something else.
                                 makeResourceInformationFromHash();
                             } else {
                                 resInfo = null;
                             }
                             //printResInfo(resInfo);
                         }//if
                     }//else if
                 }//if
             }
           }
         );
         
         ListSelectionModel rowSM = selectTable.getSelectionModel();
         rowSM.addListSelectionListener(new ListSelectionListener() {
             public void valueChanged(ListSelectionEvent e) {
                 ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                 if (lsm.isSelectionEmpty()) {
                     //System.out.println("No rows are selected.");
                     //System.out.println("the value at col 0 and row 0 = " + fromTableModel.getValueAt(0,0));
                     if(selectTableModel.getRowCount() > 0) {
                         editorPane.setText("<html><body>No Rows Selected</body></html>");
                     }
                 } else {
                     int selectedRow = lsm.getMinSelectionIndex();
                     try {
                         Document doc = reg.getRecord(new URI(selectTableModel.getValueAt(selectedRow,1).toString()));
                         String htmlString = (String)transform(doc);
                         editorPane.setText(htmlString);
                     }catch(Exception ex) {
                         //need to do something.
                         ex.printStackTrace();
                         editorPane.setText("<html />");
                     }
                 }
             }
         });         
         centerPanel.add(new JLabel("Results: "));
         centerPanel.add(selectScrollPane);         
         return centerPanel;
    }
    
    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        try {
            Source styleSource = Xml2XhtmlTransformer.getStyleSource();
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

    
    JEditorPane editorPane = new JEditorPane("text/html","<html><body></body></html>");
    private JPanel getBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        editorPane.setEditable(false);
        
        //Put the editor pane in a scroll pane.
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(250, 145));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));
        bottomPanel.add(editorScrollPane);
        return bottomPanel;
    }
    
    /**
     * Method: setupCheckColumn
     * Description: Sets up a check box renderer inside a JTable.  This really might not be needed
     * anymore because I think you can just do Boolean objects and it will put it as checkboxes anyway.
     * @param table The jtable to be used with a check box.
     * @param tableColumn  The actual column inside the jtable.
     * @param checkBox the check box itself.
     */   
    public void setupCheckColumn(JTable table,
            TableColumn tableColumn, JCheckBox checkBox ) {
            //Set up the editor for the sport cells.
        
        tableColumn.setCellEditor(new DefaultCellEditor(checkBox));
        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        //renderer.setToolTipText("Click for combo box");
        tableColumn.setCellRenderer(renderer);
    }
    
    
    /*
     * Xml2XhtmlTransformer htmlXSL = new Xml2XhtmlTransformer(Xml2XhtmlTransformer.getStyleSource());
     * String htmlString = (String)htmlXSL.transform(reg.getRecord(selectTableModel.getValueAt(editRow,1)));
     * editorPane.setText(htmlString);
     * 
     */
    
    
    
    /**
     * This method initializes jTextField   
     *  
     * @return javax.swing.JTextField   
     */    
    private JTextField getKeywordField() {
        if (keywordField == null) {
            keywordField = new JTextField();
            keywordField.setToolTipText("<html>Enter the key of a resource to view it <br>- e.g <tt>ivo://uk.ac.le.star/filemanager</tt></html>");
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
            goButton = new JButton("Go");
            //goButton.setIcon(IconHelper.loadIcon("update.gif"));
            goButton.setToolTipText("Retrieve this resource");
            goButton.addActionListener(this);
        }
        return goButton;
    }
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        // either search, or cancel..
            final String keywords = keywordField.getText();
            Object source = e.getSource();
            String sql = "Select * from Registry where ";
            //String joinSQL = " and ";
            //if(anyKeywords.isSelected()) 
            String joinSQL = " or ";
            if(source == goButton) {
                clear();
                sql += "vr:title like '" + keywords + "'" + joinSQL + 
                       "vr:description like '" + keywords + "'" + joinSQL +
                       "vr:identifier like '" + keywords + "'" + joinSQL +
                       "vr:shortName like '" + keywords + "'" + joinSQL +                            
                       "vr:subject like '" + keywords + "'";
            }else if(source == lookEveryWhere) {
                clear();
                sql += "* like '" + keywords + "'";
            }
            try {
                ResourceInformation []ri = reg.adqlSearchRI(sql);
                if(ri.length > 0) {
                    fillTableData(ri);
                }
            }catch(org.astrogrid.acr.NotFoundException nfe) {
                //need to do something for now everything should be clear in the table
                //possibly report to them that nothing was found on some kind of quick message box.
                nfe.printStackTrace();
            }catch(org.astrogrid.acr.ServiceException se) {
                //need to do something for now everything should be clear in the table
                //possibly report to them that nothing was found on some kind of quick message box.
                se.printStackTrace();                
            }
    }
    
    private void fillTableData(ResourceInformation []ri) {
        Object []rowData = new Object[selectColumns.length];
        for(int i = 0;i < ri.length;i++) {
            rowData[0] = new Boolean(false);
            rowData[1] = ri[i].getId();
            rowData[2] = ri[i].getName();
            rowData[3] = ri[i].getAccessURL();
            rowData[4] = ri[i].getDescription();
            selectTableModel.addRow(rowData);
        }
    }
    
    
    /** set an additional result filter
     * @todo implemnt
     * @param filter an adql-like where clause, null indicates 'no filter'
     */
   public void setFilter(String filter) {
       
   }
   
   /** set whether user is permitted to select multiple resources 
    * @param multiple if true, multiple selection is permitted.*/
   public void setMultipleResources(boolean multiple) {
       //@todo add event handlers, gui logic to alter display to enable / disable multiple selection.
       this.multiple = multiple;
   }
   
   public boolean isMultipleResources() {
       return multiple;
   }
   
   /** clear display, set selectedResources == null 
    * @todo implement
    *
    */
   public void clear() {
       resInfoHash.clear();
       resInfo = null;       
       while(selectTableModel.getRowCount() > 0) {
           selectTableModel.removeRow(0);
       }
   }
   
   private ResourceInformation[] resInfo;

   /** access the resources selected by the user
    * @todo implement
    * @return
    */
   public ResourceInformation[] getSelectedResources() {
       return resInfo;
   }
}


/* 
$Log: RegistryChooserPanel.java,v $
Revision 1.3  2005/09/07 11:34:10  KevinBenson
changes to reg chooser to use jtable and jeditorpane.

Revision 1.1  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs
 
*/