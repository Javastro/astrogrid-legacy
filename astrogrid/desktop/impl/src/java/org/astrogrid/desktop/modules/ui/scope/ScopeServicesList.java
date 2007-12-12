/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.util.Arrays;
import java.util.Comparator;

import javax.swing.table.TableColumn;

import net.sf.ehcache.Ehcache;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.comp.ModularColumn;
import org.astrogrid.desktop.modules.ui.scope.QueryResults.QueryResult;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.google.CapabilityIconFactory;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTableFomat;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceViewer;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.VoMonInternal;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;

/** specialized subtype of registrygooglepanel that displays summary
 * of queries services.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 2, 20075:58:51 PM
 */
public class ScopeServicesList extends RegistryGooglePanel
	implements QueryResultCollector{

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
	public ScopeServicesList(AstroScopeLauncherImpl parent,RegistryInternal reg, Ehcache resources, Ehcache bulk
	        ,TypesafeObjectBuilder uiBuilder, ActivitiesManager acts
	        ,VoMonInternal vomon, final CapabilityIconFactory iconFac,AnnotationService annServer, Preference pref) {
		super(parent,reg, resources, bulk, uiBuilder
		        ,createServicesListViews(parent,uiBuilder,acts)
		        ,vomon, iconFac,annServer,pref);
        this.astroscope = parent;		
		summary.setTitle("Query Results");
	} 

	protected static  ResourceViewer[] createServicesListViews(AstroScopeLauncherImpl parent, TypesafeObjectBuilder uiBuilder, ActivitiesManager acts) {
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
	protected ResourceTableFomat createTableFormat() {
	    return new ServicesListTableFormat(annServer,vomon,iconFac);
	}

	// query summarizer interface
	public void clear() {
		super.clear();
		queryResults.clear();

	}
	/** add a number of services to the tabular display */
	public void addAll(Service[] services) {
	    VizModel model = astroscope.getVizModel(); // can only access this now, not in the construct, as it's not yet been initialized at this point.
	    for (int i = 0; i < services.length; i++) {
	        // eagerly create this, and hang onto it - else it tends to get GC'd and we
	        // lost the results tree
	        FileObject fo = null;
	        try {
	            fo = model.createResultsDirectory(services[i]);
	        } catch (FileSystemException e) {
	            logger.warn("Inable to create results directory for " + services[i].getId());
	        }
	        QueryResult qr = new QueryResult(services[i],fo);
	        queryResults.addResult(qr);
	    }
	    items.getReadWriteLock().writeLock().lock();
	    try {
	        items.addAll(Arrays.asList(services));
	    } finally {
	        items.getReadWriteLock().writeLock().unlock();
	    }        
	}

	public void addQueryFailure(Service ri, Throwable t) {
	    String message= ExceptionFormatter.formatException(t);
	    if (astroscope.isTransientWarnings()) {
	        parent.showTransientWarning("Unable to query " + ri.getTitle()
	            ,message.length() < 200 ? message : "See services view for details");
	    }
	    queryResults.getResult(ri).error = message;
	    notifyServiceUpdated(ri);
	}

    public void addQueryResult(Service ri, AstroscopeTableHandler handler) {
        final QueryResult qr = queryResults.getResult(ri);
        qr.count = new Integer(handler.getResultCount());
        queryResults.associateNode(qr,handler.getServiceNode());
        notifyServiceUpdated(ri);
    }
    
    private void notifyServiceUpdated(Service ri) {        
        int ix = items.indexOf(ri);
        if (ix != -1) {
          items.set(ix,ri); // updates the table
        }
        // now if we're in results view, and it's the selected item that's just updated, make the 
        // results view update to.
        if (tabPane.getSelectedIndex() ==0 && ri.equals(currentlyDisplaying)) {
            resourceViewers[0].display(ri);
        }
    }
	
	/** give access to the table contents 
	 * @todo remove this if unused later
	 * */
	public EventList getList() {
		return edtItems;
	}

	/** extends the resource table format with two additional columns */
	private class ServicesListTableFormat extends ResourceTableFomat{

        public ServicesListTableFormat(AnnotationService annService,
                VoMonInternal vomon, CapabilityIconFactory capBuilder) {
            super(annService, vomon, capBuilder);
            ModularColumn[] already = getColumns();
            ModularColumn[] more = new ModularColumn[already.length+1];
            System.arraycopy(already,0,more,0,already.length);
            more[already.length] =new Column(RESULTS_NAME,Integer.class,new Comparator() {
                // has to handle integers, and the string 'failed'
                public int compare(Object arg0, Object arg1) {
                    if (arg0 instanceof String) {
                        if (arg1 instanceof String) {
                            return ((String)arg0).compareTo((String)arg1);
                        } else {
                            return -1;
                        }
                    } else if (arg1 instanceof String) {
                        return 1;
                    } else {
                        return ((Integer)arg0).compareTo(arg1);
                    }
                }
            }){
                public Object getColumnValue(Object baseObj) {
                    Service r = (Service)baseObj;
                    return queryResults.getResult(r).getFormattedResultCount();          
                }
                public void configureColumn(TableColumn tcol) {
                    tcol.setPreferredWidth(60);
                }
            };

            setColumns(more);
        }

        private final static String RESULTS_NAME = "Results"; // integer, comparableComparator

	    
	    public String[] getDefaultColumns() {
	        return new String[] {
	                STATUS_NAME,RESULTS_NAME,LABEL_NAME,CAPABILITY_NAME
	        };
	    }
	}

    public final QueryResults getQueryResults() {
        return this.queryResults;
    }


}
