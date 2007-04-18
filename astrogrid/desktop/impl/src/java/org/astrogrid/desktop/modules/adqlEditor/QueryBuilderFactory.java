/**
 * 
 */
package org.astrogrid.desktop.modules.adqlEditor;

import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/** Factory for building of new query builder instances.
 * s
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 20075:43:40 PM
 */
public class QueryBuilderFactory implements QueryBuilderInternal {

	 private final Preference pref;
	private final UIContext context;
	private final MyspaceInternal myspace;
	private final ApplicationsInternal apps;
	private final ResourceChooserInternal rChooser;
	private final List panelFactories;

	public QueryBuilderFactory( 
	    		List panelFactories
	            ,ResourceChooserInternal rChooser
	            ,ApplicationsInternal apps
	            ,MyspaceInternal myspace                 
	            ,UIContext context,  Preference pref) {
		 this.panelFactories = panelFactories;
		 this.rChooser = rChooser;
		 this.apps = apps;
		 this.myspace = myspace;
		 this.context = context;
		 this.pref = pref;
	 }
	
	
	protected QueryBuilderInternal newInstance() {
		return new QueryBuilderImpl(panelFactories,rChooser,apps,myspace, context,pref);
	}
	
	public void build(CeaApplication app) {
		QueryBuilderInternal internal = newInstance();
		internal.build(app);
	}

	public void build(DataCollection coll) {
		QueryBuilderInternal internal = newInstance();
		internal.build(coll);
	}

	public void build(CatalogService cat) {
		QueryBuilderInternal internal = newInstance();
		internal.build(cat);
	}

	public void build(Resource[] rs) {
		QueryBuilderInternal internal = newInstance();
		internal.build(rs);		
	}

	public void edit(FileObject fo) {
		QueryBuilderInternal internal = newInstance();
		internal.edit(fo);		
	}

}
