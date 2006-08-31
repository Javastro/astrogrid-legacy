/*$Id: ChooseAToolEditorPanel.java,v 1.11 2006/08/31 21:34:46 nw Exp $
 * Created on 08-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.dialogs.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.sf.ehcache.CacheManager;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ivoa.CacheFactory;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.workflow.beans.v1.Tool;

/** Tool Editor Panel that prompts the user to search for and select a tool.
 * <p>
 * just a wapper of some event listeners around the registry chooser panel - nice!
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2005
 *
 */
public class ChooseAToolEditorPanel extends AbstractToolEditorPanel implements PropertyChangeListener {

	private RegistryGooglePanel rcp;


	public ChooseAToolEditorPanel(ToolModel tm,final UIComponent parent, RegistryInternal reg, final ApplicationsInternal apps, BrowserControl browser, RegistryBrowser regBrowser, CacheFactory cache) {
		super(tm);

		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(new JLabel("Select an Application:"));
		rcp = new RegistryGooglePanel( parent,reg,browser,regBrowser,cache);
		rcp.setMultipleResources(false);
		setChooseCEAOnly(false);
		toolModel.addToolEditListener(new ToolEditAdapter() {
			public void toolCleared(ToolEditEvent te) {
				rcp.clear();
			}            
		});
		rcp.getSelectedResourcesModel().addListDataListener(new ListDataListener() {

			public void intervalAdded(ListDataEvent e) {
				Resource[] ri = rcp.getSelectedResources();
				if (ri.length > e.getIndex0()) {
					Resource resource = ri[e.getIndex0()];

					if (resource != null) {
						if (toolModel.getTool() != null) { // already got some data on the go..
							int result = JOptionPane.showConfirmDialog(ChooseAToolEditorPanel.this,"Discard the tool currently being edited?","Replace the current tool?",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
							if (result != JOptionPane.OK_OPTION) {
								return;
							}
						}
						try {
							// @todo move this query off the main thread.
							CeaApplication app = apps.getCeaApplication(resource.getId());
							String ifaceName = app.getInterfaces()[0].getName();
							if (app.getInterfaces().length > 1) {
								String[] names = new String[app.getInterfaces().length];
								for (int i = 0; i < names.length; i++) {
									names[i] = app.getInterfaces()[i].getName();
								}
								ifaceName =(String) JOptionPane.showInputDialog(ChooseAToolEditorPanel.this,"Select an interface","Which Interface?"
										, JOptionPane.QUESTION_MESSAGE,null,names,names[0]);
								if (ifaceName == null) { // user hit cancel.
									return;
								}
							}
							
							Tool t = apps.createTemplateTool(ifaceName,app);
							toolModel.populate(t,app); // fires notification, etc - lets anything else grab this.
						} catch (ACRException ex) {
							ex.printStackTrace();
						}
					} else {
						if (resource != null) {//@todo fix this.
							parent.setStatusMessage(resource.getTitle() + " is not a known kind of Application");
						} else {
							parent.setStatusMessage("NULL!!");
						}
					}
				}
			}

			public void intervalRemoved(ListDataEvent e) {
			}

			public void contentsChanged(ListDataEvent e) {
			}
		});
		add(rcp);        
	}

	private void setChooseCEAOnly(boolean ceaOnly) {

		rcp.setFilter("@xsi:type &= '*CeaApplicationType' " +
				" or @xsi:type &= '*CeaHttpApplicationType' " + 
				( ! ceaOnly ? " or @xsi:type &= '*ConeSearch' " + 
						" or @xsi:type &= '*SimpleImageAccess' "  + 
						" or @xsi:type &= '*SimpleSpectrumAccess' " 
						// @todo add this in - but need a faster registry service, or some other way of registering CDS
						// also, ned to have a ceaApplication handler that knows to treat these as a tabular sky service
						//" or (@xsi:type &= '*TabularSkyService' and vr:identifier &= 'ivo://CDS/*' and vods:table/vods:column/vods:ucd = 'POS_EQ_RA_MAIN')"

						: ""))  ;    	
	}

	/** applicable always */
	public boolean isApplicable(Tool t, CeaApplication info) {
		return true;
	}


	/** listens for the client property CEA_ONLY - and adjusts reg filter if it sees it
	 * 
	 *  bit of a nasty hack - but works for now.
	 *  */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(CompositeToolEditorPanel.CEA_ONLY_CLIENT_PROPERTY)) {
			Object o = evt.getNewValue();
			setChooseCEAOnly (o != null && o.equals(Boolean.TRUE));
		}
	}

}


/* 
$Log: ChooseAToolEditorPanel.java,v $
Revision 1.11  2006/08/31 21:34:46  nw
minor tweaks and doc fixes.

Revision 1.10  2006/08/15 10:22:06  nw
migrated from old to new registry models.

Revision 1.9  2006/06/28 11:37:26  nw
commented out active filter for now - don't work.

Revision 1.8  2006/06/27 19:12:31  nw
fixed to filter on cea apps when needed.

Revision 1.7  2006/06/27 10:28:27  nw
findbugs tweaks

Revision 1.6  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.4.30.3  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.4.30.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.4.30.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.5  2006/03/13 18:29:56  nw
fixed queries to not restrict to @status='active'

Revision 1.4  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.3.2.2  2005/11/23 04:45:51  nw
removed dev code from query.

Revision 1.3.2.1  2005/11/17 21:18:22  nw
 *** empty log message ***

Revision 1.3  2005/11/11 18:39:40  nw
2 final tweaks

Revision 1.2  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

 */