/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/base/Attic/AstPortalIdent.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/12 18:19:38 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: AstPortalIdent.java,v $
 * Revision 1.1  2003/06/12 18:19:38  dave
 * Initial import into cvs ...
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.base ;

/**
 * A generic class for a unique identifier.
 * Initially, just an instance counter.
 * Later on, we may need to make this more robust and globally unique.
 *
 */
public class AstPortalIdent
	{

	/**
	 * Our instance number.
	 *
	 */
	protected int instance = 0 ;

	/**
	 * Our global instance counter.
	 *
	 */
	protected static int instances = 0 ;

	/**
	 * Our shared sync object.
	 *
	 */
	protected static Object sync = new Object() ;

	/**
	 * Public constructor.
	 *
	 */
	public AstPortalIdent()
		{
		synchronized (sync)
			{
			this.instance = instances++ ;
			}
		this.string = "AST-" + this.instance ;
		}

	/**
	 * Our string identifier.
	 *
	 */
	protected String string ; 

	/**
	 * Convert to a string.
	 *
	 */
	public String toString()
		{
		return this.string ;
		}

	/**
	 * Compare with another ident.
	 *
	 */
	public boolean equals(Object that)
		{
		//
		// If that is not null.
		if (null != that)
			{
			//
			// If that is another AstPortalIdent.
			if (that instanceof AstPortalIdent)
				{
				//
				// Compare the instance numbers.
				return (this.instance == ((AstPortalIdent)that).instance) ;
				}
			//
			// If that is a string.
			if (that instanceof String)
				{
				//
				// Compare the string values.
				return this.string.equals(that) ;
				}
			}
		//
		// Default fail.
		return false ;
		}

	/**
	 * Generate our hash code.
	 * Returns the hash code from our string representation.
	 * Means we can compare against strings from the HttpRequest parameters.
	 *
	 */
	public int hashCode()
		{
		return this.string.hashCode() ;
		}
	}
