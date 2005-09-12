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

import org.astrogrid.workflow.beans.v1.Script;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import jedit.JEditTextArea;
import jedit.JavaTokenMarker;
/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 * @modified nww - removed superfluous panels,  display script in a JEDit text area.
 */
public class ScriptPanel extends JPanel {
	

	private JTextField jTextField = null;
	private JPanel descriptionPanel = null;
	private JEditTextArea jEditorPane = null;
	private String description;
	private String body;
	/**
	 * production constructor
	 */
	public ScriptPanel(Script script) {
		super();
		this.description = script.getDescription();
		this.body = script.getBody();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {	
        this.setLayout(new BorderLayout());
        this.add(getDescriptionPanel(), BorderLayout.NORTH);
        this.add(getBodyPanel(), BorderLayout.CENTER);
	}
  
	/**
	 * This method initializes DescriptionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getDescriptionPanel() {
		if (descriptionPanel == null) {
			descriptionPanel = new JPanel(new FlowLayout());
			descriptionPanel.add(new JLabel("Description: "));
			descriptionPanel.add(getJTextField());
		}
		return descriptionPanel;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField(description);
			jTextField.setToolTipText("Script description (if enterred)");
			jTextField.setPreferredSize(new java.awt.Dimension(60,19));			
			jTextField.setEditable(false);
		}
		return jTextField;
	}
	/**
	 * This method initializes body panel
	 * 	
	 * @return javax.swing.JPanel
	 */    
	private JEditTextArea getBodyPanel() {
			
			jEditorPane = new JEditTextArea();
            jEditorPane.setTokenMarker(new JavaTokenMarker());
			//jEditorPane.setMinimumSize(new java.awt.Dimension(780,180));
			jEditorPane.setText(body);
			jEditorPane.setCaretPosition(0);
			jEditorPane.setEditable(false);

			jEditorPane.setPreferredSize(new Dimension(765,175));			
			return jEditorPane;
	}
  }
