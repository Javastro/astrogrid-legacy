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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.astrogrid.desktop.modules.ui.comp.SpringLayoutHelper;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author pjn3
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StepDialog extends BaseBeanEditorDialog {
	private JPanel displayPanel = null;
	private JTextField nameField, descField, varField, taskField, ifaceField;
	private JLabel  label6, label7;
	
    public StepDialog(Component parentComponent) {
        super(parentComponent);
        
		this.setTitle("Edit Step");
       
		this.setSize(375,225);
		this.pack();
	}

    public void setStep(Step s) {
        setTheBean(s);
        
        nameField.setText(s == null ? "" : s.getName());
        descField.setText(s == null ? "" : s.getDescription());
        varField.setText(s == null ? "" : s.getResultVar());
        if (s != null && s.getTool() != null) {
            Tool t = s.getTool();
            taskField.setText(t.getName());
            ifaceField.setText(t.getInterface());
        } else {
            taskField.setText("");
            ifaceField.setText("");
        }
            
    }
    
	public JPanel getDisplayPanel() {
		if (displayPanel == null) {
			displayPanel = new JPanel();
			JLabel label1 = new JLabel("Step name: ", JLabel.TRAILING);
			JLabel label2 = new JLabel("Step description: ", JLabel.TRAILING);
			JLabel label3 = new JLabel("Variable name: ", JLabel.TRAILING);
			JLabel label4 = new JLabel("Task name: ", JLabel.TRAILING);
			JLabel label5 = new JLabel("Interface name: ", JLabel.TRAILING);
			label6 = new JLabel("");
			label7 = new JLabel("");
				   
			nameField = new JTextField(20);
			nameField.setFocusable(true);
			nameField.setEditable(true);
			descField = new JTextField(20);
			descField.setEditable(true);
			varField = new JTextField(20);
			varField.setEditable(true);
			taskField = new JTextField(20);
			taskField.setEditable(false);
			ifaceField = new JTextField(20);
			ifaceField.setEditable(false);


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
	
	
       protected void validateInput() {

            	String regex = "[0-9]";
            	Pattern pattern = Pattern.compile(regex);
            	String target = "";
            	if (varField.getText().length() >= 1)
            		target += varField.getText().charAt(0);
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
                } else {
                    Step s = getStep();
                    s.setName(nameField.getText());
                    s.setDescription(descField.getText());
                    s.setResultVar(varField.getText());                    
                    accept();
                }
            } 

    public Step getStep() {
        return (Step)getTheBean();
    }
    
    protected void resetWarnings() {
        label6.setText("");    
        label7.setText("");          
    } 
}
