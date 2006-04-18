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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.astrogrid.desktop.modules.ui.SpringLayoutHelper;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.Sequence;

/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 * Simple dialog that displays and 
 * allows entry of For attributes 
 */
public class ForDialog extends BaseBeanEditorDialog  {

    private JPanel displayPanel;
	private JTextField varField, itemField;
    private JLabel label3, label4;
    private final For testingFor;
	
    public void setFor(For f) {
        setTheBean(f);
        varField.setText(f == null ? "" : f.getVar());
        itemField.setText(f == null ? "" : f.getItems());        
    }       
    
    public ForDialog(Component parentComponent) {
        super(parentComponent);
        testingFor = new For();
        testingFor.setActivity(new Sequence());
		this.setTitle("Edit For");
		this.setSize(375,150);
		this.pack();
	}	
    
	public JPanel getDisplayPanel() {
		if (displayPanel == null) {
			displayPanel = new JPanel();
			JLabel label1 = new JLabel("Variable: ", JLabel.TRAILING);
			JLabel label2 = new JLabel("Items: ", JLabel.TRAILING);
			varField = new JTextField(20);
			varField.setEditable(true);
			varField.setFocusable(true);
			itemField = new JTextField(20);
			itemField.setEditable(true);
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
	 

    protected void validateInput() {

        String regex = "[0-9]";
        Pattern pattern = Pattern.compile(regex);
        String target = "";
        testingFor.setVar(varField.getText());
        testingFor.setItems(itemField.getText());        
        if (testingFor.getVar().length() >= 1)
        	target += testingFor.getVar().charAt(0);
        Matcher matcher = pattern.matcher(target);
        if (!testingFor.isValid()) {
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
        } else {
            For f = getFor();
            f.setVar(varField.getText());
            f.setItems(itemField.getText());            
            accept();
        }
    }



    protected void resetWarnings() {
        label3.setText("");
        label4.setText("");
    }
    public For getFor() {
        return (For)getTheBean();
    }   
}
