/*$Id: DatacenterToolEditorPanel.java,v 1.5 2006/04/18 23:25:47 nw Exp $
 * Created on 08-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.editors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jedit.JEditTextArea;
import jedit.SyntaxDocument;
import jedit.TSQLTokenMarker;
import jedit.TextAreaDefaults;

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.astrogrid.TabularDatabaseInformation;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.ivoa.Adql074;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.workflow.beans.v1.Tool;

/** custom tool editors for dsa - gives prominence to the query
 * <p>
 * query is constructed in a text box with text coloring. A right-click context menu provides help on sql syntax and column metadata.
 * <p>
 * Query is kept as adql/s (that is, sql). and stored in the tool document as such. It is translated to adql/x (xml) as late as possible - when the tool document is submitted to 
 * a cea server by the workbench or JES. This class should do no translation of ADQL (apart from using a translator to check for validity). 
 * 
 * 
 * @todo get insert menus to appear like eclipse 'auto-complete' - incremental search and popup-documentation 
 * @todo add a display of table metadata somewhere - but make it optional (i.e. can disable it in constructor)
 *  - maybe make it a tear off menu?
 * @todo detect that this editor is not applicable for indirect query parameters.
 * @todo later it'd be nice to add more structure to the editor - i.e. make it harder to enter an syntactically incorret sql statement. 
 * @todo mouseover documentation for table and column names.
 * @todo scrollinig menus.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2005
 *
 */
public class DatacenterToolEditorPanel extends BasicToolEditorPanel {
    /** subclass of the standard model that will display all parameters that aren't queries */
    protected class FilteringParameterTableModel extends ParameterTableModel{

        public FilteringParameterTableModel(boolean isInput) {
            super(isInput); 
        }
        
        public void toolSet(ToolEditEvent te) {
            if (isInput) {
                setRows(filter(toolModel.getTool().getInput().getParameter()));
            } else {
                setRows(filter(toolModel.getTool().getOutput().getParameter()));
            }
        }

        /**
         * @param parameter
         * @return
         */
        protected ParameterValue[] filter(ParameterValue[] parameter) {
            String interfaceName =toolModel.getTool().getInterface();
            ApplicationInformation info = toolModel.getInfo();
            List filterList = Arrays.asList(  listADQLParameters(interfaceName,info));
            List p= new ArrayList(Arrays.asList(parameter));
            for (Iterator i = p.iterator(); i.hasNext(); ) {
                if (filterList.contains( ((ParameterValue)i.next()).getName())) {
                    i.remove();
                }
            }
            return (ParameterValue[])p.toArray(new ParameterValue[]{});
        }
       
    }
    /** custom text area, that listens to edits
     * @todo configure the text area more - get rid of empty-line marks, improve code coloring.
     * @todo enable word-wrap. 
     * @todo control horizontal scrollbar;
     * @author Noel Winstanley nw@jb.man.ac.uk 09-Sep-2005
     *
     */
    protected class QueryArea extends JEditTextArea implements ToolEditListener {
                        
        
        protected ParameterValue queryParam = null;

        public QueryArea() {
            super(myDefaults);
            this.setDocument(new SyntaxDocument()); // necessary to prevent aliasing between jeditors.
            this.setTokenMarker(new TSQLTokenMarker());      
            this.setCaretPosition(0);
            
            this.setPreferredSize(new Dimension(200,100));            
            this.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    queryParam.setValue(getText());
                    toolModel.fireParameterChanged(DatacenterToolEditorPanel.this,queryParam);                    
                }

                public void insertUpdate(DocumentEvent e) {
                    queryParam.setValue(getText());
                    toolModel.fireParameterChanged(DatacenterToolEditorPanel.this,queryParam);
                }

