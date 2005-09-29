/* StepDialog.java
 * Created on 14-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.astrogrid.desktop.modules.ui.SpringLayoutHelper;
import org.astrogrid.workflow.beans.v1.Step;

/**
 * @author pjn3
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StepDialog extends JDialog implements PropertyChangeListener {
	private JOptionPane jOptionPane = null;
	private JPanel displayPanel = null;
	private JTextField nameField, descField, varField, taskField, ifaceField;
	private Step step, editedStep;
	private JLabel label1, label2, label3, label4, label5, label6, label7;
	
    public StepDialog(Component parentComponent, Step s) {
    	step = s;
    	editedStep = s;
    	setLocationRelativeTo(parentComponent);
        initialize();        
    }
    
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle("Edit Step");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
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
        this.setModal(true);
		this.setSize(375,225);
		this.setContentPane(getJOptionPane());
		this.setVisible(true);		
	}
	
    private JOptionPane getJOptionPane() {
        if (jOptionPane == null) {
            jOptionPane = new JOptionPane(getDisplayPanel(),JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
            jOptionPane.addPropertyChangeListener(this);
        }
        return jOptionPane;
    }
    
	public JPanel getDisplayPanel() {
		if (displayPanel == null) {
			displayPanel = new JPanel();
			label1 = new JLabel("Step name: ", JLabel.TRAILING);
			label2 = new JLabel("Step description: ", JLabel.TRAILING);
			label3 = new JLabel("Variable name: ", JLabel.TRAILING);
			label4 = new JLabel("Task name: ", JLabel.TRAILING);
			label5 = new JLabel("Interface name: ", JLabel.TRAILING);
			label6 = new JLabel("");
			label7 = new JLabel("");
				   
			nameField = new JTextField(20);
			nameField.setText(step.getName());
			nameField.setEditable(true);
			descField = new JTextField(20);
			descField.setText(step.getDescription());
			descField.setEditable(true);
			varField = new JTextField(20);
			varField.setText(step.getResultVar());
			varField.setEditable(true);
			taskField = new JTextField(20);
			taskField.setEditable(false);
			ifaceField = new JTextField(20);
			ifaceField.setEditable(false);
			if (step.getTool() != null) {
				taskField.setText(step.getTool().getName());
				ifaceField.setText(step.getTool().getInterface());
			}

	    	JPanel p = new JPanel(new SpringLayout());
	    	p.add(label1);
	    	p.add(nameField);
	    	p.add(label6);
	    	p.add(label2);
	    	p.add(descField);
	    	p.add(new JLabel(""));
	    	p.add(label3);
	    	p.add(varField);
	    	p.add(label7);
	    	p.add(label4);
	    	p.add(taskField);
	    	p.add(new JLabel(""));
	    	p.add(label5);
	    	p.add(ifaceField);
	    	p.add(new JLabel(""));
	    	
			SpringLayoutHelper.makeCompactGrid(p, 5,3,2,2,10,3);
			
			displayPanel.add(p);
		}
		return displayPanel;
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
            	editedStep.setName(nameField.getText());
            	editedStep.setDescription(descField.getText());
            	editedStep.setResultVar(varField.getText());
            	String regex = "[0-9]";
            	Pattern pattern = Pattern.compile(regex);
            	String target = "";
            	if (editedStep.getResultVar().length() >= 1)
            		target += editedStep.getResultVar().charAt(0);
            	Matcher matcher = pattern.matcher(target);
                if (nameField.getText().length() <= 0 ||
                	(varField.getText().length() >= 1 && matcher.matches())) {
                	if (nameField.getText().length() <= 0) {//need valid set - name is a required field, as is Tool but that is added in next dialog                	
                		label6.setForeground(Color.red);
                		label6.setText("* required");
                	} else {
                		label6.setText("");
                	}
                	if (varField.getText().length() >= 1 && matcher.matches()) {
                		label7.setForeground(Color.red);
                		label7.setText("* invalid");
                	} else {
                		label7.setText("");
                	}
                return;
                }
                resetAndHide();
            } else { //user closed dialog or clicked cancel           
                editedStep = null;
                resetAndHide();
            }
        }
	}
    

    public Step getEditedStep() {
        return editedStep;
    }
    
    public void resetAndHide() {
        setVisible(false);
    }   
}
