/*$Id: AstroScopeLauncherImpl.java,v 1.69 2007/10/12 11:04:19 nw Exp $
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.ActivityFactory;
import org.astrogrid.desktop.modules.system.ui.ArMainWindow;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContributionBuilder;
import org.astrogrid.desktop.modules.ui.comp.BiStateButton;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle;
import org.astrogrid.desktop.modules.ui.comp.DoubleDimension;
import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.NameResolvingPositionTextField;
import org.astrogrid.desktop.modules.ui.comp.PositionUtils;
import org.astrogrid.desktop.modules.ui.comp.RadiusTextField;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle.DecSexListener;
import org.astrogrid.desktop.modules.ui.scope.DalProtocol;
import org.astrogrid.desktop.modules.ui.scope.DalProtocolManager;
import org.astrogrid.desktop.modules.ui.scope.HyperbolicVizualization;
import org.astrogrid.desktop.modules.ui.scope.PrefuseGlazedListsBridge;
import org.astrogrid.desktop.modules.ui.scope.Retriever;
import org.astrogrid.desktop.modules.ui.scope.ScopeServicesList;
import org.astrogrid.desktop.modules.ui.scope.SpatialDalProtocol;
import org.astrogrid.desktop.modules.ui.scope.TemporalDalProtocol;
import org.astrogrid.desktop.modules.ui.scope.VizModel;
import org.astrogrid.desktop.modules.ui.scope.Vizualization;
import org.astrogrid.desktop.modules.ui.scope.VizualizationManager;
import org.astrogrid.desktop.modules.ui.scope.WindowedRadialVizualization;
import org.astrogrid.desktop.modules.ui.scope.ScopeHistoryProvider.SearchHistoryItem;
import org.freixas.jcalendar.JCalendarCombo;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;

public class AstroScopeLauncherImpl extends UIComponentImpl implements  AstroScopeInternal, DecSexListener, FocusListener {

        

	//@future add a application-wide tooltip formatter class - code seems to be repeated in different places at the moment.
	public static final int TOOLTIP_WRAP_LENGTH = 50;
	private static final String KEY_LINK = "KEY_LINK";

	/** safe to share this between astroscope instances - as is only ever used on single event dispatch thread */
	private final static NumberFormat nf =NumberFormat.getNumberInstance();
	static {
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(6);
		nf.setMaximumFractionDigits(6);
	}

	public AstroScopeLauncherImpl(UIContext context
			, IterableObjectBuilder protocolsBuilder
			,  final ActivityFactory activityBuilder
			, UIContributionBuilder menuBuilder	
			, EventList history
			, ScopeServicesList summary
			,FileSystemManager vfs
			,Sesame ses
			,SnitchInternal snitch)  {
		super(context);
		this.history = history;
		this.snitch = snitch;
		this.protocols = new DalProtocolManager();
		this.ses = ses;
		this.summary = summary;

		for (Iterator i = protocolsBuilder.creationIterator(); i.hasNext(); ) {
			protocols.add((DalProtocol)i.next());
		}
		
		// create the activities.
	      activities = activityBuilder.create(this);
	      JPopupMenu popup = activities.getPopupMenu();
		// create the shared model
		vizModel = new VizModel(protocols,summary,vfs);
		// create the vizualizations
		vizualizations = new VizualizationManager(vizModel);
		vizualizations.add(new WindowedRadialVizualization(vizualizations,popup,this));
		vizualizations.add(new HyperbolicVizualization(vizualizations,popup,this));

		// build the ui.
		this.setSize(1000, 707); // same proportions as A4,
		setIconImage(IconHelper.loadIcon("scope16.png").getImage());   
		FormLayout layout = new FormLayout(
				"1dlu,m,1dlu,m,1dlu"
				,"p," + protocols.getRowspec() 
				+ ",bottom:max(20dlu;p),p,p,p,p,p" // spatial
				+ ",p,bottom:max(20dlu;p),p,p,p,p" // temporal
				+ ",bottom:max(20dlu;p),m,m" // navigate
				+ ",bottom:max(20dlu;p),top:p:grow" //process
				);
		layout.setColumnGroups(new int[][]{ {2, 4} });		
		CellConstraints cc = new CellConstraints();
		PanelBuilder builder = new PanelBuilder(layout);
		
		int row = 1;
// services
		builder.addSeparator("Search for",cc.xyw(2,row,4));
		
		row++;
		// display in 2 columns
		TemporalDalProtocol tempStap = null;;
		boolean leftCol = true;
		for (Iterator i = protocols.iterator(); i.hasNext(); leftCol = ! leftCol ) {
			DalProtocol p = (DalProtocol)i.next();
			builder.add(p.getCheckBox(),cc.xy(leftCol ? 2 : 4,row));
			// while we're iterating through, look out for the stap protocol - we want to add extra logic here.
			if (p instanceof TemporalDalProtocol) {
				tempStap = (TemporalDalProtocol)p; // will need to treat this differently if there's more temporal protocols later..
			}
			if (!leftCol) {
				row++;
			}			
		}
		final TemporalDalProtocol stap = tempStap; // pesky final variables..			
		// move onto a final row if we've got an odd nummber of protocols..
		// bearing in mind the loop iterator above will have hit again at this point.
		if (!leftCol) {
			row++;
		}		
	
// spatial
		builder.addSeparator("At",cc.xyw(2,row,4));
		row++;
		builder.addLabel("Position or Object Name",cc.xyw(2,row,3));

		row++;
		posText = new NameResolvingPositionTextField(this,ses);
		posText.setAlignmentX(LEFT_ALIGNMENT);
		posText.setColumns(10);
		builder.add(posText,cc.xyw(2,row,3));

		row++;
		builder.addLabel("Search Radius (degs/arcsecs)",cc.xyw(2,row,3));

		row++;
		regionText = new RadiusTextField();
		regionText.setToolTipText("Search radius (0.008333 degs or 30.00\")");
		regionText.setAlignmentX(LEFT_ALIGNMENT);
		regionText.setColumns(10);
		builder.add(regionText,cc.xyw(2,row,3));

		row++;
		dsToggle = new DecSexToggle();
		dsToggle.addListener(posText);
		dsToggle.addListener(regionText);
		dsToggle.addListener(this);
		builder.add(dsToggle.getDegreesRadio(),cc.xy(2,row));
		builder.add(dsToggle.getSexaRadio(),cc.xy(4,row));

// temporal
		row++;
		noPosition = new JCheckBox("Any Position");
		noPosition.setToolTipText("To query with no postion, only 'Timed Data' can be selected");
		noPosition.setSelected(false);
		noPosition.addItemListener(new ItemListener() { // disables other fields when selected.
			public void itemStateChanged(ItemEvent e) {
				boolean b = e.getStateChange() == ItemEvent.DESELECTED;
				posText.setEnabled(b);
				regionText.setEnabled(b);
				dsToggle.getDegreesRadio().setEnabled(b);
				dsToggle.getSexaRadio().setEnabled(b);
			}
    	});
		// listen to what other protocols are enabled - and if they're selected, disable this control.
		protocols.getList().addListEventListener(new ListEventListener() {
			public void listChanged(ListEvent arg0) {
				while (arg0.hasNext()) {
					arg0.next();
					// don't care about the event - just need to check all other protocols.
					for (Iterator i = protocols.iterator(); i.hasNext();) {
						DalProtocol p = (DalProtocol) i.next();
						if (p != stap && p.getCheckBox().isSelected()) {
							noPosition.setSelected(false);
							noPosition.setEnabled(false);
							return;
						}
					}// end for
					// only got here if nothing else selected.
					noPosition.setSelected(true);
					noPosition.setEnabled(true);					
				}// end while.
			}
		});
		builder.add(noPosition,cc.xyw(2,row,3));
		
		row++;
		final JComponent temporalTitle = builder.addSeparator("Between",cc.xyw(2,row,4));
		
		row++;
		final JLabel temporalLabel1 = builder.addLabel("Start date && time",cc.xyw(2,row,3));
		
		row++;
        startCal = new JCalendarCombo(JCalendarCombo.DISPLAY_DATE|JCalendarCombo.DISPLAY_TIME,true);
		startCal.setNullAllowed(false);
        startCal.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        startCal.setEditable(true);
        startCal.setMaximumSize(new Dimension(50,50));
       // startCal.setAlignmentX(LEFT_ALIGNMENT);
        Calendar setStartCal = startCal.getCalendar();
        setStartCal.set(2000,0,1,0,0,0);
        startCal.setDate(setStartCal.getTime());
        builder.add(startCal,cc.xyw(2,row,3));
        
        row++;
		final JLabel temporalLabel2 = builder.addLabel("End date && time",cc.xyw(2,row,3));
		
		row++;
        endCal = new JCalendarCombo(JCalendarCombo.DISPLAY_DATE|JCalendarCombo.DISPLAY_TIME,true);
		endCal.setNullAllowed(false);
        endCal.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        endCal.setEditable(true);
        endCal.setMaximumSize(new Dimension(50,50));
       // endCal.setAlignmentX(LEFT_ALIGNMENT);
        Calendar setEndCal = endCal.getCalendar();
        setEndCal.set(2000,0,2,0,0,0);
        endCal.setDate(setEndCal.getTime());
        builder.add(endCal,cc.xyw(2,row,3));
		
        // configure all this as hidable when stap isn't in use.
        if (stap != null) {
        	JCheckBox checkBox = stap.getCheckBox();
        	checkBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					boolean b = e.getStateChange() == ItemEvent.SELECTED;
					noPosition.setVisible(b);
					temporalTitle.setVisible(b);
					temporalLabel1.setVisible(b);
					startCal.setVisible(b);
					temporalLabel2.setVisible(b);
					endCal.setVisible(b);
					/* @fixme find a nicer way to do this
					if (b) { // showing
						(new ShowOnceDialogue(AstroScopeLauncherImpl.this,
								"The time criteria are only applicable to STAP services. "
								+"\nThe other service types only accept the positional criteria."
								){}).show();
					}
					*/
				}
        	});
        	checkBox.setSelected(false);
        }
                
