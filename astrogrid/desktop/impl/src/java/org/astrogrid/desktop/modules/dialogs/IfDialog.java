/* IfDialog.java
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
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.astrogrid.desktop.modules.ui.comp.SpringLayoutHelper;
import org.astrogrid.workflow.beans.v1.If;

/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 * Simple dialog that displays and 
 * allows entry of If attributes 
 */
public class IfDialog extends BaseBeanEditorDialog implements PropertyChangeListener {
    private JPanel displayPanel;
	private JTextField testField;
	private JLabel label1, label2;

    public void setIf(If i) {
        setTheBean(i);
        testField.setText(i == null ? "" : i.getTest());
    }
    
    public IfDialog(Component parentComponent) {
        super(parentComponent);
        this.setTitle("Edit If");
        this.setSize(375,120);
        this.pack();
    }

    
	public JPanel getDisplayPanel() {
		if (displayPanel == null) {
			displayPanel = new JPanel();
			label1 = new JLabel("Test: ", JLabel.TRAILING);
			testField = new JTextField(20);
			testField.setEditable(true);
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
	
	protected void validateInput() {

                if (testField.getText().length() <= 0) {
                	//need valid if - test required field
                	label2.setForeground(Color.red);
                	label2.setText("* required");
                } else {
                    If i = getIf();
                    i.setTest(testField.getText());                    
                    accept();
                }
    }    
    


    protected void resetWarnings() {
        label2.setText("");       
    }
    
    public If getIf() {
    	return (If)getTheBean();
    }

}
