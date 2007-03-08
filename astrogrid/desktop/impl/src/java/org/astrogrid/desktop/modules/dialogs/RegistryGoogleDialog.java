/*$Id: RegistryGoogleDialog.java,v 1.7 2007/03/08 17:44:01 nw Exp $
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

import net.sf.ehcache.Ehcache;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.votech.VoMon;

/** wraps a dialogue around a registry chooser pane.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 02-Sep-2005
 *
 */
public class RegistryGoogleDialog extends JDialog implements PropertyChangeListener {

    JOptionPane jOptionPane = null;
    final RegistryGooglePanel chooserPanel;
    private final UIComponentImpl parent;
    /** Construct a new RegistryChooserDialog
     * @param pref 
     * @throws java.awt.HeadlessException
     */
    public RegistryGoogleDialog(  Configuration conf, HelpServerInternal help, UIInternal ui, RegistryGooglePanel chooserPanel) throws HeadlessException {
        super();
        this.parent = new UIComponentImpl(conf,help,ui);
        this.chooserPanel = chooserPanel;
        chooserPanel.parent.set(parent);
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
    
    public RegistryGoogleDialog(Component parentComponent, Configuration conf, HelpServerInternal help, UIInternal ui,RegistryGooglePanel reg) throws HeadlessException {
    	this(conf,help,ui,reg);
        setLocationRelativeTo(parentComponent);
    }
    
    public void setFilter(String filter) {
        chooserPanel.applyFilter(filter);
    }

    public void setMultipleResources(boolean multiple) {
        chooserPanel.setMultipleResources(multiple);
    }    
    
    public void resetAndHide() {
        setVisible(false);
        chooserPanel.clear();
    }
    
    public Resource[] getSelectedResources() {
        return selectedResources;
    }
    
    private Resource[] selectedResources = null;
    
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
$Log: RegistryGoogleDialog.java,v $
Revision 1.7  2007/03/08 17:44:01  nw
first draft of voexplorer

Revision 1.6  2007/01/29 10:53:21  nw
moved cache configuration into hivemind.

Revision 1.5  2007/01/19 19:55:16  jdt
Move flush cache to the public interface.   It's currently in the IVOA module, which is probably not the right place.  *Not tested*  I can't test because Eclipse seems to be getting confused with the mixture of JDKs 1.4 and 1.5.

Revision 1.4  2007/01/10 19:12:16  nw
integrated with preferences.

Revision 1.3  2007/01/09 16:19:57  nw
uses vomon.

Revision 1.2  2006/08/31 21:34:46  nw
minor tweaks and doc fixes.

Revision 1.1  2006/08/15 10:19:53  nw
implemented new registry google dialog.

Revision 1.9  2006/06/27 19:11:31  nw
adjusted todo tags.

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