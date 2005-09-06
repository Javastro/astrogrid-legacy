/*$Id: 
 * Created on 19/5/05
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
/**
 * @author Phil Nicolson pjn3@star.le.ac.uk 19/5/05
 *
 */
public class TaskInfoPanel extends JPanel {	
	
	private final static String vr = "http://www.ivoa.net/xml/VOResource/v0.10";
	
    protected final class paramTableModel extends AbstractTableModel {
    	
        private final static int COLUMN_COUNT = 9;
        
        
        /**
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        /**
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return params.length;
        }
        /**
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int rowIndex, int columnIndex) {            
                return params[rowIndex][columnIndex];              
            }
        /**
         * @see javax.swing.table.TableModel#getColumnName(int)
         * @return String column name
         */        
        public String getColumnName(int column) {
            switch(column) {
                case 0: return "Name";
                case 1: return "Description";
                case 2: return "Value";
                case 3: return "UCD";
                case 4: return "Units";
                case 5: return "Type";
                case 6: return "Subtype";
                case 7: return "Repeat?";
                case 8: return "";
                default: return "";
            }
        }
        /**
         * determine default renderer/editor for each cell.
         * @return class 
         */
        public Class getColumnClass(int c){
        	if (getValueAt(0,c) != null){
        		return getValueAt(0,c).getClass();
        	} else {
        		return "".getClass();
        	}
        }        
    } // end of inner class
    
    
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(TaskInfoPanel.class);

	private JPanel jContentPane, buttonBox = null;
	private JButton okButton = null;
	private JPanel parametersBox, descriptionBox = null;
	private JTable parametersTable = null;
	private JScrollPane scrollPane = null;
	private TableColumn column = null;
	
	private Object params[][]; 
    protected ApplicationDescription desc;
    protected Tool tool;
    protected ApplicationsInternal apps;
    private int paramCount = 0;   		
    protected String[] columnToolTips = { "Parameter name",
                                          "Parameter description",
                                          "Parameter value, either default or user set",
                                           null,
                                           null,
                                           null,
                                           null,
                                          "Checked parameters will accept multiple values.",
										  "Input or Output parameter."};

	
	/**
	 * This method initializes parametersTable jTable	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getParametersTable() {
		JPanel jPanel = new JPanel();
		if (parametersTable == null) {			
			parametersTable = new JTable(new paramTableModel()){
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
			};
			//parametersTable.setShowHorizontalLines(false);
			//parametersTable.setShowVerticalLines(false);
			for (int i = 0; i < paramTableModel.COLUMN_COUNT; i++) {
				column = parametersTable.getColumnModel().getColumn(i);
				if (i==0) {
					column.setPreferredWidth(200);
					column.setCellRenderer(new ToolTipRenderer());
				}
				else if(i==1) {
					column.setPreferredWidth(220);
					column.setCellRenderer(new ToolTipRenderer());
				}
				else if(i==2) {
					column.setPreferredWidth(220);
					column.setCellRenderer(new ToolTipRenderer());
				}				
				else if(i==8) {
					column.setPreferredWidth(30);
				}				
			}
		}
			
			scrollPane = new JScrollPane(parametersTable);	
			scrollPane.setBorder(BorderFactory.createTitledBorder("Parameter details"));
			parametersTable.setPreferredScrollableViewportSize(new Dimension(750,80));
			jPanel.add(scrollPane, BorderLayout.CENTER);
		
		return jPanel;
	}

	
	/**
	 * This method initializes descriptionBox jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getDescriptionBox() {
		if (descriptionBox == null){
			descriptionBox = new JPanel(new SpringLayout()); 
		}
		return descriptionBox;
	}	
	
	/**
	 * This is the default constructor
	 */
	public TaskInfoPanel() {
		super();
        desc = null;
		initialize();
	}
 
    /**  
     * production constructor - for viewing empty tasks
     * */	
    public TaskInfoPanel(ApplicationDescription desc) {
        super();
        this.desc = desc;
        initialize();
    }
    
