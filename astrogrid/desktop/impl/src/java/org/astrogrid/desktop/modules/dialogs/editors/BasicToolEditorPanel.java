package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.io.Piper;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.portable.BoxedValueHelper;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;

import javax.swing.AbstractCellEditor;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

/** A panel for editing tool documents (i.e. sets of input and output parameters)
 * 
 * @todo add support for repeated and optional parameters.
 * @todo add support for viewing / editing remote resources? maybe not.
 * @author Noel Winstanley nw@jb.man.ac.uk 13-May-2005
 */
public  class BasicToolEditorPanel extends AbstractToolEditorPanel  {
    
    /**Editor for a cell that pops up resource chooser as needed. 
     * cribbed from 
     * http://java.sun.com/docs/books/tutorial/uiswing/components/table.html#editrender
     * @author Noel Winstanley nw@jb.man.ac.uk 16-May-2005
     *
     */
    class IndirectCellEditor extends AbstractCellEditor implements TableCellEditor, ItemListener {
        
        JCheckBox button;
        Boolean indirect;
        ParameterTableModel pm;
        int row;
        
        public IndirectCellEditor() {
            button = new JCheckBox();            
            button.addItemListener(this);
            button.setBorderPainted(false);
            
        }

        public Object getCellEditorValue() {
            return indirect;
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.indirect = (Boolean)value;
            this.row = row;
            if (pm == null) {
                pm = (ParameterTableModel)table.getModel();                
            }
            return button;
        }
        
        /** Listens to the check boxes. */
        public void itemStateChanged(ItemEvent e) {
            //Now that we know which button was pushed, find out
            //whether it was selected or deselected.
         ParameterValue parameter = pm.getRows()[row];
        if (e.getStateChange() == ItemEvent.DESELECTED) {               
            fireEditingStopped();
                this.indirect = Boolean.FALSE;        
                parameter.setIndirect(false);
                pm.fireTableCellUpdated(row,2);
        
            } else {                
                fireEditingStopped();
                URI uri = resourceChooser.chooseResourceWithParent("Select resource",true,true,true,BasicToolEditorPanel.this);
                parameter.setIndirect(true);
                this.indirect = Boolean.TRUE;                  
                pm.fireTableCellUpdated(row,2);                       
                if (uri != null && uri.getScheme() != null) {
                    if (uri.getScheme().equalsIgnoreCase("file")){
                        int shallInline = JOptionPane.showConfirmDialog(BasicToolEditorPanel.this,"A file:/ resource has been selected - which won't be accessible from remote services. Inline this value?","Warning",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                        if (shallInline == JOptionPane.YES_OPTION) {
                            StringWriter sw = new StringWriter();
                            try {
                            Reader in = new FileReader(new File(uri));
                            Piper.pipe(in,sw);
                            parameter.setValue(sw.toString());
                            } catch (IOException ex) {
                                UIComponent.showError(BasicToolEditorPanel.this,ex.getMessage(),ex);
                            }
                            parameter.setIndirect(false);
                            pm.fireTableCellUpdated(row,1);
                            pm.fireTableCellUpdated(row,2);                            
                        } else { //ok, they know best..
                            parameter.setValue(uri.toString());                    
                            pm.fireTableCellUpdated(row,1);
                        }
                    } else {
                        parameter.setValue(uri.toString());                    
                        pm.fireTableCellUpdated(row,1);
                    }
                        
            }
            }
        }        
    }
    /** custom table - no functionality, just hacks and tweaks to make it look / work nicer */
    protected final class ParameterTable extends JTable {

        {                
                getColumnModel().getColumn(0).setPreferredWidth(30);                      
                getColumnModel().getColumn(1).setPreferredWidth(100);                        
            getColumnModel().getColumn(2).setPreferredWidth(20);
            getColumnModel().getColumn(2).setMaxWidth(20);
           getColumnModel().getColumn(2).setCellEditor(new IndirectCellEditor());
           setPreferredScrollableViewportSize(new Dimension(200,100));
           putClientProperty("terminateEditOnFocusLost", Boolean.TRUE); // improves editing behaviour.           
        }
        public ParameterTable(ParameterTableModel dm) {
            super(dm);
        }
        
