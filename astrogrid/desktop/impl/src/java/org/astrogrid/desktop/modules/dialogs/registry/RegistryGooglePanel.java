/*$Id: RegistryGooglePanel.java,v 1.9 2007/03/08 17:43:59 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.dialogs.registry;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.xml.stream.XMLStreamReader;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.registry.FilterPipelineFactory.PipelineStrategy;
import org.astrogrid.desktop.modules.dialogs.registry.srql.BasicRegistrySRQLVisitor;
import org.astrogrid.desktop.modules.dialogs.registry.srql.Builder;
import org.astrogrid.desktop.modules.dialogs.registry.srql.KeywordSRQLVisitor;
import org.astrogrid.desktop.modules.dialogs.registry.srql.SRQL;
import org.astrogrid.desktop.modules.dialogs.registry.srql.SRQLParser;
import org.astrogrid.desktop.modules.dialogs.registry.srql.SRQLVisitor;
import org.astrogrid.desktop.modules.dialogs.registry.strategy.AuthorityStrategy;
import org.astrogrid.desktop.modules.dialogs.registry.strategy.CapabilityStrategy;
import org.astrogrid.desktop.modules.dialogs.registry.strategy.ContentLevelStrategy;
import org.astrogrid.desktop.modules.dialogs.registry.strategy.CreatorStrategy;
import org.astrogrid.desktop.modules.dialogs.registry.strategy.PublisherStrategy;
import org.astrogrid.desktop.modules.dialogs.registry.strategy.SubjectsStrategy;
import org.astrogrid.desktop.modules.dialogs.registry.strategy.TypeStrategy;
import org.astrogrid.desktop.modules.dialogs.registry.strategy.TypesStrategy;
import org.astrogrid.desktop.modules.dialogs.registry.strategy.UcdStrategy;
import org.astrogrid.desktop.modules.dialogs.registry.strategy.WavebandStrategy;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceStreamParser;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.BiStateButton;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;
import org.votech.VoMon;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ListSelection;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.TableComparatorChooser;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Implementation of the registry-google chooser.
 * @todo implement using standard xquery
 * @todo optimize query - no //vor:resouc
 * @todo understand why this component manages it's own cache - doesn't the registry client do all the 
 * caching that's necessary? not a bug, just not time to understand this yet.
 * 		     //@todo add search-in-results.
		     //@todo add grouping / subsetting ??
 */