// navigation   
		row++;
		// start of tree navigation buttons - maybe add more here later.
		builder.addSeparator("Navigate",cc.xyw(2,row,4));
		
		row++;
		final Action haltAction = new HaltAction();	
		final Action searchAction = new SearchAction();		
		submitButton = new BiStateButton(searchAction,haltAction);
		builder.add(submitButton,cc.xyw(2,row,3));
		
		row++;
		JButton reFocusTopButton = new JButton(topAction);		
		final FocusSet sel = vizModel.getSelectionFocusSet();
		sel.addFocusListener(this);
		
		JButton clearButton = new JButton(clearAction);
		builder.add(reFocusTopButton,cc.xy(2,row));
		builder.add(clearButton,cc.xy(4,row));

		// start of activities panel.
		row++;
		builder.addSeparator("Process",cc.xyw(2,row,4));		
		
		row++; 

		final JScrollPane actionsScroll = new JScrollPane(activities.getTaskPane(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		actionsScroll.setBorder(BorderFactory.createEmptyBorder());
		actionsScroll.setMinimumSize(new Dimension(200,200));			
		builder.add(actionsScroll,cc.xyw(2,row,4));
		
		// done building the left side.
		final JPanel searchPanel = builder.getPanel();
		//@fixme this doesn't work on my mac. dunno if it doesn't work elsewhere too.
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
		searchPanel.getInputMap(searchPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, "search");
		searchPanel.getActionMap().put("search", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				submitButton.doClick(); 
			}
		});		
		JPanel pane = getMainPanel();
		pane.add(searchPanel,BorderLayout.WEST);
		
	// main part of the window
		JTabbedPane tabs = new JTabbedPane();
		// spidergrams
		for (Iterator i = vizualizations.iterator(); i.hasNext();) {
			Vizualization v = (Vizualization) i.next();
			tabs.addTab(v.getName(), v.getDisplay());
		}
	// tablular view 
		summary.parent.set(this);
		// keeps the selection models of prefuse and the summary view in-sunch..
		new PrefuseGlazedListsBridge(vizualizations,summary,tabs);
		tabs.addTab("Services", summary);
		
		pane.add(tabs, BorderLayout.CENTER);

	// menu bar
		JMenuBar mb = new JMenuBar();
		JMenu fileMenu = new JMenu();
		fileMenu.setText("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(searchAction);
		fileMenu.add(haltAction);
		fileMenu.add(clearAction);
		fileMenu.add(new JSeparator());
		fileMenu.add( new CloseAction());
		mb.add(fileMenu);
				
		JMenu historyMenu = new JMenu();
		historyMenu.setText("History");
		historyMenu.setMnemonic(KeyEvent.VK_H);
		// map the history list to menu items, and display in the history menu.
		new EventListMenuManager(new FunctionList(history,new FunctionList.Function() {
			public Object evaluate(Object arg0) {
				SearchHistoryItem i = (SearchHistoryItem)arg0;
				return new HistoryMenuItem(i);
			}
		}),historyMenu,false);
		mb.add(historyMenu);
		
		mb.add(activities.getMenu());
		
		   menuBuilder.populateWidget(mb,this,ArMainWindow.MENUBAR_NAME);
           int sz = mb.getComponentCount();
           
           JMenu help = mb.getMenu(sz-1);
           help.insertSeparator(0);
           JMenuItem sci = new JMenuItem("AstroScope: Introduction");
           getContext().getHelpServer().enableHelpOnButton(sci,"scope.intro");
           help.insert(sci,0);                 
       
       mb.add(getContext().createWindowMenu(this),sz-1); // insert before help menu.

		this.setJMenuBar(mb);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// halt all my background processes.
				haltQuery();
			}
		});

		getContext().getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.astroscopeLauncher");
		
        this.setTitle("All-VO Scope");		
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
		JMenuItem sci = new JMenuItem("Astroscope Help");
		getContext().getHelpServer().enableHelpOnButton(sci, "astroscope.menu.science");
		menu.insert(sci,0);
		return menu;
	}

	/** buffer of history items */
	private final EventList history;
	private List resourceList;
	private final SnitchInternal snitch;
	private BiStateButton submitButton;
	private final ActivitiesManager activities;	
	protected final Action clearAction = new ClearSelectionAction();
	protected final DecSexToggle dsToggle;
	protected final NameResolvingPositionTextField posText;
	protected final DalProtocolManager protocols;
	protected final RadiusTextField regionText;
	protected final Sesame ses;
	protected final Action topAction = new TopAction();
	protected final VizModel vizModel;
	protected final VizualizationManager vizualizations;
	private final JCalendarCombo startCal;
	private final JCalendarCombo endCal;
	private final JCheckBox noPosition;
	private final ScopeServicesList summary;	

