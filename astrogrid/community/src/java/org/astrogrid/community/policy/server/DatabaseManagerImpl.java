/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/DatabaseManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/06 20:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerImpl.java,v $
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
//import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
//import org.exolab.castor.jdo.DuplicateIdentityException ;
//import org.exolab.castor.jdo.TransactionNotInProgressException ;
//import org.exolab.castor.jdo.ClassNotPersistenceCapableException ;

import org.exolab.castor.util.Logger;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;


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
	 * Our log writer.
	 *
	 */
	private Logger logger = null ;

	/**
	 * Our config files path.
	 *
	 */
	private String config = "" ;

	/**
	 * Our JDO and XML mapping.
	 *
	 */
	private Mapping mapping = null ;

	/**
	 * Our JDO engine.
	 *
	 */
	private JDO jdo = null ;

	/**
	 * Our database connection.
	 *
	 */
	private Database database = null ;

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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerImpl()") ;
		//
		// Initialise our database.
		this.init() ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
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

		if (DEBUG_FLAG) System.out.println("    Mapping  : " + System.getProperty(MAPPING_CONFIG_PROPERTY)) ;
		if (DEBUG_FLAG) System.out.println("    Database : " + System.getProperty(DATABASE_CONFIG_PROPERTY)) ;
		if (DEBUG_FLAG) System.out.println("    Database : " + System.getProperty(DATABASE_NAME_PROPERTY)) ;

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
			jdo.setConfiguration(System.getProperty(DATABASE_CONFIG_PROPERTY));
			jdo.setDatabaseName(System.getProperty(DATABASE_NAME_PROPERTY));
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
	}