public class RegistryGooglePanel extends JPanel
implements ActionListener, PropertyChangeListener, ListEventListener, ListSelectionListener, ChangeListener, TableModelListener {

	private static final Log logger = LogFactory
			.getLog(RegistryGooglePanel.class);

	
	/** an asbtract background worker that provides machinery for processing the results of a streaming parse
	 * and caching the result */
	private abstract class Worker extends BackgroundWorker implements StreamProcessor {
		protected final Log logger = LogFactory.getLog(Worker.class);
		
		public Worker(UIComponent parent, String message) {
			super(parent,message);
		}

		// callback from the xml stream reader.
		public void process(XMLStreamReader reader) throws Exception {
			ResourceStreamParser p = new ResourceStreamParser(reader);
			load(p);	
		}
		/**
		 * @param p
		 */
		protected void load(ResourceStreamParser p) {
			while (p.hasNext()) {
				final Resource r = (Resource)p.next();
				try {
					items.getReadWriteLock().writeLock().lock();
					items.add(r);
				} finally {
					items.getReadWriteLock().writeLock().unlock();
				}
			}
		}
		protected void load(Resource[] arr) {
			for (int i = 0; i < arr.length; i++) {
				try {
					items.getReadWriteLock().writeLock().lock();
					items.add(arr[i]);
				} finally {
					items.getReadWriteLock().writeLock().unlock();
				}
			}			
		}
		
		protected void cacheResult( final String key) {
			Resource[] arr = (Resource[]) items.toArray(new Resource[items.size()]);
			Element el = new Element(key,arr);
			bulk.put(el);
			for (int i = 0; i < arr.length; i++) {
				if (resources.get(arr[i].getId()) == null) {
					resources.put(new Element(arr[i].getId(),  arr[i]));
				}
			}									
		}
	
		protected void runQuery(String xq) throws ServiceException {
			// check bulk cache.
			Element el = bulk.get(xq);
			if (el != null) {
				load((Resource[])el.getValue()); 
				return;// found a cached result - halt here.
			}
			reg.xquerySearchStream(xq,this);
			// no need to lock - as we know we're the thread that was doing the modifying. and it's finished now.
			if (items.size() > 0) {
				cacheResult(xq);
			}
		}
		protected void doAlways() {
			goButton.enableA();
		}
	}
	
	/** worker class that does a SRQL search
	 * @author Noel Winstanley
	 * @since Aug 15, 20062:00:41 AM
	 */
	private final class SearchWorker extends Worker{

		private final SRQL q;

		public SearchWorker(UIComponent parent, String msg, SRQL q) {
			super(parent, msg);
			this.q = q;	
		}

		protected Object construct() throws Exception {   
				// produce a query from the search parse tree.
				String briefXQuery = briefXQueryBuilder.build(this.q,filter);
				runQuery(briefXQuery);
				return null;
		}
	}
	
	/** worker class that does a query */
	private final class QueryWorker extends Worker{

		private final String q;

		public QueryWorker(UIComponent parent, String query) {
			super(parent,"Loading query");
			this.q = query;	
		}

		protected Object construct() throws Exception {   
			runQuery(q);
			return null;
		}
	}
	
	/** worker class that lists results of filtering the registry
	 * @author Noel Winstanley
	 * @since Aug 15, 20062:00:41 AM
	 */
	private final class FilterWorker extends Worker{

		public FilterWorker(UIComponent parent, String filter) {
			super(parent, "Listing contents");
			this.filter = filter;
		}
		private final String filter;

		protected Object construct() throws Exception {   
				String xq =makeXQueryFromFilter(filter);
				runQuery(xq);
				return null;
		}
		protected void doFinished(Object result) {
			selectTable.selectAll();
			
		}

	}	
	
	/** worker class that retrieves records for a list of ids.
	 * @author Noel Winstanley
	 * @since Aug 15, 20062:00:41 AM
	 */
	private final class ListWorker extends Worker{

		public ListWorker(UIComponent parent, Collection ids) {
			super(parent, "Loading List");
			this.ids = ids;
		}
		private final Collection ids;
		
		protected Object construct() throws Exception {   
				String xq = makeXQueryFromIdSet(ids);
				runQuery(xq);
				return null;
		}
		protected void doFinished(Object result) {
			selectTable.selectAll();
			
		}
	}
	// no state - so can be reused between instances.
	static final SRQLVisitor feedbackVisitor = new KeywordSRQLVisitor();	
	static final Builder briefXQueryBuilder = new BasicRegistrySRQLVisitor();
	
	// member variables.
	protected String filter = null;
	private final BiStateButton goButton ;
	private final JLabel searchCount;
	private final AutoCompleteHistoryField keywordField;
	protected final ResourceTable selectTable;
	protected final EventList  items ;
	private final EventList edtItems; // a view of the items event list, on the Event dispatch thread.
    protected final EventTableModel selectTableModel;
	// tracks the currently clicked on registry entry - i.e. the one to display in viewer
	private final EventSelectionModel currentResourceInView;
	// tracks the entries currently 'checked'
	protected final ListSelection selectedResources;
	protected final JTabbedPane tabPane;
	protected final RegistryInternal reg;
	protected final Ehcache resources ;
	protected final Ehcache bulk;
	protected final Preference advancedPreference;
	protected final ResourceViewer  xmlPane;
	protected final ResourceViewer detailsPane;
	private final JLabel keywordFieldLabel;
	
	public final UIComponentBodyguard parent;
	
	
	public JPopupMenu getPopup() {
		return selectTable.getPopup();
	}
	
	/** Construct a new RegistryChooserPanel
	 * 
	 * @param uiParent parent component
	 * @param reg used to perform the query
	 * @param browser used to display external resources
	 * @param regBrowser used to display related registry entires.
	 * @param fac caches resources.
	 * @param vomon used to annotate registry entries with availability information
	 * @param pref controls whether to display 'advanced' features of the ui.
	 */
	public RegistryGooglePanel(final RegistryInternal reg, final BrowserControl browser, final RegistryBrowser regBrowser,
			final Ehcache resources, final Ehcache bulk,
			final VoMon vomon, Preference pref) {
		this(reg,browser,regBrowser,resources, bulk,vomon,pref,true);
	}

	/** Construct a new RegistryChooserPanel
	 * 
	 * @param uiParent parent component
	 * @param reg used to perform the query
	 * @param browser used to display external resources
	 * @param regBrowser used to display related registry entires.
	 * @param fac caches resources.
	 * @param vomon used to annotate registry entries with availability information
	 * @param pref controls whether to display 'advanced' features of the ui.
	 * @param showCheckBox if false, hides the first column of the registry entries list.
	 */
	public RegistryGooglePanel(final RegistryInternal reg, final BrowserControl browser, final RegistryBrowser regBrowser, 
			final Ehcache resources, final Ehcache bulk
			, final VoMon vomon, Preference pref, boolean showCheckBox) {

		super();    
				
		this.parent = new UIComponentBodyguard();
		this.reg = reg;
		this.resources = resources;
		this.bulk = bulk;
		this.advancedPreference = pref;

		// prelims
		this.setSize(new Dimension(500,800));
		setLayout(new BorderLayout());
		CSH.setHelpIDString(this, "reg.general");
		
	//DATAPIPELINE
		// create datamodel.
		items= new BasicEventList();
		
		//listen to changes to items list - define a view that always fires events on the EDT
		edtItems = GlazedListsSwing.swingThreadProxyList(items);
		edtItems.addListEventListener(this);
		
		// sorted view of this model . all ui component should attach to this.
		SortedList sortedItems = new SortedList(items,new ResourceTitleComparator());
		

		PipelineStrategy[] pStrategies = new PipelineStrategy[] {
				new TypeStrategy()
				,new SubjectsStrategy()
				, new TypesStrategy()
				, new CapabilityStrategy()
				, new WavebandStrategy()
				, new ContentLevelStrategy()
				,new AuthorityStrategy()
				,new PublisherStrategy()
				,new CreatorStrategy()
				,new UcdStrategy()
				//@todo move strategies out to hivemind? probably necessary for more advanced ones.
				// @future add strategies for meta-metadata - last used, recently added, tags, etc.
				};
		FilterPipelineFactory mPipeline = new FilterPipelineFactory(sortedItems,pStrategies);
		EventList filteredItems = mPipeline.getFilteredItems();

				
		// list of currently 'ticked' resources
		selectedResources = new ListSelection (filteredItems); 
		selectedResources.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
   		
		// item currenlty selected in table list.
		currentResourceInView = new EventSelectionModel(filteredItems);
		currentResourceInView.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
		currentResourceInView.addListSelectionListener(this); // assume this happens on EDT?
		
		// top panel. am using jforms to lay this out.
		FormLayout form = new FormLayout(
				"4dlu, right:pref, 4dlu, 50dlu:grow,4dlu,pref,4dlu,left:40dlu,4dlu" // cols
				,"pref, 3dlu, pref, 3dlu, pref" // rows
				);
		PanelBuilder builder = new PanelBuilder(form);
		CellConstraints cc = new CellConstraints();
		keywordFieldLabel = builder.addLabel("Search", cc.xy(2,1));
		final KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
		final Action searchAction = new AbstractAction() {
			{
				putValue(Action.SMALL_ICON,IconHelper.loadIcon("search16.png"));
				putValue(Action.MNEMONIC_KEY,new Integer(KeyEvent.VK_S)); // think this is how to do it.
				putValue(Action.SHORT_DESCRIPTION,"Retrieve matching resources from the registry");		
			}
			public void actionPerformed(final ActionEvent e) {
						RegistryGooglePanel.this.actionPerformed(e);
				}
		};
		final Action haltAction = new AbstractAction() {
			{
				putValue(Action.SMALL_ICON,IconHelper.loadIcon("stop16.png"));
				putValue(Action.MNEMONIC_KEY,new Integer(KeyEvent.VK_H)); // think this is how to do it.
				putValue(Action.SHORT_DESCRIPTION,"Halt search");		
			}
			public void actionPerformed(ActionEvent e) {
				goButton.enableA();
				RegistryGooglePanel.this.parent.get().haltAll();			
			}
		};
		keywordField = new AutoCompleteHistoryField(this);
		//keywordField.addActionListener(this);
		CSH.setHelpIDString(keywordField, "reg.search");
		keywordField.setToolTipText("<html>Enter keywords to search for,<br>a phrase in quotes<br> logical operators - AND, OR, NOT,(,),<br> or see help for further details</html>");
		builder.add(keywordField, cc.xy(4, 1));
				
		goButton = new BiStateButton(searchAction,haltAction);
		CSH.setHelpIDString(goButton, "reg.search");
		builder.add(goButton, cc.xy(6, 1));
		
		this.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter,"search");
		this.getActionMap().put("search",searchAction);
		
		searchCount = new JLabel();
		builder.add(searchCount,cc.xy(8,1));

		builder.addLabel("Filter",cc.xy(2, 3));
		final JTextField filterField = mPipeline.getTextField();
		CSH.setHelpIDString(filterField, "reg.filter");
		builder.add(filterField,cc.xy(4, 3));
		builder.add(mPipeline.getExpandButton(),cc.xy(6, 3));
		final JComponent filters = mPipeline.getFilters();
		CSH.setHelpIDString(filters, "reg.filters");
		builder.add(filters,cc.xyw(2, 5,7));
	
		add(builder.getPanel(),BorderLayout.NORTH);

		// middle pane
		
		// model for the table.
		selectTableModel= new EventTableModel(filteredItems,new ResourceTableFomat(vomon,selectedResources,showCheckBox));          		
		selectTableModel.addTableModelListener(this);
		selectTable = new ResourceTable(selectTableModel,showCheckBox,filteredItems,vomon);
		CSH.setHelpIDString(selectTable, "reg.table");
		selectTable.setSelectionModel(currentResourceInView);
		// surprising - this is all that's needed tp add sorting to columns in the table.
		new TableComparatorChooser(selectTable,sortedItems,true);
		JComponent centerPanel = new JScrollPane(selectTable);
		selectTable.setPreferredScrollableViewportSize(new Dimension(300,300));
		centerPanel.setBorder(BorderFactory.createEmptyBorder());
		centerPanel.setPreferredSize(new Dimension(300,300));
		

		// bottom pane.
		tabPane = new JTabbedPane();    
		tabPane.setBorder(BorderFactory.createEmptyBorder());
		tabPane.addChangeListener(this);
		tabPane.setPreferredSize(new Dimension(200,200));
		detailsPane = new FormattedResourceViewer(browser,regBrowser);
		CSH.setHelpIDString(detailsPane.getComponent(), "reg.details");
		
		xmlPane = new XMLResourceViewer(parent,reg);
		CSH.setHelpIDString(xmlPane.getComponent(), "reg.xml");
		final JScrollPane scrollPane = new JScrollPane((Component)detailsPane,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		tabPane.addTab("Details", IconHelper.loadIcon("info16.png")
				, scrollPane
				, "Details of chosen resource");
		// xml pane not added to tabs in same way - as is an 'advanced' view.
		// will be added / removed in the property change listener, when initialized from preferneces.
		
		// stitch middle and bottom together.
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,centerPanel,tabPane);
		split.setPreferredSize(new Dimension(300,500));
	//	split.setSize(new Dimension(300,200));
		split.setDividerSize(5);
		split.setDividerLocation(300);
		split.setOneTouchExpandable(true);
		split.setBorder(BorderFactory.createEmptyBorder());
		add(split,BorderLayout.CENTER);
		
		advancedPreference.addPropertyChangeListener(this);
		advancedPreference.initializeThroughListener(this);

	}
	/** triggered when value of preference changes. - shows / hides xml representation. */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == this.advancedPreference ) {
			if (advancedPreference.asBoolean()) {
				tabPane.addTab("XML entry", IconHelper.loadIcon("xml.gif"), xmlPane.getComponent(), "View the XML as entered in the registry");       			
			} else {
				int ix = tabPane.indexOfComponent(xmlPane.getComponent());
				if (ix != -1) {
					tabPane.removeTabAt(ix);
				}
			}

		}
	}
	
