/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/service/data/ServiceStatusData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/09 01:19:50 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ServiceStatusData.java,v $
 *   Revision 1.5  2004/09/09 01:19:50  dave
 *   Updated MIME type handling in MySpace.
 *   Extended test coverage for MIME types in FileStore and MySpace.
 *   Added VM memory data to community ServiceStatusData.
 *
 *   Revision 1.4.74.1  2004/09/07 04:01:47  dave
 *   Added memory stats ...
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.52.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
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

	/**
	 * The current available memory on the server.
	 *
	 */
	private long freeMemory ;

	/**
	 * Get the current available memory on the server.
	 *
	 */
	public long getFreeMemory()
		{
		return this.freeMemory ;
		}

	/**
	 * Set the current available memory on the server.
	 *
	 */
	public void setFreeMemory(long value)
		{
		this.freeMemory = value ;
		}

	/**
	 * The current total memory on the server.
	 *
	 */
	private long totalMemory ;

	/**
	 * Get the current total memory on the server.
	 *
	 */
	public long getTotalMemory()
		{
		return this.totalMemory ;
		}

	/**
	 * Set the current total memory on the server.
	 *
	 */
	public void setTotalMemory(long value)
		{
		this.totalMemory = value ;
		}

    }
