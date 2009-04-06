/**
 * 
 */
package org.astrogrid.desktop.modules.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.swing.tree.DefaultMutableTreeNode;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

/** Constants for  data flavours and mime types.
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
	public static final DataFlavor LOCAL_FILEOBJECT_VIEW = localDataFlavor(FileObjectView.class,"VFS file object");
	public static final DataFlavor LOCAL_FILEOBJECT_VIEW_ARRAY = localDataFlavor(FileObjectView[].class,"List of VFS file objects");
	// doubt if it's possible to do a non-local version - as fileobject isn't serializable.
	
//	 reference types
	public static final DataFlavor LOCAL_URL = localDataFlavor(java.net.URL.class,"URL Reference");
	public static final DataFlavor URL = new DataFlavor("application/x-java-url;class=java.net.URL","URL Reference");
	public static final DataFlavor LOCAL_URI = localDataFlavor(URI.class,"URI Reference");
	
	public static final DataFlavor LOCAL_URI_ARRAY = localDataFlavor(URI[].class,"List of URI references");
	public static final DataFlavor URI_LIST = new DataFlavor("text/uri-list","List of URI references");
	public static final DataFlavor URI_LIST_STRING =new DataFlavor("text/uri-list;class=java.lang.String","List of URI references");
// types specific to astroscope.
	
	
	// other common-or garden votable types - 
	public static final DataFlavor STRING = DataFlavor.stringFlavor;
	public static final DataFlavor XML = new DataFlavor("text/xml","XML");
	public static final DataFlavor PLAIN = new DataFlavor("text/plain","Plain text");
	public static  final DataFlavor HTML = new DataFlavor("text/html","HTML");

	public static  final DataFlavor GIF = new DataFlavor("image/gif","GIF Image");
	public static  final DataFlavor PNG = new DataFlavor("image/png","PNG Image");
	public static  final DataFlavor JPEG = new DataFlavor("image/jpeg","JPEG Image");
	
//the rest of these types still need to be sorted out.	
	public static final String MIME_VOTABLE =  "application/x-votable+xml";
	/** votable, accessible by Input Stream */
	public static final  DataFlavor VOTABLE = new DataFlavor(MIME_VOTABLE,"VOTABLE");

	/** siap response - a specialization of votable */
	public static final DataFlavor SIAP_RESPONSE = new DataFlavor("application/x-siap+votable+xml","SIAP Response");

	/** ssap response - a specialization of votable */	
	public static final DataFlavor SSAP_RESPONSE = new DataFlavor("application/x-ssap+votable+xml","SIAP Response");

	/** FITS table */
	public static final String MIME_FITS_TABLE = "application/fits";
	public static final DataFlavor FITS_TABLE = new DataFlavor(MIME_FITS_TABLE,"FITS Table");
	
	/** FITS Image */
	public static final String MIME_FITS_IMAGE = "image/fits";
	public static final DataFlavor FITS_IMAGE = new DataFlavor(MIME_FITS_IMAGE,"FITS Image");

	/** FITS Spectrum */
	public static final String MIME_FITS_SPECTRUM = "spectrum/fits";
	public static final DataFlavor FITS_SPECTRUM = new DataFlavor(MIME_FITS_SPECTRUM,"FITS Spectrum");

	
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
		
	public static final String MIME_OCTET_STREAM = "application/octet-stream";
	
	/** voevents */
	
	public static final String MIME_VOEVENT = "application/x-voevent+xml";
	
	/** Treenode flavour - not very vo, but keeps them all in one place. */
	public static final DataFlavor TREENODE = localDataFlavor(DefaultMutableTreeNode.class,"Tree Node");
	//@todo add types for solar.
	
		//@todo define some types for movies..

	// factory methods
	
	/** create a flavour only available in local vm
	 *  suitable for internal objects, non serializable types, etc
	 */
	private static DataFlavor localDataFlavor(final Class clazz, final String descr) {
		return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
				+ ";class=\"" + clazz.getName() + "\"", descr);
	}
	
	/** java has broken handling of urls. if this url is a file:// with a host,
	 * remove the host field. else pass-thru unchanged.
	 * @param u
	 * @return
	 * @throws MalformedURLException 
	 */
	public static URL mkJavanese(final URL u) throws MalformedURLException {
	    if ("file".equals(u.getProtocol()) && u.getHost() != null) {
	        return new URL(u.getProtocol(),null,u.getFile());
	    } else {
	        return u;
	    }
	}

	
}