// view updating methods	

	/** triggered when resource list contents change - glazeed lists will always call this on the EDT*/
	public void listChanged(ListEvent arg0) {
		while(arg0.next()) { // I assume this is the correct pattern
			if (arg0.getType() == ListEvent.DELETE) {// delete is only ever a clear.
				detailsPane.clear();
				xmlPane.clear();
				tabPane.setSelectedIndex(0);
			}
		}
		// update total size count.
		int sz = edtItems.size();
		switch (sz) {
		case 0:
			searchCount.setText("");
			break;
		case 1:
			searchCount.setText("1 result");
			break;
		default:
			searchCount.setText(sz + " results");
		}
	}
	/** triggered when contents of table change */
	public void tableChanged(TableModelEvent e) {
		if (e.getType() != TableModelEvent.UPDATE) { // only interested in add or delete events.
			int resultSize = edtItems.size();
			int viewSize = selectTableModel.getRowCount();
			if (viewSize != resultSize) {
				parent.get().setStatusMessage("Filtering - showing " + viewSize + " results");
			} else {
				parent.get().setStatusMessage("Showing all results");
			}
			// on every change, if nothing is selcted, and there are some results,, select (display) first record.
			if (viewSize > 0 && selectTable.getSelectedRow() == -1) {
					selectTable.changeSelection(0, 1, false, false);			
			}  			
		}
	}
	
	/** trigger when which reosurce is selected changes */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		updateViewers();
	}
	/** triggered when selected tab changes */
	public void stateChanged(ChangeEvent ignored) {
		previous = null; // view has changed, so need to re-render.
		updateViewers();
	}
	private Resource previous;// tries to make things idempotent.
	/** controller that takes care of displayng the current resource in the currently visible viewer */
	private void updateViewers() {
		List l  = currentResourceInView.getSelected();
		if (l.isEmpty()) {
			return;
		}
		Resource res = (Resource)l.get(0); //@todo make this work nicely when I've got a multiple selection going on - want to show latest selection.
		if (res == previous) { // list has changed, but selected item is the same.
			return;
		}
		previous = res;
		switch (tabPane.getSelectedIndex()) {
		case 0:
			detailsPane.display(res);
			break;
		case 1:
			xmlPane.display(res);
		default:
			break;
		}
	}
	