// DecSexListener interface
	// another listener to the decSex toggle - convert node display
	public void degreesSelected(EventObject e) {
		toggleAndConvertNodes(vizModel.getRootNode(),false);
		vizualizations.reDrawGraphs();		
	}
	public void sexaSelected(EventObject e) {
		toggleAndConvertNodes(vizModel.getRootNode(),true);
		vizualizations.reDrawGraphs();		
	}
//	 coordinate conversion stuff.
	private void toggleAndConvertNodes(TreeNode nd,boolean fromDegrees) {
		String ndVal = null;  
		String posVal = null;
		boolean foundOffset = false;
		if((ndVal = nd.getAttribute(Retriever.OFFSET_ATTRIBUTE)) != null) {
			foundOffset = true;
			double val;
			if(fromDegrees) {
				val = Double.parseDouble(ndVal) * 3600;                
			} else {
				val = Double.parseDouble(ndVal);
			}
			nd.setAttribute(Retriever.LABEL_ATTRIBUTE,nf.format(val));
			nd.setAttribute(Retriever.TOOLTIP_ATTRIBUTE,String.valueOf(val));
			for(int i = 0;i < nd.getChildCount();i++) {
				TreeNode childNode = nd.getChild(i);
				//should be a position string.
				ndVal = childNode.getAttribute(Retriever.LABEL_ATTRIBUTE);
				if(fromDegrees) {
					posVal = PositionUtils.getRASexagesimal(ndVal) + "," + PositionUtils.getDECSexagesimal(ndVal);
				}else {
					posVal = nf.format(PositionUtils.getRADegrees(ndVal)) + "," + nf.format(PositionUtils.getDECDegrees(ndVal));
				}
				childNode.setAttribute(Retriever.LABEL_ATTRIBUTE,posVal);
				childNode.setAttribute(Retriever.TOOLTIP_ATTRIBUTE,posVal);
			}
		}//if
		for(int i = 0;i < nd.getChildCount();i++) {
			if(!foundOffset)
				toggleAndConvertNodes(nd.getChild(i), fromDegrees);
		}
	}	
	
