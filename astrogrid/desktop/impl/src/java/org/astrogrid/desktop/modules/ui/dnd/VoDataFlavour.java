/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.net.URI;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;

/** Helper class of data flavour constant objects.
 * @author Noel Winstanley
 * @since Jun 19, 20068:08:20 AM
 */
public class VoDataFlavour {

	private VoDataFlavour() {
	};
	
	
// types for registry entries..
	
	
	/** registry entry as object */
	public static final DataFlavor LOCAL_RESOURCE = localDataFlavor(Resource.class,"VO Resource");
	public static final DataFlavor RESOURCE = new DataFlavor(Resource.class,"VO Resource");
	/** list of registry entry objects */
	public static final DataFlavor LOCAL_RESOURCE_LIST = localDataFlavor(List.class,"List of resources");
	public static final DataFlavor RESOURCE_LIST = new DataFlavor(List.class,"List of resources");
	
	/** registry Entry accessible as input stream. */
	public static final DataFlavor VORESOURCE = new DataFlavor("application/x-voresource+xml","VO Registry Record");
	
	
//	 reference types
	public static final DataFlavor LOCAL_URL = localDataFlavor(java.net.URL.class,"URL Reference");
	public static final DataFlavor URL = new DataFlavor("text/x-url;class=java.net.URL","URL Reference");
	public static final DataFlavor LOCAL_URI = localDataFlavor(URI.class,"URI Reference");
	
	public static final DataFlavor LOCAL_URI_ARRAY = localDataFlavor(URI[].class,"List of URI references");
	public static final DataFlavor URI_LIST = new DataFlavor("text/uri-list","List of URI references");
	
	
	// other common-or garden votable types - 
	public static final DataFlavor STRING = DataFlavor.stringFlavor;
	public static final DataFlavor XML = new DataFlavor("text/xml","XML");
	public static final DataFlavor PLAIN = new DataFlavor("text/plain","Plain text");
	public static  final DataFlavor HTML = new DataFlavor("text/html","HTML");

	public static  final DataFlavor GIF = new DataFlavor("image/gif","GIF Image");
	public static  final DataFlavor PNG = new DataFlavor("image/png","PNG Image");
	public static  final DataFlavor JPEG = new DataFlavor("image/jpeg","JPEG Image");
	
//the rest of these types still need to be sorted out.	
	/** votable, accessible by Input Stream */
	public static final  DataFlavor VOTABLE = new DataFlavor("application/x-votable+xml","VOTABLE");

	/** siap response - a specialization of votable */
	public static final DataFlavor SIAP_RESPONSE = new DataFlavor("application/x-siap+votable+xml","SIAP Response");

	/** ssap response - a specialization of votable */	
	public static final DataFlavor SSAP_RESPONSE = new DataFlavor("application/x-ssap+votable+xml","SIAP Response");

	/** FITS table */
	public static final DataFlavor FITS_TABLE = new DataFlavor("application/fits","FITS Table");
	
	/** FITS Image */
	public static final DataFlavor FITS_IMAGE = new DataFlavor("image/fits","FITS Image");

	/** adql/x query */
	public static final DataFlavor ADQLX = new DataFlavor("application/x-adqlx+xml","ADQL/x");
	
	/** workflow */
	public static final DataFlavor WORKFLOW = new DataFlavor("application/x-jes-workflow+xml","AstroGrid Workflow");
	
	/** workflow transcript */
	public static final DataFlavor WORKFLOW_TRANSCRIPT = new DataFlavor("application/x-jes-workflow-transcript+xml","AstroGrid Workflow Transcript");

	/** cea tool document */
	public static final DataFlavor CEA_TOOL = new DataFlavor("application/x-ag-cea+xml","CEA Tool Document");
		
	
	//@todo add types for solar.
	
		//@todo define some types for movies..
	
	
	// factory methods
	
	/** create a flavour only available in local vm
	 *  suitable for internal objects, non serializable types, etc
	 */
	private static DataFlavor localDataFlavor(Class clazz, String descr) {
		return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
				+ ";class=\"" + clazz.getName() + "\"", descr);
	}

	
	
}
