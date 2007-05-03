/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableColumnModel;

import net.sf.ehcache.Ehcache;

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
	}
/** map of additional informaiton for each service resource
 * key - the resource object
 * value - an Integer (number of results), or String (err message);
 */
	final Map results = new HashMap();

	
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
	}
	/** add a query summary to the services list*/
	public void reportServiceResult(Service ri,int resultCount) {
		try {
			items.getReadWriteLock().writeLock().lock();
			results.put(ri,new Integer(resultCount));
			super.items.add(ri);
		} finally {
			items.getReadWriteLock().writeLock().unlock();
		}
	}
	
	public void reportServiceError(Service ri,String message) {

		try {
			items.getReadWriteLock().writeLock().lock();
			results.put(ri,message);
			super.items.add(ri);
		} finally {
			items.getReadWriteLock().writeLock().unlock();
		}
	}
	public void addQueryResult(Service ri, int resultCount, String message) {
		if (resultCount == QueryResultSummarizer.ERROR) {
			reportServiceError(ri,message);
		} else {
			reportServiceResult(ri,resultCount);
		}
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
