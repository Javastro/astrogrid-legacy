/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/PolicyManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/03 06:39:13 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerImpl.java,v $
 *   Revision 1.1  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
 *   Revision 1.1  2003/08/28 17:33:56  dave
 *   Initial policy prototype
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
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.PolicyPermission  ;
import org.astrogrid.community.policy.data.PolicyCredentials ;

public class PolicyManagerImpl
	implements PolicyManager
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
	public PolicyManagerImpl()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl()") ;
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
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getServiceStatus()") ;

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
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getAccountData()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		AccountData account = new AccountData() ;
		account.setIdent("frog@pond") ;
		account.setDescription("Frog in a pond") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

	/**
	 * Update an Account details.
	 *
	 */
	public void setAccountData(AccountData account)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.setAccountData()") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Request a list of Accounts.
	 *
	 */
	public Object[] getAccountList()
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getAccountList()") ;

		Collection collection = new Vector() ;

		AccountData frog = new AccountData() ;
		frog.setIdent("frog@pond") ;
		frog.setDescription("Frog in a pond") ;

		AccountData toad = new AccountData() ;
		toad.setIdent("toad@pond") ;
		toad.setDescription("Toad in a pond") ;

		collection.add(frog) ;
		collection.add(toad) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return collection.toArray() ;
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
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.checkPermissions()") ;

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
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.checkMembership()") ;

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
