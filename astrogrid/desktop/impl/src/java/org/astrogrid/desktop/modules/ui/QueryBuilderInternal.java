/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.apache.commons.collections.Factory;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ui.QueryBuilder;

/** Interface to a query builder component. Methods on this interface are called by VOExplorer to create new query builder windows
 * for selected resources.
 * 
 * @future add a public interface with some ar-suitable methods - i.e. passing uri of fileObject / resource Object as the parameter.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 20074:41:01 PM
 */
public interface QueryBuilderInternal extends QueryBuilder, Factory {
	
	/** open the specified file for editing */
	void edit(FileObject fo);
	
	/** build a query against the specified cea application */
	void build(CeaApplication app) ;
	
	/** build a query against the specified data collection */
	void build(CatalogService coll);

	
	// irritatingly, dataCollection and catalogService don't have abase class that
	// expresses 'has table metadata' - commonest baseclass is Resource. will 
	// have to do.
	/**
	 * @future
	 * buld a query against the specified data sources
	 * @params rs an array of resouces (all of which must be instances of DataCollection
	 * or CatalogService)
	 */

	//void build(Resource[] rs) ;
	

}
