/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.TapService;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

/** Factory for the Query Builder.
 * @author Noel Winstanley
 * @since Jun 21, 20067:48:11 PM
 */
public class QueryBuilderFactory implements QueryBuilderInternal{

	public QueryBuilderFactory(final TypesafeObjectBuilder builder) {
		this.builder = builder;
	}
	private final TypesafeObjectBuilder builder;

	protected QueryBuilderInternal newInstance() {
		return builder.createQueryBuilderImpl();
	}
	
	public void hide() {
		// ignored.
	}


	public void show() {
		newInstance().show();
	}


	public Object create() {
		final QueryBuilderInternal tr = newInstance();
		tr.show();
		return tr;
	}

    public void build(final CeaApplication app) {
        final QueryBuilderInternal tr = newInstance();
        tr.show();        
        tr.build(app);
    }

    public void build(final CatalogService coll) {
        final QueryBuilderInternal tr = newInstance();
        tr.build(coll);   
        tr.show();        
    }


    public void edit(final FileObjectView fo) {
        final QueryBuilderInternal tr = newInstance();
        tr.show();        
        tr.edit(fo);   
    }

    public void build(final TapService app) {
        final QueryBuilderInternal tr = newInstance();
        tr.build(app);   
        tr.show();
    }

}
