/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.buffer.BoundedFifoBuffer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.StapInformation;
import org.astrogrid.acr.ivoa.SiapInformation;
import org.astrogrid.acr.nvo.ConeInformation;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.scope.DalProtocol;
import org.astrogrid.desktop.modules.ui.scope.DalProtocolManager;
import org.astrogrid.desktop.modules.ui.scope.HyperbolicVizualization;
import org.astrogrid.desktop.modules.ui.scope.ImageLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.QueryResultSummarizer;
import org.astrogrid.desktop.modules.ui.scope.SaveNodesButton;
import org.astrogrid.desktop.modules.ui.scope.VizModel;
import org.astrogrid.desktop.modules.ui.scope.Vizualization;
import org.astrogrid.desktop.modules.ui.scope.VizualizationManager;
import org.astrogrid.desktop.modules.ui.scope.VotableLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.WindowedRadialVizualization;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;

import com.l2fprod.common.swing.JButtonBar;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Abstract base class for astroscope, helioscope, and future scopes.
 * 
 * @author Noel Winstanley
 * @since May 14, 20066:51:20 AM
 */
public abstract class AbstractScope extends UIComponentImpl implements
		PlasticWrapper, PlasticListener, ActionListener {
    /**
	 * 
	 */
	private static final String HISTORY_ITEM_SEPARATOR = "@@";

	/** close action */
    private final class CloseAction extends AbstractAction {
        public CloseAction() {
            super("Close",IconHelper.loadIcon("exit_small.png"));
            this.putValue(SHORT_DESCRIPTION,"Close");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
        }

        public void actionPerformed(ActionEvent e) {
            hide();
            dispose();
        }
    }
    
    /** clear selection action */
    private final class ClearSelectionAction extends AbstractAction {
        public ClearSelectionAction() {
            super("Clear selection",IconHelper.loadIcon("editclear.png"));
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
    
    private final class ClearHistoryAction extends AbstractAction {

		public void actionPerformed(ActionEvent arg0) {
			getConfiguration().setKey(historyKey,"");
			while (! historyBuffer.isEmpty()){
				historyBuffer.remove();
				historyMenu.remove(0);
			}
		}
    	public ClearHistoryAction() {
    		super("Clear History");
    		this.putValue(SHORT_DESCRIPTION,"Clear search history");
    		this.setEnabled(true);
    	}
    }
    
	/** extends the plastic mesagehandler to display new buttons */
	protected class ScopePlasticMessageHandler extends
			ApplicationRegisteredPlasticMessageHandler {

		public ScopePlasticMessageHandler(UIComponent parent,
				PlasticWrapper wrapper, Container container) {
			super(parent, wrapper, container);
		}

		// define this method to control what happens when a new application
		// registers.
		protected Component[] buildComponents(URI applicationId, String name,
				String description, URL iconURL, URI[] messages) {
			List results = new ArrayList();
			if (ArrayUtils.contains(messages,
					CommonMessageConstants.VOTABLE_LOAD_FROM_URL)) {
				results.add(new VotableLoadPlasticButton(applicationId, name,
						description, iconURL, vizModel.getSelectionFocusSet(),
						AbstractScope.this, AbstractScope.this));
			}
			if (ArrayUtils.contains(messages,
					CommonMessageConstants.FITS_LOAD_FROM_URL)) {
				results.add(new ImageLoadPlasticButton(applicationId, name,
						description, iconURL, vizModel.getSelectionFocusSet(),
						AbstractScope.this, AbstractScope.this));
			}
			return (Component[]) results.toArray(new Component[results.size()]);

		}
	}

	public static final int TOOLTIP_WRAP_LENGTH = 50;

	private static List acceptedMessages = new ArrayList() {
		{// initializer block.
			this.add(CommonMessageConstants.ECHO);
			this.add(CommonMessageConstants.GET_IVORN);
			this.add(CommonMessageConstants.GET_NAME);
			this.add(CommonMessageConstants.GET_VERSION);
			// this.add(CommonMessageConstants.GET_ICON); not yet.
			this.add(HubMessageConstants.APPLICATION_REGISTERED_EVENT);
			this.add(HubMessageConstants.APPLICATION_UNREGISTERED_EVENT);
		}
	};

	// used to generate a new plastic id for each astroscope window
	private static int UNQ_ID = 0;

	/**
	 * Commons Logger for this class
	 */
	protected static final Log logger = LogFactory
			.getLog(AstroScopeLauncherImpl.class);

	private Action clearAction = new ClearSelectionAction();

	protected JButtonBar dynamicButtons = new JButtonBar(JButtonBar.VERTICAL);

	protected final PlasticHubListener hub;

	protected final URI myPlasticID;

	protected final MessageHandler plasticHandler;

	protected final DalProtocolManager protocols;

	protected JButton reFocusTopButton;

	protected FlipButton submitButton;

	protected final VizModel vizModel;

	protected final VizualizationManager vizualizations;
	
	/** key used to store search history in preferences */
	private final String historyKey;

	/** Construct a new Scope.
	 * 
	 * @param conf
	 * @param hs
	 * @param ui
	 * @param p list of dal protocols to query.
	 * @throws HeadlessException
	 */
	public AbstractScope(Configuration conf, HelpServerInternal hs,
			UIInternal ui, MyspaceInternal myspace,
			ResourceChooserInternal chooser, PlasticHubListener hub,
			DalProtocol[] p) throws HeadlessException {
		super(conf, hs, ui);
		this.hub = hub;
		this.historyKey = getScopeName() + ".history";
		this.protocols = new DalProtocolManager();
		for (int i = 0; i < p.length; i++) {
			this.protocols.add(p[i]);
		}
		// create the shared model
		vizModel = new VizModel(protocols);
		// create the vizualizations
		vizualizations = new VizualizationManager(vizModel);
		vizualizations.add(new WindowedRadialVizualization(vizualizations));
		vizualizations.add(new HyperbolicVizualization(vizualizations));

		dynamicButtons.add(new SaveNodesButton(vizModel.getSelectionFocusSet(),
				this, chooser, myspace));

		// plastic setup.
		String appName = getScopeName() + "-" + ++UNQ_ID;
		// standard message handler.
		this.plasticHandler = new StandardHandler(appName,
				getScopeDescription(), getScopeRegId(), getScopeIconURL(),
				PlasticListener.CURRENT_VERSION);
		// message handler for application add and applcation remove messages.
		ApplicationRegisteredPlasticMessageHandler dynamicButtonHandler = new ScopePlasticMessageHandler(
				this, this, dynamicButtons);
		this.plasticHandler.setNextHandler(dynamicButtonHandler);
		// register with the hub
		myPlasticID = hub.registerRMI(appName, acceptedMessages, this);
		// inspect what applications have already registered .
		// get what's currently registered there.
		List ids = hub.getRegisteredIds();
		for (Iterator i = ids.iterator(); i.hasNext();) {
			URI id = (URI) i.next();
			if (!id.equals(myPlasticID)) { // no point looking at my own navel,
				dynamicButtonHandler.interrogatePlasticApp(id);
			}
		}

		// build the ui.
		this.setSize(1000, 707); // same proportions as A4,

		// this.setSize(700, 700);
		JPanel pane = getMainPanel();
		JPanel searchPanel = createLeftPanel();
		searchPanel.setMaximumSize(searchPanel.getSize());
		pane.add(searchPanel, BorderLayout.WEST);
		pane.add(makeCenterPanel(), BorderLayout.CENTER);
		this.setContentPane(pane);
		this.setTitle(getScopeName());
		this.setJMenuBar(getJJMenuBar());
	}

	
	private JMenuBar menuBar;
	/**
	 * @return
	 */
	private JMenuBar getJJMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getFileMenu());
			menuBar.add(getPreferencesMenu());
			menuBar.add(getHistoryMenu());
			menuBar.add(getHelpMenu());
		}
		return menuBar;
	}
	
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenu historyMenu;
	private JMenu preferencesMenu;
	
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");

			fileMenu.setMnemonic(KeyEvent.VK_F);	
			fileMenu.add(clearAction);
			fileMenu.add(new JSeparator());
			fileMenu.add(new CloseAction());
		}
		return fileMenu;
	}

		/** 
		 * Create an action from a stored representation of one item in the search history.
		 * @param historyItem - whatever 
		 * @return an action - that provides the readable label, the action sets the search fields of the form.
		 * may return null if cannot parse the history item.
		 */
		protected abstract JMenuItem createHistoryMenuItem(String historyItem);
		
		/**
		 * Grab a history item string from the current UI. 
		 * @return a string that can later be used as input to {@link #createHistoryMenuItem(String)}
		 */
		protected abstract String grabHistoryItem();
		private static final int HISTORY_SIZE = 20;
	private BoundedFifoBuffer historyBuffer = new BoundedFifoBuffer(HISTORY_SIZE);
	private JMenu getHistoryMenu() {
		if (historyMenu == null) {
			historyMenu = new JMenu();
			historyMenu.setText("History");
			historyMenu.setMnemonic(KeyEvent.VK_H);	
			
			// load defns from preferences.
			String hist = getConfiguration().getKey(historyKey);
			if (hist != null) {
				StringTokenizer tok = new StringTokenizer(hist,HISTORY_ITEM_SEPARATOR); // use '@@' as item delimiter - very unlikely this will be used within the seartch term
				int i = 0;
				while (tok.hasMoreElements() && i < HISTORY_SIZE) {
					String item = tok.nextToken();
					if (! historyBuffer.contains(item)) {
						JMenuItem a = createHistoryMenuItem(item);
						if (a != null) {
							historyMenu.insert(a,0);
							historyBuffer.add(item);
							i++;
						}
					}
				}
			}
			
			historyMenu.add( new JSeparator());
			historyMenu.add(new ClearHistoryAction());			
		}
		return historyMenu;
	}
	
	//** open for extension
	protected JMenu getPreferencesMenu() {
		if (preferencesMenu == null) {
			preferencesMenu = new JMenu();
			preferencesMenu.setText("Preferences");
		}
		return preferencesMenu;
	}
	
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
		}
		return helpMenu;
	}

	/** define to acutally perform the query */
	protected abstract void query();
	
	/**
	 * Various action statements for when buttons are clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == submitButton) {
			if (submitButton.isDoingSearch()) {
				haltQuery();
			} else {
				// set the query going first.
				query();
				// if the query fired off ok, it's probably semi-valid at least - so worth recording in history.
				String historyItem = grabHistoryItem();
				if (historyItem != null && ! historyBuffer.contains(historyItem)) {// adjust the history.
					if (historyBuffer.isFull()) {// remove the oldest;
						historyBuffer.remove();
						getHistoryMenu().remove(HISTORY_SIZE-1);
					}
					JMenuItem a = createHistoryMenuItem(historyItem);
					getHistoryMenu().insert(a,0);
					historyBuffer.add(historyItem);
					String items = StringUtils.join(historyBuffer.iterator(),HISTORY_ITEM_SEPARATOR);
					getConfiguration().setKey(historyKey,items);					
				}				
			}
			submitButton.flip();
		} else if (source == reFocusTopButton) {
			vizualizations.refocusMainNodes();
			vizualizations.reDrawGraphs();
		} 
	}

	public PlasticHubListener getHub() {
		return hub;
	}

	public URI getPlasticId() {
		return myPlasticID;
	}

	public Object perform(URI sender, URI message, List args) {
		return plasticHandler.perform(sender, message, args);
	}

	/**
	 * Creates the left/WEST side of the GUI. By creating a small search panel
	 * at the top(north-west). Then let the rest of the panel be a JTree for the
	 * selected data.
	 * 
	 * @return JPanel consisting of the query gui and custom controls typically
	 *         placedo on the WEST side of the main panel.
	 */
	private JPanel createLeftPanel() {
		JPanel scopeMain = new JPanel();
		scopeMain.setLayout(new BorderLayout());

		JPanel searchPanel = createSearchPanel();

		TitledBorder tb = new TitledBorder("1. Search");
		tb.setTitleColor(java.awt.Color.BLUE);
		searchPanel.setBorder(tb);

		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		scopeMain.getInputMap(scopeMain.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(enter, "search");
		scopeMain.getActionMap().put("search", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				AbstractScope.this.actionPerformed(e);
			}
		});

		// start of tree navigation buttons - maybe add more here later.
		JPanel navPanel = new JPanel();
		navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
		tb = new TitledBorder("2. Navigate");
		tb.setTitleColor(java.awt.Color.BLUE);
		navPanel.setBorder(tb);

		submitButton = new FlipButton();
		submitButton.addActionListener(this);
		searchPanel.add(submitButton);

		reFocusTopButton = new JButton("Go to Top");
		reFocusTopButton.setIcon(IconHelper.loadIcon("top.png"));
		reFocusTopButton.setToolTipText("Focus display to 'Search Results' ");
		reFocusTopButton.addActionListener(this);
		reFocusTopButton.setEnabled(false);
		navPanel.add(reFocusTopButton);


		final FocusSet sel = vizModel.getSelectionFocusSet();
		sel.addFocusListener(new FocusListener() {
			public void focusChanged(FocusEvent arg0) {
				clearAction.setEnabled(sel.size() > 0);
			}
		});
		JButton clearButton = new JButton(clearAction);
		navPanel.add(clearButton);

		// make these buttons all the same width - I know clear button is the
		// biggest.
		submitButton.setMaximumSize(clearButton.getPreferredSize());
		reFocusTopButton.setMaximumSize(clearButton.getPreferredSize());

		// start of consumer buttons.
		JScrollPane sp = new JScrollPane(dynamicButtons);
		tb = new TitledBorder("3. Process");
		tb.setTitleColor(java.awt.Color.BLUE);
		sp.setBorder(tb);
		searchPanel.setMaximumSize(sp.getMaximumSize());
		navPanel.setMaximumSize(sp.getMaximumSize());

		// assemble it all together.
		JPanel bothTop = new JPanel();
		bothTop.setLayout(new BoxLayout(bothTop, BoxLayout.Y_AXIS));
		bothTop.add(searchPanel);
		bothTop.add(navPanel);
		bothTop.setMaximumSize(sp.getMaximumSize());
		scopeMain.add(bothTop, BorderLayout.NORTH);
		scopeMain.add(sp, BorderLayout.CENTER);
		return scopeMain;
	}

	/** panel containing summary of search results */
	private JPanel makeServicesPanel() {
		JPanel servicesPanel = new JPanel();
		TableSorter sorter = new TableSorter(protocols.getQueryResultTable());
		final JTable table = new JTable(sorter);
		sorter.setTableHeader(table.getTableHeader()); 
		JScrollPane scroll = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(700, 550));
		TableColumnModel tcm = table.getColumnModel();
		final TableColumn riColumn = tcm.getColumn(0);
		riColumn.setPreferredWidth(150);
		// decorate the renderer for resource information (objects) to display a
		// nice tooltip, etc.
		riColumn.setCellRenderer(new TableCellRenderer() {
			private final TableCellRenderer original = table
					.getDefaultRenderer(ResourceInformation.class);

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel c = (JLabel) original.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);
				if (value instanceof ResourceInformation) {
					ResourceInformation ri = (ResourceInformation) value;
					c.setText(ri.getTitle());
					c.setToolTipText(mkToolTip(ri));
				}
				return c;
			}

			/**
			 * builds a tool tip from a resource information object
			 * 
			 * @todo refactor so it works in a general way - not hardcoded.
			 */
			private String mkToolTip(ResourceInformation ri) {
				StringBuffer sb = new StringBuffer();
				sb.append("<html>");
				sb.append(ri.getId());
				sb.append("<br>Service Type: ");

				if (ri instanceof SiapInformation) { // nasty little hack for
														// now. - later find a
														// way of getting this
														// info from the
														// protocol object..
					sb.append("Image");
				} else if (ri instanceof ConeInformation) {
					sb.append("Catalogue");
				} else if (ri instanceof StapInformation) {
					sb.append("Stap");

				} else { // no special type for ssap at the moment, and have
							// to assume that any other ri is a ssap
					sb.append("Spectra");
				}
				sb.append("</html>");
				// @todo extend ResourceInformation to contain curation details,
				// add these in here.
				return sb.toString();
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
				}
				return c;
			}
		});
		return servicesPanel;
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
	 * subclasses should implement to provide a search form to input parameters.
	 * Don't provide a submit button or border - these are taken care of in this
	 * class.
	 * 
	 * @return
	 */
	protected abstract JPanel createSearchPanel();

	/** subclasses should implement to provide a human-readable description of the applicaiton,
	 * used in plastic protocol 
	 * @return
	 */
	protected abstract String getScopeDescription();

	/** implement to point to a URL of the applicatioin icon.
	 * used in plastic
	 * @return
	 */
	protected abstract String getScopeIconURL();

	/**
	 * subclasses should impleemtn this method to return the name of the scope
	 * application
	 * 
	 * @return
	 */
	protected abstract String getScopeName();

	/**
	 * subclasses should implem,ent this method to return the registry id of the
	 * description of this scope application
	 * used in plastic protocol.
	 * @return
	 */
	protected abstract String getScopeRegId();

	protected void haltQuery() {
		super.getHaltAllButton().doClick();
		setStatusMessage("Halted");
		setProgressValue(getProgressMax());
	}

	/**
	 * Makes the Center Panel for the GUI. The main look is is the display graph
	 * which is the majority of the center, and a series of save buttons below
	 * it. A series of createInitialDisplay type methods are here to try and
	 * switch to see the best display.
	 * 
	 * @return
	 */
	protected JPanel makeCenterPanel() {

		JTabbedPane alternatives = new JTabbedPane();
		for (Iterator i = vizualizations.iterator(); i.hasNext();) {
			Vizualization v = (Vizualization) i.next();
			alternatives.addTab(v.getName(), v.getDisplay());
		}

		alternatives.addTab("Services", makeServicesPanel());

		JPanel wrapPanel = new JPanel();
		wrapPanel.setLayout(new BoxLayout(wrapPanel, BoxLayout.Y_AXIS));
		wrapPanel.add(alternatives);
		// wrapPanel.add(southCenterPanel);
		return wrapPanel;
	}

}