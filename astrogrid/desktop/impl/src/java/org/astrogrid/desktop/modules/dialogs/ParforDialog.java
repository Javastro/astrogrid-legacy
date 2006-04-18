/* ParforDialog.java
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
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Sequence;

/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 * Simple dialog that displays and 
 * allows entry of Parfor attributes 
 */
public class ParforDialog extends BaseBeanEditorDialog  {
    private JPanel displayPanel;
	private JTextField varField, itemField;
	private JLabel  label3, label4;
    private final Parfor testingFor;

    public ParforDialog(Component parentComponent) {
        super(parentComponent);
        testingFor = new Parfor();
        testingFor.setActivity(new Sequence());        
		this.setTitle("Edit Parallel For loop");
		this.setSize(375,150);
        this.pack();
	}

    public void setParfor(Parfor f) {
        setTheBean(f);
        varField.setText(f == null ? "" : f.getVar());
        itemField.setText(f == null ? "" : f.getItems());        
    }       
        
    
	public JPanel getDisplayPanel() {
		if (displayPanel == null) {
			displayPanel = new JPanel();
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
        testingFor.setVar(varField.getText());
        testingFor.setItems(itemField.getText());
        String regex = "[0-9]";
        Pattern pattern = Pattern.compile(regex);
        String target = "";
        if (testingFor.getVar().length() >= 1)
            target += testingFor.getVar().charAt(0);
        Matcher matcher = pattern.matcher(target);
        if ((testingFor.getVar().length() <= 0 || matcher.matches()) ||
             testingFor.getItems().length() <= 0) {
            if (testingFor.getVar().length() <= 0) {
                label3.setForeground(Color.red);
                label3.setText("* required");
            } else if (matcher.matches()) {
                label3.setForeground(Color.red);
                label3.setText("* invalid");
            }else {
                label3.setText("");
            }
            if (testingFor.getItems().length() <= 0) {
                label4.setForeground(Color.red);
                label4.setText("* required");
            } else {
                label4.setText("");
            }                          
        } else {
            Parfor f = getParfor();
            f.setVar(varField.getText());
            f.setItems(itemField.getText());            
            accept();
        }
    }
    
    protected void resetWarnings() {
        label3.setText("");
        label4.setText("");
    }
    public Parfor getParfor() {
    	return (Parfor)getTheBean();
    }
}
