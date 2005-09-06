/*$Id: QueryEditorDialog.java,v 1.2 2005/09/06 13:12:52 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.query.QueryEditorPanel;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** Diaog that allows the ser to edit a query tool document.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
 *<p>
 *All the clever work done by {@link QueryEditorPanel}. This class just displays a dialogue.
 */
public class QueryEditorDialog extends JDialog implements PropertyChangeListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(QueryEditorDialog.class);
    private JOptionPane jOptionPane = null;
    private final QueryEditorPanel editorPanel;
    private final UIComponent parent;
    /** Construct a new QueryEditorDialog
     * @throws java.awt.HeadlessException
     */
    public QueryEditorDialog(
            Configuration conf, HelpServer help, UIInternal ui,
            RegistryChooser regChooser, Registry reg,
            ApplicationsInternal apps,
            ResourceChooserInternal resourceChooser, MyspaceInternal myspace) throws HeadlessException {
        super();
        this.parent = new UIComponent(conf,help,ui);
        this.editorPanel = new QueryEditorPanel(parent,regChooser, reg,apps,resourceChooser,myspace);
        this.setTitle("Query Editor");
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
        this.setContentPane(getJOptionPane());   
    }
    
    public void resetAndHide() {
        setVisible(false);
        editorPanel.clear();
    }
    
    public Tool getTool() {     
        return editedTool;
    }
    private Tool editedTool;
    
    public void populate(Tool t) {
        editedTool = null;
        editorPanel.setTool(t);
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
                    editedTool = editorPanel.getTool();
                    resetAndHide();                
            } else { //user closed dialog or clicked cancel           
                editedTool = null;
                resetAndHide();
            }
        }
    }
    
    /**
     * @return
     */
    private JOptionPane getJOptionPane() {
       if (jOptionPane == null) {
           JPanel main = parent.getMainPanel();
           parent.remove(main); // remove from this ui;
           main.add(editorPanel,BorderLayout.CENTER);
           jOptionPane = new JOptionPane(new JComponent[]{main},JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
           jOptionPane.addPropertyChangeListener(this);
       }
       return jOptionPane;
    }
    

 


}


/* 
$Log: QueryEditorDialog.java,v $
Revision 1.2  2005/09/06 13:12:52  nw
fixed two little gotchas.

Revision 1.1  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs
 
*/