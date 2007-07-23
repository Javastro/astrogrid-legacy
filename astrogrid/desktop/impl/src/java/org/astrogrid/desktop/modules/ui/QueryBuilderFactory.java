/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.util.List;

import net.sourceforge.hiveutils.service.ObjectBuilder;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskRunnerImpl;

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
