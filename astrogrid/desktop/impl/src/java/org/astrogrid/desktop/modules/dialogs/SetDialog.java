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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
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
public class SetDialog extends BaseBeanEditorDialog {
	private JPanel displayPanel = null;
	private JTextField varField, valField;
	private JLabel label1, label2, label3;
    private final Set testingSet;

    public SetDialog(Component parentComponent) {
        super(parentComponent);
        testingSet = new Set();
		this.setTitle("Edit Set");   
		this.setSize(375,150);
		this.pack();
	}
	
    public void setSet(Set s) {
        setTheBean(s);
        varField.setText(s == null ? "" :s.getVar());        
        valField.setText(s == null ? "" : s.getValue());
    }
    
	public JPanel getDisplayPanel() {
		if (displayPanel == null) {
			displayPanel = new JPanel();
			label1 = new JLabel("Variable: ", JLabel.TRAILING);
			label2 = new JLabel("Value: ", JLabel.TRAILING);
			label3 = new JLabel("");
			varField = new JTextField(20);
			varField.setEditable(true);
			varField.setFocusable(true);
			valField = new JTextField(20);
			valField.setEditable(true);

			
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
	
	   protected void validateInput() {
            	testingSet.setVar(varField.getText());
            	testingSet.setValue(valField.getText());
            	String regex = "[0-9]";
            	Pattern pattern = Pattern.compile(regex);
            	String target = "";
            	if (testingSet.getVar().length() >= 1)
            		target += testingSet.getVar().charAt(0);
            	Matcher matcher = pattern.matcher(target);
                if (!testingSet.isValid()) { //need valid set - var required field, and is NCNAME
                	if (testingSet.getVar().length() <= 0) {
                    	label3.setForeground(Color.red);
                    	label3.setText("* required");                		
                	}
                	else if (matcher.matches()) {
                    	label3.setForeground(Color.red);
                    	label3.setText("* invalid");
                	}              
                } else {
                    Set r = getSet();
                    r.setVar(varField.getText());
                    r.setValue(valField.getText());                    
                    accept();
                }
       }
    
    protected void resetWarnings() {
        label3.setText("");       
    } 
    
    public Set getSet() {
        return (Set)getTheBean();
    }
}
