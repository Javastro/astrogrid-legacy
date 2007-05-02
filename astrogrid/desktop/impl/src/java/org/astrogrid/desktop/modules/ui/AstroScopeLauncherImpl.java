/*$Id: AstroScopeLauncherImpl.java,v 1.59 2007/05/02 15:38:29 nw Exp $
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.comp.BiStateButton;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle;
import org.astrogrid.desktop.modules.ui.comp.DimensionTextField;
import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.NameResolvingPositionTextField;
import org.astrogrid.desktop.modules.ui.comp.PlasticButtons;
import org.astrogrid.desktop.modules.ui.comp.PositionUtils;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle.DecSexListener;
import org.astrogrid.desktop.modules.ui.scope.DalProtocol;
import org.astrogrid.desktop.modules.ui.scope.DalProtocolManager;
import org.astrogrid.desktop.modules.ui.scope.HyperbolicVizualization;
import org.astrogrid.desktop.modules.ui.scope.ImageLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.QueryResultSummarizer;
import org.astrogrid.desktop.modules.ui.scope.Retriever;
import org.astrogrid.desktop.modules.ui.scope.SaveNodesButton;
import org.astrogrid.desktop.modules.ui.scope.SpatialDalProtocol;
import org.astrogrid.desktop.modules.ui.scope.SpectrumLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.VizModel;
import org.astrogrid.desktop.modules.ui.scope.Vizualization;
import org.astrogrid.desktop.modules.ui.scope.VizualizationManager;
import org.astrogrid.desktop.modules.ui.scope.VotableLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.WindowedRadialVizualization;
import org.astrogrid.desktop.modules.ui.scope.ScopeHistoryProvider.SearchHistoryItem;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;
import org.votech.plastic.CommonMessageConstants;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.swing.JEventListPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;

public class AstroScopeLauncherImpl extends UIComponentImpl implements PlasticButtons.ButtonBuilder
, AstroScopeInternal, DecSexListener, FocusListener {


	private static final String SCOPE_NAME = "AstroScope";         
	/** point at which to wrap tooltips
	 * @todo - make a workbench-wide constant 
	 */
	public static final int TOOLTIP_WRAP_LENGTH = 50;
	private static final String KEY_LINK = "KEY_LINK";

	/** safe to share this between astroscope instances - as is only ever used on single event dispatch thread */
	private final static NumberFormat nf =NumberFormat.getNumberInstance();
	static {
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(6);
		nf.setMaximumFractionDigits(6);
	}

	protected static final Log logger = LogFactory.getLog(AstroScopeLauncherImpl.class);

	public AstroScopeLauncherImpl(UIContext context, IterableObjectBuilder protocolsBuilder, EventList history
			,MyspaceInternal myspace, ResourceChooserInternal chooser,
			Sesame ses, TupperwareInternal tupp, SendToMenu sendTo,
			SnitchInternal snitch)  {
		super(context);
		this.history = history;
		this.snitch = snitch;
		this.tupperware = tupp;
		this.protocols = new DalProtocolManager();
		this.ses = ses;

		for (Iterator i = protocolsBuilder.creationIterator(); i.hasNext(); ) {
			protocols.add((DalProtocol)i.next());
		}
		// create the shared model
		vizModel = new VizModel(protocols);
		// create the vizualizations
		vizualizations = new VizualizationManager(vizModel);
		vizualizations.add(new WindowedRadialVizualization(vizualizations,sendTo,this));
		vizualizations.add(new HyperbolicVizualization(vizualizations,sendTo,this));

		// build the ui.
		this.setSize(1000, 707); // same proportions as A4,
		setIconImage(IconHelper.loadIcon("scope16.png").getImage());   
		FormLayout layout = new FormLayout(
				"2dlu,d,4dlu,d,2dlu"
				,"p,p,p,p,p,p," + protocols.getRowspec() 
				+ ",bottom:max(20dlu;p),m,m"
				+ ",bottom:max(20dlu;p),top:p:grow");
		layout.setColumnGroups(new int[][]{ {2, 4} });		
		CellConstraints cc = new CellConstraints();
		PanelBuilder builder = new PanelBuilder(layout);
		
		int row = 1;
		builder.addSeparator("Search",cc.xyw(2,row,4));
		
		row++;
		builder.addLabel("Position or Object Name",cc.xyw(2,row,3));

		row++;
		posText = new NameResolvingPositionTextField(this,ses);
		posText.setToolTipText("Object name (3c273) or Position (187.27,+2.05 or 12:29:06.00,+02:03:08.60)");
		posText.setAlignmentX(LEFT_ALIGNMENT);
		posText.setColumns(10);
		builder.add(posText,cc.xyw(2,row,3));

		row++;
		builder.addLabel("Search Radius (degs/\")",cc.xyw(2,row,3));

		row++;
		regionText = new DimensionTextField();
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
       
		row++;
		// display in 2 columns
		boolean leftCol = true;
		for (Iterator i = protocols.iterator(); i.hasNext(); leftCol = ! leftCol ) {
			DalProtocol p = (DalProtocol)i.next();
			builder.add(p.getCheckBox(),cc.xy(leftCol ? 2 : 4,row));
			if (!leftCol) {
				row++;
			}
		}
		// move onto a final row if we've got an odd nummber of protocols..
		// bearing in mind the loop iterator above will have hit again at this point.
		if (!leftCol) {
			row++;
		}
		// start of tree navigation buttons - maybe add more here later.
		builder.addSeparator("Navigate",cc.xyw(2,row,4));
		
		row++;
		final Action haltAction = new HaltAction();	
		final Action searchAction = new SearchAction();		
		submitButton = new BiStateButton(searchAction,haltAction);
		builder.add(submitButton,cc.xyw(2,row,3));
		
		row++;
		JButton reFocusTopButton = new JButton(topAction);		
		//@todo inline this listener.
		final FocusSet sel = vizModel.getSelectionFocusSet();
		sel.addFocusListener(this);
		
		JButton clearButton = new JButton(clearAction);
		builder.add(reFocusTopButton,cc.xy(2,row));
		builder.add(clearButton,cc.xy(4,row));

		// start of consumer buttons 
		row++;
		builder.addSeparator("Process",cc.xyw(2,row,4));		
		
		row++;
		// creates a list of buttons dynamically generated from plastic apps
		PlasticButtons plasticButtons = new PlasticButtons(this.tupperware.getRegisteredApplications(),this);
		// add in custom static buttons.
		plasticButtons.getStaticList().add(new SaveNodesButton(vizModel.getSelectionFocusSet(),this,chooser,myspace));		
		JEventListPanel buttonPanel = plasticButtons.getPanel();
		buttonPanel.setElementColumns(1);
		builder.add(buttonPanel,cc.xyw(2,row,4));

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
		for (Iterator i = vizualizations.iterator(); i.hasNext();) {
			Vizualization v = (Vizualization) i.next();
			tabs.addTab(v.getName(), v.getDisplay());
		}
		tabs.addTab("Services", makeServicesPanel());

		pane.add(tabs, BorderLayout.CENTER);
		this.setContentPane(pane);
		this.setTitle(SCOPE_NAME);
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
		mb.add(Box.createHorizontalGlue());
		mb.add(createHelpMenu());
		this.setJMenuBar(mb);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// halt all my background processes.
				haltQuery();
			}
		});

		getContext().getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.astroscopeLauncher");
	}

