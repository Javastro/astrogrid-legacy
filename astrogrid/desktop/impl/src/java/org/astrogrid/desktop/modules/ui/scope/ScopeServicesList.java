/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Component;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.sf.ehcache.Ehcache;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.comp.ModularColumn;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.google.CapabilityIconFactory;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTable;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTableFomat;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.VoMonInternal;
import org.votech.VoMon;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** specialized subtype of registrygooglepanel that displays summary
 * of queries services.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 2, 20075:58:51 PM
 */
public class ScopeServicesList extends RegistryGooglePanel
	implements QueryResultSummarizer{

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
	public ScopeServicesList(RegistryInternal reg, Ehcache resources, Ehcache bulk, IterableObjectBuilder views,VoMonInternal vomon, final CapabilityIconFactory iconFac,AnnotationService annServer, Preference pref) {
		super(reg, resources, bulk, views,vomon, iconFac,annServer,pref);		
		summary.setTitle("Query Results");
	} 
	// overridden -- to return results too
	protected String[] viewNames() {
	    return (String[])ArrayUtils.addAll(new String[]{"results"},super.viewNames());
	}
/** map of additional informaiton for each service resource
 * key - the resource object
 * value - an Integer (number of results), or String (err message);
 */
	final Map results = new HashMap();
	/** bidirectional map between Resource objects and TreeNodes */
	final BidiMap services = new DualHashBidiMap();

	
	protected ResourceTableFomat createTableFormat() {
		return new ServicesListTableFormat(annServer,vomon,iconFac);
	}
	
// query summarizer stuff	
	public void clear() {
		super.clear();
		results.clear();
		services.clear();
	}
	public void addQueryResult(Service ri,TreeNode node, int resultCount, String message) {
		if( node != null) {
			services.put(ri,node);
		}
		items.getReadWriteLock().writeLock().lock();
		try {
			if (resultCount == QueryResultSummarizer.ERROR) {
				results.put(ri,message);
				super.items.add(ri);
			} else {
				results.put(ri,new Integer(resultCount));
				super.items.add(ri);
			}
		} finally {
			items.getReadWriteLock().writeLock().unlock();
		}
	}
	
	public TreeNode findTreeNode(Service s) {
		return (TreeNode)services.get(s);
	}
	
	public Service findService(TreeNode t) {
		return (Service)services.getKey(t);
	}
	
	public EventList getList() {
		return edtItems;
	}

	/** extends the resource table format with two additional columns */
	private class ServicesListTableFormat extends ResourceTableFomat{

        public ServicesListTableFormat(AnnotationService annService,
                VoMonInternal vomon, CapabilityIconFactory capBuilder) {
            super(annService, vomon, capBuilder);
            ModularColumn[] already = getColumns();
            ModularColumn[] more = new ModularColumn[already.length+2];
            System.arraycopy(already,0,more,0,already.length);
            more[already.length] =new Column(RESULTS_NAME,Integer.class,GlazedLists.comparableComparator()){
                public Object getColumnValue(Object baseObj) {
                    Resource r = (Resource)baseObj;
                    Object o = results.get(r);
                    return o != null && (o instanceof Integer) ? o : null;                    
                }
                public void configureColumn(TableColumn tcol) {
                    tcol.setPreferredWidth(40);
                }
            };
            more[already.length + 1] = new StringColumn(ERRORS_NAME,true) {

                protected String getValue(Resource r) {
                    Object o = results.get(r);
                    return o != null && (o instanceof String) ? (String)o : "";
                }
                public void configureColumn(TableColumn tcol) {
                    tcol.setPreferredWidth(500);
                }
            };
            setColumns(more);
        }

        private final static String RESULTS_NAME = "Results"; // integer, comparableComparator
	    private final static String ERRORS_NAME = "Error Message"; // long string, caseInsensitiveComparator

	    
	    public String[] getDefaultColumns() {
	        return new String[] {
	                STATUS_NAME,RESULTS_NAME,LABEL_NAME,CAPABILITY_NAME,ERRORS_NAME
	        };
	    }
	}
}
