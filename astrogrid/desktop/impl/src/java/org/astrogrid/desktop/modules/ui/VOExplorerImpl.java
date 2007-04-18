/*$Id: VOExplorerImpl.java,v 1.2 2007/04/18 15:47:05 nw Exp $
 * Created on 30-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.ListOrderedMap;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.ui.ArMainWindow;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContributionBuilder;
import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.voexplorer.ExplorerWindow;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryView;
import org.astrogrid.desktop.modules.ui.voexplorer.AbstractView;
import org.astrogrid.desktop.modules.ui.voexplorer.tasks.Activity;
import org.astrogrid.desktop.modules.ui.voexplorer.tasks.PlasticActivities;
import org.astrogrid.desktop.modules.ui.voexplorer.tasks.InfoActivity;
import org.astrogrid.desktop.modules.ui.voexplorer.tasks.MyTaskPaneGroup;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;

import com.l2fprod.common.swing.JOutlookBar;
import com.l2fprod.common.swing.JTaskPane;

/** Main window of voexplorer - assembles together the contributions.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 30-Mar-2005
 *
 */
public class VOExplorerImpl extends UIComponentImpl implements  ExplorerWindow, ChangeListener
{

	public VOExplorerImpl( final UIContext context, final IterableObjectBuilder viewsBuilder, final IterableObjectBuilder activityBuilder,final UIContributionBuilder menuBuilder) {
        super(context);
 
        this.setSize(800, 800);    
        	
		    getContext().getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.voexplorer");        
		    JPanel pane = getMainPanel();
		    pane.setBorder(BorderFactory.createEmptyBorder());

		    // build the hierarchies panel - a tabbed view of hierachies, which controls what's being shown in the rest of the ui. 
		    hierarchiesPanel = new JOutlookBar();
		    hierarchiesPanel.setBorder(BorderFactory.createEmptyBorder());
			CSH.setHelpIDString(hierarchiesPanel, "voexplorer.hierarchies");
			
			// actions pane. - temporarily hacked
			Map actsMap = new ListOrderedMap();
			JTaskPane actionsPanel = new JTaskPane();
			CSH.setHelpIDString(actionsPanel, "voexplorer.actions");
		    // create the groups for this pane.
			actsMap.put(Activity.USE_SECTION,new MyTaskPaneGroup() {{
				setTitle("Utilize");
				setIconName("run16.png");
				setHelpId("resourceActions.invoke");
				setSpecial(true);
			}});
			actsMap.put(Activity.INFO_SECTION, new MyTaskPaneGroup() {{
				setTitle("Details");
				setIconName("info16.png");
				setHelpId("resourceActions.info");
				setSpecial(true);
			}});
			actsMap.put(Activity.EXPORT_SECTION, new MyTaskPaneGroup() {{//@todo move additional stuff 'export' does into activity.
				setTitle("Export");
			}});
			// compose these tasks.
			for (Iterator i = actsMap.values().iterator(); i.hasNext();) {
				MyTaskPaneGroup t = (MyTaskPaneGroup) i.next();
				actionsPanel.add(t);
			}
			// assemble the menus.
			JMenu actions = new JMenu("Actions"); // @future - might arrange the actions menu differenlty.
			popup = new JPopupMenu();
			JMenu popupNew = new JMenu("New");
			JMenu actionsNew = new JMenu("New");
			popup.add(popupNew);
			popup.addSeparator();
			actions.add(actionsNew);
			actions.addSeparator();
			JMenu popupInfo = new JMenu("About");
			JMenu actionsInfo = new JMenu("About");
			JMenu popupExport = new JMenu("Export");
			JMenu actionsExport = new JMenu("Export");
			JMenu popupAdvanced = new JMenu("Advanced");
			JMenu actionsAdvanced =new JMenu("Advanced");
			// now build the activities.
			activities = (Activity[]) IteratorUtils.toArray(activityBuilder.creationIterator(),Activity.class);
		    for (int i = 0; i < activities.length; i++) {
		    	final Activity a = activities[i];
				a.setUIParent(this);
				final String sectname = a.getSection();
				MyTaskPaneGroup t = (MyTaskPaneGroup)actsMap.get(sectname);
				if (t != null) {
					a.addTo(t);
				}
				// assemble the menus.
				if (sectname.equals(Activity.ADVANCED_SECTION)) {
					a.addTo(popupAdvanced);
					a.addTo(actionsAdvanced);
				} else if (sectname.equals(Activity.NEW_SECTION)) {
					a.addTo(popupNew);
					a.addTo(actionsNew);
				} else if (sectname.equals(Activity.INFO_SECTION)) {
					a.addTo(popupInfo);
					a.addTo(actionsInfo);
				} else if (sectname.equals(Activity.EXPORT_SECTION)) {
					a.addTo(actionsExport);
					a.addTo(popupExport);
				} else {
					a.addTo(popup);
					a.addTo(actions);
				}
			}
		    // add hte advanced submenu.
		    popup.addSeparator();
		    popup.add(popupExport);
		    popup.add(popupAdvanced);
		    popup.add(popupInfo);
		    actions.addSeparator();
		    actions.add(actionsExport);
		    actions.add(actionsAdvanced);
		    actions.add(actionsInfo);
		    
		    actionsPanel.revalidate();
		    actionsPanel.repaint();				

		    // main pane.	    
		    this.mainPanel = new FlipPanel();
		    
		    // pain pane buttons.
		    this.mainButtons = new FlipPanel();
		    
		    // menu bar.
		    menuBar = new JMenuBar();
		    menuBuilder.populateWidget(menuBar,this,ArMainWindow.MENUBAR_NAME);
		    // find the window menu..
		    
			// action to create a new voexplorer. defined here, as 
			// all the args we require are in scope (as we're in the contstructor at the moment)
			ActionListener newActionListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					VOExplorerImpl vo = new VOExplorerImpl(context,viewsBuilder,activityBuilder,menuBuilder);
					vo.setLocationRelativeTo(VOExplorerImpl.this);
					vo.setVisible(true);
				}
			};			 
		//	menuBar.add(createHelpMenu());
			int sz = menuBar.getComponentCount();
			menuBar.add(actions,sz-2); // insert before help.
			menuBar.add(createWindowMenu(newActionListener),sz-1); // insert before the help menu.
		    setJMenuBar(menuBar);
		    
			// go through the views, plugging them in.
		    views = new ListOrderedMap();
		    for (Iterator creator = viewsBuilder.creationIterator(this); creator.hasNext(); ) {
				AbstractView v = (AbstractView)creator.next();
				final String viewName = v.getName();
				views.put(viewName,v);
				
				hierarchiesPanel.addTab(viewName,v.getHierarchiesPanel());

				final JComponent mp = v.getMainPanel();
				if (mp != null) {
					mainPanel.add(mp,viewName);
				}

				
				JComponent mb = v.getMainButtons();
				if (mb != null) {
					mainButtons.add(mb,viewName);
				}
			}
		    // take a refernece to the first view.
		    currentView = (AbstractView)views.getValue(0);
		
			// listen to changes on hierarchies panel - use this to trigger change.
		    hierarchiesPanel.addChangeListener(this);
		    
		    // assemble all into main window.
		    final JScrollPane scrollPane = new JScrollPane(actionsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    scrollPane.setBorder(BorderFactory.createEmptyBorder());
			// assemble folders and tasks into LHS 
		    
			JSplitPane leftPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, hierarchiesPanel
					,scrollPane);
		    leftPane.setDividerSize(4);
		    leftPane.setDividerLocation(400);
		    leftPane.setBorder(BorderFactory.createMatteBorder(0,0,0,1,Color.GRAY));

		    // combine LHS and RSH
		    JSplitPane lrPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPane,mainPanel);
		    lrPane.setDividerSize(2);
		    lrPane.setDividerLocation(200);
		    lrPane.setBorder(BorderFactory.createEmptyBorder());
		    pane.add(lrPane,BorderLayout.CENTER); 
		    
		    // add buttonbar on top.
		    pane.add(mainButtons,BorderLayout.NORTH);
		    
		    // finish it all off..
			this.setContentPane(pane);
			this.setTitle("VO Explorer");
		    setIconImage(IconHelper.loadIcon("search16.png").getImage());  
    }
	
	public void displayResources(List uriList) {
		// find the ResourcesView.
		for (MapIterator i = views.mapIterator(); i.hasNext();) {
			AbstractView v = (AbstractView) i.getValue();
			if (v instanceof RegistryView) {
				RegistryView r= (RegistryView)v;
				// display this view.
				hierarchiesPanel.setSelectedComponent(r.getHierarchiesPanel()); // which fires everything else.
				// populate some data
				r.displayResources(uriList);
			}
		}

	}

    private final ListOrderedMap views; // manages the set of views.
    private AbstractView currentView;
    private final JOutlookBar hierarchiesPanel;
    private final FlipPanel mainPanel;
    private final FlipPanel mainButtons;
	private final JMenuBar menuBar;
	    
