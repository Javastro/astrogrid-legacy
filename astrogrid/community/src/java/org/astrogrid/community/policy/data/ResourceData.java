/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/data/Attic/ResourceData.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/09 19:15:10 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceData.java,v $
 *   Revision 1.1  2003/09/09 19:15:10  KevinBenson
 *   New ResourceData file
 *
 *   Revision 1.4  2003/09/09 16:41:53  KevinBenson
 *   Added password field
 *
 *   Revision 1.3  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 *   Revision 1.2  2003/09/04 23:58:10  dave
 *   Experimenting with using our own DataObjects rather than the Axis generated ones ... seems to work so far
 *
 *   Revision 1.1  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.data ;

public class ResourceData
	{
	/**
	 * Public constructor.
	 *
	 */
	public ResourceData()
		{
		this(null, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public ResourceData(String ident)
		{
		this(ident, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public ResourceData(String ident, String description)
		{
		this.ident = ident ;
		this.description = description ;
		}

	/**
	 * Our Resource ident.
	 *
	 */
	private String ident ;

	/**
	 * Access to our Resource ident.
	 *
	 */
	public String getIdent()
		{
		return this.ident ;
		}

	/**
	 * Access to our Resource ident.
	 *
	 */
	public void setIdent(String value)
		{
		this.ident = value ;
		}


      /**
       * Our Resource description.
       *
       */
      private String description ;

      /**
       * Access to our Resource description.
       *
       */
      public String getDescription()
         {
         return this.description ;
         }

      /**
       * Access to our Resource description.
       *
       */
      public void setDescription(String value)
         {
         this.description = value ;
         }
	}