        // overriden to make the height of scroll match viewpost height if smaller 
        public boolean getScrollableTracksViewportHeight() { 
            return getPreferredSize().height < getParent().getHeight(); 
        } 

        //Implement table cell tool tips.
           public String getToolTipText(MouseEvent e) {
               String tip = null;
               java.awt.Point p = e.getPoint();
               int rowIndex = rowAtPoint(p);
               if (rowIndex < 0) {
                   return super.getToolTipText(e);
               }
               int colIndex = columnAtPoint(p);
               int realColumnIndex = convertColumnIndexToModel(colIndex);               
               if (realColumnIndex == 0) { //Namecolumn..
                   final String parameterName = ((ParameterTableModel)getModel()).getRows()[rowIndex].getName();            
                   ParameterBean d = (ParameterBean)toolModel.getInfo().getParameters().get(parameterName);                                       
                   tip= mkToolTip(d); 
               } else if (realColumnIndex == 1) { // value column - show a popup if it's tool long to fit.
                   Object value = getValueAt(rowIndex,colIndex);
                   if (value != null) {
                       String str = value.toString();
                       if (str != null && str.trim().length() > getColumnModel().getColumn(1).getWidth()) {
                           tip = value.toString();
                       }
                   } 
               } else {
                   tip = super.getToolTipText(e);
               }
               return tip;
           }
    }
    /** table model for our custom table */
    protected  class ParameterTableModel extends AbstractTableModel implements ToolEditListener {
        public ParameterTableModel(boolean isInput) {
            this.isInput = isInput;
        }
        protected final boolean isInput;
        private  int COLUMN_COUNT = 3;
        private ParameterValue[] rows = new ParameterValue[]{};
            
        public Class getColumnClass(int columnIndex) {
            if (columnIndex == 2) {
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
                case 0: return "Name";
                case 1: return "Value";
                case 2: return "Ref?";
                default:
                    return "";                      
            }
        }

        public int getRowCount() {                
            return rows.length;               
        }
        
        public ParameterValue[] getRows() {
            return rows;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            final ParameterValue  row =  rows[rowIndex];
            switch (columnIndex) {
                case 0:     
                    try {
                        ParameterBean d = (ParameterBean)toolModel.getInfo().getParameters().get(row.getName());
                        return d.getUiName();
                    } catch (IllegalArgumentException e) {
                        return "not available";
                    }

                 case 1:
                     return row.getValue();
                 case 2:
                     return new Boolean(row.getIndirect());
                 default:
                     return null;
            }
        }
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return editable && (columnIndex == 1 || (columnIndex== 2 && allowIndirect));
        }
        
        public void setRows(ParameterValue[] rows) {
            this.rows = rows;
            fireTableDataChanged();
        }
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
         final ParameterValue row= rows[rowIndex];
         switch(columnIndex) {
             case 1:
                 row.setValue(aValue.toString());
                 toolModel.fireParameterChanged(BasicToolEditorPanel.this,row);
                 break;
             case 2:
                 row.setIndirect(((Boolean)aValue).booleanValue());
                 toolModel.fireParameterChanged(BasicToolEditorPanel.this,row);
                 break;
             default: // do nothing in all oher caes.
                 break;
         }
        }

        public void toolSet(ToolEditEvent te) {
            if (isInput) {
                setRows(toolModel.getTool().getInput().getParameter());
            } else {
                setRows(toolModel.getTool().getOutput().getParameter());
            }
        }

        public void toolChanged(ToolEditEvent te) {
          toolSet(te);
        }
  
        public void toolCleared(ToolEditEvent te) {
            setRows(new ParameterValue[]{});
        }

        public void parameterChanged(ToolEditEvent te) {
            ParameterValue pv = te.getChangedParameter();
            for (int i = 0; i < getRows().length; i++) {
                if (pv == getRows()[i]) {
                    fireTableRowsUpdated(i,i);
                }
            }                       
        }

        public void parameterAdded(ToolEditEvent te) {
                toolSet(te);            
        }

