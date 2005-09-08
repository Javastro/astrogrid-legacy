/*$Id: RegistryChooserPanel.java,v 1.5 2005/09/08 10:45:28 KevinBenson Exp $
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
import org.astrogrid.desktop.modules.system.transformers.Xml2XhtmlTransformer;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



/**
 * Implementation of the registry-google chooser.
 *@todo later add bookmark component. - won't affect public inteface, just implementation
 *@todo problems : nww - can't work out how to get split pane, etc to fill whole screen.
 *                                 - need to find out how to make tooltips split across lines - do I need to paginate the text by hand?
 */
public class RegistryChooserPanel extends JPanel implements ActionListener {
    /** model that maintains resource information objects - rather than deconstructing them and then rebuilding them afterwards
     *
     * @author Noel Winstanley nw@jb.man.ac.uk 07-Sep-2005
     *
     */
    private class ResourceInformationTableModel extends AbstractTableModel {
        public ResourceInformationTableModel() {
            selected = new DefaultListSelectionModel();
            selected.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            selected.addListSelectionListener(new ListSelectionListener() { // notify the table model when selection changes.
                public void valueChanged(ListSelectionEvent e) {
                    for (int i = e.getFirstIndex(); i < e.getLastIndex() + 1; i++) {
                        fireTableCellUpdated(i,0);
                    }
                }
            });
        }
        private final ListSelectionModel selected;
        private int COLUMN_COUNT = 2;
        private ResourceInformation[] ri = new ResourceInformation[]{};
        /** get a model of the currently selected rowss */
        public ListSelectionModel getSelectionModel() {
            return selected;
        }
        /** set the resouce infroatmion to display */
        public void setRows(ResourceInformation[] ri) {
            this.ri = ri;
            fireTableDataChanged();
        }
        // makes a checkbox appear in col 1
        public Class getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
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
            selected.clearSelection();
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
            return columnIndex == 0;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return  Boolean.valueOf(selected.isSelectedIndex(rowIndex));
                case 1:
                    return  ri[rowIndex].getName();
                default:
                       return "";
            }
        }

        public void setValueAt(Object aValue,int rowIndex,int columnIndex) {
            switch(columnIndex) {
                case 0:
                    if ( ! selected.isSelectedIndex(rowIndex)) {
                        selected.addSelectionInterval(rowIndex,rowIndex);
                    } else {
                        selected.removeSelectionInterval(rowIndex,rowIndex);
                    }
                default:
                    return;
            }
        }

        public String getColumnName(int column) {
            switch(column) {
                case 0: return "Selected";
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
    JButton lookEveryWhere = new JButton("Search");

    private JCheckBox anyKeywords = new JCheckBox("Any");


    /** assemble the ui */
    private void initialize() {
        this.setSize(new Dimension(300,500));
        
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout());
        add(getTopPanel(),BorderLayout.NORTH);
        
        //JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        //split.setPreferredSize(new Dimension(300,200));
        //split.setDividerSize(5);
        //split.setDividerLocation(70);
        //split.add(new JScrollPane(getCenterPanel()));
        //split.add(getBottomPanel());

        //JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,new JScrollPane(getCenterPanel()),getBottomPanel());
        JScrollPane jp = new JScrollPane(getCenterPanel());
        
        JComponent bottomComp = getBottomPanel();
        jp.setPreferredSize(new Dimension(300,200));
        //jp.setSize(new Dimension(300,200));
        bottomComp.setPreferredSize(new Dimension(300,200));
        //bottomComp.setSize(new Dimension(300,200));
        
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,jp,bottomComp);
        split.setPreferredSize(new Dimension(300,200));
        split.setSize(new Dimension(300,200));
        //split.setPreferredSize(new Dimension(300,200));
        split.setDividerSize(5);
        split.setDividerLocation(70);
        
        //Dimension parentDim = parent.getSize();
        //split.setPreferredSize(new Dimension(parentDim.width,200));
        add(split,BorderLayout.CENTER);
        //split.setVisible(false);
        add(noResultsLabel,BorderLayout.SOUTH);
        noResultsLabel.setVisible(false);
    }
    JSplitPane split = null;
    JLabel noResultsLabel = new JLabel("No Results Found");

    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(new JLabel("Keywords: "), null);
        topPanel.add(getKeywordField(), null);
        //topPanel.add(anyKeywords,null);
        topPanel.add(getGoButton(), null);        
        return topPanel;
    }


    ResourceInformationTableModel selectTableModel= new ResourceInformationTableModel();
    JTable selectTable = null;


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
                     result.append(ri.getDescription()!= null ?   ri.getDescription(): "");
                     result.append("</p></html>");
                     tip= result.toString();
                 } else {
                     tip = super.getToolTipText(e);
                 }
                 return tip;
             }
         };

         selectTable.getColumnModel().getColumn(0).setPreferredWidth(8);
         selectTable.getColumnModel().getColumn(0).setMaxWidth(8);
         //selectTable.setPreferredScrollableViewportSize(new Dimension(300, 70));



         selectTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
             public void valueChanged(ListSelectionEvent e) {
                 ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                 if (lsm.isSelectionEmpty()) {
                     //System.out.println("No rows are selected.");
                     //System.out.println("the value at col 0 and row 0 = " + fromTableModel.getValueAt(0,0));
                     if(selectTableModel.getRowCount() > 0) {
                         editorPane.setText("<html><body></body></html>");
                     }
                 } else if (selectTable.getSelectedColumn() ==1) {
                     final int selectedRow = lsm.getMinSelectionIndex();
                     (new BackgroundWorker(parent,"Rendering Record") {

                        protected Object construct() throws Exception {
                            Document doc = reg.getRecord(selectTableModel.getRows()[selectedRow].getId());
                            return (String)transform(doc);
                        }
                        protected void doFinished(Object o) {
                            editorPane.setText(o.toString());
                        }
                     }).start();

                 }
             }
         });
         centerPanel.add(new JLabel("Results: "));
         centerPanel.add(selectTable);
         centerPanel.add(lookEveryWhere);
         lookEveryWhere.setToolTipText("Search all text in a Resource");
         lookEveryWhere.addActionListener(this);
         //centerPanel.setPreferredSize(new Dimension(parentDim.width,200));
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
    private JComponent getBottomPanel() {
        editorPane.setEditable(false);

        //Put the editor pane in a scroll pane.
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //editorScrollPane.setPreferredSize(new Dimension(300, 145));
        Dimension parentDim = parent.getSize();
        //editorScrollPane.setPreferredSize(new Dimension(parentDim.width,200));
        //editorScrollPane.setMinimumSize(new Dimension(100, 100));
        return editorScrollPane;
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
        final String keywords = keywordField.getText();
        final Object source = e.getSource();
        (new BackgroundWorker(parent,"Searching") {
            protected Object construct() throws Exception {
                String sql = "Select * from Registry where ";
                //String joinSQL = " and ";
                String joinSQL = " or ";
                if(anyKeywords.isSelected()) {
                    joinSQL = " or ";
                }
                String []keyword = keywords.split(" ");
                
                if(source == goButton) {

                for(int j = 0;j < keyword.length;j++) {
                    sql += "(vr:title like '" + keyword[j] + "'" + " or " +
                    "vr:description like '" + keyword[j] + "'" + " or " +
                    "vr:identifier like '" + keyword[j] + "'" + " or " +
                    "vr:shortName like '" + keyword[j] + "'" + " or " +
                    "vr:subject like '" + keyword[j] + "')";
                    if(j != (keyword.length - 1)) {
                        sql += " " + joinSQL + " ";
                    }//if
                  }//for
                }else if(source == lookEveryWhere) {
                for(int j = 0;j < keyword.length;j++) {
                    sql += "(* like '" + keyword[j] + "')";
                    if(j != (keyword.length - 1)) {
                        sql += " " + joinSQL + " ";
                    }//if
                  }//for
                }
                ResourceInformation []ri = reg.adqlSearchRI(sql);
                if(ri.length > 0) {
                    split.setVisible(true);
                    //split.setPreferredSize(new Dimension(300,200));
                    //split.setSize(new Dimension(300,200));
                    //noResultsLabel.setVisible(false);
                    
                }else {
                    //split.setVisible(false);
                    //noResultsLabel.setVisible(true);
                    
                }
                return ri;
            }
            protected void doFinished(Object result) {
                clear();
                ResourceInformation[] ri = (ResourceInformation[])result;
                selectTableModel.setRows(ri);
            }

       }).start();
    }



    /** set an additional result filter
     * @todo implemnt
     * @param filter an adql-like where clause, null indicates 'no filter'
     */
   public void setFilter(String filter) {

   }


   /** expose this as a public method - so then interested clients can register listeners on the selection model */
   public ListSelectionModel getSelectionModel() {
       return selectTableModel.getSelectionModel();
   }

   /** set whether user is permitted to select multiple resources
    * @param multiple if true, multiple selection is permitted.*/
   public void setMultipleResources(boolean multiple) {
       getSelectionModel().setSelectionMode(multiple ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);
   }


   public boolean isMultipleResources() {
       return getSelectionModel().getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
   }

   /** clear display, set selectedResources == null
    * @todo implement
    *
    */
   public void clear() {
       selectTableModel.clear();
   }


   /** access the resources selected by the user
    * @return
    */
   public ResourceInformation[] getSelectedResources() {
       ListSelectionModel m = getSelectionModel();
       List results = new ArrayList();
       ResourceInformation[] rows = selectTableModel.getRows();
       if  (m.getMinSelectionIndex() > -1) {
           for (int i = m.getMinSelectionIndex(); i < m.getMaxSelectionIndex() + 1; i++) {
               if (m.isSelectedIndex(i)) {
                   results.add(rows[i]);
               }
           }
       }
       return (ResourceInformation[])results.toArray(new ResourceInformation[]{});


   }
}


/*
$Log: RegistryChooserPanel.java,v $
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