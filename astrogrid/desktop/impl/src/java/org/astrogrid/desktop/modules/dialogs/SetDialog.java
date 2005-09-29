/* SetDialog.java
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
import org.astrogrid.workflow.beans.v1.Set;

/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 * Simple dialog that displays and 
 * allows entry of Set attributes 
 */
public class SetDialog extends JDialog implements PropertyChangeListener {
	private JOptionPane jOptionPane = null;
	private JPanel displayPanel = null;
	private JTextField varField, valField;
	private Set set, editedSet;
	private JLabel label1, label2, label3;

    public SetDialog(Component parentComponent, Set s) {
    	set = s;
    	editedSet = s;        
        setLocationRelativeTo(parentComponent);
        initialize();
    }
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle("Edit Set");
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
			label1 = new JLabel("Variable: ", JLabel.TRAILING);
			label2 = new JLabel("Value: ", JLabel.TRAILING);
			label3 = new JLabel("");
			varField = new JTextField(20);
			varField.setEditable(true);
			varField.setText(set.getVar());
			valField = new JTextField(20);
			valField.setEditable(true);
			valField.setText(set.getValue());
			
	    	JPanel p = new JPanel(new SpringLayout());
	    	p.add(label1);
	    	p.add(varField);
	    	p.add(label3);
	    	p.add(label2);
	    	p.add(valField);
	    	p.add(new JLabel());
	    	
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
            	editedSet.setVar(varField.getText());
            	editedSet.setValue(valField.getText());
            	String regex = "[0-9]";
            	Pattern pattern = Pattern.compile(regex);
            	String target = "";
            	if (editedSet.getVar().length() >= 1)
            		target += editedSet.getVar().charAt(0);
            	Matcher matcher = pattern.matcher(target);
                if (!editedSet.isValid()) { //need valid set - var required field, and is NCNAME
                	if (editedSet.getVar().length() <= 0) {
                    	label3.setForeground(Color.red);
                    	label3.setText("* required");                		
                	}
                	else if (matcher.matches()) {
                    	label3.setForeground(Color.red);
                    	label3.setText("* invalid");
                	}
                	return;
                }
                resetAndHide();
            } else { //user closed dialog or clicked cancel 
            	editedSet = null;
                resetAndHide();
            }
        }
    }
    
    public void resetAndHide() {
        setVisible(false);        
    } 
    
    public Set getEditedSet() {
        return editedSet;
    }
}
