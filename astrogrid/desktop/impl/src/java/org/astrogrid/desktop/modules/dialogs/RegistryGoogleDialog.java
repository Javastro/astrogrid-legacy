/*$Id: RegistryGoogleDialog.java,v 1.21 2009/04/17 16:55:16 nw Exp $
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
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.ProgrammerError;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIDialogueComponentImpl;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.folders.ResourceTreeModel;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.UneditableResourceTree;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel.LoadEvent;

import ca.odell.glazedlists.matchers.Matcher;

/** Dialogue around a registry chooser pane.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 02-Sep-2005
 *
 */
public class RegistryGoogleDialog extends UIDialogueComponentImpl implements ListSelectionListener {

    /** representation of no resources selected */
    private static final Resource[] NOTHING_SELECTED = new Resource[0];
    public RegistryGoogleDialog(final Component parentComponent,final UIContext context,final TypesafeObjectBuilder builder, final ResourceTreeModel model) throws HeadlessException {
    	this(context,builder, model);
        setLocationRelativeTo(parentComponent);
    }
    /** Construct a new RegistryChooserDialog
     * @param model 
     * @throws java.awt.HeadlessException
     */
    public RegistryGoogleDialog( final UIContext context, final TypesafeObjectBuilder builder, final ResourceTreeModel model) throws HeadlessException {
        super(context,"Registry Resource Chooser","dialog.registry");
        this.google = builder.createGooglePanel(this);
        this.okButton = getOkButton();
        this.okButton.setEnabled(false);
        google.getCurrentResourceModel().addListSelectionListener(this);
        
        // assmble resource tree.
        resourceLists = new UneditableResourceTree(model);
        // connect resource lists to google.
        resourceLists.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(final TreeSelectionEvent evt) {
                    final ResourceFolder folder = resourceLists.getSelectedFolder();
                    if (folder != null) {
                        folder.display(google);
                    }
                }            
        }); 

        // button to stop searches
        final JButton folderButton = new JButton("Halt Search");
        folderButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(final ActionEvent e) {
                google.halt();
            }
        });
        folderButton.setEnabled(false);
        
        // listen to google, and temporarily disable tree / enable button
        google.addLoadListener(new RegistryGooglePanel.LoadListener() {
            
            public void loadCompleted(final LoadEvent e) {
                resourceLists.setEnabled(true);
                folderButton.setEnabled(false);
            }
            
            public void loadStarted(final LoadEvent e) {
                resourceLists.setEnabled(false);
                folderButton.setEnabled(true);
            }
        });
        
        // panel to contain resource lists and button.       
        final JScrollPane foldersScroll = new JScrollPane(resourceLists,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        foldersScroll.setBorder(null);
        foldersScroll.setPreferredSize(new Dimension(150,400));
        folderPanel = new JPanel(new BorderLayout());
        folderPanel.setBorder(null);
        folderPanel.add(foldersScroll,BorderLayout.CENTER);        
        folderPanel.add(folderButton,BorderLayout.SOUTH);
        
        
        final JPanel main =getMainPanel();
        main.add(google,BorderLayout.CENTER);
        main.add(folderPanel,BorderLayout.WEST);
/* might need to do something like this in a bit
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
        jOptionPane.getInputMap().remove(enter);
        jOptionPane.getInputMap(jOptionPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).remove(enter);
        jOptionPane.getInputMap(jOptionPane.WHEN_IN_FOCUSED_WINDOW).remove(enter);
        jOptionPane.getInputMap(jOptionPane.WHEN_IN_FOCUSED_WINDOW).put(enter,"search");
        */        
        this.getContentPane().add(main);           
     
        this.pack();
        this.setSize(700,600);        
    }
    private final RegistryGooglePanel google;
    private final JButton okButton;
    
    private final UneditableResourceTree resourceLists;

    private Resource[] selectedResources = NOTHING_SELECTED;
    
    private final JPanel folderPanel;   
    
    /** just resets the form for next time */
    @Override
    public void cancel() {
        super.cancel();
        selectedResources = NOTHING_SELECTED;
        google.clear();
    }
    
    /** access the user's selection */
    public Resource[] getSelectedResources() {
        return selectedResources;
    } 

    
    /** Install a matcher, that controls which resources will be selectable.
     * @param accept
     */
    public void installMatcher(final Matcher<Resource> acceptanceMatcher) {
       google.setCustomMatcher(acceptanceMatcher);
    }
    
    /** store the user's selection before resetting the form. */
    @Override
    public void ok() {
        final Transferable tran = google.getSelectionTransferable(); // see what's been chosen then
        try {
            if (tran.isDataFlavorSupported(VoDataFlavour.RESOURCE)) {
                selectedResources = new Resource[] {
                        (Resource)tran.getTransferData(VoDataFlavour.RESOURCE)
                };
            } else if (tran.isDataFlavorSupported(VoDataFlavour.RESOURCE_ARRAY)) {
                selectedResources =(Resource[]) tran.getTransferData(VoDataFlavour.LOCAL_RESOURCE_ARRAY);
            } else {
                throw new ProgrammerError("No suitable dataflavour is provided");
            }
        } catch (final UnsupportedFlavorException x) { // unlikely.
            throw new ProgrammerError(x);
        } catch (final IOException x) { // unlikely
            throw new ProgrammerError(x);
        } 
        super.ok();
        google.clear();
    }
    /** populate the dialogue by running an xquery */
    public void populateFromXQuery(final String xquery) {
        google.displayQuery("Options",xquery); // use the title variant too?
    }
    /** populate the dialogue by retireving a list of resources */
    public void populateWithIds(final URI[] ids) {
        google.displayIdSet("Options",Arrays.asList(ids)); // thre's a variant which sets a title too - use this?
    }
    
    /** enable the user to select more than one resource */
    public void setMultipleResources(final boolean multiple) {
        google.setMultipleResources(multiple);
    }
    
    /** set the prompt to display at the top of the dialogue */
    public void setPrompt(final String s) {
        setTitle(s);
    }
    
    /** display the playlist (folder tree) on the lhs of the dialogue - this allows the user to 
     * access any of their previously saved queries,
     * if false, clears any previously set matcher.
     * @param showPlaylists
     */
    public void setShowPlaylists(final boolean showPlaylists) {
        folderPanel.setVisible(showPlaylists);
        if (showPlaylists) {
            resourceLists.initialiseViewAndSelection();
        } else {
          // clear any previous matcher.
            google.setCustomMatcher(null);
        }
    }
    
  

    /** listens to changes in the user's selection. - impleentation detail */
    public void valueChanged(final ListSelectionEvent e) {
        okButton.setEnabled(google.getSelectionTransferable() != null); 
    }


}


/* 
$Log: RegistryGoogleDialog.java,v $
Revision 1.21  2009/04/17 16:55:16  nw
added new registry dialog options.

Revision 1.20  2008/11/04 14:35:52  nw
javadoc polishing

Revision 1.19  2008/08/21 11:37:21  nw
Complete - task 4: RegistryGoogle dialogue

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