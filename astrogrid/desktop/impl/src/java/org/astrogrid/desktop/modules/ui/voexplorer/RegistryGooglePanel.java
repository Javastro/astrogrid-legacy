/*$Id: RegistryGooglePanel.java,v 1.5 2007/06/18 16:41:43 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

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
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceStreamParser;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.actions.DevSymbols;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;
import org.astrogrid.desktop.modules.ui.voexplorer.google.CapabilityIconFactory;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FormattedResourceViewer;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceFormViewer;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTable;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTableFomat;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTitleComparator;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceViewer;
import org.astrogrid.desktop.modules.ui.voexplorer.google.TabularMetadataViewer;
import org.astrogrid.desktop.modules.ui.voexplorer.google.XMLResourceViewer;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.BasicRegistrySRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.Builder;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.KeywordSRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.AuthorityStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.CapabilityStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.ContentLevelStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.CreatorStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.PublisherStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.SubjectsStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.TypeStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.TypesStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.UcdStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.WavebandStrategy;
import org.votech.VoMon;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ListSelection;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.gui.TableFormat;
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
			fireLoadStarted();
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
		protected void doFinished(Object result) {
			resourceTable.selectAll();
			
		}		
		protected void doAlways() {
			fireLoadCompleted();
		}
	}
	
	/** worker class that does a SRQL search
	 * @author Noel Winstanley
	 * @since Aug 15, 20062:00:41 AM
	 */
	private final class SRQLWorker extends Worker{

		private final SRQL q;

		public SRQLWorker(UIComponent parent,  SRQL q) {
			super(parent, "Loading query");
			this.q = q;	
		}
		public SRQLWorker(String title,UIComponent parent,  SRQL q) {
			super(parent, "Loading " + title);
			this.q = q;	
		}
		protected Object construct() throws Exception {   
				// produce a query from the search parse tree.
				String briefXQuery = briefXQueryBuilder.build(this.q,null);
				runQuery(briefXQuery);
				return null;
		}
	}
	
	/** worker class that does a query */
	private final class XQueryWorker extends Worker{

		private final String q;

		public XQueryWorker(UIComponent parent, String query) {
			super(parent,"Loading query");
			this.q = query;	
		}
		public XQueryWorker(String title,UIComponent parent, String query) {
			super(parent,"Loading " + title);
			this.q = query;	
		}
		
		protected Object construct() throws Exception {   
			runQuery(q);
			return null;
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
		
		public ListWorker(String title,UIComponent parent, Collection ids) {
			super(parent, "Loading " + title);
			this.ids = ids;
		}		
		private final Collection ids;
		
		protected Object construct() throws Exception {   
				String xq = makeXQueryFromIdSet(ids);
				runQuery(xq);
				return null;
		}
	}
	// no state - so can be reused between instances.
	static final SRQLVisitor feedbackVisitor = new KeywordSRQLVisitor();	
	static final Builder briefXQueryBuilder = new BasicRegistrySRQLVisitor();
	
	// member variables.
	private final JButton haltButton ;
	private final JButton newButton;
	private final JLabel searchCount;
	private final JLabel filterCount;
	private final JLabel searchTitle;
	protected final ResourceTable resourceTable;
	protected final EventList  items ;
	protected final EventList edtItems; // a view of the items event list, on the Event dispatch thread.
    protected final EventTableModel resourceTableModel;
	// tracks the currently clicked on registry entry - i.e. the one to display in viewer
	private final EventSelectionModel currentResourceInView;

	protected final JTabbedPane tabPane;
	protected final RegistryInternal reg;
	protected final Ehcache resources ;
	protected final VoMon vomon;
	protected final CapabilityIconFactory iconFac;
	protected final Ehcache bulk;
	protected final Preference advancedPreference;
	protected final ResourceViewer  xmlPane;
	protected final ResourceViewer detailsPane;
	protected final ResourceViewer tableMetadataPane;
	protected final JComponent toolbar;
	// stuff that's accessed when composing this pane together in the UI.
	public final UIComponentBodyguard parent;
	
	public void setPopup(JPopupMenu popup) {
		resourceTable.setPopup(popup);
	}
	
	/** access the new search button - so a handler can be attached to it */
	public JButton getNewSearchButton() {
		return newButton;
	}
	public JButton getHaltSearchButton() {
		return haltButton;
	}
	
	/** access the search title label */
	public JLabel getSearchTitleLabel() {
		return searchTitle;
	}

	/** Construct a new RegistryChooserPanel
	 * 
	 * @param reg used to perform the query
	 * @param browser used to display external resources
	 * @param regBrowser used to display related registry entires.
	 * @param resources caches resources.
	 * @param vm used to annotate registry entries with availability information
	 * @param pref controls whether to display 'advanced' features of the ui.
	 */
	public RegistryGooglePanel(final RegistryInternal reg, final BrowserControl browser, final RegistryBrowser regBrowser, 
			final Ehcache resources, final Ehcache bulk
			, final VoMon vm, final CapabilityIconFactory iconFac,Preference pref) {
		super();    
		this.parent = new UIComponentBodyguard();
		this.reg = reg;
		this.resources = resources;
		this.bulk = bulk;
		this.advancedPreference = pref;
		this.vomon = vm;
		this.iconFac = iconFac;

		// prelims
		//this.setSize(new Dimension(500,800));
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
				new SubjectsStrategy()
				, new WavebandStrategy()
				,new TypeStrategy()
				, new TypesStrategy()
				,new UcdStrategy()
				, new ContentLevelStrategy()
				,new PublisherStrategy()
				,new AuthorityStrategy()
				,new CreatorStrategy()
				, new CapabilityStrategy()
				//@todo move strategies out to hivemind? probably necessary for more advanced ones.
				// @future add strategies for meta-metadata - last used, recently added, tags, etc.
				};
		FilterPipelineFactory mPipeline = new FilterPipelineFactory(sortedItems,pStrategies);
		EventList filteredItems = mPipeline.getFilteredItems();

		// item currenlty selected in table list.
		currentResourceInView = new EventSelectionModel(filteredItems);
		currentResourceInView.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
		currentResourceInView.addListSelectionListener(this); // assume this happens on EDT?
		
		FormLayout form = new FormLayout(
				"1dlu, pref,1dlu,pref,2dlu,left:pref:grow,2dlu,right:30dlu,left:60dlu,6dlu,70dlu,pref,pref,1dlu" // cols
				,"pref" // rows
				);
		PanelBuilder builder = new PanelBuilder(form);
		CellConstraints cc = new CellConstraints();
		
		newButton = new JButton("New Search");
		newButton.setToolTipText("Create a new Smart list by searching the Registry");
		newButton.setEnabled(true);
		builder.add(newButton,cc.xy(2,1));
		
		haltButton = new JButton("Stop Search");
		haltButton.addActionListener(this);
		haltButton.setEnabled(false);
		
		CSH.setHelpIDString(haltButton, "reg.search");
		builder.add(haltButton, cc.xy(4, 1));
		
		searchTitle = builder.addLabel("",cc.xy(6,1));
		
		filterCount = new JLabel();
		builder.add(filterCount,cc.xy(8,1));
		searchCount = new JLabel();
		builder.add(searchCount,cc.xy(9,1));

		final JTextField filterField = mPipeline.getTextField();
		CSH.setHelpIDString(filterField, "reg.filter");
		builder.add(filterField,cc.xy(11, 1));
		builder.add(mPipeline.getSystemToggleButton(),cc.xy(12,1));
		builder.add(mPipeline.getExpandButton(),cc.xy(13, 1));
		toolbar = builder.getPanel();
		
		final JComponent filters = mPipeline.getFilters();
		CSH.setHelpIDString(filters, "reg.filters");
		add(filters,BorderLayout.NORTH);
	
		// middle pane
		
		// model for the table.
		resourceTableModel= new EventTableModel(filteredItems,createTableFormat());          		
		resourceTableModel.addTableModelListener(this);
		resourceTable = createTable(resourceTableModel,filteredItems);
		CSH.setHelpIDString(resourceTable, "reg.table");
		resourceTable.setSelectionModel(currentResourceInView);
		// surprising - this is all that's needed tp add sorting to columns in the table.
		new TableComparatorChooser(resourceTable,sortedItems,true);
		JComponent centerPanel = new JScrollPane(resourceTable);
		centerPanel.setBorder(BorderFactory.createEmptyBorder());
		centerPanel.setMinimumSize(new Dimension(100,200));  // ensure we always get to keep some display, even when the filter wheels appear.
		
		// bottom pane.
		tabPane = new JTabbedPane();    
		tabPane.setBorder(BorderFactory.createEmptyBorder());
		tabPane.addChangeListener(this);
	//	tabPane.setPreferredSize(new Dimension(200,200));
		detailsPane = new FormattedResourceViewer(browser,regBrowser);
		CSH.setHelpIDString(detailsPane.getComponent(), "reg.details");
		
		xmlPane =  new XMLResourceViewer(parent,reg);
		CSH.setHelpIDString(xmlPane.getComponent(), "reg.xml");
		
		tableMetadataPane =  new TabularMetadataViewer();
		CSH.setHelpIDString(tableMetadataPane.getComponent(),"reg.table");
		
		final JScrollPane scrollPane = new JScrollPane(detailsPane.getComponent(),JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		tabPane.addTab("Details", IconHelper.loadIcon("info16.png")
				, scrollPane, "Details of chosen resource");
		tabPane.addTab(DevSymbols.PROBLEM + " " + "Form"
				,new ResourceFormViewer());
		tabPane.addTab("Tables",IconHelper.loadIcon("table16.png")
				,tableMetadataPane.getComponent(),"Tabular schema for this resource");
		
		tabPane.setMinimumSize(new Dimension(100,100));
		// xml pane not added to tabs in same way - as is an 'advanced' view.
		// will be added / removed in the property change listener, when initialized from preferneces.
		
		// stitch middle and bottom together.
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,centerPanel,tabPane);
		split.setResizeWeight(1.0); // all goes to the top.
		split.setDividerLocation(300);
		split.setDividerSize(7);
		split.setBorder(BorderFactory.createEmptyBorder());
		add(split,BorderLayout.CENTER);
		
		advancedPreference.addPropertyChangeListener(this);
		advancedPreference.initializeThroughListener(this);

	}
	/** called to create the format definition of the central table - maybe overridden by subclasses */
	protected TableFormat createTableFormat() {
		return new ResourceTableFomat(vomon,iconFac);
	}
	
	/** called to create the central table - may be overridden by subclasses */
	protected ResourceTable createTable(EventTableModel model,EventList list) {
		return new ResourceTable(model,list,vomon,iconFac);
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
			searchCount.setText("1 resource");
			break;
		default:
			searchCount.setText(sz + " resources");
		}
	}
	/** triggered when contents of table change */
	public void tableChanged(TableModelEvent e) {
		if (e.getType() != TableModelEvent.UPDATE) { // only interested in add or delete events.
			int resultSize = edtItems.size();
			int viewSize = resourceTableModel.getRowCount();
			if (viewSize != resultSize) {
				filterCount.setText(viewSize + " of ");
			} else {
				filterCount.setText(null);
			}
			// on every change, if nothing is selcted, and there are some results,, select (display) first record.
			if (viewSize > 0 && resourceTable.getSelectedRow() == -1) {
					resourceTable.changeSelection(0, 1, false, false);			
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
		//@todo for andy: try to recall what was said about multiple data displays - was it side-by-side?
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
			break; //@todo
		case 2:
			//@todo should I move to another tab when this isn't a tabular resouce?
			tableMetadataPane.display(res);
			break;
		case 3:
			xmlPane.display(res);
			break;
		default:
			break;
		}
	}
	
// handler for various buttons in this ui.
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == haltButton) {
				this.parent.get().haltMyTasks();
				fireLoadCompleted();
		}
	}
	
	/** expose the currently viewed resource */
	public EventSelectionModel getCurrentResourceModel() {
		return this.currentResourceInView;
	}
	
	/* clear the search results */
	public void clear() {
		try {
		items.getReadWriteLock().writeLock().lock();
		items.clear();
		} finally {
			items.getReadWriteLock().writeLock().unlock();
		}
	}
