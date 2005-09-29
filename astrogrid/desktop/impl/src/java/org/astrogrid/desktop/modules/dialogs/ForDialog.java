/* ForDialog.java
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
import org.astrogrid.workflow.beans.v1.For;

/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 * Simple dialog that displays and 
 * allows entry of For attributes 
 */
public class ForDialog extends JDialog implements PropertyChangeListener {
	private JOptionPane jOptionPane = null;
	private JPanel displayPanel = null;
	private JTextField varField, itemField;
	private For forObj, editedFor;
	private JLabel label1, label2, label3, label4;
	
    public ForDialog(Component parentComponent, For f) {
    	forObj = f;
    	editedFor = f;
    	setLocationRelativeTo(parentComponent);
        initialize();        
    }
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle("Edit For");
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
		this.setSize(375,150);
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
			JLabel label1 = new JLabel("Variable: ", JLabel.TRAILING);
			JLabel label2 = new JLabel("Items: ", JLabel.TRAILING);
			varField = new JTextField(20);
			varField.setEditable(true);
			varField.setText(forObj.getVar());
			itemField = new JTextField(20);
			itemField.setEditable(true);
			itemField.setText(forObj.getItems());
			label3 = new JLabel("");
			label4 = new JLabel("");
			
	    	JPanel p = new JPanel(new SpringLayout());
	    	p.add(label1);
	    	p.add(varField);
	    	p.add(label3);
	    	p.add(label2);
	    	p.add(itemField);
	    	p.add(label4);
	    	
			SpringLayoutHelper.makeCompactGrid(p, 2,3,2,2,10,3);			
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
            	editedFor.setVar(varField.getText());
            	editedFor.setItems(itemField.getText());
            	String regex = "[0-9]";
            	Pattern pattern = Pattern.compile(regex);
            	String target = "";
            	if (editedFor.getVar().length() >= 1)
            		target += editedFor.getVar().charAt(0);
            	Matcher matcher = pattern.matcher(target);
                if (!editedFor.isValid()) {
                	//need valid For - var and items required fields, var is of type: xsd:NCName
                	if (varField.getText().length() <= 0 ) {
                    	label3.setForeground(Color.red);
                    	label3.setText("* required");
                	} else if (matcher.matches()) {
                    	label3.setForeground(Color.red);
                    	label3.setText("* invalid");
                	} else {
                		label3.setText("");
                	}
                	if (itemField.getText().length() <= 0) {
                    	label4.setForeground(Color.red);
                    	label4.setText("* required");
                	} else {
                		label4.setText("");
                	}
                	return;
                }
                resetAndHide();
            } else { //user closed dialog or clicked cancel 
                resetAndHide();
                editedFor = null;                
            }
        }
    }    
    
    public void resetAndHide() {
        setVisible(false);        
    }  
    public For getEditedFor() {
        return editedFor;
    }   
}
