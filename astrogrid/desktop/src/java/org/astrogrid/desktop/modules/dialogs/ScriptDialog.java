/*
 * Created on 10-May-2005
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
/**
 * Simple dialog that displays script body
 * @author Phil Nicolson pjn3@star.le.ac.uk
 *
 */
public class ScriptDialog extends JDialog {

	private javax.swing.JPanel jContentPane = null;
	private JEditorPane resultDisplay = null;
	private JButton okButton = null;
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JEditorPane getResultDisplay() {
		if (resultDisplay == null) {
			resultDisplay = new JEditorPane();
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
			okButton.setText("Close");
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {                    
                    dispose();
                }
            });
		}
		return okButton;
	}
      public static void main(String[] args) {
          (new ResultDialog()).show();
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
		this.setSize(635,300);
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
			JScrollPane jScrollPane = new JScrollPane(getResultDisplay(),
                                                      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jContentPane.add(jScrollPane,BorderLayout.CENTER);
			jContentPane.add(getOkButton(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}
}
