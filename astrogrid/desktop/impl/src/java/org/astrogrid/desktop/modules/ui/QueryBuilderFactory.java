/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.DataCollection;

/** factory for the simplified app launcher.
 * @author Noel Winstanley
 * @since Jun 21, 20067:48:11 PM
 */
public class QueryBuilderFactory implements QueryBuilderInternal{

	public QueryBuilderFactory(TypesafeObjectBuilder builder) {
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
		QueryBuilderInternal tr = newInstance();
		tr.show();
		return tr;
	}

    public void build(CeaApplication app) {
        QueryBuilderInternal tr = newInstance();
        tr.build(app);
        tr.show();        
    }

    public void build(DataCollection coll) {
        QueryBuilderInternal tr = newInstance();
        tr.build(coll);   
        tr.show();        
    }
//
//    public void build(CatalogService cat) {
//        QueryBuilderInternal tr = newInstance();
//        tr.build(cat);        
//    }

    public void edit(FileObject fo) {
        QueryBuilderInternal tr = newInstance();
        tr.edit(fo);   
        tr.show();        
    }

}
