/*$Id: WorkflowDetailsDialog.java,v 1.6 2006/04/18 23:25:44 nw Exp $
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.astrogrid.desktop.modules.ui.SpringLayoutHelper;
import org.astrogrid.workflow.beans.v1.Workflow;


/**
 * Dialog for creating a new workflow
 *
 * @author   Phil Nicolson pjn3@star.le.ac.uk
 * @modified nww - uses SpringLayoutHelper
 */
public class WorkflowDetailsDialog extends BaseBeanEditorDialog {
    private JPanel displayPanel;
    private JLabel label3;
    private JTextField nameField;
    private JTextArea descTextArea;
    public WorkflowDetailsDialog(Component parentComponent) {
        super(parentComponent);
        this.setSize(325, 150);
    
        this.setTitle("Workflow Details");
        this.pack();
	}
    
    public void setWorkflow(Workflow w) {
        this.setTheBean(w);
	    	nameField.setText(w == null ? "" : w.getName());
	        descTextArea.setText(w == null ? "" : w.getDescription());
    }
	
    /**
	 * This method initializes jContentPane
     * @param descTextArea
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getDisplayPanel() {
		if(displayPanel == null) {
			displayPanel = new JPanel();
			displayPanel.setLayout(new BorderLayout());			
						
	        /* Create query components. */
	    	JLabel label1 = new JLabel("Name: ");	    	
	    	JLabel label2 = new JLabel("Description: ");
	    	label3 = new JLabel("");
	    	
	    	nameField = new JTextField();
	    	nameField.setEditable(true);
	    	nameField.setFocusable(true);
	        descTextArea = new JTextArea(3,7);
	        descTextArea.setEditable(true);
			JScrollPane scrollPane = new JScrollPane(descTextArea);
	        JPanel b = getDetailsBox();
	        b.add(label1);	
	        b.add(nameField);
	        b.add(label3);
	        b.add(label2);
	        b.add(scrollPane);
	        b.add(new JLabel(""));
	        SpringLayoutHelper.makeCompactGrid(b, 2,3,5,5,5,5);
          
	        displayPanel.add(b, BorderLayout.CENTER);
		}
		return displayPanel;
	}

   
    private JPanel detailsBox;
	private JPanel getDetailsBox() {
		if (detailsBox == null){
			detailsBox = new JPanel(new SpringLayout());
			detailsBox.setBorder(BorderFactory.createTitledBorder("Enter details:")); 
		}
		return detailsBox;
	}	

    protected void validateInput() {

                if (nameField.getText().length() <= 0) {
                	//need valid workflow - name is required field, but test length rather than validating whole document
                	label3.setForeground(Color.red);
                	label3.setText("* required");
                } else {
                    Workflow w = getWorkflow();
                    w.setName(nameField.getText());
                    w.setDescription(descTextArea.getText());                    
                    accept();
                }
    }

    protected void resetWarnings() {
        label3.setText("");
    }
    public Workflow getWorkflow() {
    	return (Workflow)getTheBean();
    }
    
}  
