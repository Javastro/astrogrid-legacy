/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/PolicyServiceImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/03 15:23:33 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceImpl.java,v $
 *   Revision 1.1  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import java.io.IOException ;
import java.rmi.RemoteException ;

import java.util.Vector ;
import java.util.Collection ;

import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException ;
import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DatabaseNotFoundException;

import org.exolab.castor.util.Logger;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;

import org.exolab.castor.persist.spi.Complex ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.PolicyPermission  ;
import org.astrogrid.community.policy.data.PolicyCredentials ;

public class PolicyServiceImpl
	implements PolicyService
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
	 * Public constructor.
	 *
	 */
	public PolicyServiceImpl()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyServiceImpl()") ;
		//
		// Initialise our service.
		this.init() ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Initialise our service.
	 *
	 */
	protected void init()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("init") ;

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
	 * Service health check.
	 *
	 */
	public ServiceData getServiceStatus()
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyServiceImpl.getServiceStatus()") ;

		ServiceData result =  new ServiceData() ;
		result.setIdent("service@localhost") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return result ;
		}

	/**
	 * Confirm access permissions
	 *
	 */
	public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyServiceImpl.checkPermissions()") ;

		if (DEBUG_FLAG) System.out.println("  Credentials") ;
		if (DEBUG_FLAG) System.out.println("    Group   : " + credentials.getGroup()) ;
		if (DEBUG_FLAG) System.out.println("    Account : " + credentials.getAccount()) ;
		if (DEBUG_FLAG) System.out.println("  Resource") ;
		if (DEBUG_FLAG) System.out.println("    Name    : " + resource) ;
		if (DEBUG_FLAG) System.out.println("    Action  : " + action) ;

		//
		// Create the complex key.
		Complex key = new Complex(
			new Object[]
				{
				resource,
				credentials.getGroup(),
				action
				}
			) ;

		//
		// Try loading the permission.
		PolicyPermission permission = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Try loading the target object.
			permission = (PolicyPermission) database.load(PolicyPermission.class, key);
			}
		catch (Exception ouch)
			{
			if (DEBUG_FLAG) System.out.println("Exception") ;
			if (DEBUG_FLAG) System.out.println(ouch) ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return permission ;

		}

	/**
	 * Confirm group membership.
	 *
	 */
	public PolicyCredentials checkMembership(PolicyCredentials credentials)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyServiceImpl.checkMembership()") ;

		if (DEBUG_FLAG) System.out.println("  Credentials") ;
		if (DEBUG_FLAG) System.out.println("    Group   : " + credentials.getGroup()) ;
		if (DEBUG_FLAG) System.out.println("    Account : " + credentials.getAccount()) ;

		//
		// Test code ... yes.
		credentials.setStatus(0xFF) ;
		credentials.setReason("Test check, always returns true") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return credentials ;

		}
	}
