/*$Id: AstroScopeLauncherImpl.java,v 1.103 2010/01/04 16:08:06 nw Exp $
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.acr.ivoa.resource.SsapService;
import org.astrogrid.acr.ivoa.resource.StapService;
import org.astrogrid.acr.ivoa.resource.SiapCapability.Query;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ConfirmDialog;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.ResourceConsumer;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.ProgrammerError;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.Tuple;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.ActivityFactory;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.actions.InfoActivity;
import org.astrogrid.desktop.modules.ui.actions.MessagingScavenger;
import org.astrogrid.desktop.modules.ui.actions.MultiConeActivity;
import org.astrogrid.desktop.modules.ui.actions.SimpleDownloadActivity;
import org.astrogrid.desktop.modules.ui.actions.ViewInBrowserActivity;
import org.astrogrid.desktop.modules.ui.comp.BiStateButton;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle;
import org.astrogrid.desktop.modules.ui.comp.DoubleDimension;
import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.comp.PositionUtils;
import org.astrogrid.desktop.modules.ui.comp.RadiusTextField;
import org.astrogrid.desktop.modules.ui.comp.TimerDrivenNameResolvingPositionTextField;
import org.astrogrid.desktop.modules.ui.comp.UserDrivenNameResolvingPositionTextField;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle.DecSexListener;
import org.astrogrid.desktop.modules.ui.comp.TimerDrivenNameResolvingPositionTextField.ResolutionEvent;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.fileexplorer.IconFinder;
import org.astrogrid.desktop.modules.ui.scope.AbstractRetriever;
import org.astrogrid.desktop.modules.ui.scope.DalProtocol;
import org.astrogrid.desktop.modules.ui.scope.DalProtocolManager;
import org.astrogrid.desktop.modules.ui.scope.HyperbolicVizualization;
import org.astrogrid.desktop.modules.ui.scope.PositionHistoryItem;
import org.astrogrid.desktop.modules.ui.scope.Retriever;
import org.astrogrid.desktop.modules.ui.scope.ScopeServicesList;
import org.astrogrid.desktop.modules.ui.scope.SpatialDalProtocol;
import org.astrogrid.desktop.modules.ui.scope.TemporalDalProtocol;
import org.astrogrid.desktop.modules.ui.scope.VizModel;
import org.astrogrid.desktop.modules.ui.scope.VizualizationController;
import org.astrogrid.desktop.modules.ui.scope.VizualizationsPanel;
import org.astrogrid.desktop.modules.ui.scope.WindowedRadialVizualization;
import org.freixas.jcalendar.JCalendarCombo;
import org.joda.time.DateTime;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * implementation of astroscope.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 3, 20083:25:44 PM
 */
public class AstroScopeLauncherImpl extends UIComponentImpl implements  AstroScopeInternal, DecSexListener, FocusListener {

    /**
     * 
     */
    private static final int SEARCH_RADIUS_WARNING_THRESHOLD = 1;
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(AstroScopeLauncherImpl.class);
    private static final String SHOW_ASTROSCOPE_POPUPS_KEY = "show.astroscope.popups";
         

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

