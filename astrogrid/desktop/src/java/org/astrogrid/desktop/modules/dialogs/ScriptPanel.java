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
/**
 * @author Phil Nicolson pjn3@star.le.ac.uk
 *
 */
public class ScriptPanel extends JPanel {
	
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ScriptPanel.class);	

	private JLabel jLabel = null;
	private JTextField jTextField = null;
	private JPanel jContentPane, descriptionPanel, bodyPanel = null;
	private JEditorPane jEditorPane = null;
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
            jContentPane.add(getDescriptionPanel(), BorderLayout.NORTH);
		    jContentPane.add(getBodyPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
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
	private JPanel getBodyPanel() {
		if (bodyPanel == null) {
			bodyPanel = new JPanel();
			
			jEditorPane = new JEditorPane();
			//jEditorPane.setMinimumSize(new java.awt.Dimension(780,180));
			jEditorPane.setText(body);
			jEditorPane.setCaretPosition(0);
			jEditorPane.setEditable(false);
			
			JScrollPane pane = new JScrollPane(jEditorPane,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			pane.setPreferredSize(new Dimension(765,175));			

			bodyPanel.add(pane);
		}
		return bodyPanel;
	}
  }
