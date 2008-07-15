/*$Id: RegistryGooglePanel.java,v 1.35 2008/07/15 23:17:33 nw Exp $
>>>>>>> 1.12.2.6
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.ResourceConsumer;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.AdjustableColumnModel;
import org.astrogrid.desktop.modules.ui.comp.CheckBoxMenu;
import org.astrogrid.desktop.modules.ui.comp.MyTitledBorder;
import org.astrogrid.desktop.modules.ui.comp.TableColumnModelAdapter;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.voexplorer.google.CapabilityIconFactory;
import org.astrogrid.desktop.modules.ui.voexplorer.google.EditableResourceViewer;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTable;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTableFomat;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTitleComparator;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceViewer;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.Builder;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.HeadClauseSRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.AuthorityStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.CapabilityStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.ContentLevelStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.CreatorStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.PublisherStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.SourceStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.SubjectsStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.TagStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.TypeStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.TypesStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.UcdStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.WavebandStrategy;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.VoMonInternal;

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
 */
public class RegistryGooglePanel extends JPanel
implements ListEventListener, ListSelectionListener, ChangeListener, TableModelListener {

	protected static final Log logger = LogFactory
			.getLog(RegistryGooglePanel.class);

    /** key in preferences database for this class giving list of table columns displayed.  The value is a tab-separated list of column names. */
    public static final String COLUMNS_KEY = "columns";

    /** class that takes care of concatenating together various bits of information and updating a title 
     * 
     * not  thread safe - expects to be called on EDT
     * 
     * also listens to the advanced preference, and according to it's value, takes a reading either before or after the 'system filter'
     *
     * */
    protected static class SearchSummaryFormatter implements PropertyChangeListener {
        private final JLabel lab;
        private final StrBuilder sb = new StrBuilder();
        private final Preference advanced;
        private final List advancedList;
        private final List basicList;
        private final List filteredList;

        /**
         * 
         * @param lab label to format to
         * @param advanced the 'advanced' preference
         * @param advancedList resuylt list to take reading from in 'advanced' ode.
         * @param basicList result list to take reading from in 'basic' mode.
         * @param filteredList the filtered list.
         */
        public SearchSummaryFormatter(JLabel lab, Preference advanced, List advancedList, List basicList, List filteredList) {
            super();
            this.lab = lab;
            this.advanced = advanced;
            this.advancedList = advancedList;
            this.basicList = basicList;
            this.filteredList = filteredList;
            advanced.addPropertyChangeListener(this);
            advanced.initializeThroughListener(this);           
        }
        private String title = null;
        private List searchCountSource;
        private int searchCount = 0;
        private int filterCount = 0;
        public void setTitle(String t) {
            title = t;
            update();
        }
        
        public void recount() {
            int f = filteredList.size();
            int s = searchCountSource.size();
            if (f != filterCount || s != searchCount) {
                filterCount = f;
                searchCount = s;
                update();
            }
        }
        
        private void update() {
            sb.clear();
            if (title != null) {
                sb.append(title).append(" - ");
            }
            if (filterCount != searchCount) {
                sb.append("filtering to ").append(filterCount).append(" of ");
            }
            
            switch (searchCount) {
                case 0:
                    break;
                case 1:
                    sb.append("1 resource");
                    break;
                default:
                    sb.append(searchCount).append(" resources");
                } 
            
            lab.setText(sb.toString());
        }
        // triggered when preference changes.
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getSource() == this.advanced) {
                if (advanced.asBoolean()) {
                    searchCountSource = advancedList;
                } else {
                    searchCountSource = basicList;
                }
                recount();
            }
        }
        
    }
    
	/** an asbtract background worker that provides machinery for processing the results of a streaming parse
	 * and caching the result */
	private abstract class Worker extends BackgroundWorker implements ResourceConsumer {
		
        public Worker(UIComponent parent, String message) {
			super(parent,message,BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY);
			fireLoadStarted();
		}

		// callback from the the registry.
		public void process(final Resource r) {
	        if (isInterrupted()) {
                return;
            }
	        reportProgress("Loaded " + r.getTitle());
	        setProgress(resourceCount++,size);
	        // need to run the setProgress on the EDT, 
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    parent.setProgressValue(parent.getProgressValue() +1);
                }
            });
	        try {
	            items.getReadWriteLock().writeLock().lock();
	            items.add(r);
	        } finally {
	            //this throws an illegal monitor state exception if I didn't lock it.
	            items.getReadWriteLock().writeLock().unlock();
	        }
	        
		}
		
		int resourceCount = 0; // current number of resources
		int size = 0; // estimated size.

        public void estimatedSize(final int i) {
            size = i;
            logger.debug("Size is " + i);
            if (isInterrupted()) {
                return;
            }
            setProgress(resourceCount,i);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    parent.setProgressMax(i);
                }
            });            
        }		
		
		private Thread _thread;
		private volatile boolean interrupted = false;
		/** keep track of whrther this background process was interrupted */
		private boolean isInterrupted() {
		    if (_thread == null) {
		        // must be called first on the background thread to initialize this.
		        _thread = Thread.currentThread(); 
		    }
		    if (! interrupted) { // else we know already that we've been interrupted.
		        interrupted = _thread.isInterrupted();
		    }
		    return interrupted;
		}

		
		protected void runQuery(String xq) throws ServiceException {
		    // check bulk cache.
		    reportProgress("Running query");
		    if (bypassCache) {		        
	            reg.consumeXQueryReload(xq,this);		        
		    } else {
		          reg.consumeXQuery(xq,this);
		    }
		// no need to lock - as we know we're the thread that was doing the modifying. and it's finished now.
		}
		protected void doFinished(Object result) {
			//resourceTable.selectAll(); dislike this.
		    if (! isInterrupted()) {
		        parent.setProgressMax(0);
		        if (resourceTable.getRowCount() > 0) {
		            resourceTable.getSelectionModel().setSelectionInterval(0,0);
		        }
		    }
			
		}		
		protected void doAlways() {
		    // think it should fire even on interrupt.
		   // if (! isInterrupted()) {
		        fireLoadCompleted();
		    //}
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
		        reportProgress("Building Query");
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

		public ListWorker(UIComponent parent, Collection<URI> ids) {
			super(parent, "Loading List");
			this.ids = ids;
		}
		
		public ListWorker(String title,UIComponent parent, Collection<URI> ids) {
			super(parent, "Loading " + title);
			this.ids = ids;
		}		
		private final Collection<URI> ids;
		
		protected Object construct() throws Exception {   
		    reportProgress("Running query");
            if (bypassCache) {      
                reg.consumeResourceListReload(ids,this);                
            } else {
                reg.consumeResourceList(ids,this);
            }
			return null;
		}
		
	}
	// no state - so can be reused between instances.
	//seems unneeded static final SRQLVisitor feedbackVisitor = new KeywordSRQLVisitor();	
	static final Builder briefXQueryBuilder = new HeadClauseSRQLVisitor();
	
	// member variables.
	protected final ResourceTable resourceTable;
	protected final EventList  items ;
	protected final EventList edtItems; // a view of the items event list, on the Event dispatch thread.
    private final EventTableModel resourceTableModel;
	// tracks the currently clicked on registry entry - i.e. the one to display in viewer
	private final EventSelectionModel currentResourceInView;
    private final ResettableAdjustableColumnModel resourceColumnModel;
    private final JScrollPane tableScroller;

	protected final JTabbedPane tabPane;
	protected final RegistryInternal reg;
	protected final VoMonInternal vomon;
	protected final CapabilityIconFactory iconFac;
	protected final JComponent toolbar;
	protected final ResourceViewer[] resourceViewers;
	protected final AnnotationService annServer;
	// stuff that's accessed when composing this pane together in the UI.
	protected final UIComponent parent;
	protected final TypesafeObjectBuilder uiBuilder;

    protected final SearchSummaryFormatter summary;

    private final JCheckBoxMenuItem systemToggleButton;

    private final Action expandAction;

	
	public void setPopup(JPopupMenu popup) {
		resourceTable.setPopup(popup);
	}

	/** Construct a new RegistryChooserPanel
	 * 
	 * @param reg used to perform the query
	 * @param browser used to display external resources
	 * @param regBrowser used to display related registry entires.
	 * @param resources caches resources.
	 * @param vm used to annotate registry entries with availability information
	 * @param advancedPreference 
	 * @param pref controls whether to display 'advanced' features of the ui.
	 */
	public RegistryGooglePanel(final UIComponent parent,final RegistryInternal reg
			,TypesafeObjectBuilder uiBuilder
			, final VoMonInternal vm, final CapabilityIconFactory iconFac, final AnnotationService annServer, Preference advancedPreference) {
	    this(parent,reg,uiBuilder,createDefaultViews(parent,uiBuilder)
	        ,vm,iconFac,annServer,advancedPreference);
	}
	
	/** constructor for extension that allows an alternate set of resource viewers to be provided */
    protected RegistryGooglePanel(final UIComponent parent,final RegistryInternal reg
            ,TypesafeObjectBuilder uiBuilder, ResourceViewer[] viewers
            , final VoMonInternal vm, final CapabilityIconFactory iconFac, final AnnotationService annServer, Preference advancedPreference) {	
		super();    
		this.parent = parent;
		this.uiBuilder = uiBuilder;
		this.reg = reg;
		this.vomon = vm;
		this.iconFac = iconFac;
		this.annServer = annServer;
		this.resourceViewers = viewers;
		// prelims
		//this.setSize(new Dimension(500,800));
		setLayout(new BorderLayout());
		
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
				, new org.astrogrid.desktop.modules.ui.voexplorer.strategy.ServiceTypeStrategy()
				,new UcdStrategy()
				, new ContentLevelStrategy()
				,new PublisherStrategy()
				,new AuthorityStrategy()
				,new CreatorStrategy()
				, new CapabilityStrategy()
				,new TagStrategy(annServer)
				, new SourceStrategy()
				// @future add strategies for meta-metadata - last used, recently added, tags, etc.
				};
		FilterPipelineFactory mPipeline = new FilterPipelineFactory(sortedItems,pStrategies,annServer,advancedPreference);
		filteredItems = mPipeline.getFilteredItems();
        // item currenlty selected in table list.
		currentResourceInView = new EventSelectionModel(filteredItems);
		currentResourceInView.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
		currentResourceInView.addListSelectionListener(this); // assume this happens on EDT?
		
		FormLayout form = new FormLayout(
				"2dlu,d:grow,60dlu,0dlu,d,1dlu" // cols
				,"d" // rows
				);
		PanelBuilder builder = new PanelBuilder(form);
		CellConstraints cc = new CellConstraints();

        final JLabel summaryLabel = builder.addLabel("Resources",cc.xy(2,1));
        summaryLabel.setForeground(Color.DARK_GRAY);
        summaryLabel.setFont(UIConstants.SMALL_DIALOG_FONT);
        this.summary = new SearchSummaryFormatter(summaryLabel,advancedPreference, edtItems,mPipeline.getSystemFilteredItems(),mPipeline.getFilteredItems());

		final JTextField filterField = mPipeline.getTextField();
		CSH.setHelpIDString(filterField, "reg.filter");
		builder.add(filterField,cc.xy(3, 1));
		this.systemToggleButton = mPipeline.getSystemToggleButton();
		final JToggleButton expandButton = mPipeline.getExpandButton();		
		CSH.setHelpIDString(expandButton,"reg.showFilters");
        builder.add(expandButton,cc.xy(5, 1));
        this.expandAction = expandButton.getAction();
		toolbar = builder.getPanel();
		Box topBox = Box.createVerticalBox();
		this.add(topBox,BorderLayout.NORTH);
		topBox.add(toolbar);
		
		final JComponent filters = mPipeline.getFilters();
		CSH.setHelpIDString(filters, "reg.filters");
		topBox.add(filters);
	
		// middle pane
		
		// model for the table.
		final ResourceTableFomat tableFormat = createTableFormat();
        resourceTableModel= new EventTableModel(filteredItems,tableFormat);          		
		resourceTableModel.addTableModelListener(this);
		resourceTable = createTable(resourceTableModel,filteredItems);
		CSH.setHelpIDString(resourceTable, "reg.table");
		resourceTable.setSelectionModel(currentResourceInView);
		// surprising - this is all that's needed tp add sorting to columns in the table.
		new TableComparatorChooser(resourceTable,sortedItems,true);
		tableScroller = new JScrollPane(resourceTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScroller.setPreferredSize(null);
		tableScroller.getViewport().setBackground(Color.WHITE);
		this.setBorder(MyTitledBorder.createUntitledLined());
		
        // arrange for the column model of the table to be configurable by the user.
        resourceColumnModel = createResourceColumnModel(tableFormat.getDefaultColumns(),resourceTable);
        resourceTable.setColumnModel(resourceColumnModel);
        Action colsAct = new AbstractAction(null, IconHelper.loadIcon("configure14.png")) {
            final JPopupMenu colMenu = createColumnsMenu("").getPopupMenu();
            public void actionPerformed(ActionEvent evt) {
                colMenu.show((Component) evt.getSource(), 0, 0);
            }
        };
        tableScroller.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new JButton(colsAct));
        tableScroller.setMinimumSize(new Dimension(50,100));
        resourceColumnModel.addColumnModelListener(new TableColumnModelAdapter() {
            public void columnAdded(TableColumnModelEvent evt) {
                adjustScrolling();
            }
            public void columnRemoved(TableColumnModelEvent evt) {
                adjustScrolling();
            }
        });
        // ensure scroller is set up properly - need to queue this for later, when the layout will have been done
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                adjustScrolling();
            }
        });

		// bottom pane.
		tabPane = new JTabbedPane();    
		tabPane.setBorder(BorderFactory.createEmptyBorder());
		tabPane.addChangeListener(this);

		/** attach all the resource viewers */
		for (int i = 0; i < resourceViewers.length; i++) {
			resourceViewers[i].addTo(tabPane);
			if (resourceViewers[i] instanceof EditableResourceViewer) {
			    // using an internal class here, as registryGooglePanel already implements 
			    // change listener, and there's no easy way to distinguish between the different emitters.
			    ((EditableResourceViewer)resourceViewers[i]).addChangeListener(new ChangeListener(){
			        public void stateChanged(ChangeEvent e) {
			            resourceTableModel.fireTableRowsUpdated(
			                    currentResourceInView.getMinSelectionIndex()
			                    ,currentResourceInView.getMaxSelectionIndex()
			                    );			            
			        }
			    });
			}
		}	
		// stitch middle and bottom together.
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,tableScroller,tabPane);
		split.setResizeWeight(1.0); // all goes to the top. necessary for appearing filterwheels to work correctly		
		split.setDividerLocation(250);
		split.setDividerSize(6);
		split.setBorder(null);
		add(split,BorderLayout.CENTER);		
	}

	/** create a default set of resource views */
	protected static ResourceViewer[] createDefaultViews(UIComponent parent,TypesafeObjectBuilder uiBuilder) {
		return new ResourceViewer[] {
		        uiBuilder.createAnnotatedResourceView()
		        ,uiBuilder.createTableResourceView()
		        ,uiBuilder.createXMLResourceView(parent)
		        };
	   
	}
	
	/** called to create the format definition of the central table - maybe overridden by subclasses */
	protected ResourceTableFomat createTableFormat() {
		return new ResourceTableFomat(annServer,vomon,iconFac);
	}

    /** Returns a new column model for use with a given resource JTable.
     * Columns in this model can be removed/restored under user control.
     * The visibility of columns is initialised from the prefs database 
     * and subsequent user-initiated changes to this visibility will be
     * persisted there.  If the prefs database contains a value which 
     * can't be interpreted (for instance because the column model has
     * changed since last time the program was run) a default list will
     * be used.  This means that version skew will not cause problems.
     *
     * @param  resourceTable  table whose columns are to be controlled
     * @return   new column model which can be used with <code>resourceTable</code>
     */
    private ResettableAdjustableColumnModel createResourceColumnModel(String[] defaultColNames,JTable resourceTable) {
        final ResettableAdjustableColumnModel colModel = 
            new ResettableAdjustableColumnModel(
                    resourceTable.getColumnModel()
                    , resourceTable.getModel()
                    ,defaultColNames
                    );

        // initialise visible column list with persisted value from prefs
        final Preferences prefs = Preferences.userNodeForPackage(this.getClass()); //NWW find the class name dynamically, so subclasses have their own preferences.
        String colNameList = prefs.get(COLUMNS_KEY, null);
        String[] colNames = colNameList == null ? defaultColNames
                                                : colNameList.split("\t");
        if (! colModel.setVisibleColumnsByName(colNames)) {
            boolean ok = colModel.setVisibleColumnsByName(defaultColNames);
            assert ok;
        }

        // write subsequent changes to visible list to prefs
        colModel.addColumnModelListener(new TableColumnModelAdapter() {
            public void columnAdded(TableColumnModelEvent evt) {
                saveState();
            }
            public void columnMoved(TableColumnModelEvent evt) {
                saveState();
            }
            public void columnRemoved(TableColumnModelEvent evt) {
                saveState();
            }
            private void saveState() {
                prefs.put(COLUMNS_KEY, new StrBuilder(64).appendWithSeparators(colModel.getVisibleColumnsByName(), "\t").toString());
            }
        });
        return colModel;
    }
    
    /** an extended column model which knows how to reset itself back to it's original layout */
    private static class ResettableAdjustableColumnModel extends AdjustableColumnModel {
        private final String[] defaultColNames;
        public ResettableAdjustableColumnModel(
                TableColumnModel baseColumnModel, TableModel tableModel,String[] defaultColNames) {
            super(baseColumnModel, tableModel);
            this.defaultColNames = defaultColNames;
        }
        
        public void reset() {
            setVisibleColumnsByName(defaultColNames);
        }
    }
	
	/** called to create the central table - may be overridden by subclasses */
	protected ResourceTable createTable(EventTableModel model,EventList list) {
		return new ResourceTable(model,list,vomon);
	}

    /** configures the resource display JTable appropriately in its scroll pane according to column widths */
    private void adjustScrolling() {
        int prefWidth = 0;
        for (int ic = 0; ic < resourceColumnModel.getColumnCount(); ic++ ) {
            TableColumn tcol = resourceColumnModel.getColumn(ic);
            prefWidth += Math.max(tcol.getPreferredWidth(), tcol.getWidth());
        }
        boolean hscroll = prefWidth > tableScroller.getWidth();
        resourceTable.setAutoResizeMode(hscroll ? JTable.AUTO_RESIZE_OFF : JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }

// view updating methods	

	/** triggered when resource list contents change - glazeed lists will always call this on the EDT*/
	public void listChanged(ListEvent arg0) {
		while(arg0.next()) { // I assume this is the correct pattern
			if (arg0.getType() == ListEvent.DELETE) {// delete is only ever a clear.
				for (int i = 0; i < resourceViewers.length; i++) {
					resourceViewers[i].clear();
				}
				tabPane.setSelectedIndex(0);
			}
		}
		summary.recount();

	}
	/** triggered when contents of table change */
	public void tableChanged(TableModelEvent e) {
		if (e.getType() != TableModelEvent.UPDATE) { // only interested in add or delete events.
		    summary.recount();
			final int viewSize = resourceTableModel.getRowCount();
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
	public void stateChanged(ChangeEvent evt) {
	        currentlyDisplaying = null; // view has changed, so need to re-render.
	        updateViewers();
	}
	protected Resource currentlyDisplaying;// tries to make things idempotent.
	/** controller that takes care of displayng the current resource in the currently visible viewer */
	protected void updateViewers() {
		List l  = currentResourceInView.getSelected();
		if (l.isEmpty()) {
			return;
		}
		Resource res = (Resource)l.get(0); //@todo make this work nicely when I've got a multiple selection going on - want to show latest selection.
		if (res == currentlyDisplaying) { // list has changed, but selected item is the same.
			return;
		}
		currentlyDisplaying = res;
		int ix = tabPane.getSelectedIndex();
		if (ix > -1) {
			resourceViewers[ix].display(res);
		}
	}
	
/** halts the current query */
	public void halt() {
				this.parent.haltMyTasks();
				fireLoadCompleted(); 
	}
	
	/** set a flag to indicate that next query will bypass the cache
	 *  - i.e. will remove this query results from cache first.
	 */
	public void setNextToBypassCache() {
	    bypassCache = true;
	}
	private boolean bypassCache = false;

	/** the filtered list of items, as currently displayed in the table. */
    private final EventList filteredItems;
    /** access an event list of the items currently displayed in the table */
    public EventList getCurrentDisplayedResources() {
        return filteredItems;
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

    /** returns a menu which allows the user to configure visibility of columns in the displayed JTable.
     * @param   name   menu name
     * @return   new menu giving column visiblity options
     */
    public JMenu createColumnsMenu(String name) {
        CheckBoxMenu menu = resourceColumnModel.makeCheckBoxMenu(name);
        menu.insertAction(new AbstractAction("Show Defaults") {
            public void actionPerformed(ActionEvent evt) {
                resourceColumnModel.reset();
            }
        });
        menu.insertAction(new AbstractAction("Show All") {
            public void actionPerformed(ActionEvent evt) {
                resourceColumnModel.setAllVisible();
            }
        });
        return menu;
    }

// configure the behaviour of this component.
	public boolean isMultipleResources() {
		//@fixme implement
		return false;
	}
	
	/** set scope to just display this list of resources */
	public void displayIdSet(Collection<URI> idList) {
	    summary.setTitle("ID Set");
		(new ListWorker(parent,idList)).start();		
	}
	
	public void displayIdSet(String title,Collection<URI> idList) {
	    summary.setTitle(title);
		(new ListWorker(title,parent,idList)).start();		
	}
	
	
	/** set scope to display results of this query
	 * @param query - an xquery
	 */
	public void displayQuery(String query) {	
	    summary.setTitle("XQuery");
		(new XQueryWorker(parent,query)).start();
	}
	
	public void displayQuery(String title,String query) {
        summary.setTitle(title);	
		(new XQueryWorker(title,parent,query)).start();
	}	
	
	public void displayQuery(SRQL query) {
	    summary.setTitle("Query");
		(new SRQLWorker(parent,query)).start();
	}
	public void displayQuery(String title,SRQL query) {
        summary.setTitle(title);		
		(new SRQLWorker(title,parent,query)).start();
	}

	
	/** set whether user is permitted to select multiple resources 
	 * @param multiple if true, multiple selection is permitted.*/
	public void setMultipleResources(boolean multiple) {
	    resourceTable.setSelectionMode(
	            multiple ? ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE
	                    : ListSelectionModel.SINGLE_SELECTION
	            );
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
	    bypassCache = false;
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

    /**
     * @return the systemToggleButton
     */
    public final JCheckBoxMenuItem getSystemToggleButton() {
        return this.systemToggleButton;
    }

    /**
     * @return the expandAction
     */
    public final Action getExpandAction() {
        return this.expandAction;
    }
}

/* 
$Log: RegistryGooglePanel.java,v $
Revision 1.35  2008/07/15 23:17:33  nw
Complete - task 428: optimize srql translator output

Revision 1.34  2008/07/08 17:34:40  nw
Complete - task 400: Alternate caching strategy.

Revision 1.33  2008/05/28 12:27:17  nw
Alternate caching strategy.

Revision 1.32  2008/05/09 11:33:51  nw
Complete - task 394: process reg query results in a stream.

Incomplete - task 391: get to grips with new CDS

Revision 1.31  2008/03/28 13:08:58  nw
help-tagging

Revision 1.30  2008/03/26 10:35:14  nw
checked configuraiton of html builder and strbuilder

Revision 1.29  2008/03/13 07:17:57  nw
doesn't appear to be case sensitive after all.

Revision 1.28  2008/03/10 16:54:55  nw
removed dead code

Revision 1.27  2008/03/10 12:47:09  nw
removed dead code.

Revision 1.26  2008/03/10 12:25:38  nw
noted a shortcoming.

Revision 1.25  2008/01/25 07:53:25  nw
Complete - task 134: Upgrade to reg v1.0

Revision 1.24  2007/12/12 13:54:15  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.23  2007/11/26 14:44:45  nw
Complete - task 224: review configuration of all backgroiund workers

Revision 1.22  2007/11/13 05:22:38  nw
Complete - task 229: menuing redesign.

Complete - task 182: add ability for user to send *any* plastic message.

Revision 1.21  2007/10/12 10:55:49  nw
minor fixes, discovered while trying to use this in a dialogue

Revision 1.20  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.19  2007/09/17 13:39:33  nw
improved annotations implementation

Revision 1.18  2007/09/13 13:48:12  nw
minor ui fixes.

Revision 1.17  2007/09/11 12:10:27  nw
implemented andy's new lists.

Revision 1.16  2007/08/23 15:09:43  nw
Complete - task 147: add starter xquery to edit box

Revision 1.15  2007/08/22 16:12:19  nw
RESOLVED - bug 2285: All VOscope broken in latest snapshots
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2285

Revision 1.14  2007/08/13 19:14:17  nw
merged in mark's adjustable columns

<<<<<<< RegistryGooglePanel.java
Revision 1.13  2007/08/06 14:37:37  nw
Complete - task 133: make cancel more effective.

=======
Revision 1.12.2.6  2007/08/07 09:25:40  mbt
Fix resource display JTable's scrolling within its scroll pane appropriately for column widths

Revision 1.12.2.5  2007/08/06 16:51:16  mbt
ResourceTable constructor signature modified

Revision 1.12.2.4  2007/08/06 12:37:09  mbt
Add Show All and Show Defaults to column visibility menus

Revision 1.12.2.3  2007/08/04 10:15:00  mbt
Implement persistence for resource table column visibility

Revision 1.12.2.2  2007/08/03 16:12:38  mbt
Rename MetaColumnModel to the more meaningful AdjustableColumnModel

Revision 1.12.2.1  2007/08/03 15:45:27  mbt
Provide a menu for column remove/restore in resource table display

>>>>>>> 1.12.2.6
Revision 1.12  2007/08/02 11:12:47  nw
improved the formatting of filtering information.

Revision 1.11  2007/08/02 00:17:34  nw
moved formatting methods into vomon component.

Revision 1.10  2007/08/01 10:45:45  nw
changed persistence mechanism to XStream, and configured a central service to manage this.

Revision 1.9  2007/07/26 18:21:45  nw
merged mark's and noel's branches

Revision 1.8.2.1  2007/07/26 17:48:19  nw
removed misleading comment.

Revision 1.8  2007/07/23 11:41:49  nw
removed dev symbols.

Revision 1.7  2007/07/12 10:13:31  nw
made layout more compact.

Revision 1.6  2007/06/27 11:14:10  nw
added annotations view

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
