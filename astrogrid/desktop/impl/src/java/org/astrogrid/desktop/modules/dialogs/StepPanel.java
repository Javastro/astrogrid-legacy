/* StepInfoPanel.java
 * Created on 07-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.desktop.modules.ui.SpringLayoutHelper;
import org.astrogrid.workflow.beans.v1.Step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 *@modified nww - removed superfluous panes, calls to springLayoutHelper.
 *@todo change into a dialog - see script dialog.
 */
public class StepPanel extends JPanel {

	private JTextField nameField, varField, taskField, interfaceField = null;
	private JEditorPane descPane = null;

	private String description;
	private String name;
	private String var;
	private String task = "--";
	private String inter = "--";
	/**
	 * production constructor
	 */
	public StepPanel(Step step) {
		super();
		this.description = step.getDescription();
		this.name = step.getName();
		this.var = step.getResultVar();
		if (step.getTool() != null) {
			this.task = step.getTool().getName();
			this.inter = step.getTool().getInterface();			
		}
		initialize();
	}

	/**
	 * This method initializes body panel
	 * 	
	 * @return javax.swing.JPanel
	 */    
	private void initialize() {
			
			JLabel label1 = new JLabel("Step name: ", JLabel.TRAILING);
			JLabel label2 = new JLabel("Step description: ", JLabel.TRAILING);
			JLabel label3 = new JLabel("Variable name: ", JLabel.TRAILING);
			JLabel label4 = new JLabel("Task name: ", JLabel.TRAILING);
			JLabel label5 = new JLabel("Interface name: ", JLabel.TRAILING);
			
	    	nameField = new JTextField(name);
	    	nameField.setEditable(false);
	    	descPane = new JEditorPane();
	    	descPane.setEditable(false);
	    	descPane.setText(description);
	    	descPane.setCaretPosition(0);
	    	descPane.setPreferredSize(new Dimension(400,80));
	    	
	    	JScrollPane pane = new JScrollPane(descPane);
	    	pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    	pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    	varField = new JTextField(var);
	    	varField.setEditable(false);
	    	taskField = new JTextField(task);
	    	taskField.setEditable(false);
	    	taskField.setToolTipText("Select the task tab to see task details");
	    	interfaceField = new JTextField(inter);
	    	interfaceField.setEditable(false);
	    	interfaceField.setToolTipText("Select the task tab to see task details");
	    	
	    	JPanel p =new JPanel(new SpringLayout());
	    	p.add(label1);
	    	p.add(nameField);
	    	p.add(label2);
	    	p.add(pane);
	    	p.add(label3);
	    	p.add(varField);
	    	p.add(label4);
	    	p.add(taskField);
	    	p.add(label5);
	    	p.add(interfaceField);
	    	
			SpringLayoutHelper.makeCompactGrid(p, 5,2,2,2,10,3);
            this.add(p);
			this.setPreferredSize(new Dimension(800,200));

	}
	


  }
