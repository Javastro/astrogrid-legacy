/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.net.URI;
import java.util.List;

import org.apache.commons.vfs.FileObject;
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
	public static final DataFlavor LOCAL_RESOURCE_ARRAY = localDataFlavor(Resource[].class,"List of resources");
	public static final DataFlavor RESOURCE_ARRAY = new DataFlavor(Resource[].class,"List of resources");
	
	/** registry Entry accessible as input stream. */
	public static final DataFlavor VORESOURCE = new DataFlavor("application/x-voresource+xml","VO Registry Record");
	
	
// types for file objects...
	public static final DataFlavor LOCAL_FILEOBJECT = localDataFlavor(FileObject.class,"VFS file object");
	public static final DataFlavor LOCAL_FILEOBJECT_LIST = localDataFlavor(FileObject[].class,"List of VFS file objects");
	// dunno if it's possible to do a non-local version - as fileobject isn't serializable.
	
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
	public static final String MIME_VOTABLE =  "application/x-votable+xml";
	public static final  DataFlavor VOTABLE = new DataFlavor(MIME_VOTABLE,"VOTABLE");

	/** siap response - a specialization of votable */
	public static final DataFlavor SIAP_RESPONSE = new DataFlavor("application/x-siap+votable+xml","SIAP Response");

	/** ssap response - a specialization of votable */	
	public static final DataFlavor SSAP_RESPONSE = new DataFlavor("application/x-ssap+votable+xml","SIAP Response");

	/** FITS table */
	public static final String MIME_FITS_TABLE = "application/fits";
	public static final DataFlavor FITS_TABLE = new DataFlavor(MIME_FITS_TABLE,"FITS Table");
	
	/** FITS Image */
	public static final DataFlavor FITS_IMAGE = new DataFlavor("image/fits","FITS Image");


	/** adql query */
	public static final String MIME_ADQL = "application/x-adql";
	public static final DataFlavor ADQL = new DataFlavor(MIME_ADQL,"ADQL");
	/** adql/x query */
	public static final String MIME_ADQLX = "application/x-adql+xml";
	public static final DataFlavor ADQLX = new DataFlavor(MIME_ADQLX,"ADQL/x");
	
	/** workflow */
	public static final DataFlavor WORKFLOW = new DataFlavor("application/x-jes-workflow+xml","AstroGrid Workflow");
	
	/** workflow transcript */
	public static final DataFlavor WORKFLOW_TRANSCRIPT = new DataFlavor("application/x-jes-workflow-transcript+xml","AstroGrid Workflow Transcript");

	/** cea tool document */
	public static final String MIME_CEA = "application/x-tool+xml";
	public static final DataFlavor CEA_TOOL = new DataFlavor(MIME_CEA,"CEA Tool Document");
		
	
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
