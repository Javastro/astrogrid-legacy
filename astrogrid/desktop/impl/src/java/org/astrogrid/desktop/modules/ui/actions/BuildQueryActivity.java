/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.QueryBuilderInternal;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** build a query from a selected resource.
 * @todo implement
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20074:13:13 PM
 */
public final class BuildQueryActivity extends AbstractFileOrResourceActivity {
/**
 * 
 */
public BuildQueryActivity(QueryBuilderInternal t) {
	this.t = t;
	CSH.setHelpIDString(this, "resourceTask.buildQuery");		
	setText("Build ADQL");
	setIcon(IconHelper.loadIcon("db16.png"));
	setToolTipText("Construct an ADQL query against the selected table descriptions");
	
}
// thing to invoke
private final QueryBuilderInternal t;

// unsure whether there's a better place to put these helper methods.
	public static boolean hasAdqlParameter(CeaApplication a) {
	    return findAdqlParameter(a) != null;
	}

	/** find the first adql parameter, or return null */
	   public static ParameterBean findAdqlParameter(CeaApplication a) {
	        ParameterBean[] parameters = a.getParameters();
	        for (int i = 0; i < parameters.length; i++) {
	            if (parameters[i].getType().equalsIgnoreCase("adql")) {
	            return parameters[i];
	        }
	        }
	    return null;
	    }

	   /** find the name of the first interface contianing an adql input parameter
	    * or null */
	    public static String findNameOfFirstADQLInterface(CeaApplication app) {
	        ParameterBean adql = findAdqlParameter(app);
	        if (adql == null) {
	            return null;
	        }
	        String name = adql.getName();
	        for (int i = 0; i < app.getInterfaces().length; i++) {
	            // search through parameters.
	            final InterfaceBean iface = app.getInterfaces()[i];
                ParameterReferenceBean[] refs = iface.getInputs();
	            for (int j = 0; j < refs.length; j++) {
                    if (refs[j].getRef().equals(name)) {
                        return iface.getName();
                    }
                }
	        }
	        return null;
	    }
	    /** find the name of the first interface NOT contianing an adql input parameter
	        * or null */
	        public static String findNameOfFirstNonADQLInterface(CeaApplication app) {
	            ParameterBean adql = findAdqlParameter(app);
	            if (adql == null) {
	                return app.getInterfaces()[0].getName();
	            }
	            String name = adql.getName();
	            for (int i = 0; i < app.getInterfaces().length; i++) {
	                // search through parameters.
	                boolean found = false;
	                final InterfaceBean iface = app.getInterfaces()[i];
	                ParameterReferenceBean[] refs = iface.getInputs();
	                for (int j = 0; j < refs.length; j++) {
	                    if (refs[j].getRef().equals(name)) {
	                        found = true;
	                    }
	                }
	                if (found == false) {
	                    return iface.getName();
	                }
	            }
	            return null;
	        }	   
	
	public void actionPerformed(ActionEvent e) {
	    List resources = computeInvokableResources();
	    switch(resources.size()) {
	        case 0:
	            break;
	        case 1:
	            Resource r = (Resource) resources.get(0);
	            if (isDbSchema(r)) { 
	                // single db schema.
	                if (r instanceof DataCollection) {
	                    t.build((DataCollection)r);
	                } /* not implemented else { // must be a catalog service.
	                    t.build((CatalogService)r);
	                }*/
	            } else if (r instanceof CeaApplication) {
	                // a cea app.
	                t.build((CeaApplication)r);
	            }
	            break;
	        default:
	            //future for multi-table querying.	            
	    }
	    List files= computeInvokableFiles();
	    switch(files.size()) {
	        case 0:
	            break;
	        case 1:
	            FileObject fo = (FileObject)files.get(0);
	            t.edit(fo);
	            break;
	        default:
	            //future - do something with a multiple selection.
	    }
	}

	 // can't operate on more than one file.
	// ponder - should we open each selected adql file in a new viewer?
	public void manySelected(FileObject[] l) {
		noneSelected();
	}

	// accept a single adql file.
	public boolean invokable(FileObject fo) {
	    try {
	        if (fo.getType().hasContent()) {
	            String mime = fo.getContent().getContentInfo().getContentType();
	            if (mime != null && mime.startsWith(VoDataFlavour.MIME_ADQL)) {
	                // testing with start-with to match against adql and adqlx
	                return true;
	            } 	
	        }
	    } catch (FileSystemException e) {
	        return false;
	    }
	    return false;
	}
	
	// accept a single database schema, or a single queriable service.
	public boolean invokable(Resource resource) {
		return isDbSchema(resource) ||
				 (resource instanceof CeaApplication && hasAdqlParameter((CeaApplication)resource));

	}

	// accept if any in selection are database schema.
	public void manyResourcesSelected(Resource[] l) {
		noneSelected();
		// later - support multi-query later

	}
	private boolean isDbSchema(Object r) { // just for tabular db at the moment.
		return r instanceof DataCollection; // || r instanceof CatalogService;
	}

	
}
