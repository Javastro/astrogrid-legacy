package org.astrogrid.desktop.modules.ui.voexplorer.strategy;

import java.util.ArrayList;
import java.util.List;

import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.voexplorer.google.FilterPipelineFactory.PipelineStrategy;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.matchers.Matcher;

/** Filters on UCD of table columns.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 15, 20073:36:34 PM
 */
public final class UcdStrategy extends PipelineStrategy {
	@Override
    public Matcher<Resource> createMatcher(final List<String> selected) {
		return new Matcher<Resource>() {
			public boolean matches(final Resource r) {
				if (r instanceof DataCollection) {
					final DataCollection dc = (DataCollection)r;
					final Catalog[] catalogues = dc.getCatalogues();
					for (int i = 0; i < catalogues.length; i++) {
						final TableBean[] t = catalogues[i].getTables();
						for (int j = 0; j < t.length; j++) {
							final ColumnBean[] cs = t[j].getColumns();
							for (int k = 0; k < cs.length; k++) {
								final String u = cs[k].getUCD();
								if (u != null && selected.contains(u)) {
									return true;
								}
							}
						}
					} 
				} else if (r instanceof CatalogService) {
					final TableBean[] t = ((CatalogService)r).getTables();
					for (int j = 0; j < t.length; j++) {
						final ColumnBean[] cs = t[j].getColumns();
						for (int k = 0; k < cs.length; k++) {
							final String u = cs[k].getUCD();
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
	

	@Override
    public TransformedList<Resource,String> createView(final EventList<Resource> base) {
		return new CollectionList<Resource,String>(base,
				new CollectionList.Model<Resource,String>() {
			public List<String> getChildren(final Resource r) {
				final List<String> l = new ArrayList<String>();
				if (r instanceof DataCollection) {
					final DataCollection dc = (DataCollection)r;
					final Catalog[] catalogues = dc.getCatalogues();
					for (int i = 0; i < catalogues.length; i++) {
						final TableBean[] t = catalogues[i].getTables();
						for (int j = 0; j < t.length; j++) {
							final ColumnBean[] cs = t[j].getColumns();
							for (int k = 0; k < cs.length; k++) {
								final String u = cs[k].getUCD();
								if (u != null) {
									l.add(u);
								}
							}
						}
					}
				} else if (r instanceof CatalogService) {
					final TableBean[] t = ((CatalogService)r).getTables();
					for (int j = 0; j < t.length; j++) {
						final ColumnBean[] cs = t[j].getColumns();
						for (int k = 0; k < cs.length; k++) {
							final String u = cs[k].getUCD();
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

	@Override
    public String getName() {
		return "Table Column UCD";
	}
}