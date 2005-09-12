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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import jedit.JEditTextArea;
import jedit.JavaTokenMarker;
import jedit.SyntaxDocument;
/**
 * Simple dialog that displays script body
 * the result into something else.
 * @author Phil Nicolson pjn3@star.le.ac.uk 12/8/05
 * 
 *@modified nww - display results in a jedit box - gives syntax coloring.
 *@todo maybe rewrite to just embed a scriptPanel in a dialog?
 */
public class ScriptDialog extends JDialog {

	private javax.swing.JPanel jContentPane = null;
	private JEditTextArea resultDisplay = null;
	private JButton okButton = null;
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JEditTextArea getResultDisplay() {
		if (resultDisplay == null) {
			resultDisplay = new JEditTextArea();
            resultDisplay.setDocument(new SyntaxDocument()); // prevents aliasing between jeditors.
            resultDisplay.setTokenMarker(new JavaTokenMarker());
            resultDisplay.setEditable(false);
		}
		return resultDisplay;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("Ok");
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {                    
                    dispose();
                }
            });
		}
		return okButton;
	}
      public static void main(String[] args) {
          (new ScriptDialog()).show();
    }
	/**
	 * This is the default constructor
	 */
	public ScriptDialog() {
		super();
		initialize();
	}
    
    public ScriptDialog(Component parentComponent, Object message) {
        this();
        getResultDisplay().setText(message.toString());
        getResultDisplay().setCaretPosition(0);
        setLocationRelativeTo(parentComponent);
    }
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle("Script body");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(600,400);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getResultDisplay(),BorderLayout.CENTER);
			jContentPane.add(getOkButton(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}
}
