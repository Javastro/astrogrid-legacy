/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/store/adapter/aladin/AladinAdapterSecurityException.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: AladinAdapterSecurityException.java,v $
 *   Revision 1.2  2005/03/11 13:37:05  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.1  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.3  2004/11/17 16:22:53  clq2
 *   nww-itn07-704
 *
 *   Revision 1.2.20.1  2004/11/16 16:47:28  nw
 *   copied aladinAdapter interfaces into a neutrally-named package.
 *   deprecated original interfaces.
 *   javadoc
 *
 *   Revision 1.2  2004/09/28 10:24:19  dave
 *   Added AladinAdapter interfaces and mock implementation.
 *
 *   Revision 1.1.2.1  2004/09/23 16:32:02  dave
 *   Added better Exception handling ....
 *   Added initial mock container ....
 *   Added initial root container tests ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.store.adapter.aladin ;

/**
 * An exception thrown if the adapter attempts to access something it is not allowed to.
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.FileManagerClientFactory} instead.
 *
 */
public class AladinAdapterSecurityException
	extends AladinAdapterException
	{
	/**
	 * Public constructor, with cause and message.
	 * @param message The Exception message.
	 *
	 */
	public AladinAdapterSecurityException(String message)
		{
		super(message, null) ;
		}

	/**
	 * Public constructor, with cause and message.
	 * @param message The Exception message.
	 * @param cause   The Exception cause.
	 *
	 */
	public AladinAdapterSecurityException(String message, Throwable cause)
		{
		super(message, cause) ;
		}

	}
