/*$Id: QueryBuilderImpl.java,v 1.2 2007/06/27 11:12:20 nw Exp $
 * Created on 12-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor;

import java.awt.Image;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.workflow.beans.v1.Tool;
/** main class for the query builder.
 * Based on the old application launcher  - but will have to stay
 * until querybuilder has it's own way of loading/saving queries, 
 * authenticating, submitting queries &  specifying savepoints, 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 12-May-2005
 *
 */
public class QueryBuilderImpl extends UIComponentImpl  implements QueryBuilderInternal  {

	private final ApplicationsInternal apps;
	
    public QueryBuilderImpl( 
    		List panelFactories
            ,ResourceChooserInternal rChooser
            ,ApplicationsInternal apps
            ,MyspaceInternal myspace                 
            ,UIContext context,  Preference pref) {
            super(context);
            editor =  new QueryBuilderCompositeToolEditorPanel(
                    panelFactories,rChooser,apps,myspace,this,context.getHelpServer(),context.getBrowser(),pref);
            this.apps = apps;
            this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            this.setSize(800,600);   
            final Image defaultImage = IconHelper.loadIcon("applaunch16.png").getImage();
			setIconImage(defaultImage);                
            JPanel pane = getMainPanel();
             pane.add(editor, java.awt.BorderLayout.CENTER);
            this.setContentPane(pane);
            this.setTitle("Query Builder");
            getJMenuBar().add(createHelpMenu());
            getContext().getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.queryBuilder");
            // belt and braces.
            getContext().getHelpServer().enableHelpKey(editor,"userInterface.queryBuilder");             
            editor.getToolModel().addToolEditListener(new ToolEditAdapter() {

                public void toolSet(ToolEditEvent te) {
                    final CeaApplication info = editor.getToolModel().getInfo();
					setTitle("Query Builder - " + info.getTitle());
					
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
            setVisible(true);
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
 	JMenuItem sci = new JMenuItem("Query Builder Help");
 	getContext().getHelpServer().enableHelpOnButton(sci, "queryBuilder.menu.science");
 	menu.insert(sci,0);
 	return menu;
    }
    
    final QueryBuilderCompositeToolEditorPanel editor;


	// Implementation of the QueryBuilderInternal interface.

	public void build(CeaApplication app) {
	//	CeaApplication app = apps.getCeaApplication(resource.getId());
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
	}


	public void build(DataCollection coll) { 
		//@FIXME implement - find the associated cea app.
	}


	public void build(CatalogService cat) {
		//@FIXME implement	 
	}


	public void build(Resource[] rs) {
		//@FIXME implement	 - 	
	}


	public void edit(FileObject fo) {
		//this reads a CEA Tool document from disk - not sure if this is what is wanted
		// or whether it should just be an isolated adql query that's saved.
		// @FIXME implement.
//		try {
//		Reader reader = new InputStreamReader(fo.getContent().getInputStream());
//		Tool t = Tool.unmarshalTool(reader);
//		CeaApplication app = apps.getCeaApplication(new URI("ivo://" + t.getName()));
//		editor.getToolModel().populate(t,app);
//		} catch (Exception e) {
//			showError("Failed to load query",e);
//		}
	}


	// factory interface. ignore, and remove later.
	public Object create() {
		return null;
	}
  
}
