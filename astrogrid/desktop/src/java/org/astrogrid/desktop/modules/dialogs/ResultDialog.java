/*$Id: ResultDialog.java,v 1.3 2005/05/12 15:59:08 clq2 Exp $
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

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.text.JTextComponent;
/**
 * Simple dialog that displays a result in a text box - which allows for cutting / copying
 * the result into something else.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-May-2005
 *
 */
public class ResultDialog extends JDialog {

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
            resultDisplay.setContentType("text/html");
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
          (new ResultDialog()).show();
    }
	/**
	 * This is the default constructor
	 */
	public ResultDialog() {
		super();
		initialize();
	}
    
    public ResultDialog(Component parentComponent, Object message) {
        this();
        getResultDisplay().setText(message.toString());
        setLocationRelativeTo(parentComponent);
    }
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle("Result");
		//this.setModal(true);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(400,200);
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
            jContentPane.add(new JScrollPane(getResultDisplay()),BorderLayout.CENTER);
			jContentPane.add(getOkButton(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}
}


/* 
$Log: ResultDialog.java,v $
Revision 1.3  2005/05/12 15:59:08  clq2
nww 1111 again

Revision 1.1.2.2  2005/05/11 14:25:25  nw
javadoc, improved result transformers for xml

Revision 1.1.2.1  2005/05/11 10:59:05  nw
made results selectable, so can be copied and pasted.
 
*/