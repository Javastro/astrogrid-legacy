/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.TableColumn;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.comp.ModularColumn;
import org.astrogrid.desktop.modules.ui.scope.QueryResults.QueryResult;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.google.CapabilityIconFactory;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTableFomat;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceViewer;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;
import org.astrogrid.desktop.modules.ui.voexplorer.strategy.ResultsStrategy;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.VoMonInternal;

/** specialized subtype of registrygooglepanel that displays summary
 * of queries retrievers.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 2, 20075:58:51 PM
 */
public class ScopeServicesList extends RegistryGooglePanel
	implements QueryResultCollector{

    /**
     * 
     */
    private static final int RETRIEVERS_COUNT_TO_FLIP_TO_SERVICES_VIEW = 50;
    private final AstroScopeLauncherImpl astroscope;
    /**
	 * @param reg
	 * @param browser
	 * @param regBrowser
	 * @param resources
	 * @param bulk
	 * @param vomon
	 * @param pref 
	 * @param pref
	 */
	public ScopeServicesList(final AstroScopeLauncherImpl parent,final RegistryInternal reg
	        ,final TypesafeObjectBuilder uiBuilder, final ActivitiesManager acts
	        ,final VoMonInternal vomon, final CapabilityIconFactory iconFac,final AnnotationService annServer, final Preference pref) {
		super(parent,reg, uiBuilder
		        ,createServicesListViews(parent,uiBuilder,acts)
		        ,vomon, iconFac,annServer,pref);
        this.astroscope = parent;		
		summary.setTitle("Query Results");
		CSH.setHelpIDString(this,"scope.viz.services");
	} 
	
	/** exteded to add a new strategy for filtering on results. */
	@Override
	protected PipelineStrategy[] createPipeStrategies() {
	    
	    final PipelineStrategy[] supers =  super.createPipeStrategies();
	    final PipelineStrategy[] more = new PipelineStrategy[supers.length + 1];
	    System.arraycopy(supers,0,more,0,supers.length);
	    more[more.length-1] = new ResultsStrategy(this);
	    return more;	                                                   
	}

	protected static  ResourceViewer[] createServicesListViews(final AstroScopeLauncherImpl parent, final TypesafeObjectBuilder uiBuilder, final ActivitiesManager acts) {
	    return (ResourceViewer[])ArrayUtils.addAll(
                new ResourceViewer[]{
                        uiBuilder.createResultsResourceView(parent,acts)
                        }
                ,createDefaultViews(parent,uiBuilder)
                );
	}

/** map of additional informaiton for each service resource
 * key - the resource object
 * value - an Integer (number of results), or String (err message);
 */
	private final QueryResults queryResults = new QueryResults();
	/** overridden - to show additional columns */
	@Override
    protected ResourceTableFomat createTableFormat() {
	    return new ServicesListTableFormat(annServer,vomon,iconFac);
	}

	// query summarizer interface
	@Override
    public void clear() {
		super.clear();
		queryResults.clear();

	}
	/** add a number of services to the tabular display */
	public void addAll(final Retriever[] retrievers) {
	    final VizModel model = astroscope.getVizModel(); // can only access this now, not in the construct, as it's not yet been initialized at this point.
	    for (int i = 0; i < retrievers.length; i++) {
            final Retriever abstractRetriever = retrievers[i];
	        // eagerly create this, and hang onto it - else it tends to get GC'd and we
	        // lost the results tree
	        FileObject fo = null;
	        try {
	            fo = model.createResultsDirectory(abstractRetriever);
	        } catch (final FileSystemException e) {
	            logger.warn("Inable to create results directory for " + abstractRetriever.getLabel());
	        }
	        final QueryResult qr = new QueryResult(abstractRetriever,fo);
	        queryResults.addResult(qr);
	    }
	    items.getReadWriteLock().writeLock().lock();
	    try {
            for (int i = 0; i < retrievers.length; i++) {
                items.add(RetrieverService.create(retrievers[i]));
            }
	    } finally {
	        items.getReadWriteLock().writeLock().unlock();
	    }   
	    
	    // now check what size the list has grown to, and flip astroscope views if needed.
	    // it's possible that if the user then flips back, this might flip back again
	    // when more services are added - tough.
	    if (items.size() > RETRIEVERS_COUNT_TO_FLIP_TO_SERVICES_VIEW
	            && ! astroscope.currentlyServicesTable()) {
	        // show warning.
	        astroscope.showTransientWarning("Large Number of Services to Query"
	                ,"Switching to the 'Services Table' display" +
	                		"<br>The previous display can be accessed from the 'View' menu"
	                );
	        astroscope.flipToServicesTable();
	    }
	}

	public void addQueryFailure(final Retriever ri, final Throwable t) {
	    final QueryResult result = queryResults.getResult(ri); // there may be no result if it's a hanger-on from a previous search.

	    if (result != null) {
	        final String message= ExceptionFormatter.formatException(t);
	        if (astroscope.isTransientWarnings()) {
	            parent.showTransientWarning("Unable to query " + ri.getLabel()
	                    ,message.length() < 200 ? message : "See services view for details");
	        }

	        result.error = message;
	        notifyServiceUpdated(ri);
	    }
	}

    public void addQueryResult(final Retriever ri, final AstroscopeTableHandler handler) {
        final QueryResult qr = queryResults.getResult(ri);// there may be no result if it's a hanger-on from a previous search.
        if (qr != null) {
            qr.count = Integer.valueOf(handler.getResultCount());
            queryResults.associateNode(qr,handler.getServiceNode());
            notifyServiceUpdated(ri);
        }
    }
    
    private void notifyServiceUpdated(final Retriever abstractRetriever) {        
        // Creating a new RetrieverService here will not give an object which
        // is actually in the table alredy, but it will have the correct
        // equivalence relations (equals()/hashCode()).
        final Service ri = RetrieverService.create(abstractRetriever);

        final int ix = items.indexOf(ri);
        if (ix != -1) {
          items.set(ix,ri); // updates the table
        }
        // now if we're in results view, and it's the selected item that's just updated, make the 
        // results view update too.
        if (tabPane.getSelectedIndex() ==0 && ri.equals(currentlyDisplaying)) {
            resourceViewers[0].display(ri);
        }
    }
	


	/** extends the resource table format with two additional columns */
	private class ServicesListTableFormat extends ResourceTableFomat{

        public ServicesListTableFormat(final AnnotationService annService,
                final VoMonInternal vomon, final CapabilityIconFactory capBuilder) {
            super(annService, vomon, capBuilder);
            final List colList = new ArrayList(Arrays.asList(getColumns()));
            colList.add(new Column(RESULTS_NAME,Integer.class,new Comparator() {
                // has to handle integers, and the string 'failed'
                public int compare(final Object arg0, final Object arg1) {
                    if (arg0 instanceof String) {
                        if (arg1 instanceof String) {
                            return ((String)arg0).compareTo((String)arg1);
                        } else {
                            return -1;
                        }
                    } else if (arg1 instanceof String) {
                        return 1;
                    } else {
                        return ((Integer)arg0).compareTo((Integer)arg1);
                    }
                }
            }){
                @Override
                public Object getColumnValue(final Object baseObj) {
                    final Retriever r = ((RetrieverService)baseObj).getRetriever();
                    return queryResults.getResult(r).getFormattedResultCount();
                }
                @Override
                public void configureColumn(final TableColumn tcol) {
                    tcol.setPreferredWidth(60);
                }
            });
            colList.add(new StringColumn(SUBNAME_NAME) {
                @Override
                protected String getValue(final Resource res) {
                    final Retriever r = ((RetrieverService)res).getRetriever();
                    return r.getSubName();
                }
            });

            setColumns((ModularColumn[])colList.toArray(new ModularColumn[0]));
        }

        private final static String RESULTS_NAME = "Results"; // integer, comparableComparator
        private final static String SUBNAME_NAME = "Sub Title";

	    
	    @Override
        public String[] getDefaultColumns() {
	        return new String[] {
	                //STATUS_NAME,
	                FLAG_NAME,RESULTS_NAME,LABEL_NAME,SUBNAME_NAME,CAPABILITY_NAME,
	        };
	    }
	}

    public final QueryResults getQueryResults() {
        return this.queryResults;
    }
}