    /**  
     * production constructor - for viewing user populated tasks
     * */	
    public TaskInfoPanel(Tool tool, ApplicationsInternal apps) {
        super();
        this.tool = tool;
        this.desc = null;
        this.apps = apps;
        initialize();
    }    

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {		
//		this.setSize(625, 350);
        if (desc != null) { // if desc != null then we are looking at the default task details
            tool = desc.createToolFromDefaultInterface();
            params = new Object[tool.getInput().getParameterCount() + tool.getOutput().getParameterCount()][paramTableModel.COLUMN_COUNT];
            this.add(getJContentPane());
            addDescription(desc, tool);
            Interface iface = desc.getInterfaces().get_interface(0); // will always be the first, as we're using the default.
            for (int i = 0; i < tool.getInput().getParameterCount(); i++) {
                ParameterValue v = tool.getInput().getParameter(i);
                BaseParameterDefinition def = desc.getDefinitionForValue(v,iface);
                addField(v,def,"In", null);
            }
            for (int i = 0; i < tool.getOutput().getParameterCount(); i++) {
                ParameterValue v = tool.getOutput().getParameter(i);
                BaseParameterDefinition def = desc.getDefinitionForValue(v,iface);
                addField(v,def,"Out", null);
            }           
        }
        else if(tool != null) { // desc must be null so we are looking at a populated task
            ApplicationDescription appDesc = null;
            try {
            	String temp = "ivo://"+tool.getName();
            	URI uri = new URI(temp);           	
            	appDesc = apps.getApplicationDescription(uri);            	            
           }
            catch (Exception wi) {
            	logger.error("Error getting application description: " + wi.getMessage());
            }
            this.desc = appDesc;
        	params = new Object[tool.getInput().getParameterCount() + tool.getOutput().getParameterCount()][paramTableModel.COLUMN_COUNT];
        	this.add(getJContentPane());
        	addDescription(desc, tool);
        	Interface iface = null;
        	InterfacesType intType = desc.getInterfaces();
        	for (int i = 0; i <= intType.get_interfaceCount(); i++) {
        		if (intType.get_interface(i).getName().equalsIgnoreCase(tool.getInterface())) {
        			iface = intType.get_interface(i);
        			break;
        		}
        	}
            for (int i = 0; i < tool.getInput().getParameterCount(); i++) {
                ParameterValue v = tool.getInput().getParameter(i);
                BaseParameterDefinition def = desc.getDefinitionForValue(v,iface);
                addField(v,def,"In", tool.getInput().getParameter(i).getValue());
            }
            for (int i = 0; i < tool.getOutput().getParameterCount(); i++) {
                ParameterValue v = tool.getOutput().getParameter(i);
                BaseParameterDefinition def = desc.getDefinitionForValue(v,iface);
                addField(v,def,"Out", tool.getOutput().getParameter(i).getValue());
            }        	
        }
	}
   
	/** 
	 * add a field into the builder.
     * @param v
     * @param def
     * @return void
     */
    private void addField(final ParameterValue v, BaseParameterDefinition def, String direction, String value) {
        JLabel nameJLabel = new JLabel(def.getUI_Name());
        JLabel descJLabel = new JLabel(def.getUI_Description().getContent());
        JLabel ucdJLabel = new JLabel(def.getUCD());
        JLabel unitsJLable = new JLabel(def.getUnits());
        JLabel typeJLabel = new JLabel(def.getType().toString());
        JLabel subtypeJLabel = new JLabel(def.getSubType());
        JLabel defaultJLabel = new JLabel(def.getDefaultValue());
        
        ParameterRef pRef = getParameterRef(desc, tool, v);
        
        params[paramCount][0] = (String)def.getUI_Name();
        params[paramCount][1] = (String)def.getUI_Description().getContent();
        if (value == null) {
        	params[paramCount][2] = (String)def.getDefaultValue();
        } else {
        	params[paramCount][2] = value;
        }        
        params[paramCount][3] = (String)def.getUCD();
        params[paramCount][4] = (String)def.getUnits();
        params[paramCount][5] = (String)def.getType().toString();        
        params[paramCount][6] = (String)def.getSubType();
        if (pRef.getMaxoccurs() == 0) {
        	params[paramCount][7] = new Boolean(true);
        } else {
        	params[paramCount][7] = new Boolean(false);
        }
        params[paramCount][8] = direction;
        paramCount = paramCount +1 ;
    }
    
