/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/DatabaseManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/08 20:28:50 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerImpl.java,v $
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import java.io.IOException ;

import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;

import org.exolab.castor.util.Logger;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;

import org.astrogrid.community.policy.data.CommunityConfig ;

public class DatabaseManagerImpl
	implements DatabaseManager
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * The name of our system property to read the location of our JDO mapping from.
	 *
	 */
	private static final String MAPPING_CONFIG_PROPERTY = "org.astrogrid.policy.server.mapping" ;

	/**
	 * Our log writer.
	 *
	 */
	private Logger logger ;

	/**
	 * Our JDO and XML mapping.
	 *
	 */
	private Mapping mapping ;

	/**
	 * Our JDO engine.
	 *
	 */
	private JDO jdo ;

	/**
	 * Our database connection.
	 *
	 */
	private Database database ;

	/**
	 * Access to our database.
	 *
	 */
	public Database getDatabase()
		{
		return this.database ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public DatabaseManagerImpl()
		{
		//
		// Initialise our database.
		this.init() ;
		}

	/**
	 * Initialise our database.
	 *
	 */
	protected void init()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerImpl.init()") ;

		//
		// Get the local configuration instance.
		CommunityConfig config = CommunityConfig.getConfig() ;

		if (DEBUG_FLAG) System.out.println("    Mapping  : " + System.getProperty(MAPPING_CONFIG_PROPERTY)) ;
		if (DEBUG_FLAG) System.out.println("    Database : " + config.getDatabaseName())   ;
		if (DEBUG_FLAG) System.out.println("    Database : " + config.getDatabaseConfig()) ;

		try {
			//
			// Create our log writer.
			logger = new Logger(System.out).setPrefix("castor");
			//
			// Load our object mapping.
			mapping = new Mapping(getClass().getClassLoader());
			mapping.loadMapping(System.getProperty(MAPPING_CONFIG_PROPERTY));

			//
			// Create our JDO engine.
			jdo = new JDO();
			jdo.setLogWriter(logger);
			jdo.setConfiguration(config.getDatabaseConfig());
			jdo.setDatabaseName(config.getDatabaseName());
			//
			// Create our database connection.
			database = jdo.getDatabase();
			}
// TODO
// Need to do something with these ??
//
		catch(IOException ouch)
			{
			if (DEBUG_FLAG) System.out.println("IOException during initialisation.") ;
			if (DEBUG_FLAG) System.out.println(ouch) ;
			}

		catch(DatabaseNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("DatabaseNotFoundException during initialisation.") ;
			if (DEBUG_FLAG) System.out.println(ouch) ;
			}

		catch(PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("PersistenceException during initialisation.") ;
			if (DEBUG_FLAG) System.out.println(ouch) ;
			}

		catch(MappingException ouch)
			{
			if (DEBUG_FLAG) System.out.println("MappingException during initialisation.") ;
			if (DEBUG_FLAG) System.out.println(ouch) ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Create an object in the database.
	 *
	public void create(Object object)
		throws PersistenceException
		{
		//
		// Try performing our transaction.
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Try creating the object in the database.
			database.create(object);
			//
			// Try comitting the transaction.
			database.commit() ;
			}
		//
		// If anything else bang.
		catch (PersistenceException ouch)
			{
			//
			// Roll back the transaction.
			database.rollback() ;
			//
			// Re-throw the exception.
			throw ouch ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	 */

	/**
	 * Select an object from the database.
	 *
	public Object select(Class type, Object ident)
		throws PersistenceException
		{
		Object result = null ;
		//
		// Try performing our transaction.
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Try loading the object from the database.
			result = database.load(type, object);
			//
			// Try comitting the transaction.
			database.commit() ;
			}
		//
		// If anything went bang.
		catch (PersistenceException ouch)
			{
			//
			// Roll back the transaction.
			database.rollback() ;
			//
			// Set the result to null.
			result = null ;
			//
			// Re-throw the exception.
			throw ouch ;
			}
		return result ;
		}
	 */

	/**
	 * Update an object in the database.
	 *
	public void update(Class type, Object ident)
		throws PersistenceException
		{
		}
	 */

	/**
	 * Delete an object in the database.
	 *
	public void delete(Class type, Object ident)
		throws PersistenceException
		{
		}
	 */

	}
