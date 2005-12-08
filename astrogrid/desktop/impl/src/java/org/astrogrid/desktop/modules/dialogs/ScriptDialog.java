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

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import jedit.JEditTextArea;
import jedit.JavaTokenMarker;
import jedit.SyntaxDocument;

import org.astrogrid.workflow.beans.v1.Script;
import org.codehaus.groovy.control.CompilationFailedException;
/**
 * Simple dialog that displays script activities
 * @author Phil Nicolson pjn3@star.le.ac.uk 12/8/05
 * 
 * @modified nww - display results in a jedit box - gives syntax coloring. rewrittn to use joptionpane.
 * @modified pjn - groovy validation added
 */
public class ScriptDialog extends BaseBeanEditorDialog implements ActionListener {
	private JEditTextArea scriptField;
	private JPanel displayPanel;
	private JTextField descField;
	private JButton validateButton;
	
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
            scriptField.addCaretListener(new CaretListener(){
            	public void caretUpdate(CaretEvent e) {
            		if (scriptField.getDocumentLength() >0) {
            		  validateButton.setEnabled(true);
            		} else {
            			validateButton.setEnabled(false);
            		}
            	}
            });
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
		validateButton = new JButton();
		validateButton.setText("Validate script");
		validateButton.setToolTipText("<html>Check if script is valid groovy script. <br>(Scripts are validated in isolation to the workflow document)</html>");
		validateButton.setEnabled(false);
		validateButton.addActionListener(this);
		p.add(label);
		p.add(descField);
		p.add(validateButton);
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

    /**
     * 
     */
    public void actionPerformed(ActionEvent e) {
    	try{
    		Binding binding = new Binding();
    		GroovyShell shell = new GroovyShell(binding);
    		Object value = shell.evaluate(getScriptField().getText());
    		JOptionPane.showMessageDialog(this, 
    				                      "Your script appears to be valid",
										  "Script appears valid", 
										  JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (CompilationFailedException ex ) {
        	showError("Your script may contain errors",ex);
        }
    	catch (MissingMethodException ex)  {
    		showError("Your script may contain errors",ex);
    	}
    	catch (MissingPropertyException ex) {
    		JOptionPane.showMessageDialog(this, 
                                          "<html>Your script appears valid, assuming the following variable is set <br>elsewhere in your workflow document (if not please add a 'SET' activity..)<br><br>" +ex + "</html>" ,
					                      "Script appears valid", 
					                      JOptionPane.INFORMATION_MESSAGE);
    	}
    }
    
    /**
     * @param s
     * @throws HeadlessException
     */
    protected void showError(String s, Exception ex) throws HeadlessException {
        JOptionPane.showMessageDialog(this,ex,s,JOptionPane.ERROR_MESSAGE);
    }
}
