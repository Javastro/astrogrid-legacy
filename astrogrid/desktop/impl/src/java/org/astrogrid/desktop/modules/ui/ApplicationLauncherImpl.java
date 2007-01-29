/*$Id: ApplicationLauncherImpl.java,v 1.18 2007/01/29 16:45:09 nw Exp $
 * Created on 12-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import java.awt.Image;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.ApplicationLauncher;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.CompositeToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.system.UIInternal;
/** Implementation of the Application Launcher component
 * <p>
 * not just a thin wrapper around the composite tool editor;
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 12-May-2005
 *
 */
public class ApplicationLauncherImpl extends UIComponentImpl  implements ApplicationLauncher {


    public ApplicationLauncherImpl( 
    		List panelFactories
            ,ResourceChooserInternal rChooser
            ,ApplicationsInternal apps
            ,MyspaceInternal myspace
            ,Lookout lookout                                  
            ,Configuration conf, HelpServerInternal help, UIInternal ui, BrowserControl browser, Preference pref) {
            super(conf, help, ui);
            editor =  new CompositeToolEditorPanel(
                    panelFactories,rChooser,apps,myspace,this,help,browser,pref);
            editor.setLookout(lookout);
            this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            this.setSize(600,425); // same proportions as A4, etc., and 600 high.   
            final Image defaultImage = IconHelper.loadIcon("thread_view.gif").getImage();
			setIconImage(defaultImage);                
            JPanel pane = getMainPanel();
             pane.add(editor, java.awt.BorderLayout.CENTER);
            this.setContentPane(pane);
            this.setTitle("Task Launcher");
            getJMenuBar().add(createHelpMenu());
            getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.applicationLauncher");
            // belt and braces.
            getHelpServer().enableHelpKey(editor,"userInterface.applicationLauncher");             
            editor.getToolModel().addToolEditListener(new ToolEditAdapter() {

                public void toolSet(ToolEditEvent te) {
                    final CeaApplication info = editor.getToolModel().getInfo();
					setTitle("Task Launcher - " + info.getTitle());
					
					if (info.getCuration().getCreators().length > 0 && info.getCuration().getCreators()[0].getLogo() != null) {
						(new BackgroundOperation("Fetching Creator Icon") {
							protected Object construct() throws Exception {
								return IconHelper.loadIcon( info.getCuration().getCreators()[0].getLogo()).getImage();
							}
							protected void doFinished(Object result) {
								setIconImage((Image)result);
							}
							protected void doError(Throwable ex) {//ignore
							};
						}).start();
					} else {
						setIconImage(defaultImage);
					}
                }
                public void toolCleared(ToolEditEvent te) {
                    setTitle("Task Launcher");   
                    setIconImage(defaultImage);
                }                
            });
    }

	
	/** override:  create a help menu with additional entries */
    protected JMenu createHelpMenu() {
 	JMenu menu = super.createHelpMenu();
 	menu.insertSeparator(0);
 /*
 	JMenuItem ref = new JMenuItem("Reference");
 	getHelpServer().enableHelpOnButton(ref, "applicationLauncher.menu.reference");
 	menu.insert(ref,0);
 	*/
 	JMenuItem sci = new JMenuItem("Task Launcher Help");
 	getHelpServer().enableHelpOnButton(sci, "applicationLauncher.menu.science");
 	menu.insert(sci,0);
 	return menu;
    }
    
    final CompositeToolEditorPanel editor;
  
}

/* 
$Log: ApplicationLauncherImpl.java,v $
Revision 1.18  2007/01/29 16:45:09  nw
cleaned up imports.

Revision 1.17  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.16  2007/01/12 13:20:05  nw
made sure every ui app has a help menu.

Revision 1.15  2007/01/10 19:12:16  nw
integrated with preferences.

Revision 1.14  2006/08/15 10:10:20  nw
migrated from old to new registry models.

Revision 1.13  2006/07/20 12:32:12  nw
added display of tool creator's logo.

Revision 1.12  2006/06/27 19:14:10  nw
fixed to filter on cea apps when needed.

Revision 1.11  2006/06/27 10:33:29  nw
labelling fixes

Revision 1.10  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.9.2.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.9.2.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.9.2.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.9  2006/02/24 15:25:34  nw
minor fix to remove use of deprecated method

Revision 1.8  2005/11/21 18:26:23  pjn3
basic task editor help added

Revision 1.7  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.6  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.5  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.4.6.1  2005/10/12 09:21:38  nw
added java help system

Revision 1.4  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.3  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:33  nw
first release of application launcher
 
*/