/** panel containing summary of search results */
	private JPanel makeServicesPanel() {
		JPanel servicesPanel = new JPanel();
		final JTable table = new JTable(protocols.getQueryResultTable());

		table.setPreferredScrollableViewportSize(new Dimension(700, 550));
		TableColumnModel tcm = table.getColumnModel();
		final TableColumn riColumn = tcm.getColumn(0);
		riColumn.setPreferredWidth(150);

		riColumn.setCellRenderer(new DefaultTableCellRenderer() {
			protected void setValue(Object value) {
				Resource ri = (Resource)value;
				String link= "<html><a href='" + ri.getId() + "'>" + ri.getTitle() + "</a>";
				setText(link);
				putClientProperty(KEY_LINK,ri.getId());
			}

		});

		final TableColumn countColumn = tcm.getColumn(1);
		countColumn.setPreferredWidth(60);
		countColumn.setMaxWidth(60);
		servicesPanel.add(new JScrollPane(table));
		// decorate the default renderer for integers to display something
		// special for -1
		countColumn.setCellRenderer(new TableCellRenderer() {
			private final TableCellRenderer original = table
			.getDefaultRenderer(Integer.class);

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel c = (JLabel) original.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);
				if (value instanceof Integer
						&& ((Integer) value).intValue() == QueryResultSummarizer.ERROR) {
					c.setText("<html><font color='red'>ERROR</font></html>");
				}
				return c;
			}
		});
		TableColumn msgColumn = tcm.getColumn(2);
		msgColumn.setPreferredWidth(50);
		msgColumn.setCellRenderer(new TableCellRenderer() {
			private final TableCellRenderer original = table
			.getDefaultRenderer(String.class);

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel c = (JLabel) original.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);
				String s = value.toString();
				if (s != null && s.length() > 0) {
					c.setToolTipText("<html>"
							+ WordUtils.wrap(s, 50, "<br>", false) + "</html>");
				} else {
					c.setToolTipText("");
				}
				return c;
			}
		});
		return servicesPanel;
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
	protected final Action clearAction = new ClearSelectionAction();
	protected final DecSexToggle dsToggle;
	protected final NameResolvingPositionTextField posText;
	protected final DalProtocolManager protocols;
	protected final DimensionTextField regionText;
	protected final Sesame ses;
	protected final Action topAction = new TopAction();
	protected final TupperwareInternal tupperware;
	protected final VizModel vizModel;
	protected final VizualizationManager vizualizations;