//Astroscope internal interface.
	// configure to run against this list of services.
	public void runSubset(List resources) {
		this.resourceList = resources;
		StrBuilder sb = new StrBuilder("VOScope");
		if (resources.size() == 1) {
		    sb.append(" - ").append(((Resource)resources.get(0)).getTitle());
		}
		for (Iterator i = protocols.iterator(); i.hasNext(); ) {
			DalProtocol p = (DalProtocol)i.next();
			p.getCheckBox().setEnabled(false); // no point showing these - there's no option.
			Service[] services = p.filterServices(resources);
			if (services != null && services.length > 0) {
			    p.getCheckBox().setSelected(true);
			    sb.append(" - ")
			        .append(services.length)
			        .append(" ")
			        .append(p.getName())
			        .append(" service");			    
			} else {
			    p.getCheckBox().setSelected(false);
			}
		}	
		setTitle(sb.toString());
	}
	public Object create() {
		return null; // @todo will refactor this away later.
	}
// innards of the scope itself.

	/** perform a query */
	protected void query() {
		// slightly tricky - what we do depends on whether posText is currently in the middle of resolving a name or not.
		final String positionString = posText.getObjectName(); // grab this first, in case we need it in a mo.
		Point2D position = posText.getPosition();	
		if (Double.isNaN(position.getX())) { // position is not a number - indicates that it's currently being resolved.
			// so we'll resolve it ourselves - simplest thing to do - if we've got the position string..
			// hopefully we've got something we can work with..
			if (positionString != null) {
				(new BackgroundOperation("Resolving object " + positionString) {
					protected Object construct() throws Exception {
						return ses.resolve(positionString.trim());
					}
					protected void doError(Throwable ex) {
						showError("Simbad failed to resolve " + positionString,ex);
					}
					protected void doFinished(Object result) {
						SesamePositionBean pb = (SesamePositionBean)result;
						Point2D pos = new Point2D.Double(pb.getRa(),pb.getDec());
						// now on with the query
						queryBody(pos);
					}
				}).start();
			} else { // no position, and no objectName 
				//blergh - caught it at just thw wrong time - lets try again.
				query();
			}
		} else {
			// everything is fine - so we'll query directly.
			queryBody(position);
		}
	}	
	/** actually do the query */
	private void queryBody(Point2D position) {  
		setStatusMessage("" + position.getX() + ',' + position.getY());
		clearTree();
		topAction.setEnabled(true);
		setProgressMax(0);
		// ok. everything looks valid. add a task to later on storee the history item
		// make this a later task, so that resolver thread has chance to return.
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				storeHistoryItem();
			}
		});

		final double ra = position.getX();
		final double dec = position.getY();

		final double radius = regionText.getRadius();

		for (Iterator i = protocols.iterator(); i.hasNext(); ) {
			final DalProtocol p =(DalProtocol)i.next();
			if (p.getCheckBox().isSelected()) {
				(new BackgroundOperation("Searching for " + p.getName() + " Services") {
					protected Object construct() throws Exception {
						if (resourceList == null) {
							return p.listServices();
						} else {
							return p.filterServices(resourceList);
						}
					}
					protected void doFinished(Object result) {
						Service[] services = (Service[])result;
						parent.showTransientMessage(services.length + " " + p.getName() + " services to query","");
						setProgressMax(getProgressMax() + services.length);
						if (p instanceof SpatialDalProtocol) {
							SpatialDalProtocol spatial = (SpatialDalProtocol)p;
							for (int i = 0; i < services.length; i++) {
								spatial.createRetriever(AstroScopeLauncherImpl.this,services[i],ra,dec,radius,radius).start();
							}                            
						} else if (p instanceof TemporalDalProtocol) {
							TemporalDalProtocol temporal = (TemporalDalProtocol)p;
							Calendar start = startCal.getCalendar();
							Calendar end = endCal.getCalendar();
							for (int i = 0; i < services.length; i++) {
								if (noPosition.isSelected()) { // zero out the positional fields.
									temporal.createRetriever(AstroScopeLauncherImpl.this,services[i],start,end,Double.NaN,Double.NaN,Double.NaN,Double.NaN).start();
								} else {
									temporal.createRetriever(AstroScopeLauncherImpl.this,services[i],start,end,ra,dec,radius,radius).start();
								}
							}      							
						}
					}                            
				}).start();
			}
		}
	}
	protected void haltQuery() {
		haltMyTasks();
		setProgressValue(getProgressMax());
	}
	/** removes previous results, just leaving the skeleton */
	protected void clearTree() {
		// reset selection too.
		vizModel.clear();
		vizualizations.refocusMainNodes();
		setProgressMax(1);
		setProgressValue(0);
	}
	/**
	 * take a snapshot of current form inputs, and ad to the history list.
	 * Call this at an appropriate point withing 'query()' to save searcg state to history.
	 */
	protected void storeHistoryItem() {
		SearchHistoryItem shi = new SearchHistoryItem();
		shi.setPosition(posText.getPositionAsSesameBean());
		// blank out aliases - as long, and unneeded. - for storage efficiency.
		shi.getPosition().setAliases(null);
        double radius = regionText.getRadius();
		shi.setRadius(new DoubleDimension(radius,radius));
		history.add(0,shi); // insert at top of list.
	}


	/** implementation of an item in the history menu.
	 * 
	 * each listens to decSexToggle - to change representation from one system to other.
	 * @author Noel Winstanley
	 * @since May 17, 20067:34:18 PM
	 */
	private class HistoryMenuItem extends JMenuItem implements ActionListener, DecSexListener{

		/**
		 * @param i
		 */
		public HistoryMenuItem(SearchHistoryItem i) {
			this.shi = i;
			if (dsToggle.isDegrees()) {
				this.degreesSelected(null);
			} else {
				this.sexaSelected(null);
			}
			this.addActionListener(this);
			dsToggle.addListener(this);
		}
		private final SearchHistoryItem shi;

		public void actionPerformed(ActionEvent e) {
					posText.setPosition(shi.getPosition().getRa(),shi.getPosition().getDec());
					regionText.setRadius(shi.getRadius().getWidth());
		}

		public void degreesSelected(EventObject ignored) {
			StringBuffer sb = new StringBuffer();
			try {
				if (shi.getPosition().getOName() != null) {
					sb.append("Object: ").append(shi.getPosition().getOName()).append(", ");
				}
				sb.append("position: ")
				.append(nf.format(shi.getPosition().getRa()))
				.append(',')
				.append(nf.format(shi.getPosition().getDec()))
				.append(", radius: ");
                double rw = shi.getRadius().getWidth();
                double rh = shi.getRadius().getHeight();
                if (rw == rh) {
                    sb.append(nf.format(rw));
                }
                else {
                    sb.append(nf.format(rw))
                      .append(',')
                      .append(nf.format(rh));
                }
			} catch (NumberFormatException e) {
				// don't care too much.
			}
			setText(sb.toString());
		}

		public void sexaSelected(EventObject ignored) {
			StringBuffer sb = new StringBuffer();
			try {
				if (shi.getPosition().getOName() != null) {
					sb.append("Object: ").append(shi.getPosition().getOName()).append(", ");
				}
				// thought - could use the sexa string provided in the position
				// bean, if it's there - although this might be a bit inconsistent
				// as we're converting everywhere else.
				sb.append("position: ")
				.append(PositionUtils.decimalToSexagesimal(
						shi.getPosition().getRa()
						,shi.getPosition().getDec()))
				.append(" radius: "); 
                double rw = shi.getRadius().getWidth();
                double rh = shi.getRadius().getHeight();
                if (rw == rh) {
                    sb.append(RadiusTextField.formatAsArcsec(rw)).append('"');
                }
                else {
                    sb.append(RadiusTextField.formatAsArcsec(rw)).append('"')
                      .append(',')
                      .append(RadiusTextField.formatAsArcsec(rh)).append('"');
                }
			} catch (NumberFormatException e) {
				// don't care too much.
			}
			setText(sb.toString());		
		}
	}
	
