/*$Id: QueryBuilderImpl.java,v 1.1 2007/03/20 10:02:34 nw Exp $
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
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
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
import org.astrogrid.workflow.beans.v1.Tool;
/** main class for the query builder.
 * Based on the old application launcher framework at the moment - all needs to be changed
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 12-May-2005
 *
 */
public class QueryBuilderImpl extends UIComponentImpl  implements TaskInvoker  {

	private final ApplicationsInternal apps;
	
    public QueryBuilderImpl( 
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
            getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.applicationLauncher");
            // belt and braces.
            getHelpServer().enableHelpKey(editor,"userInterface.applicationLauncher");             
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
 	getHelpServer().enableHelpOnButton(sci, "applicationLauncher.menu.science");
 	menu.insert(sci,0);
 	return menu;
    }
    
    final CompositeToolEditorPanel editor;

	public void invokeTask(Resource resource) {
		if (resource instanceof DataCollection) {
			JOptionPane.showMessageDialog(this,"Not implemented for DataCollections yet - choose a CEA Task instead");
			//@todo implement finding the appropriate cea app from a datacollection resource.
			return;
			// later - there'll be cases here for the new TAP protocol, no doubt too.
		}
		try {
			// @todo move this query off the main thread.
			CeaApplication app = apps.getCeaApplication(resource.getId());
			String paramName = null;
			// work out which interface of the cea app to call.
			ParameterBean[] ps = app.getParameters();
			for (int i =0; i < ps.length; i++) {
				if (ps[i].getType().equalsIgnoreCase("adql")) {
					paramName = ps[i].getName();
					break;
				}
			}
			if (paramName == null) {
				JOptionPane.showMessageDialog(this,"Unable to find an adql parameter for this application"); //@todo make these err messages better
				return;
			}
			String ifaceName = null;
			// now find the name of the first interface containing that parameter
			InterfaceBean[] is = app.getInterfaces();
			endsearch: {
				for (int i = 0; i < is.length; i++) {	
					ParameterReferenceBean[] refs = is[i].getInputs();
					for (int j = 0; j < refs.length; j++) {
						if (refs[i].getRef().equals(paramName)) {
							ifaceName = is[i].getName();
							break endsearch; // break out of both loops.
						}
					}
				}
			}
			if (ifaceName == null) {
				JOptionPane.showMessageDialog(this,"Unable to find a way of invoking this application using adql"); // @todo improve err msg
				return;
			}
			// finally.
			Tool t = apps.createTemplateTool(ifaceName,app);
			editor.getToolModel().populate(t,app); // fires notification, etc - lets anything else grab this.
			setVisible(true);
		} catch (ACRException ex) {
			ex.printStackTrace();
		}		
	}
  
}
