/*
 * Created on 12/8/2005
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jedit.JEditTextArea;
import jedit.JavaTokenMarker;
import jedit.SyntaxDocument;
/**
 * Simple dialog that displays script body
 * the result into something else.
 * @author Phil Nicolson pjn3@star.le.ac.uk 12/8/05
 * 
 *@modified nww - display results in a jedit box - gives syntax coloring. rewrittn to use joptionpane.
 */
public class ScriptDialog extends BaseBeanEditorDialog {
	private JEditTextArea scriptField;
	private JPanel displayPanel;
	private JTextField descField;
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JEditTextArea getScriptField() {
		if (scriptField == null) {
			scriptField = new JEditTextArea();
            scriptField.setDocument(new SyntaxDocument()); // prevents aliasing between jeditors.
            scriptField.setTokenMarker(new JavaTokenMarker());
            scriptField.setEditable(true);
		}
		return scriptField;
	}
	
	public JPanel getDisplayPanel() {
		if (displayPanel == null) {
			displayPanel = new JPanel();
            displayPanel.setLayout(new BorderLayout());
			displayPanel.add(getScriptField(), BorderLayout.CENTER);
			displayPanel.add(getDescriptionPanel(), BorderLayout.NORTH);
		}
		return displayPanel;
	}
	
	private JPanel getDescriptionPanel() {
		JPanel p = new JPanel(new FlowLayout());
		JLabel label = new JLabel("Description:  ");
		descField = new JTextField(30);
		p.add(label);
		p.add(descField);
		p.setBorder(BorderFactory.createEtchedBorder());
		return p;
	}	


    
    public ScriptDialog(Component parentComponent) {
        super(parentComponent);
        this.setTitle("Edit Script");
        this.setSize(685,570);
        this.pack();
        
    }

    
    protected void validateInput() {
        // no validation here at present.
        Script s = getScript();
        s.setBody(getScriptField().getText());
        s.setDescription(descField.getText());
        accept();
    }
    
    public void setScript(Script s) {
        setTheBean(s);
        getScriptField().setText(s.getBody());
        getScriptField().setCaretPosition(0);
        descField.setText(s.getDescription());        
    }
    
    public Script getScript() {
        return (Script)getTheBean();
    }

    

	
}
