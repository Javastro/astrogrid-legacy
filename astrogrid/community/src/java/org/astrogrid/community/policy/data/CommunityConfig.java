/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/data/Attic/CommunityConfig.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/08 20:28:50 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityConfig.java,v $
 *   Revision 1.1  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.data ;

/**
 * Configuration data for the community service.
 *
 */
public abstract class CommunityConfig
	{
	/**
	 * A reference to the local config instance.
	 *
	 */
	private static CommunityConfig instance ;

	/**
	 * Access to the local config instance.
	 *
	 */
	public static void setConfig(CommunityConfig config)
		{
		instance = config ;
		}

	/**
	 * Access to the local config instance.
	 *
	 */
	public static CommunityConfig getConfig()
		{
		return instance ;
		}

	/**
	 * Access to the local community name.
	 * Returns the local host name in this iteration.
	 *
	 */
	public abstract String getCommunityName() ;

	/**
	 * Access to the local database name.
	 *
	 */
	public abstract String getDatabaseName() ;

	/**
	 * Access to the local database config.
	 * Returns the file path to the Castor database.XML config.
	 *
	 */
	public abstract String getDatabaseConfig() ;

	}