                public void removeUpdate(DocumentEvent e) {
                    queryParam.setValue(getText());
                    toolModel.fireParameterChanged(DatacenterToolEditorPanel.this,queryParam);                    
                }
            });          
        }

        public void parameterAdded(ToolEditEvent te) {
            // can't apply to this - assume query is mandatory
        }

        public void parameterChanged(ToolEditEvent te) {
            if (te.getSource() != DatacenterToolEditorPanel.this // only listen if change has come from elsewhere..
                && te.getChangedParameter() == queryParam){ 
                setText(queryParam.getValue());
            }
        }

        public void parameterRemoved(ToolEditEvent te) {
            // can't apply - assume query is mandatory.
        }

        public void toolChanged(ToolEditEvent te) {
            toolSet(te);
        }


        public void toolCleared(ToolEditEvent te) {
            queryParam = null;
            setText("");
            setEnabled(false);
        }
        public void toolSet(ToolEditEvent te) {
            String[] toks = listADQLParameters(toolModel.getTool().getInterface(),toolModel.getInfo());
            if (toks.length > 0) {
                setEnabled(true);
                queryParam = (ParameterValue)toolModel.getTool().findXPathValue("input/parameter[name='" + toks[0] +"']");
                setText(queryParam.getValue());
            }
        }
    }
    /** default settings for the query area */
    static TextAreaDefaults myDefaults =TextAreaDefaults.getDefaults();
    static {
        myDefaults.eolMarkers = false;
        myDefaults.bracketHighlightColor = Color.RED;
        myDefaults.cols = 30;
        myDefaults.rows = 10;
        myDefaults.caretBlinks=true;
        myDefaults.caretVisible=true;
    }    

    /** returns true if this app has an adql parameter */
    public static String[] listADQLParameters(String interfaceName,ApplicationInformation info) {
        InterfaceBean ib = null;
        List results = new ArrayList();
        for (int i = 0; i < info.getInterfaces().length; i++) {        
            if (info.getInterfaces()[i].getName().equals(interfaceName)) {
                ib = info.getInterfaces()[i];
            }            
        }
        if (ib == null) {
            return new String[]{};
        }
        for (int i =0; i < ib.getInputs().length; i++) {
            ParameterReferenceBean prb = ib.getInputs()[i];
            ParameterBean pb = (ParameterBean)info.getParameters().get(prb.getRef());
            if (pb ==null) {
                return new String[]{};
            }
            if (pb.getType() != null && pb.getType().equalsIgnoreCase("adql")) {
                results.add(pb.getName());
            }
        }
        return (String[])results.toArray(new String[]{});
        
    }
    protected final UIComponent parent;
    protected final RegistryChooser regChooser;
    protected final Adql074 validator;
    
    private JButton chooseResourceButton;
    private JMenu columnMenu;
    
    
    private JPopupMenu popupMenu;
    
    private QueryArea queryArea;
    private JMenu sqlMenu;
    
    private JButton validateButton;

    /** Construct a new DatacenterToolEditorPanel
     * @param toolModel
     */
    public DatacenterToolEditorPanel(ToolModel toolModel, ResourceChooserInternal resourceChooser,RegistryChooser regChooser,Adql074 adql, UIComponent parent) {
        super(toolModel,resourceChooser);
        this.validator = adql;
        this.parent = parent;
        this.regChooser = regChooser;
    }

    /** applicable when it's a dsa-style tool - ie. has an ADQL parameter*/
    public boolean isApplicable(Tool t, ApplicationInformation info) {
        
        return t != null && info != null && listADQLParameters(t.getInterface(),info).length > 0;
    }
    /** 
     *Button that lets the user select the table description associated with this dsa cea app. 
     * 
     * @author Noel Winstanley nw@jb.man.ac.uk 12-Sep-2005
     *
     @todo stopgap until registry entries for cea data apps contain reference to table description - then we can determine this automatically */ 
    protected JButton getChooseResourceButton() {
        if (chooseResourceButton == null) {
            chooseResourceButton = new JButton("Set Catalog Definition..");
            chooseResourceButton.addActionListener(new ActionListener() {               
                public void actionPerformed(ActionEvent e) {
                   TabularDatabaseInformation catalogueResource = (TabularDatabaseInformation)regChooser.chooseResourceWithFilter("Select Catalogue description for " + toolModel.getInfo().getName(),
                           "(@xsi:type like 'TabularDB')");
                   if (catalogueResource != null) {
                       updateMenu(catalogueResource);
                   }
                }
            });
        }
        return chooseResourceButton;
    }
    
  
    /** overridden from parent class - makes the input table filter out the adql parameter */
    protected ParameterTableModel getInputTableModel() {
        if (inputTableModel == null) {
            return new FilteringParameterTableModel(true);
        } 
        return inputTableModel;
    }
    /** get the text area for the query */
    protected QueryArea getQueryArea() {
        if (queryArea == null) {
            queryArea = new QueryArea();
            toolModel.addToolEditListener(queryArea);
            queryArea.setRightClickPopup(getPopupMenu());
        }
        return queryArea;
    }
    /** @todo - improve validation to check for correct table references, as well as just correct syntax
     * @todo at the moment seems to throw an uncatchable exception. which is a bit odd.
     *  */
    protected JButton getValidateButton() {
        if (validateButton == null) {
            validateButton = new JButton("Validate ADQL");
            validateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String adqls = queryArea.getText();
                    // validate sql string by trying to translate to xml - hopefully this will throw on error
                    try {
                    validator.s2x(adqls); // don't care about result.
                    parent.setStatusMessage("<html><font color='green'>Sql is valid</font></html>");
                    } catch (Throwable ex) {
                        parent.setStatusMessage("<html><font color='red'>INVALID - " + ex.getCause().getMessage() +"</font></html>");
                    }
                }
            });
        }
        return validateButton;
    }
    

    protected void initialize() {
        
        add(getQueryArea());
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        buttonBox.add(getChooseResourceButton());
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(getValidateButton());
        add(buttonBox);
        super.initialize();
    }
    /** @todo add table -> column hirarchy of submenus, populated from table description
     * in registry.
     * @return
     */
    private JMenu getInsertColumnMenu() {
        if (columnMenu == null) {
            columnMenu = new JMenu("Insert Column..");
            columnMenu.add("Select a Catalog Definition First"); //@todo make this a link to the same action as the 'select catalog' button.
        }
        return columnMenu;
    }
    /** @todo add the other parts  of sql syntax to the menu */
    private JMenu getInsertSQLMenu() {
        if (sqlMenu == null) {
            sqlMenu = new JMenu("Insert SQL..");
            sqlMenu.add(new AbstractAction("Select") {
                public void actionPerformed(ActionEvent e) {
                    queryArea.setSelectedText("Select ");
                }
            });
        }
        return sqlMenu;
    }
    private JPopupMenu getPopupMenu() {
        if (popupMenu == null) {
            popupMenu = new JPopupMenu();            
            popupMenu.add(new AbstractAction("Cut") {
                public void actionPerformed(ActionEvent e) {
                    queryArea.cut();
                }
            });
            popupMenu.add(new AbstractAction("Copy") {
                public void actionPerformed(ActionEvent e) {
                    queryArea.copy();
                }
            });
            popupMenu.add(new AbstractAction("Paste") {
                public void actionPerformed(ActionEvent e) {
                    queryArea.paste();
                }
            });            
            popupMenu.add(new AbstractAction("Select All") {
                public void actionPerformed(ActionEvent e) {
                    queryArea.selectAll();
                }
            });
            popupMenu.addSeparator();
            popupMenu.add(getInsertSQLMenu());
            popupMenu.add(getInsertColumnMenu());
            
        }
        return popupMenu;
    }
    
    private static final String names="abcdefghijklmnopqrstuvwxyz";
    
    /** rebuild columnMenu based on the info from registry
     * @todo present this info elsewhere too - drag-n-droppable list on Left hand side?
     * @todo handle aliasing properly. 
     * @todo make more efficient by using single action object to process all requests.
     * @todo does the database name need to be prefixed to the table name? in what syntax.
     * */
    private void updateMenu(TabularDatabaseInformation catalog) {
        JMenu insertMenu = getInsertColumnMenu();
        insertMenu.removeAll();
        DatabaseBean[] dbs = catalog.getDatabases();
        for (int i = 0; i < dbs.length; i++) {
            JMenu dbMenu= new JMenu(dbs[i].getName(),true);
            dbMenu.setToolTipText(dbs[i].getDescription());
            insertMenu.add(dbMenu);
            final TableBean[] tables = dbs[i].getTables();
            for (int j = 0; j < tables.length; j++) {
                JMenu tableMenu = new JMenu(tables[j].getName(),true);
                dbMenu.add(tableMenu);
                tableMenu.setToolTipText(tables[j].getDescription());
                final String alias = names.substring(j,j+1);
                final int jBar = j; // ugh.
                tableMenu.add(new AbstractAction(tables[j].getName()) {
                    public void actionPerformed(ActionEvent e) {
                        queryArea.setSelectedText(" " + tables[jBar].getName() + " as " + alias + " ");
                    }
                });                
                tableMenu.addSeparator();
                final ColumnBean[] cols = tables[j].getColumns();
                for (int k = 0; k < cols.length; k++) {
                    final int kBar = k; // yechh.
                    tableMenu.add(new AbstractAction(cols[k].getName()) {
                        {
                            this.putValue(AbstractAction.SHORT_DESCRIPTION,mkTooltip(cols[kBar]));
                        }
                        private String mkTooltip(ColumnBean c) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("<html>");
                            sb.append("<b>Description</b>:").append(c.getDescription()).append("<br>");
                            sb.append("<b>UCD</b>:").append(c.getUCD()).append("<br>");
                            sb.append("<b>Datatype</b>:").append(c.getDatatype()).append("<br>");
                            sb.append("<b>Unit</b>:").append(c.getUnit()).append("<br>");                            
                            sb.append("</html>");
                            return sb.toString();
                        }
                        public void actionPerformed(ActionEvent e) {
                            queryArea.setSelectedText(" " + alias + "." + cols[kBar].getName() + " " );
                        }
                    });
                }
            }
        }
    }
}


/* 
$Log: DatacenterToolEditorPanel.java,v $
Revision 1.5  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.4.46.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.4  2005/10/19 18:16:47  nw
made more error tolerant

Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.2.6.1  2005/10/12 09:21:38  nw
added java help system

Revision 1.2  2005/09/12 18:53:45  nw
finished shaping workflow builder.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/