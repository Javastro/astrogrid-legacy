/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/service/data/ServiceStatusData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ServiceStatusData.java,v $
 *   Revision 1.2  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.1.2.1  2004/02/19 21:09:26  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 * </cvs:log>
 *
 * TODO .. Re-factor this as a Map of properties ?
 *
 *
 */
package org.astrogrid.community.common.service.data ;

public class ServiceStatusData
    {
    /**
     * Our config file location.
     *
     */
    private String configPath ;

    /**
     * Get our config file location.
     *
     */
    public String getConfigPath()
        {
        return this.configPath ;
        }

    /**
     * Set our config file location.
     *
     */
    public void setConfigPath(String value)
        {
        this.configPath = value ;
        }

	/**
	 * Our database name.
	 *
	 */
	private String databaseName ;

	/**
	 * Get our database name.
	 *
	 */
	public String getDatabaseName()
		{
		return this.databaseName ;
		}

	/**
	 * Set our database name.
	 *
	 */
	public void setDatabaseName(String name)
		{
		this.databaseName = name ;
		}


    }