// configure the behaviour of htis component.
	public boolean isMultipleResources() {
		//@fixme implement
		return false;
	}
	
	/** set scope to just display this list of resources */
	public void displayIdSet(Collection idList) {
		//getSearchTitleLabel().setText(null);
		(new ListWorker(parent.get(),idList)).start();		
	}
	
	public void displayIdSet(String title,Collection idList) {
		// don't like this.
		//getSearchTitleLabel().setText(title);
		(new ListWorker(title,parent.get(),idList)).start();		
	}
	
	
	/** set scope to display results of this query
	 * @param query - an xquery
	 */
	public void displayQuery(String query) {
		//getSearchTitleLabel().setText(null);		
		(new XQueryWorker(parent.get(),query)).start();
	}
	
	public void displayQuery(String title,String query) {
		//getSearchTitleLabel().setText(title);		
		(new XQueryWorker(title,parent.get(),query)).start();
	}	
	
	public void displayQuery(SRQL query) {
		//getSearchTitleLabel().setText(null);	
		(new SRQLWorker(parent.get(),query)).start();
	}
	public void displayQuery(String title,SRQL query) {
		//getSearchTitleLabel().setText(title);			
		(new SRQLWorker(title,parent.get(),query)).start();
	}
	
	/** build an xquery that will retrieve all items in a list */
	public static String makeXQueryFromIdSet(Collection l) {
		//@future investigae whether some kind of ' vr:identifier in idset' form is more efficient
		// as at the moment defining a static list of 300+ items causes the server to fail with an out-of-memory error.
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


	/** set whether user is permitted to select multiple resources 
	 * @param multiple if true, multiple selection is permitted.*/
	public void setMultipleResources(boolean multiple) {
		//@fixme implement
	}

	/** access the 'toolbar' for this component - it's up to the hosting
	 * application to display this in some appropriate place.
	 */
	public JComponent getToolbar() {
		return toolbar;
	}
	

	public Transferable getSelectionTransferable() {
		return resourceTable.getSelectionTransferable();
	}

	// event notification for loading.
	public void addLoadListener(LoadListener l) {
		listenerList.add(LoadListener.class,l);
	}
	public void removeLoadListener(LoadListener l) {
		listenerList.remove(LoadListener.class,l);
	}

	protected void fireLoadStarted() {
		clear();
		haltButton.setEnabled(true);
		newButton.setEnabled(false);
		      Object[] listeners = listenerList.getListenerList();
		      // Process the listeners last to first, notifying
		      // those that are interested in this event
		      LoadEvent loadEvent = null;
		      for (int i = listeners.length-2; i>=0; i-=2) {
		          if (listeners[i]==LoadListener.class) {
		              // Lazily create the event:
		              if (loadEvent == null)
		                  loadEvent = new LoadEvent(this);
		              ((LoadListener)listeners[i+1]).loadStarted(loadEvent);
		          }
		      }		
	}
	
	protected void fireLoadCompleted() {
		haltButton.setEnabled(false);
		newButton.setEnabled(true);
	      Object[] listeners = listenerList.getListenerList();
	      // Process the listeners last to first, notifying
	      // those that are interested in this event
	      LoadEvent loadEvent = null;
	      for (int i = listeners.length-2; i>=0; i-=2) {
	          if (listeners[i]==LoadListener.class) {
	              // Lazily create the event:
	              if (loadEvent == null)
	                  loadEvent = new LoadEvent(this);
	              ((LoadListener)listeners[i+1]).loadCompleted(loadEvent);
	          }
	      }			
	}
	
	public static interface LoadListener extends EventListener {
		public void loadStarted(LoadEvent e) ;
		public void loadCompleted(LoadEvent e);
	}
	
	public static class LoadEvent extends EventObject {
		
		public LoadEvent(Object source) {
			super(source);
		}
	}
}

/* 
$Log: RegistryGooglePanel.java,v $
Revision 1.5  2007/06/18 16:41:43  nw
added icons to voexplorer table to denote the 'capabilities' of each resource

Revision 1.4  2007/05/18 06:18:10  nw
placeholder for form-layout of registry fields.

Revision 1.3  2007/05/10 19:35:27  nw
reqwork

Revision 1.2  2007/05/03 19:18:38  nw
refactored to make more extensible.

Revision 1.1  2007/05/02 15:38:27  nw
changes for 2007.3.alpha1

Revision 1.10  2007/04/18 15:47:08  nw
tidied up voexplorer, removed front pane.

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