	public AstroScopeLauncherImpl(final UIContext context
			, final IterableObjectBuilder<DalProtocol> protocolsBuilder
			,  final ActivityFactory activityBuilder
			, final TypesafeObjectBuilder uiBuilder
			, final EventList<PositionHistoryItem> history
			,final FileSystemManager vfs
			,final Sesame ses
			,final SnitchInternal snitch
			,final IconFinder iconFinder)  {
		super(context,"All-VO Astroscope","window.scope");
		this.history = history;
		this.snitch = snitch;
		this.protocols = new DalProtocolManager();
		this.ses = ses;
		// create the activities.
		acts = activityBuilder.create(this, new Class[]{
		        ViewInBrowserActivity.class
		        ,SimpleDownloadActivity.class
		        ,MultiConeActivity.class
		        ,MessagingScavenger.class
		        ,InfoActivity.class
		});
		final JPopupMenu popup = acts.getPopupMenu();
		// create summary after acts - as it relies upon
		this.servicesList = uiBuilder.createScopeServicesList(this,acts);

		for (final Iterator<DalProtocol> i = protocolsBuilder.creationIterator(); i.hasNext(); ) {
			protocols.add(i.next());
		}
		
		// create the shared model
		vizModel = new VizModel(this,protocols,servicesList,vfs,iconFinder);
		// create the vizualizations
		vizualizations = new VizualizationController(vizModel);
		final WindowedRadialVizualization radialViz = new WindowedRadialVizualization(vizualizations,popup,this);
        vizualizations.add(radialViz);
		final HyperbolicVizualization hyperbolicViz = new HyperbolicVizualization(vizualizations,popup,this);
        vizualizations.add(hyperbolicViz);

		// build the ui.
		this.setSize(1000, 707); // same proportions as A4,
		setIconImage(IconHelper.loadIcon("scope16.png").getImage());   
		final FormLayout layout = new FormLayout(
				"1dlu,m,1dlu,m,p,1dlu"
				,"p," + protocols.getRowspec() 
				+ ",bottom:max(20dlu;p),p,p,p,p,p" // spatial
				+ ",p,bottom:max(20dlu;p),p,p,p,p" // temporal
				+ ",bottom:max(20dlu;p),m,m" // navigate
				+ ",bottom:max(20dlu;p),fill:p:grow" //process
				);
		layout.setColumnGroups(new int[][]{ {2, 4} });		
		final CellConstraints cc = new CellConstraints();
		final PanelBuilder builder = new PanelBuilder(layout);
		
		int row = 1;
// services
		builder.addSeparator("Search for",cc.xyw(2,row,5));
		
		row++;
		// display in 2 columns
		TemporalDalProtocol tempStap = null;;
		boolean leftCol = true;
		for (final Iterator<DalProtocol> i = protocols.iterator(); i.hasNext(); leftCol = ! leftCol ) {
			final DalProtocol p = i.next();
			builder.add(p.getCheckBox(),cc.xyw(leftCol ? 2 : 4,row, leftCol ? 1 : 2));
			// while we're iterating through, look out for the stap protocol - we want to add extra logic here.
			if (p instanceof TemporalDalProtocol) {
				tempStap = (TemporalDalProtocol)p; // will need to treat this differently if there's more temporal protocols later..
			}
			if (!leftCol) {
				row++;
			}			
		}
		stap = tempStap;
        // move onto a final row if we've got an odd nummber of protocols..
		// bearing in mind the loop iterator above will have hit again at this point.
		if (!leftCol) {
			row++;
		}		
	
// spatial
		builder.addSeparator("At",cc.xyw(2,row,5));
		row++;
		builder.addLabel("Position (RA,Dec) or Object Name",cc.xyw(2,row,4));

		row++;
	//	posText = new TimerDrivenNameResolvingPositionTextField(this,ses);
		posText = new UserDrivenNameResolvingPositionTextField(this,ses);
		posText.setAlignmentX(LEFT_ALIGNMENT);
		posText.setColumns(10);
		builder.add(posText,cc.xyw(2,row,3));
		builder.add(posText.getProgressButton(),cc.xy(5,row));

		row++;
		builder.addLabel("Search Radius (degs/arcsecs)",cc.xyw(2,row,5));

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
		//if we start as sexa,  servicesList and other visualizations needs initializing.
		if (!dsToggle.isDegrees()) {
		    this.sexaSelected(null);
		}
		builder.add(dsToggle.getDegreesRadio(),cc.xy(2,row));
		builder.add(dsToggle.getSexaRadio(),cc.xyw(4,row,2));

// temporal
		row++;
		noPosition = new JCheckBox("Any Position");
		noPosition.setToolTipText("To query with no postion, only 'Timed Data' can be selected");
		noPosition.setSelected(false);
		noPosition.addItemListener(new ItemListener() { // disables other fields when selected.
			public void itemStateChanged(final ItemEvent e) {
				final boolean b = e.getStateChange() == ItemEvent.DESELECTED;
				posText.setEnabled(b);
				regionText.setEnabled(b);
				dsToggle.getDegreesRadio().setEnabled(b);
				dsToggle.getSexaRadio().setEnabled(b);
			}
    	});
		// listen to what other protocols are enabled - and if they're selected, disable this control.
		protocols.getList().addListEventListener(new ListEventListener<DalProtocol>() {
			public void listChanged(final ListEvent<DalProtocol> arg0) {
				while (arg0.hasNext()) {
					arg0.next();
					// don't care about the event - just need to check all other protocols.
					for (final DalProtocol p : protocols) {
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
		builder.add(noPosition,cc.xyw(2,row,4));
		
		row++;
		final JComponent temporalTitle = builder.addSeparator("Between",cc.xyw(2,row,5));
		
		row++;
		final JLabel temporalLabel1 = builder.addLabel("Start date && time",cc.xyw(2,row,4));
		
		row++;
        startCal = new JCalendarCombo(JCalendarCombo.DISPLAY_DATE|JCalendarCombo.DISPLAY_TIME,true);
		startCal.setNullAllowed(false);
        startCal.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        startCal.setEditable(true);
        startCal.setMaximumSize(new Dimension(50,50));
       // startCal.setAlignmentX(LEFT_ALIGNMENT);
        final Calendar setStartCal = startCal.getCalendar();
        setStartCal.set(2000,0,1,0,0,0);
        startCal.setDate(setStartCal.getTime());
        builder.add(startCal,cc.xyw(2,row,4));
        
        row++;
		final JLabel temporalLabel2 = builder.addLabel("End date && time",cc.xyw(2,row,4)); // double ampersand is intentional.
		
		row++;
        endCal = new JCalendarCombo(JCalendarCombo.DISPLAY_DATE|JCalendarCombo.DISPLAY_TIME,true);
		endCal.setNullAllowed(false);
        endCal.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        endCal.setEditable(true);
        endCal.setMaximumSize(new Dimension(50,50));
       // endCal.setAlignmentX(LEFT_ALIGNMENT);
        final Calendar setEndCal = endCal.getCalendar();
        setEndCal.set(2000,0,2,0,0,0);
        endCal.setDate(setEndCal.getTime());
        builder.add(endCal,cc.xyw(2,row,4));
		
        // configure all this as hidable when stap isn't in use.
        if (stap != null) {
        	final JCheckBox checkBox = stap.getCheckBox();
        	checkBox.addItemListener(new ItemListener() {
				public void itemStateChanged(final ItemEvent e) {
					final boolean b = e.getStateChange() == ItemEvent.SELECTED;
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
		builder.addSeparator("Navigate",cc.xyw(2,row,5));
		
		row++;
		final Action haltAction = new HaltAction();	
		final Action searchAction = new SearchAction(posText);		
		submitButton = new BiStateButton(searchAction,haltAction);
		builder.add(submitButton,cc.xyw(2,row,4));
		
		row++;
		final FocusSet sel = vizModel.getSelectionFocusSet();
		sel.addFocusListener(this);
		
		builder.add(new JButton(topAction),cc.xy(2,row));
		builder.add(new JButton(clearAction),cc.xyw(4,row,2));

		// start of activities panel.
		row++;
		builder.addSeparator("Process",cc.xyw(2,row,5));		
		
		row++; 

		final JScrollPane actionsScroll = new JScrollPane(acts.getTaskPane(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		actionsScroll.setBorder(BorderFactory.createEmptyBorder());
		actionsScroll.setMinimumSize(new Dimension(200,200));			
		builder.add(actionsScroll,cc.xyw(2,row,5));
		
		// done building the left side.
		final JPanel searchPanel = builder.getPanel();
		CSH.setHelpIDString(searchPanel,"scope.searchPanel");
		final KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
		final String CMD = "pressButton";
		searchPanel.getInputMap(searchPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, CMD);
		searchPanel.getActionMap().put(CMD, new AbstractAction() {
			public void actionPerformed(final ActionEvent e) {
				submitButton.doClick(); 
			}
		});		
		final JPanel pane = getMainPanel();
		pane.add(searchPanel,BorderLayout.WEST);

		
	// main part of the window
		flip = new VizualizationsPanel(vizualizations,radialViz,hyperbolicViz,servicesList);
		flip.addPropertyChangeListener(FlipPanel.SHOWING,new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                topAction.setEnabled(! flip.currentlyServicesTable());                
            }
		});
		//initialize this too.
		topAction.setEnabled(! flip.currentlyServicesTable());
		
		pane.add(flip, BorderLayout.CENTER);
		showTransientWarnings = new JCheckBoxMenuItem("Show Popup Warnings");
		showTransientWarnings.setSelected(PREFERENCES.getBoolean(SHOW_ASTROSCOPE_POPUPS_KEY,true));
        showTransientWarnings.setToolTipText("Show warnings about service failures in transient popup windows");
		showTransientWarnings.addItemListener(new ItemListener() {
		    public void itemStateChanged(final ItemEvent e) {
		        PREFERENCES.putBoolean(SHOW_ASTROSCOPE_POPUPS_KEY
		                ,ItemEvent.SELECTED == e.getStateChange());
		    }
		});
		final UIComponentMenuBar menuBar = new UIComponentMenuBar(this) {
            @Override
            protected void populateEditMenu(final EditMenuBuilder emb) {
                //@fixme hook these operations into the result displays.
                emb.cut() // fields only
                    .copy() // fields & results
                    .paste() //fields only
                    .selectAll() // fields or results
                    .clearSelection() // fields & results  clearAction for results.
                    .invertSelection(); // results only.
            }

            @Override
            protected void populateFileMenu(final FileMenuBuilder fmb) {
                fmb
                // @todo select services - requires reg google - maybe. or may just leave it.
                    .windowOperation(searchAction)
                    .windowOperation(haltAction)
                    .separator();
                for(final DalProtocol p : protocols ) {
                    fmb.checkbox(p.getMenuItemCheckBox());
                }
                fmb.separator();
                fmb.closeWindow();
            }
            
            @Override
            protected void constructAdditionalMenus() {
                final MenuBuilder vmb = new MenuBuilder("View",KeyEvent.VK_V)
                       .windowOperation(topAction)
                       .windowOperation(upAction)
                       .separator();
                    final JRadioButtonMenuItem radial = new JRadioButtonMenuItem(flip.getRadialAction());
                    final JRadioButtonMenuItem hyper = new JRadioButtonMenuItem(flip.getHyperbolicAction());
                    servicesRadioButton = new JRadioButtonMenuItem(flip.getServicesAction());
                    final ButtonGroup bg = new ButtonGroup();
                    bg.add(servicesRadioButton);
                    bg.add(radial);
                    bg.add(hyper);
 
                    if (flip.currentlyHyperbolic()) {
                        hyper.setSelected(true);
                    } else if (flip.currentlyRadial()) {
                        radial.setSelected(true);
                    } else if (flip.currentlyServicesTable()) {
                        servicesRadioButton.setSelected(true);
                    }
                       vmb.radiobox(servicesRadioButton)
                       .radiobox(radial)
                           .radiobox(hyper)
                           
                       .separator()                           
                       .windowOperationWithIcon(flip.getShowServicesFiltersAction())
                       .separator()
                       .checkbox(showTransientWarnings)
                       ;                
                add(vmb.create());
                // toggle viz-specific actions off when services actions are on.
                flip.getShowServicesFiltersAction().addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(final PropertyChangeEvent evt) {
                        if ("enabled".equals(evt.getPropertyName())) {
                            topAction.setEnabled(! flip.getShowServicesFiltersAction().isEnabled());
                            upAction.setEnabled(! flip.getShowServicesFiltersAction().isEnabled());
                        }
                    }
                });
                // initialize these actions too.
                topAction.setEnabled(!flip.getShowServicesFiltersAction().isEnabled());
                upAction.setEnabled(!flip.getShowServicesFiltersAction().isEnabled());
                //history menu
                final JMenu historyMenu = new JMenu("History");
                historyMenu.setMnemonic(KeyEvent.VK_H);
                final FilterList<PositionHistoryItem> filteredHistory = new FilterList<PositionHistoryItem>(history,new ProtocolsMatcherEditor());
                // map the history list to menu items, and display in the history menu.
                new EventListMenuManager(new FunctionList<PositionHistoryItem,JMenuItem>(filteredHistory
                        ,new FunctionList.Function<PositionHistoryItem,JMenuItem>() {
                    public JMenuItem evaluate(final PositionHistoryItem i) {
                        return new HistoryMenuItem(i);
                    }
                }),historyMenu,false);
                historyMenu.addSeparator();
                historyMenu.add(new ClearHistoryAction());
                add(historyMenu);
                historyMenu.setEnabled(true); // can start off not.
                
                final MenuBuilder rmb = new MenuBuilder("Result",KeyEvent.VK_R);
                rmb
                    .windowOperation(acts.getActivity(ViewInBrowserActivity.class))
                    .windowOperation(acts.getActivity(SimpleDownloadActivity.class))
                    ;
               acts.getActivity(MessagingScavenger.class).addTo(rmb.getMenu());
               add(rmb.create());
            }
		};					
		setJMenuBar(menuBar);

		this.addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosing(final WindowEvent e) {
				// halt all my background processes.
				haltQuery();
			}
			@Override
			public void windowOpened(final WindowEvent e) {
			    posText.requestFocusInWindow();
			    posText.selectAll();
			}
		});
	
	}



	/** buffer of history items */
	private final EventList<PositionHistoryItem> history;
	private List<? extends Resource> resourceList;
	private final SnitchInternal snitch;
	private final BiStateButton submitButton;
	final ActivitiesManager acts;	
	protected final VizualizationsPanel flip;


    protected final Action clearAction = new ClearSelectionAction();

	protected final DecSexToggle dsToggle;
	//protected final TimerDrivenNameResolvingPositionTextField posText;
	protected final UserDrivenNameResolvingPositionTextField posText;
	protected final DalProtocolManager protocols;
	protected final RadiusTextField regionText;
	protected final Sesame ses;
	private final Action topAction = new TopAction();
	protected final Action upAction = new UpAction();
	protected final VizModel vizModel;
	protected final VizualizationController vizualizations;
	private final JCalendarCombo startCal;
	private final JCalendarCombo endCal;
	private final JCheckBox noPosition;
	private final ScopeServicesList servicesList;
    private final JCheckBoxMenuItem showTransientWarnings;
    private final TemporalDalProtocol stap;
    /** radio button that switches to services view */
    private JRadioButtonMenuItem servicesRadioButton;	

    public final boolean isTransientWarnings() {
        return showTransientWarnings.isSelected();
    }
    
	public final VizModel getVizModel() {
	    return vizModel;
	}
	
// DecSexListener interface
	// another listener to the decSex toggle - convert node display
	public void degreesSelected(final EventObject e) {
	    vizModel.setSexagesimal(false);
	    vizModel.toggleAndConvertNodes(vizModel.getTree().getRoot());
	    if (isShowing()) {
	        vizualizations.reDrawGraphs();
            servicesList.redrawResultsTable();
	    }
	}
	public void sexaSelected(final EventObject e) {
	    vizModel.setSexagesimal(true);
	    vizModel.toggleAndConvertNodes(vizModel.getTree().getRoot());
	    if (isShowing()) {
	        vizualizations.reDrawGraphs();
	        servicesList.redrawResultsTable();
	    }
	}
	
	
//Astroscope internal interface.
	// configure to run against this list of services.
	public void runSubset(final List<? extends Resource> resources) {
	    this.resourceList = resources;
	    final StringBuilder sb = new StringBuilder("Astroscope");
	    if (resources.size() == 1) {
	        sb.append(" - ").append(((Resource)resources.get(0)).getTitle());
	    } 
	    for (final DalProtocol p : protocols) {                 
	        p.getCheckBox().setEnabled(false); // no point showing these - there's no option.
	        final CountServices cs = new CountServices();
	        p.processSuitableServicesInList(resources,cs);
	        if (cs.length > 0) {
	                    p.getCheckBox().setSelected(true);
	                if (resources.size() > 1) {
	                    sb.append(" - ")
	                    .append(cs.length)
	                    .append(" ")
	                    .append(p.getName().endsWith("s") ? StringUtils.substringBeforeLast(p.getName(),"s") : p.getName());
	                    sb.append(cs.length == 1 ? " Service" : " Services"); 
	                }
	        } else {
	            p.getCheckBox().setSelected(false);
	        }
	    }
	    setTitle(sb.toString());
	    // find the first resource in the list which has a testQuery of some kind.
	    for (final Resource r : resources) {
            if (r instanceof SiapService &&  ((SiapService)r).findSiapCapability().getTestQuery() != null) {
                final Query testQuery = ((SiapService)r).findSiapCapability().getTestQuery();
                posText.setPosition(testQuery.getPos().getLong(),testQuery.getPos().getLat()); //@todo check I've got these the correct way rouind.
                regionText.setRadius(testQuery.getSize().getLong()); // can only set region to a single dim - not both. no matter.
                break; // no need to continue
            }
            if (r instanceof ConeService && ((ConeService)r).findConeCapability().getTestQuery() != null) {
                final org.astrogrid.acr.ivoa.resource.ConeCapability.Query testQuery = ((ConeService)r).findConeCapability().getTestQuery();
                posText.setPosition(testQuery.getRa(),testQuery.getDec()); //
                regionText.setRadius(testQuery.getSr()); 
                break; // no need to continue
            }
            if (r instanceof SsapService && ((SsapService)r).findSsapCapability().getTestQuery() != null) {
                final org.astrogrid.acr.ivoa.resource.SsapCapability.Query testQuery = ((SsapService)r).findSsapCapability().getTestQuery();
                posText.setPosition(testQuery.getPos().getLong(),testQuery.getPos().getLat()); //@todo check I've got these the correct way rouind.
                regionText.setRadius(testQuery.getSize());                 
                break; // no need to continue
            }
            if (r instanceof StapService && ((StapService)r).findStapCapability().getTestQuery() != null) {
                final org.astrogrid.acr.ivoa.resource.StapCapability.Query testQuery = ((StapService)r).findStapCapability().getTestQuery();
                posText.setPosition(testQuery.getPos().getLong(),testQuery.getPos().getLat()); //@todo check I've got these the correct way rouind.
                regionText.setRadius(testQuery.getSize().getLong());
                try {
                    DateTime dt = new DateTime(testQuery.getStart()); // jodatime parses ISO format dates.
                    startCal.setDate(dt.toDate() );
                    dt = new DateTime(testQuery.getEnd());
                    endCal.setDate(dt.toDate());
                } catch (final IllegalArgumentException e) {
                    logger.warn("Failed to set date",e);
                }
                break; // no need to continue
            }
        }
	}
	
	
	/** count the number of services */
	private static class CountServices implements ResourceConsumer {

	    public int length = 0;
        public void process(final Resource s) {
            length++;
        }
        public void estimatedSize(final int i) {
        }
	}
	
	   public void runSubsetAsHelioscope(final List<? extends Resource> resources) {
	       runSubset(resources);
	       setTitle("All-VO Helioscope: standard set of timed solar data services");
	   }
	
	public Object create() {
		return null; // @todo will refactor this away later.
	}
// innards of the scope itself.

	/** perform a query */
	protected void query() {
	    for (final DalProtocol p : protocols) {
            final TreeNode rootNode = vizModel.getTree().getRoot();
            if (!p.getCheckBox().isSelected()) {
                // remove this protocol node - we're not searching on it.
                //@todo replace removal with just hiding it.
                if (rootNode.isChild(p.getPrimaryNode())) {
                    rootNode.removeChild(p.getPrimaryNode());
                }
            } else {
                // previously this protocol has been removed - splice it back in
                if (!rootNode.isChild(p.getPrimaryNode())) {
                    final DefaultEdge edge = new DefaultEdge(rootNode,p.getPrimaryNode());
                    //blemish - code coopied verbatim from vizModel
                    edge.setAttribute(AbstractRetriever.WEIGHT_ATTRIBUTE,"3");                  
                    rootNode.addChild(edge);
                }
            }
        }
	       clearTree();
	       
		// slightly tricky - what we do depends on whether posText is currently in the middle of resolving a name or not.
	       ///@todo not working nicely at the moment - need to rethink all this.
		final String positionString = posText.getObjectName(); // grab this first, in case we need it in a mo.
		final Point2D position = posText.getPosition();	
		if (Double.isNaN(position.getX())) { // position is not a number - indicates that it's currently being resolved.
			// so we'll resolve it ourselves - simplest thing to do - if we've got the position string..
			// hopefully we've got something we can work with..
			if (positionString != null) {
				(new BackgroundOperation<SesamePositionBean>("Resolving object " + positionString,BackgroundWorker.SHORT_TIMEOUT,Thread.MAX_PRIORITY) {
					@Override
                    protected SesamePositionBean construct() throws Exception {
						return ses.resolve(positionString.trim());
					}
					@Override
                    protected void doError(final Throwable ex) {
						showTransientError("Simbad failed to resolve " + positionString,ex.getMessage());
						submitButton.enableA(); 
					}
					@Override
                    protected void doFinished(final SesamePositionBean pb) {
						final Point2D pos = new Point2D.Double(pb.getRa(),pb.getDec());
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
	private void queryBody(final Point2D position) {  
		setStatusMessage("" + position.getX() + ',' + position.getY());
		topAction.setEnabled(true);
		upAction.setEnabled(true);
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
		if (radius > SEARCH_RADIUS_WARNING_THRESHOLD) { // issue warning.
		  showTransientWarning("Large Search Radius","Some services may not allow this large a search radius; others may return limited results, or fail to respond at all. " +
		           "<br>Check the resource descriptions of the services you are querying for details."
		          + (! currentlyServicesTable() ?
		  		    "<br>If too many results are returned, it may help to switch to 'View > as Services Table' "
		          : "")
		  );  
		}
		for (final DalProtocol p : protocols) {
			final TreeNode rootNode = vizModel.getTree().getRoot();
			if (p.getCheckBox().isSelected()) {
				new ListServicesRegistryQuerier( radius, dec, ra, p).start();
			}
		}
	}
	protected void haltQuery() {
		for (final BackgroundWorker w : getContext().getTasksList()) {
		    // halts all retrievers and list-services queries for this window - leaving other tasks (e.g. downloads, plastic interactions) untouched.
		   
		    if (w.parent == this &&
		            (w instanceof Retriever  || w instanceof ListServicesRegistryQuerier) ) {
		        w.interrupt();
		    }
		}
		setProgressMax(0);
	}
	/** removes previous results, just leaving the skeleton */
	protected void clearTree() {
		// reset selection too.
		vizModel.clear();
		vizualizations.refocusMainNodes();
		vizualizations.reDrawGraphs();
		setProgressMax(1);
		setProgressValue(0);
	}
	/**
	 * take a snapshot of current form inputs, and ad to the history list.
	 * Call this at an appropriate point withing 'query()' to save searcg state to history.
	 */
	protected void storeHistoryItem() {
		final PositionHistoryItem shi = new PositionHistoryItem();
		if (posText.isShowing() && posText.isEnabled()) {
		    shi.setPosition(posText.getPositionAsSesameBean());
		    // blank out aliases - as long, and unneeded. - for storage efficiency.
		    shi.getPosition().setAliases(null);
		}
		if (regionText.isShowing() && regionText.isEnabled()) {
		    final double radius = regionText.getRadius();
		    shi.setRadius(new DoubleDimension(radius,radius));
		}
		if (startCal.isShowing() && startCal.isEnabled()) {
		    shi.setStartTime(startCal.getDate());
		}
		if (endCal.isShowing() && endCal.isEnabled()) {
		    shi.setEndTime(endCal.getDate());
		}
		try {
		    history.getReadWriteLock().writeLock().lock();
		    if (! history.contains(shi)) {
		        history.add(0,shi); // insert at top of list.
		    }
		} finally {
		    history.getReadWriteLock().writeLock().unlock();
		}
	}


	/** a matcher editor to filter the history list.
	 * removes history items that aren't applicable to the current protocol
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Dec 11, 20071:31:30 PM
     */
    private final class ProtocolsMatcherEditor extends AbstractMatcherEditor<PositionHistoryItem> implements PropertyChangeListener, ItemListener {

        public ProtocolsMatcherEditor() {
            // wanted to listen to startCal, but it's enabled property doen't seem to change.
            posText.addPropertyChangeListener("enabled",this);
            stap.getCheckBox().addItemListener(this);
            updateFilter();
        }
        private final Matcher<PositionHistoryItem> timeMatcher = new Matcher<PositionHistoryItem>() {

            public boolean matches(final PositionHistoryItem item) {
                return item.getStartTime() != null;               
            }
        };
        private final Matcher<PositionHistoryItem> spaceMatcher = new Matcher<PositionHistoryItem>() {

            public boolean matches(final PositionHistoryItem item) {
                return item.getPosition() != null;
            }
        };

        public void propertyChange(final PropertyChangeEvent evt) {
            updateFilter();
        }

        public void itemStateChanged(final ItemEvent e) {
            updateFilter();
        }
        
        private void updateFilter() {
            final boolean time= stap.getCheckBox().isSelected();
            final boolean space = posText.isEnabled();
            if (time && space) {
                fireMatchAll(); // even though some won't cover all inputs
            } else if (time) {
                fireChanged(timeMatcher);
            } else if (space) {
                fireChanged(spaceMatcher);
            } else {                        
                fireMatchNone();
            }
        }
    }

    /** worker that queries registry to produce a list of all services of a particlar capability.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 28, 20075:30:43 PM
     */
    public final class ListServicesRegistryQuerier extends BackgroundOperation<Void> implements ResourceConsumer {
        private final double radius;
        private final double dec;
        private final double ra;
        private final DalProtocol p;

        private ListServicesRegistryQuerier(final double radius, final double dec, final double ra,
                final DalProtocol p) {
            super("Searching for " + p.getName() + " Services",  BackgroundWorker.LONG_TIMEOUT, Thread.MAX_PRIORITY);
            this.radius = radius;
            this.dec = dec;
            this.ra = ra;
            this.p = p;
        }

        @Override
        protected Void construct() throws Exception {
        	if (resourceList == null) {
        		this.p.processAllServices(this);
        	} else {
        		this.p.processSuitableServicesInList(resourceList,this);
        	}
        	return null;
        }

        // called in 'construct' for each service in turn.
        public void process(final Resource r) {
            if (! (r instanceof Service)) {
                return; // got some kind of garbage - ignore. 
            }
            final Service s = (Service)r;
            // build a set of retrievers for this service.
            final AbstractRetriever[] retrievers; 
            if (this.p instanceof SpatialDalProtocol) {
                final SpatialDalProtocol spatial = (SpatialDalProtocol)this.p;
                    retrievers = spatial.createRetrievers(s,this.ra,this.dec,this.radius,this.radius);
            } else if (this.p instanceof TemporalDalProtocol) {
                final TemporalDalProtocol temporal = (TemporalDalProtocol)this.p;
                final Date start = startCal.getDate();
                final Date end = endCal.getDate();
                    if (noPosition.isSelected()) { // zero out the positional fields
                        retrievers = temporal.createRetrievers(s,start,end,Double.NaN,Double.NaN,Double.NaN,Double.NaN);
                    } else {
                        retrievers = temporal.createRetrievers(s,start,end,this.ra,this.dec,this.radius,this.radius);
                    }                                    
            } else {
                throw new ProgrammerError("unknown subtype of protocol " + p.getClass().getName());
            }
           // now create a results directory for each retreiver
            // factored this out of addAll, as it cannot be done on EDT.
            final List<Tuple<Retriever,FileObjectView>> tups = new ArrayList<Tuple<Retriever,FileObjectView>>(retrievers.length);
            for (final Retriever retr : retrievers) {
                // eagerly create this, and hang onto it - else it tends to get GC'd and we
                // lost the results tree
                FileObjectView fov = null;
                try {
                    final FileObject fo = vizModel.createResultsDirectory(retr);
                    fov = new FileObjectView(fo,vizModel.iconFinder);
                } catch (final FileSystemException e) {
                    logger.warn("Unable to create results directory for " + retr.getLabel());
                }   
                tups.add(new Tuple<Retriever,FileObjectView>(retr,fov));
            }
            
            // now register these retreivers with the system and start them running - do all this on the edt.
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    vizModel.getSummarizer().addAll(tups);
                    setProgressMax(getProgressMax() + tups.size());  
                    for (final AbstractRetriever rvr : retrievers) {
                        rvr.start();
                    }
                }
            });
        }

        // called from construct() to indicate number of expected resources.
        public void estimatedSize(final int i) {
            // estimated size gives the number of services - but there may be multiple capabiltities per service...
            // still, present this informatino early - number of services.
            // call this on EDT
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    parent.showTransientMessage(i + " " + p.getName().toLowerCase() + " services to query", "");
                }
            });
        }
        
        @Override
        protected void doFinished(final Void result) {
            // do nothing.
        }

    } // end of list services registry querier.

    /** an item in the history menu.
	 * 
	 * each listens to decSexToggle - to change representation from one system to other.
	 * @author Noel Winstanley
	 * @since May 17, 20067:34:18 PM
	 */
	private class HistoryMenuItem extends JMenuItem implements ActionListener, DecSexListener{

		/**
		 * @param i
		 */
		public HistoryMenuItem(final PositionHistoryItem i) {
			this.shi = i;
			if (dsToggle.isDegrees()) {
				this.degreesSelected(null);
			} else {
				this.sexaSelected(null);
			}
			this.addActionListener(this);
			dsToggle.addListener(this);
		}
		private final PositionHistoryItem shi;

		// loads what fields this history object provides back into the form.
		public void actionPerformed(final ActionEvent e) {
		        if (shi.getPosition() != null) {
					posText.setPosition(shi.getPosition().getRa(),shi.getPosition().getDec());
		        }
		        if (shi.getRadius() != null) {
					regionText.setRadius(shi.getRadius().getWidth());
		        }
		        if (shi.getStartTime() != null) {
		            startCal.setDate(shi.getStartTime());
		        }
		        if (shi.getEndTime() != null) {
		            endCal.setDate(shi.getEndTime());
		        }
		}

		public void degreesSelected(final EventObject ignored) {
			buildMenuLabel();
		}

        public void sexaSelected(final EventObject ignored) {
            buildMenuLabel();
        }		

        /**
         * 
         */
		private void buildMenuLabel() {
		    final StrBuilder sb = new StrBuilder();
		    if (shi.getPosition() != null) { // assume that whatever applies to position applies to radius too.
		        if (shi.getPosition().getOName() != null) {
		            sb.append(shi.getPosition().getOName()).append(", ");
		        } 
		        try { // assume that if we've got a position, we've got a radius as well.
		            if (dsToggle.isDegrees()) {
		                sb.append("position: ")
		                .append(nf.format(shi.getPosition().getRa()))
		                .append(',')
		                .append(nf.format(shi.getPosition().getDec()))
		                .append(", radius: ");
		                final double rw = shi.getRadius().getWidth();
		                final double rh = shi.getRadius().getHeight();
		                if (rw == rh) {
		                    sb.append(nf.format(rw));
		                }
		                else {
		                    sb.append(nf.format(rw))
		                    .append(',')
		                    .append(nf.format(rh));
		                }
		            } else {
		                sb.append("position: ")
		                .append(PositionUtils.decimalToSexagesimal(
		                        shi.getPosition().getRa()
		                        ,shi.getPosition().getDec()))
		                .append(" radius: "); 
		                final double rw = shi.getRadius().getWidth();
		                final double rh = shi.getRadius().getHeight();
		                if (rw == rh) {
		                    sb.append(RadiusTextField.formatAsArcsec(rw)).append('"');
		                }
		                else {
		                    sb.append(RadiusTextField.formatAsArcsec(rw)).append('"')
		                      .append(',')
		                      .append(RadiusTextField.formatAsArcsec(rh)).append('"');
		                }		                
		            }
		        } catch (final NumberFormatException e) {
		            // don't care too much.
		        }
		    }
		    if (shi.getStartTime() != null) { // assume applies to end date too
		        sb.appendSeparator(",");
		        sb.append(startCal.getDateFormat().format(shi.getStartTime()));
		        sb.append(" - ");
		        sb.append(endCal.getDateFormat().format(shi.getEndTime()));
		    }
		    setText(sb.toString());
		}

	}
	
// focus listener interface - links in the activities, etc.
	public void focusChanged(final FocusEvent arg0) {
		final boolean somethingSelected = vizModel.getSelectionFocusSet().size() > 0;
		clearAction.setEnabled(somethingSelected);
		if (somethingSelected) {
			final Transferable tran = vizModel.getSelectionTransferable();
			if (tran == null) { // unlikely
				acts.clearSelection();
			} else {
				acts.setSelection(tran);
			}
		} else {
			acts.clearSelection();			
		}
	}
	
// action classes

	/** clear the user's selection */
	protected class ClearSelectionAction extends AbstractAction {
		public ClearSelectionAction() {
			super("Clear Selection");//,IconHelper.loadIcon("editclear32.png"));
			CSH.setHelpIDString(this,"scope.clear");
			this.putValue(SHORT_DESCRIPTION,"Clear the node selection");
			this.setEnabled(false);           
		}

		public void actionPerformed(final ActionEvent e) {
			final FocusSet set = vizModel.getSelectionFocusSet();
			for (final Iterator i = set.iterator(); i.hasNext();) {
				final TreeNode n = (TreeNode) i.next();
				// i.remove();
				n.setAttribute("selected", "false");
			}
			set.clear();
			servicesList.getCurrentResourceModel().clearSelection();
			vizualizations.reDrawGraphs();
			this.setEnabled(false);
		}    	
	}

	/** halts the current search */
	protected class HaltAction extends AbstractAction {
		public HaltAction() {
			super("Halt Search",IconHelper.loadIcon("stop32.png"));
			this.putValue(SHORT_DESCRIPTION,"Halt the search");
			this.putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD,UIComponentMenuBar.MENU_KEYMASK));
		}

		public void actionPerformed(final ActionEvent e) {
			submitButton.enableA();
			haltQuery();
		}
	}

	/** perform search action.
	 *  listens to position field and is only enabled when position field is valiud.*/
	protected class SearchAction extends AbstractAction implements TimerDrivenNameResolvingPositionTextField.ResolutionListener {
		public SearchAction(final UserDrivenNameResolvingPositionTextField pos) {		    
			super("Search",IconHelper.loadIcon("search32.png"));
			this.putValue(SHORT_DESCRIPTION,"Find data for this Position");
			this.putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_G,UIComponentMenuBar.MENU_KEYMASK));
			pos.addResolutionListener(this);
		}

		public void actionPerformed(final ActionEvent e) {
			submitButton.enableB();
			final Map<String,String> m = new HashMap<String,String>();
			m.put("name","VO Scope");
			snitch.snitch("SUBMIT",m);
			query();		
		}
		//enable the search button
        public void resolved(final ResolutionEvent ev) {
            setEnabled(true);
        }
        // even though the resolve failed, enable the search button.
        public void resolveFailed(final ResolutionEvent ev) {
            setEnabled(true);
        }
        // disable the 'search button'
        public void resolving(final ResolutionEvent ev) {
            setEnabled(false);
        }
	}


	/** goto top button */

	protected class TopAction extends AbstractAction {
		public TopAction() {
			super("Go To Top"); //,IconHelper.loadIcon("top32.png"));
			this.putValue(SHORT_DESCRIPTION,"Focus display to 'Search Results'");
			CSH.setHelpIDString(this,"scope.top");
			putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_T,UIComponentMenuBar.MENU_KEYMASK));
			this.setEnabled(false);
		}
		public void actionPerformed(final ActionEvent e) {
			vizualizations.refocusMainNodes();
			vizualizations.reDrawGraphs();
		}
	}
	    /** go up one level */
	   protected class UpAction extends AbstractAction {
	        public UpAction() {
	            super("Up"); //,IconHelper.loadIcon("top32.png"));
	            this.putValue(SHORT_DESCRIPTION,"Focus display on the parent of the currently focussed node'");
	            CSH.setHelpIDString(this,"scope.up");	            
	            putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_UP,UIComponentMenuBar.MENU_KEYMASK));
	            this.setEnabled(false);
	        }
	        public void actionPerformed(final ActionEvent e) {
	            vizualizations.moveUp();
	            vizualizations.reDrawGraphs();
	        }
	    }
	   /** clear the history list */
	   protected class ClearHistoryAction extends AbstractAction {
	       /**
	        * 
	        */
	       public ClearHistoryAction() {
	           super("Clear History");
	          
	       }
	       public void actionPerformed(final ActionEvent e) {
	           ConfirmDialog.newConfirmDialog(AstroScopeLauncherImpl.this,"Clear History","All history entries will be lost - continue?"
	                   ,new Runnable() {
	               public void run() {
	                   history.clear();
	               }
	           }
	           ).show();
	       }
	}



    public final ScopeServicesList getServicesList() {
        return this.servicesList;
    }

    public final BiStateButton getSubmitButton() {
        return this.submitButton;
    }


    /** reutnrs true if currenlty showing the services table */
    public boolean currentlyServicesTable() {
        return this.flip.currentlyServicesTable();
    }


    /** flip to the services table
     */
    public void flipToServicesTable() {
        servicesRadioButton.doClick();
    }

}
