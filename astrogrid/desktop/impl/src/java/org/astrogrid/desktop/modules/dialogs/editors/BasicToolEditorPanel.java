package org.astrogrid.desktop.modules.dialogs.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.io.Piper;
import org.astrogrid.workflow.beans.v1.Tool;

/** A panel for editing tool documents (i.e. sets of input and output parameters)
 * 
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

	
    class DeleteCellEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ItemListener {
        
        ParameterTableModel pm;
        Boolean delete;
        boolean allowDel = false;
        int row;

        public Object getCellEditorValue() {
            return delete;
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        	this.row = row;
            if (pm == null) {
                pm = (ParameterTableModel)table.getModel();                
            }
        	JComponent comp;
        	if (((Boolean)value).booleanValue()) {
        		comp = new JCheckBox();
        		((JCheckBox)comp).setHorizontalAlignment(JLabel.HORIZONTAL);
        		((JCheckBox)comp).setToolTipText("Check to delete this optional parameter, (uncheck to undelete)");
        	} else {
        		comp = new JLabel();
        	}
        	comp.setOpaque(true);
        	
        	if (isSelected) {
        		comp.setForeground((Color)UIManager.get("Table.selectionForeground"));
        		comp.setBackground((Color)UIManager.get("Table.selectionBackground"));
        		comp.setBorder(BorderFactory.createEmptyBorder());
        	} else {
        		comp.setForeground((Color)UIManager.get("Table.foreground"));
        		comp.setBackground((Color)UIManager.get("Table.background"));
        		comp.setBorder(BorderFactory.createEmptyBorder());
        	}        	
        	if (hasFocus) {
        		comp.setForeground((Color)UIManager.get("Table.focusCellForeground"));
        		comp.setBackground((Color)UIManager.get("Table.focusCellBackground"));
        		comp.setBorder((Border)UIManager.get("Table.focusCellHighlightBorder"));
        	}
        	return comp;
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        	this.row = row;
            if (pm == null) {
                pm = (ParameterTableModel)table.getModel();                
            }
        	JComponent comp;
        	if (((Boolean)value).booleanValue()) {
        		comp = new JCheckBox();
        		((JCheckBox)comp).setHorizontalAlignment(JLabel.HORIZONTAL);
        		((JCheckBox)comp).setToolTipText("Check to delete this optional parameter");
        		((JCheckBox)comp).addItemListener(this);
        	} else {
        		comp = new JLabel();
        	}
        	comp.setOpaque(true);
        	
        	return comp;
        }  
        
        /** Listens to the check boxes. */
        public void itemStateChanged(ItemEvent e) {
            //Now that we know which button was pushed, find out
            //whether it was selected or deselected.
        	ParameterValue parameter = pm.getRows()[row];
        	if (e.getStateChange() == ItemEvent.DESELECTED) {               
        		fireEditingStopped();
            } else {                
                fireEditingStopped();
            }
        }
    }
    
    class RepeatCellEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ItemListener {
        
        ParameterTableModel pm;
        Boolean delete;
        int row;  
        JComponent comp;
        
        public Object getCellEditorValue() {
            return delete;
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        	this.row = row;
            if (pm == null) {
                pm = (ParameterTableModel)table.getModel();                
            }
        	
        	if (((Boolean)value).booleanValue()) {
        		comp = new JCheckBox();
        		((JCheckBox)comp).setHorizontalAlignment(JLabel.HORIZONTAL);
        		((JCheckBox)comp).setToolTipText("Add another " + pm.getValueAt(row, 0) + " parameter");        		
        	} else {
        		comp = new JLabel();
        	}
        	comp.setOpaque(true);
        	
        	if (isSelected) {
        		comp.setForeground((Color)UIManager.get("Table.selectionForeground"));
        		comp.setBackground((Color)UIManager.get("Table.selectionBackground"));
        		comp.setBorder(BorderFactory.createEmptyBorder());
        	} else {
        		comp.setForeground((Color)UIManager.get("Table.foreground"));
        		comp.setBackground((Color)UIManager.get("Table.background"));
        		comp.setBorder(BorderFactory.createEmptyBorder());
        	}        	
        	if (hasFocus) {
        		comp.setForeground((Color)UIManager.get("Table.focusCellForeground"));
        		comp.setBackground((Color)UIManager.get("Table.focusCellBackground"));
        		comp.setBorder((Border)UIManager.get("Table.focusCellHighlightBorder"));
        	}
        	return comp;
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
         	this.row = row;
            if (pm == null) {
                pm = (ParameterTableModel)table.getModel();                
            }
        	JComponent comp;
        	if (((Boolean)value).booleanValue()) {
        		comp = new JCheckBox();
        		((JCheckBox)comp).setHorizontalAlignment(JLabel.HORIZONTAL);
        		((JCheckBox)comp).setToolTipText("Check to delete this optional parameter");
        		((JCheckBox)comp).addItemListener(this);
        	} else {
        		comp = new JLabel();
        	}
        	comp.setOpaque(true);
        	comp.setBackground((Color)UIManager.get("Table.selectionBackground"));
        	
        	return comp;
        } 
        
        /** Listens to the check boxes. */
        public void itemStateChanged(ItemEvent e) {
            //Now that we know which button was pushed, find out
            //whether it was selected or deselected.
        	ParameterValue parameter = pm.getRows()[row];
        	if (e.getStateChange() == ItemEvent.SELECTED) {       		
        		fireEditingStopped(); 
            } else {                
                fireEditingStopped();
            }
        }
    }    
    
    class ValueCellEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ItemListener {
        
        ParameterTableModel pm;
        Boolean delete;
        int row;

        public Object getCellEditorValue() {
            return null;
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        	this.row = row;
            if (pm == null) {
                pm = (ParameterTableModel)table.getModel();                
            }
        	JComponent comp;
        	if (value instanceof String[]) {    		
        		comp = new JComboBox((String[])value);        	
        	} else {
        		comp = new JLabel();
        	}
        	comp.setOpaque(true);
        	
        	return comp;
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        	this.row = row;
            if (pm == null) {
                pm = (ParameterTableModel)table.getModel();                
            }
        	JComponent comp;
        	if (value instanceof String[]) {  		
        		comp = new JComboBox((String[])value);
        		((JComboBox)comp).setSelectedItem(pm.getValueAt(row, 1));
        		((JComboBox)comp).setToolTipText("Select value");
        		((JComboBox)comp).addItemListener(this);
        	} else {
        		comp = new JLabel();
        	}
        	comp.setOpaque(true);
        	
        	return comp;
        } 
        
        /** Listens to the check boxes. */
        public void itemStateChanged(ItemEvent e) {
        	//fireEditingStopped();
            //Now that we know which button was pushed, find out
            //whether it was selected or deselected.
        	ParameterValue parameter = pm.getRows()[row];
        	JComboBox cb = (JComboBox)e.getSource();
        	parameter.setValue((String)cb.getSelectedItem());
        	pm.fireTableCellUpdated(row,1);
        }
    }
	
	
	class IndirectCellEditor extends AbstractCellEditor implements TableCellEditor, ItemListener {
        
        private final JCheckBox button;
        private Boolean indirect;
        private ParameterTableModel pm;
        private int row;
        private final boolean isInput;

        public IndirectCellEditor(boolean isInput) {
            this.isInput = isInput;
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
                URI uri; 
                if (isInput) {
                        uri = resourceChooser.chooseResourceWithParent("Select input location", true,true, true,BasicToolEditorPanel.this);
                } else {
                    uri = resourceChooser.chooseResourceWithParent("Select output location", true,false, true,BasicToolEditorPanel.this);                    
                }
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
                        toolModel.fireParameterChanged(BasicToolEditorPanel.this, parameter ) ; 
            }
        }        
    }

    
    
    /** custom table - no functionality, just hacks and tweaks to make it look / work nicer */
    protected final class ParameterTable extends JTable {
        public ParameterTable(ParameterTableModel dm,boolean isInput) {
            super(dm);
           getColumnModel().getColumn(0).setPreferredWidth(150);    //Name
           getColumnModel().getColumn(0).setMinWidth(110);
           getColumnModel().getColumn(1).setPreferredWidth(200);   //Value
           getColumnModel().getColumn(1).setMinWidth(160);
           getColumnModel().getColumn(2).setCellEditor(new IndirectCellEditor(isInput));
           getColumnModel().getColumn(2).setPreferredWidth(35);     //Indirect    
           getColumnModel().getColumn(2).setMaxWidth(35);
           getColumnModel().getColumn(3).setCellEditor(new RepeatCellEditor());           
           getColumnModel().getColumn(3).setPreferredWidth(35);    // Repeat
           getColumnModel().getColumn(3).setMaxWidth(35);
           getColumnModel().getColumn(4).setCellEditor(new DeleteCellEditor());
           getColumnModel().getColumn(4).setPreferredWidth(35);    //Delete
           getColumnModel().getColumn(4).setMaxWidth(35);           
           setPreferredScrollableViewportSize(new Dimension(200,100));
           putClientProperty("terminateEditOnFocusLost", Boolean.TRUE); // improves editing behaviour.           
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
           
        protected String[] columnToolTips = {
        		"Parameter name",
				"Parameter value",
				"<html> <b>Reference</b> to remote file: " +
				"<br> If a checkbox is visible then the value of this parameter may be a reference to a remote file. " +
				"<br> Checking the box will open a dialog to allow you to locate the file you wish to use. </html>",
				"<html> <b>Repeating</b> parameter: " +
				"<br> If a checkbox is visible then this task will accept more than entry for the particular parameter. " +
				"<br> Checking the box will open another row in the table and allow you to enter another value. </html>",
				"<html> <b>Delete</b> this optional parameter? " +
				"<br> If a checkbox is visible then this parameter is optional for the chosen task. " +
				"<br> Checking the box will delete the parameter, and remove it's row from the display. </html>"
        };
        
        // Implement column header tool tips
        protected JTableHeader createDefaultTableHeader() {
        	return new JTableHeader(columnModel) {
        		public String getToolTipText(MouseEvent e) {
        			String tip = null;
        			java.awt.Point p = e.getPoint();
        			int index = columnModel.getColumnIndexAtX(p.x);
        			int realIndex = columnModel.getColumn(index).getModelIndex();
        			return columnToolTips[realIndex];
        		}
        	};
        }
        
        // getCellRenderer for delete button
        public TableCellRenderer getCellRenderer(int row, int column) {
        	DeleteCellEditor ed = new DeleteCellEditor();
        	RepeatCellEditor re = new RepeatCellEditor();
        	ValueCellEditor ve = new ValueCellEditor();
            if (column == 4) {            	
                return ed;
             } else if (column == 3) {            	
                return re;
             } else if(column == 1 && (getValueAt(row, 1) instanceof String[])) {
             	return ve;
             }
             // else...
            return super.getCellRenderer(row, column);
        }
        
        // getCellEditor for delete button
        public TableCellEditor getCellEditor(int row, int column) {
        	DeleteCellEditor ed = new DeleteCellEditor();
        	RepeatCellEditor re = new RepeatCellEditor();
        	ValueCellEditor ve = new ValueCellEditor();
            if (column == 4) {            	
                return ed;
             } else if (column == 3) {            	
                return re;
             }
        	 else if(column == 1 && (getValueAt(row, 1) instanceof String[])) {
         	return ve;
         }
             // else...
            return super.getCellEditor(row, column);
        }
        
    }
    /** table model for our custom table */
    protected  class ParameterTableModel extends AbstractTableModel implements ToolEditListener {
        public ParameterTableModel(boolean isInput) {
            this.isInput = isInput;
        }
        protected final boolean isInput;
        private  int COLUMN_COUNT = 5;
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
                case 3: return "Rep?";
                case 4: return "Del?";
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
        	if (!isInput)
        		outputLabel.setText("Outputs:");
            final ParameterValue  row =  rows[rowIndex];
            ParameterValue  previousRow = null;
            if (rowIndex >= 1) {
            	previousRow =  rows[rowIndex-1];
            }
            ParameterBean d = (ParameterBean)toolModel.getInfo().getParameters().get(row.getName());
            ParameterReferenceBean[] paramRef;
            InterfaceBean intBean[] = toolModel.getInfo().getInterfaces();
            switch (columnIndex) {
                case 0:     
                    try {                        
                        return d.getUiName();
                    } catch (IllegalArgumentException e) {
                        return "not available";
                    }

                 case 1:
                 	 // Value may be made from an options list in registry
                 	 if (d.getOptions() != null) { // Entry has options list
                 	 	String[] ol = d.getOptions(); // Option List
                 	 	String[] options = new String[d.getOptions().length + 1];
                 	 	options[0] = "Please select:"; // Set 1st item to message
                 	 	for (int i = 0; i< ol.length; i++) { // Add other options
                 	 		options[i+1] = ol[i];
                 	 	}
                 	 	if (row.getValue() != "") // Parameter already has value entered, set to 1st item in list
                      	   options[0] = row.getValue();
                 	 	return options;
                 	 }
                 	 return row.getValue();
                 case 2:
                     return new Boolean(row.getIndirect());
                 case 3:                 	
                 	int max = 1;
                    for (int i = 0; i < intBean.length; i++) { 
                        if (intBean[i].getName().equalsIgnoreCase(toolModel.getTool().getInterface())) {
                        	if (isInput) {
                        		paramRef = intBean[i].getInputs();
                        	} else {
                        		paramRef = intBean[i].getOutputs();
                        	}
                        	for (int j = 0; j < paramRef.length ; j++) {
                        		if (paramRef[j].getRef().equalsIgnoreCase(row.getName())) {
                        			max = paramRef[j].getMax();                        				
                        			break;
                        		}
                        	}
                        }
                        break;
                    }
                    return new Boolean(max <= 0? true:false);                	
                 case 4:
                 	int min = 1;
                    for (int i = 0; i < intBean.length; i++) { 
                        if (intBean[i].getName().equalsIgnoreCase(toolModel.getTool().getInterface())) {
                        	if (isInput) {
                        		paramRef = intBean[i].getInputs();
                        	} else {
                        		paramRef = intBean[i].getOutputs();
                        	}
                        	for (int j = 0; j < paramRef.length ; j++) {
                        		if (paramRef[j].getRef().equalsIgnoreCase(row.getName())) {
                        			min = paramRef[j].getMin();                        				
                        			break;
                        		}
                        	}
                        }
                        break;
                    }
                    if (previousRow != null && previousRow.getName().equalsIgnoreCase(row.getName()))
                    	return new Boolean(true);                    
                    return new Boolean(min <= 0? true:false);                    
                 default:
                    return null;
            }
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return editable && (columnIndex == 1 ||
            				   (columnIndex == 2 && allowIndirect) ||
							   (columnIndex == 3 && (((Boolean)getValueAt(rowIndex, 3)).booleanValue())) ||
							   (columnIndex == 4 && (((Boolean)getValueAt(rowIndex, 4)).booleanValue())));
        }
        
        public void setRows(ParameterValue[] rows) {
            this.rows = rows;
            fireTableDataChanged();
        }
        
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
         final ParameterValue row= rows[rowIndex];
         switch(columnIndex) {
             case 1: // name
             	 if (aValue != null) 
                   row.setValue(aValue.toString());
                 toolModel.fireParameterChanged(BasicToolEditorPanel.this,row);
                 break;
             case 2: // ref
                 row.setIndirect(((Boolean)aValue).booleanValue());
                 toolModel.fireParameterChanged(BasicToolEditorPanel.this,row);
                 break;
             case 3: // rep
				 ParameterValue newPv = new ParameterValue();
				 newPv.setName(row.getName());
				 newPv.setValue("");
				 toolModel.getTool().getInput().addParameter(rowIndex+1, newPv);
             	 toolModel.fireParameterAdded(BasicToolEditorPanel.this,newPv);
             	 break;
             case 4: // del
             	 toolModel.fireParameterRemoved(BasicToolEditorPanel.this, row);
             	 break;
             default: // do nothing in all other cases.
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
            if( te.getSource() == BasicToolEditorPanel.this )
                return ;
            ParameterValue pv = te.getChangedParameter();
            for (int i = 0; i < getRows().length; i++) {
                if (pv == getRows()[i]) {
                    fireTableRowsUpdated(i,i);
                }
            }                       
        }

        public void parameterAdded(ToolEditEvent te) {
        	ParameterValue pv = te.getChangedParameter();
            for (int i = 0; i < getRows().length; i++) {
                if (pv == getRows()[i]) {
                    fireTableRowsInserted(i,i);
                }
            }
            toolSet(te);       	
        }

        public void parameterRemoved(ToolEditEvent te) {
        	ParameterValue pv = te.getChangedParameter();
            if (isInput) {
                toolModel.getTool().getInput().removeParameter(pv);
            } else {
                toolModel.getTool().getOutput().removeParameter(pv);
            }
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

    /** create a parameters panel, 
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
            inputTable = new ParameterTable(inputs,true);              
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
            outputTable = new ParameterTable(outputs,false);         
        }
        return outputTable;              
    }
    
    /**
     * 
     */
    JLabel outputLabel = new JLabel("Outputs: none for this tool");
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
        outputLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        add(outputLabel);
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
Revision 1.15  2006/03/06 15:45:51  pjn3
improved column headings to make 'less criptic'

Revision 1.14  2005/12/16 09:42:47  jl99
Merge from branch desktop-querybuilder-jl-1404

Revision 1.13  2005/11/28 10:41:48  pjn3
Now displaying parameters that take their values in the form of an option list.

Revision 1.12  2005/11/25 15:39:09  pjn3
Reworked repeating parameters, and added delete to all repeated params

Revision 1.11  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.9.2.2  2005/11/23 04:46:33  nw
disabled 'file' tab of indirect dialogue for output parameters.

Revision 1.9.2.1  2005/11/17 21:18:22  nw
*** empty log message ***

Revision 1.10  2005/11/16 20:49:58  pjn3
Checkbox column widths improved

Revision 1.9  2005/11/11 15:24:23  pjn3
overwrote previous fix!

Revision 1.7.2.2  2005/11/11 15:11:17  pjn3
corrected overwritten fix

Revision 1.7.2.1  2005/11/10 10:47:34  pjn3
Single parameter insert working

Revision 1.7  2005/11/09 18:48:26  pjn3
*** empty log message ***

Revision 1.6.4.1  2005/11/09 18:39:41  pjn3
added basic delete back in

Revision 1.6  2005/11/08 09:54:56  pjn3
branch pjn_workbench_2_11_05

Revision 1.5.2.1  2005/11/03 09:39:50  pjn3
delete/repeat buttons swapped to checkboxes, listeners added

Revision 1.5  2005/11/01 15:17:50  pjn3
branch pjn_workbench_1306_again

Revision 1.4.4.3  2005/11/01 11:07:49  pjn3
Initial add parameter added

Revision 1.4.4.2  2005/10/31 15:50:11  pjn3
Ability to delete parameters added

Revision 1.4.4.1  2005/10/27 11:21:37  pjn3
Initial changes for repeating and optional params

Revision 1.4  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.6.1  2005/10/10 18:12:37  nw
merged kev's datascope lite.

Revision 1.3  2005/10/07 12:12:21  KevinBenson
resorted back to adding to the ResoruceChooserInterface a new method for selecting directories.
And then put back the older one.

Revision 1.2  2005/10/04 20:43:38  KevinBenson
set it to "false" for selecting directories on the local file system.

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