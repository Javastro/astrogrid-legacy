/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.collections.buffer.BoundedFifoBuffer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.system.ReportingListModel.ReportingListDataListener;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.comp.BiStateButton;
import org.astrogrid.desktop.modules.ui.comp.TableSorter;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;
import org.votech.plastic.CommonMessageConstants;

import com.l2fprod.common.swing.JButtonBar;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Abstract base class for astroscope, helioscope, and future scopes.
 * 
 * <p/>
 * Subclasses should implement missing methods - most are quite straightforward.
 * 
 * @author Noel Winstanley
 * @since May 14, 20066:51:20 AM
 */
public abstract class AbstractScope extends UIComponentImpl implements ReportingListDataListener{
	/** action to clear history menu */
    protected class ClearHistoryAction extends AbstractAction {

		public ClearHistoryAction() {
    		super("Clear History");
    		this.putValue(SHORT_DESCRIPTION,"Clear search history");
    		this.setEnabled(true);
    	}
    	public void actionPerformed(ActionEvent arg0) {
			getConfiguration().setKey(historyKey,"");
			while (! historyBuffer.isEmpty()){
				historyBuffer.remove();
				historyMenu.remove(0);
			}
		}
    }

	/** clear selection action */
    protected class ClearSelectionAction extends AbstractAction {
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
    

    
    /** halt seartch action */
    protected class HaltAction extends AbstractAction {
        public HaltAction() {
            super("Halt",IconHelper.loadIcon("fileclose32.png"));
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
            super("Search",IconHelper.loadIcon("find.png"));
            this.putValue(SHORT_DESCRIPTION,"Find resources for this Position");
        }

        public void actionPerformed(ActionEvent e) {
	        	submitButton.enableB();
	        	Map m = new HashMap();
	        	m.put("name",scopeName);
	        	snitch.snitch("SUBMIT",m);
				query();		
        }
    }


    

    
    
    
    /** goto top button */
    
    protected class TopAction extends AbstractAction {
        public TopAction() {
            super("Go To Top",IconHelper.loadIcon("top.png"));
            this.putValue(SHORT_DESCRIPTION,"Focus display to 'Search Results'");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
            this.setEnabled(false);
        }
        public void actionPerformed(ActionEvent e) {
			vizualizations.refocusMainNodes();
			vizualizations.reDrawGraphs();
        }
    }
    
	/**
	 * separtator used for textual representation of history items.
	 */
	public static final String HISTORY_ITEM_SEPARATOR = "@@";

	/** number of history items to remeber */
	public static final int HISTORY_SIZE = 20;

	/** point at which to wrap tooltips
	 * @todo - make a workbench-wide constant 
	 */
	public static final int TOOLTIP_WRAP_LENGTH = 50;

	/**
	 * Commons Logger for this class
	 */
	protected static final Log logger = LogFactory
			.getLog(AstroScopeLauncherImpl.class);

	private JMenu fileMenu;
	/** buffer of history items */
	private BoundedFifoBuffer historyBuffer = new BoundedFifoBuffer(HISTORY_SIZE);
	/** key used to store search history in preferences */
	private final String historyKey;
	
	private JMenu historyMenu;

	private JMenuBar menuBar;


	protected Action closeAction = new CloseAction();
	
	protected Action clearAction = new ClearSelectionAction();

	/** bar of buttons dynamically built for available plastic apps */
	protected JButtonBar dynamicButtons = new JButtonBar(JButtonBar.VERTICAL);

	protected Action haltAction = new HaltAction();

	protected final DalProtocolManager protocols;
	protected final TupperwareInternal tupperware;

	protected final Action searchAction = new SearchAction();
	
	protected Action topAction = new TopAction();
	protected final VizModel vizModel;
	protected final VizualizationManager vizualizations;
	private final SnitchInternal snitch;
	private final String scopeName;
	private final RegistryBrowser browser;

	private BiStateButton submitButton;
	
