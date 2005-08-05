/* StepInfoPanel.java
 * Created on 07-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.workflow.beans.v1.Step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 *
 */
public class StepPanel extends JPanel {
	
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(StepPanel.class);	

	private JTextField nameField, varField, taskField, interfaceField = null;
	private JEditorPane descPane = null;
	private JPanel jContentPane, bodyPanel = null;
	private String description;
	private String name;
	private String var;
	private String task;
	private String inter;
	/**
	 * production constructor
	 */
	public StepPanel(Step step) {
		super();
		this.description = step.getDescription();
		this.name = step.getName();
		this.var = step.getResultVar();
		this.task = step.getTool().getName();
		this.inter = step.getTool().getInterface();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {	
		this.add(getJContentPane());
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
		    jContentPane.add(getBodyPanel());
		}
		return jContentPane;
	}
	/**
	 * This method initializes body panel
	 * 	
	 * @return javax.swing.JPanel
	 */    
	private JPanel getBodyPanel() {
		if (bodyPanel == null) {
			bodyPanel = new JPanel();
			
			JLabel label1 = new JLabel("Step name: ", JLabel.TRAILING);
			JLabel label2 = new JLabel("Step description: ", JLabel.TRAILING);
			JLabel label3 = new JLabel("Variable name: ", JLabel.TRAILING);
			JLabel label4 = new JLabel("Task name: ", JLabel.TRAILING);
			JLabel label5 = new JLabel("Interface name: ", JLabel.TRAILING);
			
	    	nameField = new JTextField(name);
	    	nameField.setEditable(false);
	    	descPane = new JEditorPane();
	    	descPane.setEditable(false);
	    	descPane.setText(description);
	    	descPane.setCaretPosition(0);
	    	descPane.setPreferredSize(new Dimension(400,80));
	    	
	    	JScrollPane pane = new JScrollPane(descPane);
	    	pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    	pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    	varField = new JTextField(var);
	    	varField.setEditable(false);
	    	taskField = new JTextField(task);
	    	taskField.setEditable(false);
	    	taskField.setToolTipText("Select the task tab to see task details");
	    	interfaceField = new JTextField(inter);
	    	interfaceField.setEditable(false);
	    	interfaceField.setToolTipText("Select the task tab to see task details");
	    	
	    	JPanel p = new JPanel(new SpringLayout());
	    	p.add(label1);
	    	p.add(nameField);
	    	p.add(label2);
	    	p.add(pane);
	    	p.add(label3);
	    	p.add(varField);
	    	p.add(label4);
	    	p.add(taskField);
	    	p.add(label5);
	    	p.add(interfaceField);
	    	
			makeCompactGrid(p, 5,2,2,2,10,3);
			bodyPanel.add(p);
			bodyPanel.setPreferredSize(new Dimension(800,200));
		}
		return bodyPanel;
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

  }
