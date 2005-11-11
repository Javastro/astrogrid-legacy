/*$Id: ResultDialog.java,v 1.3 2005/11/11 10:08:18 nw Exp $
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
 * Simple dialog that displays a result in a text box - which allows for cutting / copying
 * the result into something else. 
 * @todo alter so that is uses a joptionPane, like everything else.
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
        getResultDisplay().setCaretPosition(0);
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
		this.setSize(565,400);
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
Revision 1.3  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.3  2005/06/02 14:34:32  nw
first release of application launcher

Revision 1.1.2.2  2005/05/11 14:25:25  nw
javadoc, improved result transformers for xml

Revision 1.1.2.1  2005/05/11 10:59:05  nw
made results selectable, so can be copied and pasted.
 
*/