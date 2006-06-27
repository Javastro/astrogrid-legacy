/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.datatransfer.DataFlavor;

/** Helper class of data flavour constant objects.
 * @author Noel Winstanley
 * @since Jun 19, 20068:08:20 AM
 */
public class VoDataFlavour {

	private VoDataFlavour() {
	};
	/** votable, accessible by Input Stream */
	public static final  DataFlavor VOTABLE = new DataFlavor("application/x-ivoa-votable","VOTABLE");
	/** votable, accessible as String */
	public static final DataFlavor VOTABLE_STRING = new DataFlavor("application/x-ivoa-votable; class=java.lang.String","VOTABLE");
	
	/** siap response - a specialization of votable */
	public static final DataFlavor SIAP_RESPONSE = new DataFlavor("application/x-ivoa-siap-response","SIAP Response");
	/** siap response - a specialization of votable, accessible as String */
	public static final DataFlavor SIAP_RESPONSE_STRING = new DataFlavor("application/x-ivoa-siap-response; class=java.lang.String","SIAP Response");
	
	/** ssap response - a specialization of votable */	
	public static final DataFlavor SSAP_RESPONSE = new DataFlavor("application/x-ivoa-ssap-response","SIAP Response");
	/** ssap response - a specialization of votable, accessible as String */
	public static final DataFlavor SSAP_RESPONSE_STRING = new DataFlavor("application/x-ivoa-ssap-response; class=java.lang.String","SIAP Response");
	
	/** FITS table */
	public static final DataFlavor FITS_TABLE = new DataFlavor("application/x-fits","FITS Table");
	/** FITS table accesible as byte array */
//	public static DataFlavor FITS_TABLE_BYTES = new DataFlavor("application/x-fits; class=[B","FITS Table");
	
	/** FITS Image */
	public static final DataFlavor FITS_IMAGE = new DataFlavor("image/x-fits","FITS Image");
	/** FITS Image accesible as byte array */
//	public static DataFlavor FITS_IMAGE_BYTES = new DataFlavor("image/x-fits; class=[B","FITS Image");
	
	/** registry Entry accessible as input stream. */
	public static final DataFlavor VORESOURCE = new DataFlavor("application/x-ivoa-voresource","VO Registry Record");
	
	/** registry Entry as string */
	public static final DataFlavor VORESOURCE_STRING = new DataFlavor("application/x-ivoa-voresource; class=java.lang.String","VO Registry Record");
		
	/** adql/x query */
	public static final DataFlavor ADQLX = new DataFlavor("application/x-ivoa-adqlx","ADQL/x");
	/** adql/x query as string */
	public static final DataFlavor ADQLX_STRING = new DataFlavor("application/x-ivoa-adqlx; class=java.lang.String","ADQL/x");
	
	/** workflow */
	public static final DataFlavor WORKFLOW = new DataFlavor("application/x-ag-workflow","AstroGrid Workflow");
	/** workflow as string */
	public static final DataFlavor WORKFLOW_STRING = new DataFlavor("application/x-ag-workflow; class=java.lang.String","AstroGrid Workflow");

	/** workflow transcript */
	public static final DataFlavor WORKFLOW_TRANSCRIPT = new DataFlavor("application/x-ag-workflow-transcript","AstroGrid Workflow Transcript");
	/** workflow transcript as string */
	public static final DataFlavor WORKFLOW_TRANSCRIPT_STRING = new DataFlavor("application/x-ag-workflow-transcript; class=java.lang.String","AstroGrid Workflow Transcript");

	/** cea tool document */
	public static final DataFlavor CEA_TOOL = new DataFlavor("application/x-ag-cea-invocation","CEA Tool Document");
	/** cea tool document as String*/
	public static final DataFlavor CEA_TOOL_STRING = new DataFlavor("application/x-ag-cea-invocation; class=java.lang.String","CEA Tool Document");
		
	
	//@todo add types for solar.
	
	// reference types
	public static final DataFlavor URL = new DataFlavor("reference/x-url; class=java.net.URL","URL Reference");
	public static final DataFlavor IVORN = new DataFlavor("reference/x-ivoa-ivorn; class=java.net.URI","Ivorn Reference");
	
	
	// other common-or garden votable types - 
	public static final DataFlavor XML = new DataFlavor("text/xml","XML");
	public static  final DataFlavor HTML = new DataFlavor("text/html","HTML");
	public static  final DataFlavor PLAIN = new DataFlavor("text/plain","Plain Text");
	public static  final DataFlavor PLAIN_STRING = new DataFlavor("text/plain; class=java.lang.String","Plain Text");
	public static  final DataFlavor XML_STRING = new DataFlavor("text/xmll; class=java.lang.String","XML");
	public static  final DataFlavor HTML_STRING = new DataFlavor("text/html; class=java.lang.String","HTML");
	public static  final DataFlavor GIF = new DataFlavor("image/gif","GIF Image");
	public static  final DataFlavor PNG = new DataFlavor("image/png","PNG Image");
	public static  final DataFlavor JPEG = new DataFlavor("image/jpeg","JPEG Image");
	//@todo define some types for movies..
	

	
	
}
