/*$Id: RegistryGoogleDialog.java,v 1.18 2008/03/28 13:09:01 nw Exp $
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
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIDialogueComponentImpl;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;

/** wraps a dialogue around a registry chooser pane.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 02-Sep-2005
 *
 */
public class RegistryGoogleDialog extends UIDialogueComponentImpl implements ListSelectionListener {

    final RegistryGooglePanel chooserPanel;
    private final JButton okButton;
    /** Construct a new RegistryChooserDialog
     * @throws java.awt.HeadlessException
     */
    public RegistryGoogleDialog( UIContext context, TypesafeObjectBuilder builder) throws HeadlessException {
        super(context,"Registry Resource Chooser","dialog.registry");
        this.chooserPanel = builder.createGooglePanel(this);
        this.okButton = getOkButton();
        this.okButton.setEnabled(false);
        chooserPanel.getCurrentResourceModel().addListSelectionListener(this);
        JPanel main =getMainPanel();
        main.add(getTopLabel(),BorderLayout.NORTH);
        main.add(chooserPanel,BorderLayout.CENTER);
/* might need to do something like this in a bit
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
        jOptionPane.getInputMap().remove(enter);
        jOptionPane.getInputMap(jOptionPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).remove(enter);
        jOptionPane.getInputMap(jOptionPane.WHEN_IN_FOCUSED_WINDOW).remove(enter);
        jOptionPane.getInputMap(jOptionPane.WHEN_IN_FOCUSED_WINDOW).put(enter,"search");
        */        
        this.getContentPane().add(main);           
     
        this.pack();
        this.setSize(600,600);        
    }
    
    public RegistryGoogleDialog(Component parentComponent,UIContext context,TypesafeObjectBuilder builder) throws HeadlessException {
    	this(context,builder);
        setLocationRelativeTo(parentComponent);
    }
    
    public void setFilter(String filter) {
     //@todo implement or reexamine..
    	//@fixme - either re-introduce filtering (although doesn't work with stored playlists)
    	// or need some way to express a filter on resource objects. 
    	// which means another string-based expressiion language...
    	// - script interpreter? bean constraint and update language?
    	// maybe this is a general requirement  anyhow - re brian's wishlist
    	// 
        // think I need to allow the client to provide the query to run to provide a list of
        // resources.
        // actually, I need this in all cases.
        // 
    }

    public void setMultipleResources(boolean multiple) {
        chooserPanel.setMultipleResources(multiple);
    }    

    
    public Resource[] getSelectedResources() {
        return selectedResources;
    }
    
    private Resource[] selectedResources = null;
    
    public void ok() {
        Transferable tran = chooserPanel.getSelectionTransferable(); // see what's been chosen then
        try {
            selectedResources =(Resource[]) tran.getTransferData(VoDataFlavour.RESOURCE_ARRAY);
        } catch (UnsupportedFlavorException x) { // unlikely.
            logger.error("UnsupportedFlavorException",x);
        } catch (IOException x) { // unlikely
            logger.error("IOException",x);
        } 
        super.ok();
        chooserPanel.clear();
    }
    
    public void cancel() {
        super.cancel();
        selectedResources = null;
        chooserPanel.clear();
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

    // listens to changes in the user's selection.
    public void valueChanged(ListSelectionEvent e) {
        okButton.setEnabled(chooserPanel.getSelectionTransferable() != null); 
    }

}


/* 
$Log: RegistryGoogleDialog.java,v $
Revision 1.18  2008/03/28 13:09:01  nw
help-tagging

Revision 1.17  2007/12/12 13:54:15  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.16  2007/11/21 07:55:39  nw
Complete - task 65: Replace modal dialogues

Revision 1.15  2007/11/20 06:02:55  nw
added help ids.

Revision 1.14  2007/10/12 10:58:24  nw
re-worked dialogues to use new ui baseclass and new ui components.

Revision 1.13  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.12  2007/09/11 12:08:22  nw
services filter, and various layout alterations.

Revision 1.11  2007/08/30 23:46:48  nw
Complete - task 73: upgrade filechooser dialogue to new fileexplorer code
replaced uses of myspace by uses of vfs where sensible

Revision 1.10  2007/06/18 17:03:12  nw
javadoc fixes.

Revision 1.9  2007/05/02 15:38:32  nw
changes for 2007.3.alpha1

Revision 1.8  2007/04/18 15:47:10  nw
tidied up voexplorer, removed front pane.

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