/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/client/src/java/org/astrogrid/store/util/MimeTypeUtil.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/10/05 15:39:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: MimeTypeUtil.java,v $
 *   Revision 1.2  2004/10/05 15:39:29  dave
 *   Merged changes to AladinAdapter ...
 *
 *   Revision 1.1.2.1  2004/10/05 15:30:44  dave
 *   Moved test base from test to src tree ....
 *   Added MimeTypeUtil
 *   Added getMimeType to the adapter API
 *   Added logout to the adapter API
 *
 * </cvs:log>
 *
 */


package org.astrogrid.store.util ;

/**
 * A temp fix to guess the mime type from a file name.
 * This is only required until the myspace manager API allows the client to set the mime type.
 * @todo Reseolve the duplicate mime type values in filestore FileProperties.
 *
 */
public class MimeTypeUtil
	{
	/**
	 * Known MIME type values.
	 * Note - these are a duplication of the values in filestore FileProperties.
	 *
	 */
	public static final String MIME_TYPE_TEXT     = "text/raw"  ;
	public static final String MIME_TYPE_XML      = "text/xml"  ;
	public static final String MIME_TYPE_HTML     = "t"  ;

	public static final String MIME_TYPE_VOLIST   = "text/xml +org.astrogrid.mime.volist"  ;
	public static final String MIME_TYPE_VOTABLE  = "text/xml +org.astrogrid.mime.votable" ;

	public static final String MIME_TYPE_JOB      = "text/xml +org.astrogrid.mime.job"      ;
	public static final String MIME_TYPE_WORKFLOW = "text/xml +org.astrogrid.mime.workflow" ;

	public static final String MIME_TYPE_ADQL     = "text/xml +org.astrogrid.mime.adql"     ;


	/**
	 * Public constructor.
	 *
	 */
	public MimeTypeUtil()
		{
		}

	/**
	 * Guess the mime type from a file name.
	 * @param name The target filename.
	 * @return The mime type if the filename has a recognised .extension, otherwise null.
	 *
	 */
	public String resolve(String name)
		{
		String mime = null ;
		//
		// If we have a name.
		if (null != name)
			{
			//
			// Find the last '.' in the name.
			int index = name.lastIndexOf('.') ;
			//
			// If we found a '.' in the name.
			if (index != -1)
				{
				//
				// Check for recognised types.
				String type = name.substring(
					index
					).toLowerCase() ;
				//
				// Vanilla XML.
				if (".xml".equals(type))
					{
					mime = MIME_TYPE_XML ;
					}
				//
				// Vanilla XSL.
				if (".xsl".equals(type))
					{
					mime = MIME_TYPE_XML ;
					}
				//
				// Astrogrid VoTable.
				if (".vot".equals(type))
					{
					mime = MIME_TYPE_VOTABLE ;
					}
				if (".votable".equals(type))
					{
					mime = MIME_TYPE_VOTABLE ;
					}
				//
				// Astrogrid VoList.
				if (".vol".equals(type))
					{
					mime = MIME_TYPE_VOLIST ;
					}
				if (".volist".equals(type))
					{
					mime = MIME_TYPE_VOLIST ;
					}
				//
				// Astrogrid Job details.
				if (".job".equals(type))
					{
					mime = MIME_TYPE_JOB ;
					}
				//
				// Astrogrid workflow.
				if (".work".equals(type))
					{
					mime = MIME_TYPE_WORKFLOW ;
					}
				if (".flow".equals(type))
					{
					mime = MIME_TYPE_WORKFLOW ;
					}
				if (".workflow".equals(type))
					{
					mime = MIME_TYPE_WORKFLOW ;
					}
				//
				// Astrogrid ADQL.
				if (".adql".equals(type))
					{
					mime = MIME_TYPE_ADQL ;
					}
				}
			}
		return mime ;
		}
	}
