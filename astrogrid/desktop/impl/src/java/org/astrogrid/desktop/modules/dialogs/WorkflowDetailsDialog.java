/*$Id: WorkflowDetailsDialog.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 29-5-05
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;


/**
 * Dialog for creating a new workflow
 *
 * @author   Phil Nicolson pjn3@star.le.ac.uk
 */
public class WorkflowDetailsDialog extends JDialog {
	
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(WorkflowDetailsDialog.class);	

    private JPanel jContentPane, buttonBox, detailsBox = null;
    private JButton updateButton, cancelButton = null;
    private JTextField nameField = null;
    private JTextArea descTextArea = null;
    private JOptionPane opane = null;
    
    protected Preferences prefs = null;
    protected Workflow workflow;

    /**
     * Constructs a new dialog.
     */
    public WorkflowDetailsDialog(Workflow wf) {    	
		super();
		this.workflow = wf;
		initialize();
	}    

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setSize(325, 150);
        this.setModal(true);
        this.setTitle("Workflow Details");
        this.setContentPane(getJContentPane());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(false); 			
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
						
	        /* Create query components. */
	    	JLabel label1 = new JLabel("Name: ");	    	
	    	JLabel label2 = new JLabel("Description: ");
	    	
	    	nameField = new JTextField();
	        descTextArea = new JTextArea(3,7);
	        descTextArea.setEditable(true);
			JScrollPane scrollPane = new JScrollPane(descTextArea);
	        JPanel b = getDetailsBox();
	        b.add(label1);	
	        b.add(nameField);
	        b.add(label2);
	        b.add(scrollPane);
	        makeCompactGrid(b, 2,2,5,5,5,5);
						
			jContentPane.add(getButtonBox(), BorderLayout.SOUTH);           
		    jContentPane.add(b, BorderLayout.CENTER);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonBox() {
		if (buttonBox == null) {
			buttonBox = new JPanel();
			buttonBox.add(getUpdateButton(), null);
			buttonBox.add(getCancelButton(), null);
		}
		return buttonBox;
	}
	/**
	 * This method initializes detailsBox jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getDetailsBox() {
		if (detailsBox == null){
			detailsBox = new JPanel(new SpringLayout());
			detailsBox.setBorder(BorderFactory.createTitledBorder("Enter details:")); 
		}
		return detailsBox;
	}	
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getUpdateButton() {
		if (updateButton == null) {
			updateButton = new JButton();
			updateButton.setText("OK");
			updateButton.setToolTipText("Create workflow");
			updateButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					workflow.setName(getName());
					workflow.setDescription(getDesc());
                    dispose(); // causes dialogue to return
				}
			});
		}
		return updateButton;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
				}
			});
		}
		return cancelButton;
	}	
    /**
     * Returns the content of the name text entry field.
     *
     * @return  workflow name
     */
    public String getName() {
        return nameField.getText();
    }
    /**
     * Returns the content of the description text entry field.
     * 
     * @return  workflow description
     */
    public String getDesc() {
        return descTextArea.getText();
    }
	/**
	 * 
	 */
    public void show() {
        super.show();
        requestFocus();
    }
    public Workflow getWorkflow() {
        return this.workflow;
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
    private static void makeCompactGrid(Container parent,
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