        public void parameterRemoved(ToolEditEvent te) {
                toolSet(te);            
        }            
    }
    /**
     * Commons Logger for this class
     */
    public static final Log logger = LogFactory.getLog(BasicToolEditorPanel.class);
    
    protected JTable inputTable;
    protected JTable outputTable;
    
    protected final ResourceChooserInternal resourceChooser;
   private boolean allowIndirect = true;

   
   /** set to true to allow indirect parameters */
   public void setAllowIndirect(boolean allowIndirect) {
       this.allowIndirect = allowIndirect;
   }

    /** ctreate a parameters panel, 
     *  Construct a new ParametersPanel
     * @param resourceChooser choose to use to support indirect parameters editor. 
     */
    public BasicToolEditorPanel(ResourceChooserInternal resourceChooser) {
        this(new ToolModel(),resourceChooser,true);
    }
    
    public BasicToolEditorPanel(ResourceChooserInternal resourceChooser, boolean useScrollBars) {
        this(new ToolModel(),resourceChooser,useScrollBars);
    }
    public BasicToolEditorPanel(ToolModel tm,ResourceChooserInternal resourceChooser) {
        this(tm,resourceChooser,true);
    }
            
        
                    
    /** Construct a new BasicToolEditorPanel
     * @param toolModel
     * @param resourceChooser2
     */
    public BasicToolEditorPanel(ToolModel toolModel, ResourceChooserInternal resourceChooser, boolean useScrollBars) {
        super(toolModel);
        this.resourceChooser = resourceChooser;
        this.useScrollBars = useScrollBars;
        initialize();
    }

    private final boolean useScrollBars;
    protected JTable getInputTable() {
        if (inputTable == null) {
            ParameterTableModel inputs = getInputTableModel();         
            toolModel.addToolEditListener(inputs);            
            inputTable = new ParameterTable(inputs);              
        }
        return inputTable;
    }
    
    /**
     * @return
     */
    protected ParameterTableModel getInputTableModel() {
       if (inputTableModel == null) {
           inputTableModel =  new ParameterTableModel(true);
       } 
       return inputTableModel;
    }
    protected ParameterTableModel inputTableModel;

    protected JTable getOutputTable() {
        if (outputTable == null) {
            ParameterTableModel outputs = new ParameterTableModel(false);
            toolModel.addToolEditListener(outputs); 
            outputTable = new ParameterTable(outputs);         
        }
        return outputTable;              
    }
    
    /**
     * 
     */
    protected void initialize() {
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Inputs");
        label.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        add(label);
        if (useScrollBars) {
            add(new JScrollPane(getInputTable()));
        } else {
            add(getInputTable());
        }
        add(new JSeparator());
        label = new JLabel("Outputs");
        label.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        add(label);
        if (useScrollBars) {
            add(new JScrollPane(getOutputTable()));
        } else {
            add(getOutputTable());
        }
        add(Box.createGlue());
    }
    private String mkToolTip(ParameterBean def) {
        StringBuffer result = new StringBuffer();
        result.append("<html>");
        result.append(def.getDescription()!= null ?   def.getDescription(): "");
        result.append("<dl>");
        if (def.getUcd() != null && def.getUcd().trim().length() > 0) 
                result.append("<dt><b>").append("UCD").append("</b></dt><dd>").append(def.getUcd()).append("</dd>");
        if (def.getUnits() != null && def.getUnits().trim().length() > 0) 
                result.append("<dt><b>").append("Units").append("</b></dt><dd>").append(def.getUnits()).append("</dd>");
        if (def.getType() != null) 
            result.append("<dt><b>").append("Type").append("</b></dt><dd>").append(def.getType()).append("</dd>");
        if (def.getSubType() != null && def.getSubType().trim().length() > 0) 
                result.append("<dt><b>").append("Subtype").append("</b></dt><dd>").append(def.getSubType()).append("</dd>");     
        if (def.getOptions() != null) 
                result.append("<dt><b>").append("One of").append("</b></dt><dd>").append(Arrays.asList(def.getOptions())).append("</dd>");        
                
        result.append("</dl></html>");
        logger.debug(result.toString());
        return result.toString();
    }

    /** applicable to any kind of tool */
    public boolean isApplicable(Tool t, ApplicationInformation info) {
        return t != null;
    }
}






/* 
$Log: BasicToolEditorPanel.java,v $
Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.4  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:32  nw
first release of application launcher
 
*/