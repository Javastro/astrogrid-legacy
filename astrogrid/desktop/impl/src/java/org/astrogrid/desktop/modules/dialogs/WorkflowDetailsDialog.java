/*$Id: WorkflowDetailsDialog.java,v 1.3 2005/09/29 17:16:40 pjn3 Exp $
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
public class WorkflowDetailsDialog extends JDialog implements PropertyChangeListener{
	
    private JPanel displayPanel, buttonBox, detailsBox = null;
    private JTextField nameField = null;
    private JTextArea descTextArea = null;
    private JOptionPane jOptionPane = null;
    private Workflow workflow, editedWorkflow;
    private JLabel label1, label2, label3;


    /**
     * Constructs a new WorkflowDetailsDialog
     * @param parentComponent
     */
    public WorkflowDetailsDialog(Component parentComponent, Workflow w) {
    	workflow = w;
    	editedWorkflow = w;
    	setLocationRelativeTo(parentComponent);
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
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
            /*
             * Instead of directly closing the window,
             * we're going to change the JOptionPane's
             * value property.
             */
                jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
        }
    });
        this.setTitle("Workflow Details");
        this.setContentPane(getJOptionPane());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true); 			
	}
	
    private JOptionPane getJOptionPane() {
        if (jOptionPane == null) {
            jOptionPane = new JOptionPane(getDisplayPanel(),JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
            jOptionPane.addPropertyChangeListener(this);
        }
        return jOptionPane;
    }	
	
    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDisplayPanel() {
		if(displayPanel == null) {
			displayPanel = new JPanel();
			displayPanel.setLayout(new BorderLayout());			
						
	        /* Create query components. */
	    	label1 = new JLabel("Name: ");	    	
	    	label2 = new JLabel("Description: ");
	    	label3 = new JLabel("");
	    	
	    	nameField = new JTextField();
	    	nameField.setEditable(true);
	    	nameField.setText(workflow.getName());
	        descTextArea = new JTextArea(3,7);
	        descTextArea.setEditable(true);
	        descTextArea.setText(workflow.getDescription());
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
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (isVisible()
         && (e.getSource() == jOptionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = jOptionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            jOptionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            if (JOptionPane.OK_OPTION == ((Integer)value).intValue()) {
            	editedWorkflow.setName(nameField.getText());
            	editedWorkflow.setDescription(descTextArea.getText());
                if (editedWorkflow.getName().length() <= 0) {
                	//need valid workflow - name is required field, but test length rather than validating whole document
                	label3.setForeground(Color.red);
                	label3.setText("* required");
                	return;
                }
            	resetAndHide();
            	editedWorkflow = null;
            } else { //user closed dialog or clicked cancel           
                editedWorkflow = null;
                resetAndHide();
            }
        }
    }
    public void resetAndHide() {
        setVisible(false);        
    }   
    public Workflow getEditableWorkflow() {
    	return editedWorkflow;
    }
    
}  