//	action methods
	public void doSearch(String s) {
		keywordField.setValue(s);
		goButton.doClick();
	}

	public void doOpen(final URI ivorn) {
		Collection c= new ArrayList();
		c.add(ivorn);
		displayIdSet(c);
	}
	/**
	 * called when 'search' button is pressed., or return hit in search field.
	 */
	public void actionPerformed(ActionEvent e) {
		goButton.enableB();
		String searchTerm =  keywordField.getValue();
		clear();
		SRQLParser qp = new SRQLParser(searchTerm);
		SRQL q;
		try {
			q = qp.parse();
			if (q == null) {
				return; // nothing to parse.
			}
			String summary =(String) q.accept(feedbackVisitor);
			(new SearchWorker(parent.get(), "Searching for: " + summary, q)).start();
		} catch (InvalidArgumentException x) {
			parent.get().showError("Failed to parse search expression",x);
		}
	}


// access the current selection, in various ways.
	/** access the resources selected by the user
	 * @return an array of resources. - maybe an empty array, but never null;
	 */
	public Resource[] getSelectedResources() {
		List l = selectedResources.getSelected();
		Resource[] results = new Resource[l.size()];
		results = (Resource[])l.toArray(results);
		return results;                
	}

	/** expose this as a public method - so then interested clients can register listeners on the selection model */
	public ListSelection getListSelection() {
		return this.selectedResources;
	}
	
	/** expose the currently viewed resource */
	public EventSelectionModel getCurrentResourceModel() {
		return this.currentResourceInView;
	}
	
	/* clear the search results */
	public void clear() {
			// wonderwhether I should lock here?? - or need to clear somethig else at least.
		try {
		items.getReadWriteLock().writeLock().lock();
		items.clear();
		} finally {
			items.getReadWriteLock().writeLock().unlock();
		}
	}
