/*$Id: VOExplorerImpl.java,v 1.1 2007/03/08 17:43:59 nw Exp $
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
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.collections.IteratorUtils;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryView;
import org.astrogrid.desktop.modules.ui.voexplorer.ViewController;

import com.l2fprod.common.swing.JOutlookBar;

/** Main window of voexplorer - assembles together the contributions.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 30-Mar-2005
 *
 */
public class VOExplorerImpl extends UIComponentImpl implements  ChangeListener
{
   
	public VOExplorerImpl( final HelpServerInternal hs,final UIInternal ui,
    		final Configuration conf,  final IterableObjectBuilder viewsBuilder) {
        super(conf,hs,ui);
 
        this.setSize(800, 800);    
		    getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.voexplorer");        
		    JPanel pane = getMainPanel();
		    pane.setBorder(BorderFactory.createEmptyBorder());

		    // build the hierarchies panel - a tabbed view of hierachies, which controls what's being shown in the rest of the ui. 
		    hierarchiesPanel = new JOutlookBar();
		    hierarchiesPanel.setBorder(BorderFactory.createEmptyBorder());
			CSH.setHelpIDString(hierarchiesPanel, "voexplorer.hierarchies");
			
			// actions pane.
			this.actionsCardLayout = new CardLayout(); 
			this.actionsPanel = new JPanel(this.actionsCardLayout);
			CSH.setHelpIDString(actionsPanel, "voexplorer.actions");

		    // main pane.
		    this.mainPanelCardLayour = new CardLayout(); 		    
		    this.mainPanel = new JPanel(this.mainPanelCardLayour);

		    // menu bar, and other ui stuff.
			JMenuBar jJMenuBar = new JMenuBar();
			
			// action to create a new voexplorer. defined here, as 
			// all the args we require are in scope (as we're in the contstructor at the moment)
			ActionListener newActionListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					VOExplorerImpl vo = new VOExplorerImpl(hs,ui,conf,viewsBuilder);
					vo.setVisible(true);
				}
			};			
			jJMenuBar.add(createFileMenu(newActionListener));		 
			jJMenuBar.add(createHelpMenu());
		    setJMenuBar(jJMenuBar);
		    //@todo add actions from views into menu bar too.
		    
			// go through the views, plugging them in.
		    views = (ViewController[])IteratorUtils.toArray(viewsBuilder.creationIterator(this),ViewController.class);
		    
			for (int i = 0; i < views.length ; i++) {
				ViewController v = views[i];
				final String viewName = v.getName();
				hierarchiesPanel.addTab(viewName,v.getHierarchiesPanel());
				
				final JComponent ap = v.getActionsPanel();
				if (ap != null) {
					actionsPanel.add(ap,viewName);
				}
				final JComponent mp = v.getMainPanel();
				if (mp != null) {
					mainPanel.add(mp,viewName);
				}
			}

			// listen to changes on hierarchies panel - use this to trigger change.
		    hierarchiesPanel.addChangeListener(this);

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
		    
		    // finish it all off..
			this.setContentPane(pane);
			this.setTitle("VO Explorer");
		    setIconImage(IconHelper.loadIcon("search16.png").getImage());  

    }
	
	public void displayResources(List uriList) {
		// find the ResourcesView.
		for (int i = 0; i < views.length; i++) {
			if (views[i] instanceof RegistryView) {
				RegistryView r= (RegistryView)views[i];
				// display this view.
				hierarchiesPanel.setSelectedComponent(r.getHierarchiesPanel());
				// populate some data
				r.displayResources(uriList);
			}
		}

	}

    private final ViewController[] views;
    private final  JOutlookBar hierarchiesPanel;
    private final CardLayout actionsCardLayout;
    private final CardLayout mainPanelCardLayour;
    private final JPanel mainPanel;
    private final JPanel actionsPanel;

    
 // ui construction helper methods.s
	private JMenu createFileMenu(ActionListener newActionListener) {
			JMenu fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.setMnemonic(KeyEvent.VK_F);
			final JMenuItem newMenuItem = new JMenuItem("New Window") ;
			newMenuItem.addActionListener(newActionListener);
			fileMenu.add(newMenuItem);
			fileMenu.add(new CloseAction());
			return fileMenu;
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
		getHelpServer().enableHelpOnButton(sci, "userInterface.voexplorer");
		menu.insert(sci,0);
		return menu;
	}

	/** called when outlook buttons tab changes. */
	public void stateChanged(ChangeEvent e) {
		final String viewName = hierarchiesPanel.getSelectedComponent().getName();
		//only flips if there's a corresponding component.
		mainPanelCardLayour.show(mainPanel,viewName);
		actionsCardLayout.show(actionsPanel,viewName);
	}   
}

