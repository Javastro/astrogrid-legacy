/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableColumnModel;

import net.sf.ehcache.Ehcache;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTable;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTableFomat;
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
	public ScopeServicesList(RegistryInternal reg, BrowserControl browser, RegistryBrowser regBrowser, Ehcache resources, Ehcache bulk, VoMon vomon, Preference pref) {
		super(reg, browser, regBrowser, resources, bulk, vomon, pref);
		getSearchTitleLabel().setText("Query Results");
		getNewSearchButton().setVisible(false);
		getHaltSearchButton().setVisible(false);
		
	//	tabPane.insertTab("Results",null,,"Results retruns from this service",0);
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
		//@todo work out why I'm getting the wrong row's tooltips here.
		ResourceTable rt=  new ResourceTable(model,list,vomon) {
			protected void createTooltipFor(int col,Resource r, StringBuffer sb) {
				if (col == 2 ) {
					Object o = results.get(r);
					if (o != null && o instanceof String) {
						sb.append(o);
					}
				}
			}
		};
		TableColumnModel cm = rt.getColumnModel();
		cm.getColumn(0).setPreferredWidth(40);
		cm.getColumn(0).setMaxWidth(40);
		cm.getColumn(1).setPreferredWidth(350);
		cm.getColumn(1).setMaxWidth(350);
		cm.getColumn(1).setResizable(true);
		cm.getColumn(2).setPreferredWidth(500);
		cm.getColumn(2).setMaxWidth(1000);
		cm.getColumn(2).setResizable(true);
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
		return items;
	}
	
	private class ServicesListTableFormat implements AdvancedTableFormat{

		public Class getColumnClass(int arg0) {
			switch(arg0) {
			case 0:
				return Integer.class;
			case 1:
			case 2:
				return Object.class;
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
				return GlazedLists.caseInsensitiveComparator();
			default:
				throw new IndexOutOfBoundsException("Oversized column ix:" +arg0);
			}
		}

		public int getColumnCount() {
			return 3;
		}

		public String getColumnName(int column) {
			switch(column) {
			case 0 :return "Results";
			case 1: return "Title";
			case 2: return "Error Message";
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
				return ResourceTableFomat.createTitle(r);
			case 2:
				o = results.get(r);
				return o != null && (o instanceof String) ? o : "";
			default:
				throw new IndexOutOfBoundsException("Oversized column ix:" +columnIndex);
			}
		}


	}
}
