/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/PolicyService.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/08/28 17:33:56 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyService.java,v $
 *   Revision 1.1  2003/08/28 17:33:56  dave
 *   Initial policy prototype
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

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

import java.util.Vector ;
import java.util.Collection ;

import java.io.IOException ;

public class PolicyService
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
	private static final String MAPPING_CONFIG_PROPERTY = "astrogrid.policy.server.mapping" ;

	/**
	 * The name of the system property to read the location of our database config.
	 *
	 */
	private static final String DATABASE_CONFIG_PROPERTY = "astrogrid.policy.server.database.config" ;

	/**
	 * The name of the system property to read our database name from.
	 *
	 */
	private static final String DATABASE_NAME_PROPERTY = "astrogrid.policy.server.database.name" ;

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
	public PolicyService()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyService()") ;
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
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyService.getServiceStatus()") ;

		ServiceData result =  new ServiceData() ;
		result.setIdent("service@localhost") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return result ;
		}

	/**
	 * Request an Account details.
	 *
	 */
	public AccountData getAccountData(String ident)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyService.getAccountData()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		AccountData result = new AccountData("frog@pond", "Frog in a pond") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return result ;
		}

	/**
	 * Update an Account details.
	 *
	 */
	public void setAccountData(AccountData account)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyService.setAccountData()") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Request a list of Accounts.
	 *
	 */
	public Collection getAccountList()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyService.getAccountList()") ;

		Collection collection = new Vector() ;

		collection.add(
			new AccountData("frog@pond", "Frog in a pond")
			) ;
		collection.add(
			new AccountData("toad@pond", "Toad in a pond")
			) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return collection ;
		}

	/**
	 * Confirm access permissions
	 *
	 */
	public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyService.checkPermissions()") ;

		if (DEBUG_FLAG) System.out.println("  Credentials") ;
		if (DEBUG_FLAG) System.out.println("    Group   : " + credentials.getGroup()) ;
		if (DEBUG_FLAG) System.out.println("    Account : " + credentials.getAccount()) ;
		if (DEBUG_FLAG) System.out.println("  Resource") ;
		if (DEBUG_FLAG) System.out.println("    Name    : " + resource) ;
		if (DEBUG_FLAG) System.out.println("    Action  : " + action) ;

		//
		// PolicyPermission result = new PolicyPermission(resource, credentials.getGroup(), action) ;

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

		PolicyPermission result = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Try loading the target object.
			result = (PolicyPermission) database.load(PolicyPermission.class, key);
			}
		catch (Exception ouch)
			{
			if (DEBUG_FLAG) System.out.println("Exception") ;
			if (DEBUG_FLAG) System.out.println(ouch) ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return result ;

		}


	}