// configure the behaviour of htis component.
	public boolean isMultipleResources() {
		return selectedResources.getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
	}

	/** set an additional result filter
	 * @param filter an xquery where clause, null indicates 'no filter'
	 */
	public void applyFilter(String filter) {
		clear(); 
		this.filter = filter;  
		setSearchVisible(true);
	}
	
	/** set scope to display the results of applying this filter to registry
	 * 
	 * @param filter an xquery where clause. Needs to filter to < 500 resouces to 
	 * be sensible.
	 */
	public void displayFilter(String filter) {
		clear();
		setSearchVisible(false);
		(new FilterWorker(parent.get(),filter)).start();
	}
	
	/** set scope to just display this list of resources */
	public void displayIdSet(Collection idList) {
		clear();
		this.filter = null;
		setSearchVisible(false);
		(new ListWorker(parent.get(),idList)).start();		
	}
	
	/** set scope to display results of this query
	 * @param query
	 */
	public void displayQuery(String query) {
		clear();
		this.filter = null;
		setSearchVisible(false);
		(new QueryWorker(parent.get(),query)).start();
	}
	
	/** build an xquery that will retrieve all items in a list */
	public static String makeXQueryFromIdSet(Collection l) {
		//@later investigae whether some kind of ' vr:identifier in idset' form is more efficient
		StringBuffer sb = new StringBuffer("for $r in //vor:Resource[not (@status = 'inactive' or @status= 'deleted')]\nwhere (");
		for (Iterator i = l.iterator(); i.hasNext();) {
			URI id = (URI) i.next();
			sb.append("$r/vr:identifier = '").append(id.toString()).append("'"); // @todo - am doing straight match here, not 'like' - hope it ignores whitespace - need to test this.
			if (i.hasNext()) {
				sb.append(" or ");
			}
		}
		sb.append(")\nreturn $r");
		return sb.toString();	
	}
	
	/** buld an xquery that will retrive all items passed by a filter */
	public static String makeXQueryFromFilter(String filter) {
		if (filter == null || filter.trim().length() ==0) {
			throw new IllegalArgumentException("Bang! - just downloaded the registry");
		}
		StringBuffer sb = new StringBuffer("for $r in //vor:Resource[not (@status = 'inactive' or @status= 'deleted')]\nwhere (");
		sb.append(filter);
		sb.append(")\nreturn $r");
		return sb.toString();		
	}

	/** set whether user is permitted to select multiple resources 
	 * @param multiple if true, multiple selection is permitted.*/
	public void setMultipleResources(boolean multiple) {
		selectedResources.setSelectionMode(multiple ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);     
	}
	/** whether the search field is visible or not. */
	public boolean isSearchVisible() {
		return this.keywordField.isVisible();
	}
	public void setSearchVisible(boolean b) {
		this.keywordField.setVisible(b);
		this.goButton.setVisible(b);
		this.keywordFieldLabel.setVisible(b);
		this.searchCount.setVisible(b);
	}

	/**
	 * @return
	 */
	public Transferable getSelectionTransferable() {
		return selectTable.getSelectionTransferable();
	}

}

