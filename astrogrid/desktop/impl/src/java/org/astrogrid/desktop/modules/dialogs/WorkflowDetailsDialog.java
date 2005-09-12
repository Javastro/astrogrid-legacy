/*$Id: WorkflowDetailsDialog.java,v 1.2 2005/09/12 15:21:16 nw Exp $
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

import org.astrogrid.desktop.modules.ui.SpringLayoutHelper;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
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
import javax.swing.SpringLayout;


/**
 * Dialog for creating a new workflow
 *
 * @author   Phil Nicolson pjn3@star.le.ac.uk
 * @modified nww - uses SpringLayoutHelper
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
	        SpringLayoutHelper.makeCompactGrid(b, 2,2,5,5,5,5);
						
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

    
}  