	/** 
	 * add task details into the builder.
     * @param ApplicationDescription desc
     * @param Tool tool
     * @return void
     */
    private void addDescription(ApplicationDescription desc, Tool tool) {    	
    	JLabel label1 = new JLabel("Task: ");
    	JLabel label2 = new JLabel("Interface: ");
    	JLabel label3 = new JLabel("Description: ");
        JLabel taskLabel = new JLabel(tool.getName());
        JLabel intLabel = new JLabel(tool.getInterface());
        JTextArea descTextArea = new JTextArea(3,6);
        descTextArea.setLineWrap(true);
        descTextArea.setEditable(false);        
        //descTextArea.setText(DomHelper.getNodeTextValue(desc.getOriginalVODescription(),"Description","vr"));
        try {
            descTextArea.setText((desc.getOriginalVODescription().getElementsByTagNameNS(vr,"description").item(0).getFirstChild().getNodeValue()));
        }
        catch(NullPointerException ex) {
        	logger.error("Error loading task description for task, " + ex.getMessage() );
        	descTextArea.setText("");
        }
        descTextArea.setCaretPosition(0);
        descTextArea.setWrapStyleWord(true);
		scrollPane = new JScrollPane(descTextArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
        JPanel b = getDescriptionBox();
        b.add(label1);
        b.add(taskLabel);
        b.add(label2);
        b.add(intLabel);
        b.add(label3);
        b.add(scrollPane);
        makeCompactGrid(b, 3,2,0,0,1,2);
    }      
    
            
    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
            Box b = Box.createVerticalBox();
            b.add(getDescriptionBox());
            b.add(getParametersTable());
            b.add(Box.createVerticalGlue());
		    jContentPane.add(b, BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * getParameterRef
	 * @param desc ApplicationDescription
	 * @param t Tool
	 * @param v ParameterValue
	 * @return pRef ParameterRef
	 */
	private ParameterRef getParameterRef(ApplicationDescription desc, Tool t, ParameterValue v) {
	    
	    int[] cardinality = new int[2] ; 	    
	    Interface[] intfs = desc.getInterfaces().get_interface();
	    Interface intf = null;
	    for (int i = 0; i < intfs.length; i++) {
	        if (intfs[i].getName().equals(tool.getInterface())) {
	            intf = intfs[i];
	            break;
	        }            
	    }	            
	    ParameterRef pRef = desc.getReferenceForValue( v, intf );	    
	    return pRef;        
	}
	
    public Tool getTool() {
        return this.tool;
    }
    
    /**
     * Aligns the first <code>rows</code> * <code>cols</code>
     * components of <code>parent</code> in
     * a grid. Each component in a column is as wide as the maximum
     * preferred width of the components in that column;
     * height is similarly determined for each row.
     * The parent is made just big enough to fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            logger.error("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                                   getConstraintsForCell(r, c, parent, cols).
                                       getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                                    getConstraintsForCell(r, c, parent, cols).
                                        getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }

    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(
                                                int row, int col,
                                                Container parent,
                                                int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    } 
    
    public class ToolTipRenderer extends JLabel implements TableCellRenderer {
    	public Component getTableCellRendererComponent(JTable table, 
    			                                       Object value,
													   boolean isSelected,
													   boolean hasFocus,
													   int rowIndex,
													   int colIndex) {    		
    		if (value == null || value.toString().length() <= 0) {
    			setText("--");
    			setToolTipText("--");
    		}    		
    		else if (value.toString().length() >100) {
    			int rtn = value.toString().indexOf(" ",80);
    			String tip = "<html>" + value.toString().substring(0, rtn) + "<br>" ;
    			tip += value.toString().substring(rtn + 1) + "</html>";
    			setToolTipText(tip);
    			setText(value.toString());
    		}
    		else {
    			setText(value.toString());
    			setToolTipText(value.toString());
    		}
    		return this;
    	}
    }
}  