// focus listener interface - links in the activities, etc.
	public void focusChanged(FocusEvent arg0) {
		final boolean somethingSelected = vizModel.getSelectionFocusSet().size() > 0;
		clearAction.setEnabled(somethingSelected);
		if (somethingSelected) {
			Transferable tran = vizModel.getSelectionTransferable();
			if (tran == null) { // unlikely
				activities.clearSelection();
			} else {
				activities.setSelection(tran);
			}
		} else {
			activities.clearSelection();			
		}
	}
	
// action classes
	/** clear selection action */
	protected class ClearSelectionAction extends AbstractAction {
		public ClearSelectionAction() {
			super("Clear selection");//,IconHelper.loadIcon("editclear32.png"));
			this.putValue(SHORT_DESCRIPTION,"Clear selected nodes");
			this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
			this.setEnabled(false);           
		}

		public void actionPerformed(ActionEvent e) {
			FocusSet set = vizModel.getSelectionFocusSet();
			for (Iterator i = set.iterator(); i.hasNext();) {
				TreeNode n = (TreeNode) i.next();
				// i.remove();
				n.setAttribute("selected", "false");
			}
			set.clear();
			summary.getCurrentResourceModel().clearSelection();
			vizualizations.reDrawGraphs();
			this.setEnabled(false);
		}    	
	}

	/** halt seartch action */
	protected class HaltAction extends AbstractAction {
		public HaltAction() {
			super("Halt",IconHelper.loadIcon("stop32.png"));
			this.putValue(SHORT_DESCRIPTION,"Halt the search");
		}

		public void actionPerformed(ActionEvent e) {
			submitButton.enableA();
			haltQuery();
		}
	}

	/** search action */
	protected class SearchAction extends AbstractAction {
		public SearchAction() {
			super("Search",IconHelper.loadIcon("search32.png"));
			this.putValue(SHORT_DESCRIPTION,"Find resources for this Position");
		}

		public void actionPerformed(ActionEvent e) {
			submitButton.enableB();
			Map m = new HashMap();
			m.put("name","VO Scope");
			snitch.snitch("SUBMIT",m);
			query();		
		}
	}


	/** goto top button */

	protected class TopAction extends AbstractAction {
		public TopAction() {
			super("Go To Top"); //,IconHelper.loadIcon("top32.png"));
			this.putValue(SHORT_DESCRIPTION,"Focus display to 'Search Results'");
			this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
			this.setEnabled(false);
		}
		public void actionPerformed(ActionEvent e) {
			vizualizations.refocusMainNodes();
			vizualizations.reDrawGraphs();
		}
	}

}
