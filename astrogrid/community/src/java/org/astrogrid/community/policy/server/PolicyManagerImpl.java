/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/PolicyManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/04 23:33:05 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerImpl.java,v $
 *   Revision 1.3  2003/09/04 23:33:05  dave
 *   Implemented the core account manager methods - needs data object to return results
 *
 *   Revision 1.2  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
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
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.DuplicateIdentityException ;
import org.exolab.castor.jdo.TransactionNotInProgressException ;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException ;

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
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.init()") ;

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
		result.setIdent("localhost") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return result ;
		}

	/**
	 * Create a new Account.
	 * TODO Change this to only accept the account name.
	 *
	 */
	public AccountData addAccount(AccountData account)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.addAccount()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + account.getIdent()) ;

		//
		// Check that the ident is valid.
		//

		//
		// Try performing our transaction.
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Try creating the account in the database.
			database.create(account);
			}
		//
		// If we already have an object with that ident.
		catch (DuplicateIdentityException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("DuplicateIdentityException in addAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in addAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != account)
					{
					database.commit() ;
					}
				else {
					database.rollback() ;
					}
				}
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in addAccount() finally clause") ;

				//
				// Set the response to null.
				account = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		// TODO
		// Need to return something to the client.
		// Possible a new DataObject ... AccountResult ?
		//

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

	/**
	 * Request an Account details.
	 *
	 */
	public AccountData getAccount(String ident)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getAccount()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		AccountData account = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Load the Account from the database.
			account = (AccountData) database.load(AccountData.class, ident) ;
			}
		//
		// If we couldn't find the object.
		catch (ObjectNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in getAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in getAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != account)
					{
					database.commit() ;
					}
				else {
					database.rollback() ;
					}
				}
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in getAccount() finally clause") ;

				//
				// Set the response to null.
				account = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

	/**
	 * Update an Account details.
	 *
	 */
	public AccountData setAccount(AccountData account)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.setAccount()") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		//
		// Try update the database.
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Load the Account from the database.
			AccountData data = (AccountData) database.load(AccountData.class, account.getIdent()) ;
			//
			// Update the account data.
			data.setDescription(account.getDescription()) ;
			}
		//
		// If we couldn't find the object.
		catch (ObjectNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in setAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in setAccount()") ;

			//
			// Set the response to null.
			account = null ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != account)
					{
					database.commit() ;
					}
				else {
					database.rollback() ;
					}
				}
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in setAccount() finally clause") ;

				//
				// Set the response to null.
				account = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

	/**
	 * Delete an Account.
	 *
	 */
	public void delAccount(String ident)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.delAccount()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		//
		// Try update the database.
		AccountData account = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Load the Account from the database.
			account = (AccountData) database.load(AccountData.class, ident) ;
			//
			// Delete the account.
			database.remove(account) ;
			}
		//
		// If we couldn't find the object.
		catch (ObjectNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in delAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in delAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != account)
					{
					database.commit() ;
					}
				else {
					database.rollback() ;
					}
				}
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in delAccount() finally clause") ;

				//
				// Set the response to null.
				account = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

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





// ===========================





	}