// Plastic button builder interface.
	public JButton[] buildPlasticButtons(final PlasticApplicationDescription plas) {
		List results = new ArrayList();
		if (plas.understandsMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL)) {
			results.add(new VotableLoadPlasticButton(plas, vizModel.getSelectionFocusSet(),
					this,tupperware));
		}
		if (plas.understandsMessage(CommonMessageConstants.FITS_LOAD_FROM_URL)) {
			results.add(new ImageLoadPlasticButton(plas, vizModel.getSelectionFocusSet(),
					this, tupperware));
		}
		if  (plas.understandsMessage(SpectrumLoadPlasticButton.SPECTRA_LOAD_FROM_URL)) {
			results.add(new SpectrumLoadPlasticButton(plas,vizModel.getSelectionFocusSet(), this, tupperware));
		}		
		return (JButton[]) results.toArray(new JButton[results.size()]);
	}


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
		setTitle("Astroscope : on subset");
		for (Iterator i = protocols.iterator(); i.hasNext(); ) {
			DalProtocol p = (DalProtocol)i.next();
			p.getCheckBox().setVisible(false);
		}		
	}
	public Object create() {
		return null; // not implemented - will refactor this away later.
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
						return ses.sesame(positionString.trim(),"x");
					}
					protected void doError(Throwable ex) {
						showError("Simbad failed to resolve " + positionString);
					}
					protected void doFinished(Object result) {
						String temp = (String) result;
						Point2D pos;
						try {
							double ra = Double.parseDouble( temp.substring(temp.indexOf("<jradeg>")+ 8, temp.indexOf("</jradeg>")));
							double dec = Double.parseDouble( temp.substring(temp.indexOf("<jdedeg>")+ 8, temp.indexOf("</jdedeg>")));
							pos = new Point2D.Double(ra,dec);
						} catch (Throwable t) {
							doError(t);
							return;
						}
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
		// ok. everything looks valid. add a task to later on storee the history item
		// make this a later task, so that resolver thread has chance to return.
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				storeHistoryItem();
			}
		});

		final double ra = position.getX();
		final double dec = position.getY();

		Dimension2D dim = regionText.getDimension();
		final double raSize = dim.getWidth();
		final double decSize = dim.getHeight();

		for (Iterator i = protocols.iterator(); i.hasNext(); ) {
			final SpatialDalProtocol p =(SpatialDalProtocol)i.next();
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
						logger.info(services.length + " " + p.getName() + " services found");
						for (int i = 0; i < services.length; i++) {
							setProgressMax(getProgressMax()+1); // should give a nice visual effect.
							p.createRetriever(AstroScopeLauncherImpl.this,services[i],ra,dec,raSize,decSize).start();
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
		shi.setRadius(regionText.getDimension());
		boolean[] bits = new boolean[protocols.size()];
		int i = 0;
		for (Iterator p = protocols.iterator(); p.hasNext();i++) {
			DalProtocol protocol = (DalProtocol) p.next();
			bits[i] = protocol.getCheckBox().isSelected();
		}
		shi.setProtocols(bits);
		history.add(0,shi); // insert at top of list.
	}


	/** implementation of an item in the history menu.
	 * 
	 * each listens to decSexToggle - to change representation from one system to other.
	 * @author Noel Winstanley
	 * @since May 17, 20067:34:18 PM
	 */
	private class HistoryMenuItem extends JMenuItem
	implements ActionListener, DecSexListener{

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
					regionText.setDimension(shi.getRadius());
					boolean[] bits = shi.getProtocols();
					int ix = 0; 
					for (Iterator i = protocols.iterator(); i.hasNext(); ix++) {
						DalProtocol p = (DalProtocol) i.next();
						p.getCheckBox().setSelected(bits[ix]);
						
					}
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
				.append(", radius: ") 
				.append(nf.format(shi.getRadius().getWidth()))
				.append(',')
				.append(nf.format(shi.getRadius().getHeight()));
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
				.append(" radius: ") 
				.append(PositionUtils.decimalToSexagesimal(	
						shi.getRadius().getWidth()
						,shi.getRadius().getHeight()));
			} catch (NumberFormatException e) {
				// don't care too much.
			}
			setText(sb.toString());		
		}
	}
	
// focus listener interface.
	public void focusChanged(FocusEvent arg0) {
		clearAction.setEnabled(vizModel.getSelectionFocusSet().size() > 0);
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
			m.put("name",SCOPE_NAME);
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