/* 
$Log: RegistryGooglePanel.java,v $
Revision 1.9  2007/03/08 17:43:59  nw
first draft of voexplorer

Revision 1.8  2007/01/29 10:51:49  nw
moved cache configuration into hivemind.

Revision 1.7  2007/01/23 11:49:10  nw
preferences integration.

Revision 1.5  2007/01/10 19:12:16  nw
integrated with preferences.

Revision 1.4  2007/01/09 16:19:33  nw
uses vomon.

Revision 1.3  2006/09/02 00:48:34  nw
fixed caching bug

Revision 1.2  2006/08/31 21:33:38  nw
finsihed query parser

Revision 1.1  2006/08/15 10:14:14  nw
supporting classes for new registry google UI

Revision 1.35  2006/07/18 13:12:08  KevinBenson
placed the action map to WHEN_ANCESTOR_OF_FOCUSED_COMPONENT so the go button is searched when a user hits return/enter

Revision 1.34  2006/06/27 19:12:49  nw
adjusted todo tags.

Revision 1.33  2006/06/27 10:28:47  nw
findbugs tweaks

Revision 1.32  2006/05/17 15:45:17  nw
factored common base class out of astroscope and helioscope.improved error-handline on astroscope input.

Revision 1.31  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.30  2006/03/14 15:46:21  pjn3
Scrollpane replaced

Revision 1.29  2006/03/14 15:06:40  pjn3
reduced WRAP_LENGTH slightly

Revision 1.28  2006/03/14 13:58:46  pjn3
Added basic cell renderer to wrap cell text e.g. descriptions

Revision 1.27  2006/03/10 17:33:19  pjn3
set tree model to null when no entry available to prevent default 'color' tree appearing

Revision 1.26  2006/03/09 14:48:33  pjn3
Initial work to add JTree to registry browser

Revision 1.25  2006/03/06 17:05:50  pjn3
Correctly clear selection

Revision 1.24  2006/02/24 12:41:43  KevinBenson
added the filter on exhaustiveQuery

Revision 1.23  2006/02/24 11:06:46  KevinBenson
the joins were not working right for and's/or's

Revision 1.22  2006/01/23 14:38:52  KevinBenson
reverting back to 1.20 version

Revision 1.20  2005/12/16 12:30:01  pjn3
 *** empty log message ***

Revision 1.19  2005/12/16 12:03:12  pjn3
render immediately if only 1 match

Revision 1.18  2005/12/16 10:35:00  pjn3
 *** empty log message ***

Revision 1.17  2005/12/15 15:19:07  pjn3
corrected when checkbox appear

Revision 1.16  2005/12/08 13:01:12  pjn3
Merge of pjn_wprkbench_1_12_05

Revision 1.15.4.2  2005/12/08 11:35:14  pjn3
Prevent checkbox appearing when not req'd

Revision 1.15.4.1  2005/12/08 10:37:46  pjn3
Moved xml to seperate tab to improve display

Revision 1.15  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.14  2005/11/22 18:58:19  pjn3
added XHTMLEditorKit to improve how xhtml is displayed in JEditorPane

Revision 1.13  2005/11/17 13:34:59  pjn3
Query sql string alterred to include vr:content as per Kevin's email

Revision 1.12  2005/11/11 15:25:08  pjn3
caret added

Revision 1.11.24.1  2005/11/11 15:13:23  pjn3
caret added

Revision 1.11  2005/09/09 10:19:39  nw
implemented filtering

Revision 1.10  2005/09/09 09:59:14  nw
fixed status bar messages.

Revision 1.9  2005/09/09 08:45:40  KevinBenson
added "and"/"or" ability to be placed in the text field so it will be ignored and be used for the join operations between keywords

Revision 1.8  2005/09/09 08:09:51  KevinBenson
small changes on the query side to put them in methods to better do exhuastivequeries

Revision 1.7  2005/09/08 13:53:30  KevinBenson
small change to wordwrap text

Revision 1.6  2005/09/08 11:14:08  nw
adjusted visible model object.

Revision 1.5  2005/09/08 10:45:28  KevinBenson
small fixes on the sizing and arrangement of components

Revision 1.4  2005/09/07 14:27:24  nw
added background workers.
implemented a custom table model to maintain state.

Revision 1.3  2005/09/07 11:34:10  KevinBenson
changes to reg chooser to use jtable and jeditorpane.

Revision 1.1  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs

 */