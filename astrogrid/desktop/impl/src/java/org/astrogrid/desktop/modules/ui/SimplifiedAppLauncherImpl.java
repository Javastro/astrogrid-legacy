/*$Id: SimplifiedAppLauncherImpl.java,v 1.3 2007/07/12 10:15:16 nw Exp $
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.CompositeToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.workflow.beans.v1.Tool;
/** Implementation of the  simplified app launcher component.
 * <p>
 * not just a thin wrapper around the composite tool editor;
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 12-May-2005
 * @fixme to remove
 *
 */
public class SimplifiedAppLauncherImpl extends UIComponentImpl  implements TaskRunnerInternal  {

	private final ApplicationsInternal apps;
	
    public SimplifiedAppLauncherImpl( 
    		List panelFactories
            ,ResourceChooserInternal rChooser
            ,ApplicationsInternal apps
            ,MyspaceInternal myspace             
            ,UIContext context,Preference pref) {
            super(context);
            editor =  new CompositeToolEditorPanel(
                    panelFactories,rChooser,apps,myspace,this,context.getHelpServer(),context.getBrowser(),pref);
            this.apps = apps;
            this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            this.setSize(600,425); // same proportions as A4, etc., and 600 high.   
            final Image defaultImage = IconHelper.loadIcon("applaunch16.png").getImage();
			setIconImage(defaultImage);                
            JPanel pane = getMainPanel();
             pane.add(editor, java.awt.BorderLayout.CENTER);
            this.setContentPane(pane);
            this.setTitle("Task Launcher");
            getJMenuBar().add(createHelpMenu());
            getContext().getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.applicationLauncher");
            // belt and braces.
            getContext().getHelpServer().enableHelpKey(editor,"userInterface.applicationLauncher");             
            editor.getToolModel().addToolEditListener(new ToolEditAdapter() {

                public void toolSet(ToolEditEvent te) {
                    final CeaApplication info = editor.getToolModel().getInfo();
					setTitle("Task Launcher - " + info.getTitle());
					
					if (info.getCuration().getCreators().length > 0 && info.getCuration().getCreators()[0].getLogoURI() != null) {
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
 	getContext().getHelpServer().enableHelpOnButton(sci, "applicationLauncher.menu.science");
 	menu.insert(sci,0);
 	return menu;
    }
    
    final CompositeToolEditorPanel editor;

	public void invokeTask(Resource resource) {
		try {
			// @todo move this query off the main thread.
			CeaApplication app = apps.getCeaApplication(resource.getId());
			String ifaceName = app.getInterfaces()[0].getName();
			if (app.getInterfaces().length > 1) {
				String[] names = new String[app.getInterfaces().length];
				for (int i = 0; i < names.length; i++) {
					names[i] = app.getInterfaces()[i].getName();
				}
				ifaceName =(String) JOptionPane.showInputDialog(SimplifiedAppLauncherImpl.this,"Select an interface","Which Interface?"
						, JOptionPane.QUESTION_MESSAGE,null,names,names[0]);
				if (ifaceName == null) { // user hit cancel.
					return;
				}
			}

			Tool t = apps.createTemplateTool(ifaceName,app);
			editor.getToolModel().populate(t,app); // fires notification, etc - lets anything else grab this.
			setVisible(true);
		} catch (ACRException ex) {
			ex.printStackTrace();
		}		
	}


	public Object create() {
		return null;
	}
  
}
