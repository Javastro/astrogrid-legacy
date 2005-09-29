/* WhileDialog.java
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

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.astrogrid.desktop.modules.ui.SpringLayoutHelper;
import org.astrogrid.workflow.beans.v1.While;

/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 * Simple dialog that displays and 
 * allows entry of While attributes 
 */
public class WhileDialog extends JDialog implements PropertyChangeListener {
	private JOptionPane jOptionPane = null;
	private JPanel displayPanel = null;
	private JTextField testField;
	private JLabel label1, label2;
	private While whileObj, editedWhile;

    public WhileDialog(Component parentComponent, While w) {
        whileObj = w;
        editedWhile = w;
        setLocationRelativeTo(parentComponent);
        initialize();
    }
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle("Edit While");
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
		this.setSize(375,120);
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
			label1 = new JLabel("Test: ", JLabel.TRAILING);
			testField = new JTextField(20);
			testField.setEditable(true);
			testField.setText(whileObj.getTest());
			label2 = new JLabel("");
			
	    	JPanel p = new JPanel(new SpringLayout());
	    	p.add(label1);
	    	p.add(testField);
	    	p.add(label2);
	    	
			SpringLayoutHelper.makeCompactGrid(p, 1,3,2,2,10,3);			
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
            	editedWhile.setTest(testField.getText());
                if (editedWhile.getTest().length() <= 0) {
                	//need valid while - test required field, check length as Sequence is required as well
                	label2.setForeground(Color.red);
                	label2.setText("* required");
                	return;
                }
            	resetAndHide();
            	editedWhile = null;
            } else { //user closed dialog or clicked cancel           
                editedWhile = null;
                resetAndHide();
            }
        }
    }
    
    public void resetAndHide() {
        setVisible(false);        
    }   
    public While getEditableWhile() {
    	return editedWhile;
    }
}
