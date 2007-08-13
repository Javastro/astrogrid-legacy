/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Component;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
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
	 */
	public ScopeServicesList(RegistryInternal reg, Ehcache resources, Ehcache bulk, IterableObjectBuilder views,VoMonInternal vomon, final CapabilityIconFactory iconFac,AnnotationService annServer) {
		super(reg, resources, bulk, views,vomon, iconFac,annServer);
		getSearchTitleLabel().setText("Query Results");
		getNewSearchButton().setVisible(false);
		getHaltSearchButton().setVisible(false);
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

	
	protected TableFormat createTableFormat() {
		return new ServicesListTableFormat();
	}
	protected ResourceTable createTable(EventTableModel model, EventList list) {
		ResourceTable rt=  new ResourceTable(model,list,vomon) {
			protected void createTooltipFor(int col,Resource r, StringBuffer sb) {
				if (col == 23) {
					Object o = results.get(r);
					if (o != null && o instanceof String) {
						sb.append(o);
					}
				}
			}
		};
		TableColumnModel cm = rt.getColumnModel();
		cm.getColumn(1).setPreferredWidth(350);
		cm.getColumn(1).setMaxWidth(350);
		cm.getColumn(3).setPreferredWidth(500);
		cm.getColumn(3).setMaxWidth(1000);
		return rt;
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
	
	private class ServicesListTableFormat implements AdvancedTableFormat{

		public Class getColumnClass(int arg0) {
			switch(arg0) {
			case 0:
				return Integer.class;
			case 1:
			case 3:
				return Object.class;
			case 2:
				return Icon.class;
			default:
				throw new IndexOutOfBoundsException("Oversized column ix:" +arg0);					
			}
		}

		public Comparator getColumnComparator(int arg0) {
			switch (arg0) {
			case 0:
				return GlazedLists.comparableComparator();
			case 1:
				return GlazedLists.caseInsensitiveComparator();
			case 2:
				return hashCodeComparator;
			case 3:
				return GlazedLists.caseInsensitiveComparator();
			default:
				throw new IndexOutOfBoundsException("Oversized column ix:" +arg0);
			}
		}

		private final Comparator hashCodeComparator = new Comparator() {

			public int compare(Object arg0, Object arg1) {
				return arg0.hashCode() - arg1.hashCode();
			}
		};
		
		public int getColumnCount() {
			return 4;
		}

		public String getColumnName(int column) {
			switch(column) {
			case 0 :return "Results";
			case 1: return "Title";
			case 2: return "Capabilities";
			case 3: return "Error Message";
			default: 
				throw new IndexOutOfBoundsException("Oversized column ix:" +column);
			}
		}

		public Object getColumnValue(Object arg0, int columnIndex) {
			Resource r = (Resource)arg0;
			switch(columnIndex) {
			case 0:
				Object o = results.get(r);
				return o != null && (o instanceof Integer) ? o : null;
			case 1:
				return createTitle(r);
			case 2:
				return iconFac.buildIcon(r);
			case 3:
				o = results.get(r);
				return o != null && (o instanceof String) ? o : "";
			default:
				throw new IndexOutOfBoundsException("Oversized column ix:" +columnIndex);
			}
		}
		
		public  String createTitle(Resource r) {
			if (r == null) {
				return "";
			}
			String title = StringUtils.replace(r.getTitle(), "\n", " ") ;

			return title;
		}		


	}
}
