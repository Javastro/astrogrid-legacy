/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/info/StoreInfo.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/14 13:50:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: StoreInfo.java,v $
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.2  2004/07/06 09:16:12  dave
 *   Added delegate interface and mock implementation
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.info ;

/**
 * A base class for infomation objects.
 *
 */
public class StoreInfo
	{
	/**
	 * The object identifier.
	 *
	 */
	private String ident ;

	/**
	 * Access to the identifier.
	 *
	 */
	public String getIdent()
		{
		return this.ident ;
		}

	/**
	 * Access to the identifier.
	 *
	 */
	public void setIdent(String value)
		{
		if (null == this.ident)
			{
			this.ident = value ;
			}
		}
	}

