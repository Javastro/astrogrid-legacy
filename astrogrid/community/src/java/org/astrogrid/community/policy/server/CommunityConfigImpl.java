/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/CommunityConfigImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/08 20:28:50 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityConfigImpl.java,v $
 *   Revision 1.1  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import org.astrogrid.community.policy.data.CommunityConfig ;

import java.net.InetAddress ;

/**
 * Configuration data for the community service.
 *
 */
public class CommunityConfigImpl
	extends CommunityConfig
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * The name of the system property to read the location of our database config.
	 *
	 */
	private static final String DATABASE_CONFIG_PROPERTY = "org.astrogrid.policy.server.database.config" ;

	/**
	 * The name of the system property to read our database name from.
	 *
	 */
	private static final String DATABASE_NAME_PROPERTY = "org.astrogrid.policy.server.database.name" ;

	/**
	 * Public constructor.
	 *
	 */
	public CommunityConfigImpl()
		{
		this.init() ;
		}

	/**
	 * Our local community name.
	 *
	 */
	private String communityname ;

	/**
	 * Access to the local community name.
	 * Returns the local host name in this iteration.
	 *
	 */
	public String getCommunityName()
		{
		return this.communityname ;
		}

	/**
	 * The local database name.
	 *
	 */
	private String databasename ;

	/**
	 * Access to the local database name.
	 *
	 */
	public String getDatabaseName()
		{
		return this.databasename ;
		}

	/**
	 * The location of the database config.
	 *
	 */
	private String databaseconfig ;

	/**
	 * Access to the local database config.
	 * Returns the file path to the Castor database.XML config.
	 *
	 */
	public String getDatabaseConfig()
		{
		return this.databaseconfig ;
		}

	/**
	 * Initialise our configuration.
	 *
	 */
	public void init()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ConfigurationManagerImpl.init()") ;
		//
		// Initialise our local host address.
		try {
			communityname = InetAddress.getLocalHost().getHostName() ;
			}
		catch (Exception ouch)
			{
			communityname = "localhost" ;
			}

		//
		// Initialise our database properties.
		this.databasename   = System.getProperty(DATABASE_NAME_PROPERTY)   ;
		this.databaseconfig = System.getProperty(DATABASE_CONFIG_PROPERTY) ;

		if (DEBUG_FLAG) System.out.println("  ----") ;
		if (DEBUG_FLAG) System.out.println("  Community : " + communityname) ;
		if (DEBUG_FLAG) System.out.println("  Database  : " + databasename) ;
		if (DEBUG_FLAG) System.out.println("  Config    : " + databaseconfig) ;
		if (DEBUG_FLAG) System.out.println("  ----") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	}

