/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.QueryBuilderInternal;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** Open QueryBuilder for the selected resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20074:13:13 PM
 * @TEST unit test where possible.
 */
public final class BuildQueryActivity extends AbstractFileOrResourceActivity {
/**
 * 
 */
public BuildQueryActivity(final QueryBuilderInternal t) {
	this.t = t;
	setHelpID("activity.adql");	
	setText("Build ADQL");
	setIcon(IconHelper.loadIcon("db16.png"));
	setToolTipText("Construct an ADQL query against the selected table descriptions");
   // setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,UIComponentMenuBar.MENU_KEYMASK));	
}
// thing to invoke
private final QueryBuilderInternal t;

// unsure whether there's a better place to put these helper methods.
	public static boolean hasAdqlParameter(final CeaApplication a) {
	    return findAdqlParameter(a) != null;
	}
	
	/** indicate what kinds of interface this application has
	 * @return 0 - query and app interfasces
	 *             <br>1 - query only 
	 *             <br>-1 - application only.
	 */
	public static int whatKindOfInterfaces(final CeaApplication a) {
	    final ParameterBean pb = findAdqlParameter(a);
	    if (pb == null) {
	        return -1;
	    }
	    final String name = pb.getName();
	    // so now see if there's query and/or non-query interfaces
	    boolean foundQuery = false;
	    boolean foundNonQuery = false;
        final InterfaceBean[] interfaces = a.getInterfaces();         
        for (int i = 0; i < interfaces.length; i++) {  
            if (foundQuery && foundNonQuery) {
                break; // no need to search anymore
            }
            // search through parameters of this interface. assume it's a non-adql until shown otherwise.            
            final InterfaceBean iface = interfaces[i];
            final ParameterReferenceBean[] refs = iface.getInputs();
            boolean currentIsNonQuery = true;
            for (int j = 0; j < refs.length; j++) {
                if (refs[j].getRef().equals(name)) {
                    foundQuery = true; // it's an adql interface
                    currentIsNonQuery = false;
                    break; // no need to scan any more of the parameters.
                }
            }
            // it's a non-adql interface.
            foundNonQuery = foundNonQuery || currentIsNonQuery;
        }	    
        
        return (foundQuery ? 1 : 0) + (foundNonQuery ? -1 : 0);
	}
	/** find the first adql parameter, or return null */
	   public static ParameterBean findAdqlParameter(final CeaApplication a) {
	        final ParameterBean[] parameters = a.getParameters();
	        for (int i = 0; i < parameters.length; i++) {
	            if (parameters[i].getType().equalsIgnoreCase("adql")) {
	            return parameters[i];
	        }
	        }
	    return null;
	    }

	   /** find the name of the first interface contianing an adql input parameter
	    * or null */
	    public static String findNameOfFirstADQLInterface(final CeaApplication app) {
	        final ParameterBean adql = findAdqlParameter(app);
	        if (adql == null) {
	            return null;
	        }
	        final String name = adql.getName();
	        for (int i = 0; i < app.getInterfaces().length; i++) {
	            // search through parameters.
	            final InterfaceBean iface = app.getInterfaces()[i];
                final ParameterReferenceBean[] refs = iface.getInputs();
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
	        public static String findNameOfFirstNonADQLInterface(final CeaApplication app) {
	            final ParameterBean adql = findAdqlParameter(app);
	            if (adql == null) {
	                return app.getInterfaces()[0].getName();
	            }
	            final String name = adql.getName();
	            for (int i = 0; i < app.getInterfaces().length; i++) {
	                // search through parameters.
	                boolean found = false;
	                final InterfaceBean iface = app.getInterfaces()[i];
	                final ParameterReferenceBean[] refs = iface.getInputs();
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
	
	public void actionPerformed(final ActionEvent e) {
	    final List resources = computeInvokableResources();
	    switch(resources.size()) {
	        case 0:
	            break;
	        case 1:
	            final Resource r = (Resource) resources.get(0);
	            if (r instanceof CatalogService && r instanceof CeaService) {
	                    t.build((CatalogService)r);
	            } else if (r instanceof CeaApplication) {
	                // a cea app.
	                t.build((CeaApplication)r);
	            }
	            break;
	        default:
	            //future for multi-table querying.	            
	    }
	    final List files= computeInvokableFiles();
	    switch(files.size()) {
	        case 0:
	            break;
	        case 1:
	            final FileObject fo = (FileObject)files.get(0);
	            t.edit(fo);
	            break;
	        default:
	            //future - do something with a multiple selection.
	    }
	}

	 // can't operate on more than one file.
	// ponder - should we open each selected adql file in a new viewer?
	public void manySelected(final FileObject[] l) {
		noneSelected();
	}

	// accept a single adql file.
	public boolean invokable(final FileObject fo) {
	    try {
	        if (fo.getType().hasContent()) {
	            final String mime = fo.getContent().getContentInfo().getContentType();
	            if (mime != null && mime.startsWith(VoDataFlavour.MIME_ADQL)) {
	                // testing with start-with to match against adql and adqlx
	                return true;
	            } 	
	        }
	    } catch (final FileSystemException e) {
	        return false;
	    }
	    return false;
	}
	
	// accept a single database schema, or a single queriable service.
	public boolean invokable(final Resource resource) {
		return
		        (resource instanceof CatalogService && resource instanceof CeaService)
		    ||
				 (resource instanceof CeaApplication && hasAdqlParameter((CeaApplication)resource));

	}

	// accept if any in selection are database schema.
	public void someSelected(final Resource[] list) {
		noneSelected();
		// later - support multi-query later

	}

	
}
