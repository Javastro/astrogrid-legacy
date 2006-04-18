/*$Id: RegistryChooserDialog.java,v 1.8 2006/04/18 23:25:44 nw Exp $
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryChooserPanel;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;

/** wraps a dialogue around a registry chooser pane.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
 * @todo catch 'enter' and pass to search button, instead of 'ok' button. am trying todo this here, but don't seem to be working.
 *
 */
public class RegistryChooserDialog extends JDialog implements PropertyChangeListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RegistryChooserDialog.class);

    private JOptionPane jOptionPane = null;
    private final RegistryChooserPanel chooserPanel;
    private final UIComponentImpl parent;
    /** Construct a new RegistryChooserDialog
     * @throws java.awt.HeadlessException
     */
    public RegistryChooserDialog(  Configuration conf, HelpServerInternal help, UIInternal ui,Registry reg) throws HeadlessException {
        super();
        this.parent = new UIComponentImpl(conf,help,ui);
        this.chooserPanel = new RegistryChooserPanel(parent,reg);
        this.setContentPane(getJOptionPane());           
     
        this.setTitle("Resource Chooser");
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
        this.setSize(425,600);        
    }
    
    public RegistryChooserDialog(Component parentComponent, Configuration conf, HelpServerInternal help, UIInternal ui,Registry reg) throws HeadlessException {
        this(conf,help,ui,reg);
        setLocationRelativeTo(parentComponent);
    }
    
    public void setFilter(String filter) {
        chooserPanel.setFilter(filter);
    }

    public void setMultipleResources(boolean multiple) {
        chooserPanel.setMultipleResources(multiple);
    }    
    
    public void resetAndHide() {
        setVisible(false);
        chooserPanel.clear();
    }
    
    public ResourceInformation[] getSelectedResources() {
        return selectedResources;
    }
    
    private ResourceInformation[] selectedResources = null;
    
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
                selectedResources = chooserPanel.getSelectedResources();
                    resetAndHide();                
            } else { //user closed dialog or clicked cancel           
                selectedResources = null;
                resetAndHide();
            }
        }
    }
    
    public void setPrompt(String s) {
        getTopLabel().setText(s);
    }
    
    private JLabel topLabel;
    private JLabel getTopLabel() {
        if (topLabel == null) {
            topLabel = new JLabel();
        }
        return topLabel;
    }
        
    /**
     * @return
     */
    private JOptionPane getJOptionPane() {
       if (jOptionPane == null) {
           JPanel main = parent.getMainPanel();
           parent.remove(main); // remove from this ui;
           
           main.add(getTopLabel(),BorderLayout.NORTH);
           main.add(chooserPanel,BorderLayout.CENTER);
           jOptionPane = new JOptionPane(main,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
           jOptionPane.addPropertyChangeListener(this);
           KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
           jOptionPane.getInputMap().remove(enter);
           jOptionPane.getInputMap(jOptionPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).remove(enter);
           jOptionPane.getInputMap(jOptionPane.WHEN_IN_FOCUSED_WINDOW).remove(enter);
           jOptionPane.getInputMap(jOptionPane.WHEN_IN_FOCUSED_WINDOW).put(enter,"search");
           jOptionPane.getActionMap().put("search",new AbstractAction() {
               public void actionPerformed(ActionEvent e) {
                   chooserPanel.actionPerformed(e);
               }
           });              
       }
       return jOptionPane;
    }
    


}


/* 
$Log: RegistryChooserDialog.java,v $
Revision 1.8  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.7.30.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.7  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.6.2.1  2005/11/23 04:47:18  nw
added keybindings

Revision 1.6  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.5  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.3.6.2  2005/10/12 09:21:38  nw
added java help system

Revision 1.3.6.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.4  2005/09/29 17:16:40  pjn3
Drag and drop work complete 1322

Revision 1.3.2.1  2005/09/23 09:36:06  pjn3
setRelativeToParent and size added

Revision 1.3  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.2  2005/09/06 13:12:52  nw
fixed two little gotchas.

Revision 1.1  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs
 
*/