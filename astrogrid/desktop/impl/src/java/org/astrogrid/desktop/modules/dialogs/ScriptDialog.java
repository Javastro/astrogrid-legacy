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

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

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
public class ScriptDialog extends JDialog implements PropertyChangeListener {
    private JOptionPane jOptionPane;
	private JEditTextArea resultDisplay = null;
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
            resultDisplay.setEditable(true);
		}
		return resultDisplay;
	}


    
    public ScriptDialog(Component parentComponent) {
        initialize();
        setLocationRelativeTo(parentComponent);
    }
    
    public void setText(String message) {
        getResultDisplay().setText(message);
        getResultDisplay().setCaretPosition(0);
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (isVisible()
         && (e.getSource() == jOptionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = jOptionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            jOptionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            if (JOptionPane.OK_OPTION == ((Integer)value).intValue()) {
                editedScript = getResultDisplay().getText();
                    resetAndHide();                
            } else { //user closed dialog or clicked cancel           
                editedScript = null;
                resetAndHide();
            }
        }
    }
    
    public void resetAndHide() {
        setVisible(false);
        getResultDisplay().setText("");        
    }
    
    private String editedScript = null;
    public String getEditedScript() {
        return editedScript;
    }
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle("Edit Script");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
            /*
             * Instead of directly closing the window,
             * we're going to change the JOptionPane's
             * value property.
             */
                jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
        }
    });                
        this.setModal(true);
		this.setSize(600,400);
		this.setContentPane(getJOptionPane());
	}
    
    private JOptionPane getJOptionPane() {
        if (jOptionPane == null) {
            jOptionPane = new JOptionPane(getResultDisplay(),JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
            jOptionPane.addPropertyChangeListener(this);
        }
        return jOptionPane;
    }
	
}
