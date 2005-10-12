/*$Id: BaseBeanEditorDialog.java,v 1.2 2005/10/12 13:30:10 nw Exp $
 * Created on 10-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.common.bean.BaseBean;
import org.astrogrid.workflow.beans.v1.AbstractActivity;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** Abstract base class for all pop-up dialogs used in the workflow editor.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Oct-2005
 *
 */
public abstract class BaseBeanEditorDialog extends JDialog implements PropertyChangeListener {
    private JOptionPane jOptionPane;
    private BaseBean theBean;
    
    /** set the activity object this dialog is editing
     *  - subclasses may like to provide a type-restricted version of this
     * @param a
     */
    protected final void setTheBean(BaseBean a) {
        this.theBean = a;
    }
    
    /** access the activity object this dialog is editing
     *  - subclasse may like to provide a type-restructed version of this.
     * @return
     */
    protected final BaseBean getTheBean() {
        return theBean;
    }
    
    
    public BaseBeanEditorDialog(Component parentComponent) {
        setLocationRelativeTo(parentComponent);

        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
            /*
             * Instead of directly closing the window,
             * we're going to change the JOptionPane's
             * value property.
             */
                getJOptionPane().setValue(new Integer(JOptionPane.CLOSED_OPTION));
        }            
    });  
        
        this.setModal(true);
        this.setContentPane(getJOptionPane());        
    }
    
    public final void propertyChange(PropertyChangeEvent e) {
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
                validateInput();                
            } else { //user closed dialog or clicked cancel 
                accept();
                setTheBean(null);           
            }
        }
    }   
    
    /** called when the user hits 'OK' - check that the form contains correct 
     * values, and if so, copy into 'theActivity' and  call  {@link #accept()} to close the dialog.
     *
     */
    protected abstract void validateInput();
    
    private JOptionPane getJOptionPane() {
        if (jOptionPane == null) {
            jOptionPane = new JOptionPane(getDisplayPanel(),JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
            jOptionPane.addPropertyChangeListener(this);
        }
        return jOptionPane;
    }
    
    
    /** called from {@link #validateInput()} to indicate input value is acceptable */
    protected  final void accept() {
        setVisible(false);        
       resetWarnings(); 
    }  
    
    /** extend to remove warning labels, etc */
    protected void resetWarnings() {
    }
    
    /** build the form panel */
    protected abstract JPanel getDisplayPanel();
    

}


/* 
$Log: BaseBeanEditorDialog.java,v $
Revision 1.2  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.2.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp
 
*/