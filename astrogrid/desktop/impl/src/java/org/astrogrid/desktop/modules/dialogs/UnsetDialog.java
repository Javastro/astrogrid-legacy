/* UnsetDialog.java
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.astrogrid.desktop.modules.ui.comp.SpringLayoutHelper;
import org.astrogrid.workflow.beans.v1.Unset;

/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 * Simple dialog that displays and 
 * allows entry of Unset attributes 
 */
public class UnsetDialog extends BaseBeanEditorDialog {
	private JPanel displayPanel = null;
	private JTextField varField;
	private JLabel label1, label2;
    private final Unset testingUnset;

    public UnsetDialog(Component parentComponent) {
        super(parentComponent);
        testingUnset = new Unset();
		this.setTitle("Edit Unset");            
		this.setSize(375,120);
        this.pack();
	}
	
        public void setUnset(Unset u) {
            setTheBean(u);
            varField.setText(u == null ? "" : u.getVar());            
        }
            
    
	public JPanel getDisplayPanel() {
		if (displayPanel == null) {
			displayPanel = new JPanel();
			label1 = new JLabel("Variable: ", JLabel.TRAILING);
			varField = new JTextField(20);
			varField.setEditable(true);
			varField.setFocusable(true);
			label2 = new JLabel("");			
	    	JPanel p = new JPanel(new SpringLayout());
	    	p.add(label1);
	    	p.add(varField);
	    	p.add(label2);
	    	
			SpringLayoutHelper.makeCompactGrid(p, 1,3,2,2,10,3);			
			displayPanel.add(p);
		}
		return displayPanel;
	}    
	
	
    
       protected void validateInput() {
               testingUnset.setVar(varField.getText());
            	String regex = "[0-9]";
            	Pattern pattern = Pattern.compile(regex);
            	String target = "";
            	if (testingUnset.getVar().length() >= 1)
            		target += testingUnset.getVar().charAt(0);
            	Matcher matcher = pattern.matcher(target);
                if (!testingUnset.isValid()) { //need valid unset - var required field and NCName
                	if (testingUnset.getVar().length() <= 0) {
                    	label2.setForeground(Color.red);
                    	label2.setText("* required");
                	}
                	else if (matcher.matches()) {
                    	label2.setForeground(Color.red);
                    	label2.setText("* invalid");
                	}
                } else {
                    Unset u = getUnset();
                    u.setVar(varField.getText());
                    accept();
                }
       }
    
    protected void resetWarnings() {
        label2.setText("");       
    } 
    
    public Unset getUnset() {
        return (Unset)getTheBean();
    }    
    
}
