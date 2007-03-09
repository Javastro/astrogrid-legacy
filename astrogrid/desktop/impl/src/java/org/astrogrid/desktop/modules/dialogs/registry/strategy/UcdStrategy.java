package org.astrogrid.desktop.modules.dialogs.registry.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.HasTables;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.dialogs.registry.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Strategy for filtering on list of subjects.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:36:34 PM
 */
public final class UcdStrategy extends PipelineStrategy {
	public Matcher createMatcher(final List selected) {
		return new Matcher() {
			public boolean matches(Object r) {
				if (r instanceof DataCollection) {
					DataCollection dc = (DataCollection)r;
					Catalog[] catalogues = dc.getCatalogues();
					for (int i = 0; i < catalogues.length; i++) {
						TableBean[] t = catalogues[i].getTables();
						for (int j = 0; j < t.length; j++) {
							ColumnBean[] cs = t[j].getColumns();
							for (int k = 0; k < cs.length; k++) {
								String u = cs[k].getUCD();
								if (u != null && selected.contains(u)) {
									return true;
								}
							}
						}
					} 
				} else if (r instanceof HasTables) {
					TableBean[] t = ((HasTables)r).getTables();
					for (int j = 0; j < t.length; j++) {
						ColumnBean[] cs = t[j].getColumns();
						for (int k = 0; k < cs.length; k++) {
							String u = cs[k].getUCD();
							if (u != null && selected.contains(u)) {
								return true;
							}
						}
					}					
				}
				return false;
			}					
		};
	}
	

	public TransformedList createView(EventList base) {
		return new CollectionList(base,
				new CollectionList.Model() {
			public List getChildren(Object r) {
				List l = new ArrayList();
				if (r instanceof DataCollection) {
					DataCollection dc = (DataCollection)r;
					Catalog[] catalogues = dc.getCatalogues();
					for (int i = 0; i < catalogues.length; i++) {
						TableBean[] t = catalogues[i].getTables();
						for (int j = 0; j < t.length; j++) {
							ColumnBean[] cs = t[j].getColumns();
							for (int k = 0; k < cs.length; k++) {
								String u = cs[k].getUCD();
								if (u != null) {
									l.add(u);
								}
							}
						}
					}
				} else if (r instanceof HasTables) {
					TableBean[] t = ((HasTables)r).getTables();
					for (int j = 0; j < t.length; j++) {
						ColumnBean[] cs = t[j].getColumns();
						for (int k = 0; k < cs.length; k++) {
							String u = cs[k].getUCD();
							if (u != null) {
								l.add(u);
							}
						}
					}							
				}
				return l;
			}
		});
	}

	public String getName() {
		return "UCD";
	}
}