	/** Construct a new Scope.
	 * Configure by passing in the usual components, _plus_ a list of the protocols to query.
	 * @param conf
	 * @param hs
	 * @param ui
	 * @param p list of dal protocols to query.
	 * @param browser todo
	 * @throws HeadlessException
	 */
	public AbstractScope(Configuration conf, HelpServerInternal hs,
			UIInternal ui, MyspaceInternal myspace,
			ResourceChooserInternal chooser, TupperwareInternal tupp, SendToMenu sendTo, 
			SnitchInternal snitch,
			String scopeName,
			DalProtocol[] p, RegistryBrowser browser) throws HeadlessException {
		super(conf, hs, ui);
		this.scopeName = scopeName;
		this.historyKey = scopeName + ".history";
		this.snitch = snitch;
		this.tupperware = tupp;
		this.protocols = new DalProtocolManager();
		for (int i = 0; i < p.length; i++) {
			this.protocols.add(p[i]);
		}
		this.browser = browser;
		// create the shared model
		vizModel = new VizModel(protocols);
		// create the vizualizations
		vizualizations = new VizualizationManager(vizModel);
		vizualizations.add(new WindowedRadialVizualization(vizualizations,sendTo,this));
		vizualizations.add(new HyperbolicVizualization(vizualizations,sendTo,this));

		dynamicButtons.add(new SaveNodesButton(vizModel.getSelectionFocusSet(),
				this, chooser, myspace));

		// build the ui.
		this.setSize(1000, 707); // same proportions as A4,

		// this.setSize(700, 700);
		JPanel pane = getMainPanel();
		JPanel searchPanel = createLeftPanel();
		searchPanel.setMaximumSize(searchPanel.getSize());
		pane.add(searchPanel, BorderLayout.WEST);
		pane.add(makeCenterPanel(), BorderLayout.CENTER);
		this.setContentPane(pane);
		this.setTitle(scopeName);
		this.setJMenuBar(getJJMenuBar());
		
		this.tupperware.getRegisteredApplicationsModel().addListDataListener(this);
		// refresh current list of apps.
		this.contentsChanged(null);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// halt all my background processes.
				AbstractScope.this.haltQuery();
			}
		});
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

		// start of tree navigation buttons - maybe add more here later.
		JPanel navPanel = new JPanel();
		navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
		tb = new TitledBorder("2. Navigate");
		tb.setTitleColor(java.awt.Color.BLUE);
		navPanel.setBorder(tb);

		submitButton = new BiStateButton(searchAction,haltAction);
		searchPanel.add(submitButton);
		
		//@fixme this doesn't work on my mac. dunno if it doesn't work elsewhere too.
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
		searchPanel.getInputMap(searchPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(enter, "search");
		searchPanel.getActionMap().put("search", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				submitButton.doClick(); 
			}
		});		

		JButton reFocusTopButton = new JButton(topAction);		
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
	
	protected JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");

			fileMenu.setMnemonic(KeyEvent.VK_F);
			fileMenu.add(searchAction);
			fileMenu.add(haltAction);
			fileMenu.add(clearAction);
			fileMenu.add(new JSeparator());
			fileMenu.add( closeAction);
		}
		return fileMenu;
	}

	/** @todo implement the history menu using xml serialization of the SesamePositionBeans */
	protected JMenu getHistoryMenu() {
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

	/**
	 * @return
	 */
	protected JMenuBar getJJMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getFileMenu());
			menuBar.add(getHistoryMenu());
            menuBar.add(Box.createHorizontalGlue());
            menuBar.add(createHelpMenu());
		}
		return menuBar;
	}

	
	private static final String KEY_LINK = "KEY_LINK";
	/** panel containing summary of search results */
	private JPanel makeServicesPanel() {
		JPanel servicesPanel = new JPanel();
		final TableSorter sorter = new TableSorter(protocols.getQueryResultTable());
		final JTable table = new JTable(sorter);
		table.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				int column = table.convertColumnIndexToModel(table.columnAtPoint(e.getPoint()));
				if (column == 0) {
					table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));					
				} else {
					table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
			public void mouseExited(MouseEvent e) {
				int column = table.convertColumnIndexToModel(table.columnAtPoint(e.getPoint()));
				if (column == 0) {
					table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
			public void mouseClicked(MouseEvent e) {
				int column = table.convertColumnIndexToModel(table.columnAtPoint(e.getPoint()));
				int row = sorter.modelIndex(table.rowAtPoint(e.getPoint()));
				if (column == 0 && row > -1 && row < sorter.getRowCount()) {
					Resource ri = (Resource)sorter.getTableModel().getValueAt(row,column);
					browser.open(ri.getId());
				}
			}
		});
		sorter.setTableHeader(table.getTableHeader()); 
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

	/** removes previous results, just leaving the skeleton */
	protected void clearTree() {
		// reset selection too.
		vizModel.clear();
		vizualizations.refocusMainNodes();
		setProgressMax(1);
		setProgressValue(0);
	}

	/** 
	 * Create a menu item from a stored representation of one item in the search history.
	 * @param historyItem - whatever 
	 * @return  a menu item, with configured action handler to fill in the search form.
	 * may return null if cannot parse the history item.
	 */
	protected abstract JMenuItem createHistoryMenuItem(String historyItem);

	/**
	 * subclasses should implement to provide a search form to input parameters.
	 * Don't provide a submit button or border - these are taken care of in this
	 * class.
	 * 
	 * @return an unattached search panel.
	 */
	protected abstract JPanel createSearchPanel();



	/**
	 * Grab a history item string from the current UI. 
	 * @return a string that can later be used as input to {@link #createHistoryMenuItem(String)}
	 */
	protected abstract String grabHistoryItem();

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

	/** define to acutally perform the query */
	protected abstract void query();

	/**
	 * take a snapshot of current form inputs, and ad to the history list.
	 * Call this at an appropriate point withing 'query()' to save searcg state to history.
	 */
	protected void storeHistoryItem() {
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
	
	// plastic integration
	
	
	public void contentsChanged(ListDataEvent e) {
		// clear, and rebuild.
		componentMap.clear();
		dynamicButtons.removeAll();
		ListModel lm = tupperware.getRegisteredApplicationsModel();
		for (int i = 0; i < lm.getSize(); i++) {
			PlasticApplicationDescription plas= (PlasticApplicationDescription)lm.getElementAt(i);
			addPlasticApp(plas); // nice- loads all in parallel in background thread..
		}
	}
	public void intervalAdded(ListDataEvent e) {
		ListModel lm = tupperware.getRegisteredApplicationsModel();
		for (int i = e.getIndex0(); i<= e.getIndex1(); i++) {
			PlasticApplicationDescription plas = (PlasticApplicationDescription)lm.getElementAt(i);
			addPlasticApp(plas);
		}
	}
	public void intervalRemoved(ListDataEvent e) {
// do nothing 
	}
	
	public void objectsRemoved(Object[] obj) {
	
		for (int i = 0; i < obj.length; i++) {
			removePlasticApp((PlasticApplicationDescription)obj[i]);
		}
	}
	
	/** used to keep track of the buttons we've got present */
    private final Map componentMap = new HashMap();

    
    private void addPlasticApp(final PlasticApplicationDescription plas) {
    	// runs in a background thread, as involved fetching icons from urls.
    	(new BackgroundOperation("Building Buttons for  "+ plas.getName()) {                     
    	        protected Object construct() throws Exception {
    	    		return buildPlasticButtons(plas);
    	        }
    	                
    	        protected void doFinished(Object result) {
    	            // add the freshly created button to the UI
    	            Component[] components = (Component[])result;
    	            if (components == null || components.length == 0) {
    	                return;
    	            }
    	            for (int i = 0; i < components.length; i++) {
    	                dynamicButtons.add(components[i]);
    	            }
    	                	           
    	            // record what components pertain to this application.
    	            componentMap.put(plas,components);     
    	            dynamicButtons.revalidate(); // fixes bz 1777
    	            dynamicButtons.repaint();
    	        }
    	    }).start(); 	
    }
    
    private void removePlasticApp(PlasticApplicationDescription id) {
        if (! componentMap.containsKey(id)) {
            return;
        }
        final Component[] components = (Component[]) componentMap.get(id);
        if (components == null || components.length ==0) {
            return;
        }
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {           
                for (int i = 0; i < components.length; i++) {
                    dynamicButtons.remove(components[i]);
                }
                dynamicButtons.revalidate(); // fixes bz 1777
                dynamicButtons.repaint();
            }
         });
    }
	
	/** Builds VOTABLE and FITS load plastic buttons for applications that support this.
	 * Override this method to add further button types.
	 * @param plas
	 * @return
	 */
	protected Component[] buildPlasticButtons(final PlasticApplicationDescription plas) {
		List results = new ArrayList();
		if (plas.understandsMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL)) {
			results.add(new VotableLoadPlasticButton(plas, vizModel.getSelectionFocusSet(),
					AbstractScope.this,tupperware));
		}
		if (plas.understandsMessage(CommonMessageConstants.FITS_LOAD_FROM_URL)) {
			results.add(new ImageLoadPlasticButton(plas, vizModel.getSelectionFocusSet(),
					AbstractScope.this, tupperware));
		}
		return (Component[]) results.toArray(new Component[results.size()]);
	}
}