// event listener.
	/** called when outlook buttons tab changes. - changes view of whole ui. */
	public void stateChanged(ChangeEvent e) {
		final String viewName = hierarchiesPanel.getSelectedComponent().getName();
		//only flips if there's a corresponding component.
		mainPanel.show(viewName);
		mainButtons.show(viewName);
		// notify the views themselves that things have changed.
		currentView.setVisible(false);
		currentView = (AbstractView)views.get(viewName);
		currentView.setVisible(true);
	}   
	
	private JMenu createWindowMenu(ActionListener newActionListener) {
		JMenu windowMenu = new JMenu();
		windowMenu.setText("Window");
		windowMenu.setMnemonic(KeyEvent.VK_W);
		final JMenuItem newMenuItem = new JMenuItem("New Window") ;
		newMenuItem.addActionListener(newActionListener);
		windowMenu.add(newMenuItem);	
		windowMenu.addSeparator();
		// add window list here
		JMenu windowListMenu = new JMenu("Windows");
		windowMenu.add(windowListMenu); // would like to display this in-line really.
		EventList windows = new FunctionList(getContext().getWindowList(),new FunctionList.Function() {

			public Object evaluate(Object arg0) {
				final UIComponent c = (UIComponent)arg0;
				JMenuItem mi = new JMenuItem(c.getTitle());
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						c.setVisible(true);
					}
				});
				return mi;
			}
		});
		new EventListMenuManager(windows,windowListMenu,false);
		windowMenu.addSeparator();
		windowMenu.add(new CloseAction());
		windowMenu.add(new HideAllAction());
		return windowMenu;
		
	}
	
	   /** override:  create a help menu with additional entries */
	   protected JMenu createHelpMenu() {
		JMenu menu = super.createHelpMenu();
		menu.insertSeparator(0);
	/*
		JMenuItem ref = new JMenuItem("Reference");
		getHelpServer().enableHelpOnButton(ref, "astroscope.menu.reference");
		menu.insert(ref,0);
		*/
		JMenuItem sci = new JMenuItem("VOExplorer Help");
		getContext().getHelpServer().enableHelpOnButton(sci, "userInterface.voexplorer");
		menu.insert(sci,0);
		return menu;
	}

	public void clearSelection() {
		for (int i = 0; i < activities.length; i++) {
			activities[i].noneSelected();
		}		
	}

	private final JPopupMenu popup ;
	private Activity[] activities;
	public JPopupMenu getPopupMenu() {
		return popup;
	}

	public void setSelection(Transferable tran) {
		if (tran == null) {
			for (int i = 0; i < activities.length; i++) {
				activities[i].noneSelected();
			}
		} else {
			for (int i = 0; i < activities.length ; i++) {
				activities[i].selected(tran);
			}
		}		
	}
}

