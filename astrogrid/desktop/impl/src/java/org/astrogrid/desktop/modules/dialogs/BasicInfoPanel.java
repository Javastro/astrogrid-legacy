/* ScriptInfoPanel.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 *@modified nww - moved spring layout stuff into a separate class, and shared it with workflow builder.
 *@todo change it into a dialog. see script dialog.
 */
public class BasicInfoPanel extends JPanel {
	
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BasicInfoPanel.class);	

	private JTextField nameField, valueField = null;
	private JPanel jContentPane, bodyPanel = null;
	private String[] values;
    /**
     * Production constructor 
     * @param values array of label/value pairs
     */
	public BasicInfoPanel(String[] values) {
		super();
		this.values = values;
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {	
		this.add(getJContentPane());
	}
    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
		    jContentPane.add(getBodyPanel());
		}
		return jContentPane;
	}
	/**
	 * This method initializes body panel
	 * 	
	 * @return javax.swing.JPanel
	 */    
	private JPanel getBodyPanel() {
		if (bodyPanel == null) {
			bodyPanel = new JPanel();			
	    	
	    	JPanel p = new JPanel(new SpringLayout());
	    	int numPairs = values.length/2;
	    	
	    	for (int i = 0; i < values.length; i++) {
	    		JLabel l = new JLabel(values[i].trim(), JLabel.TRAILING);
	    		p.add(l);
	    		JTextField t = new JTextField(values[i+1]);
	    		t.setEditable(false);
	    		p.add(t);
	    		i = i +1;
	    	}
	    	
			SpringLayoutHelper.makeCompactGrid(p, numPairs,2,2,2,10,3);
			bodyPanel.add(p);
		}
		return bodyPanel;
	}
	
   
  }
