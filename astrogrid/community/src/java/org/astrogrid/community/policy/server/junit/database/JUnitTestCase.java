/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/junit/database/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/04 23:33:05 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitTestCase.java,v $
 *   Revision 1.1  2003/09/04 23:33:05  dave
 *   Implemented the core account manager methods - needs data object to return results
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server.junit.database ;

import junit.framework.TestCase ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;

import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.ObjectNotFoundException ;

import org.exolab.castor.util.Logger;
import org.exolab.castor.persist.spi.Complex ;
import org.exolab.castor.mapping.Mapping;

import java.io.PrintWriter;

import java.util.Iterator ;
import java.util.Collection ;

/**
 *
 * JUnit test for the policy server components.
 *
 */
public class JUnitTestCase
	extends TestCase
	{
	/**
	 * Our test account ident.
	 *
	 */
	private static final String TEST_ACCOUNT_IDENT = "server.database@junit" ;

	/**
	 * Our test account description.
	 *
	 */
	private static final String TEST_ACCOUNT_DESC = "JUnit test account" ;

	/**
	 * Switch for our debug statements.
	 *
	 */
	private static final boolean DEBUG_FLAG = true ;

	/**
	 * Switch for our assert statements.
	 *
	 */
	private static final boolean ASSERT_FLAG = false ;

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
	 * Setup our tests.
	 *
	 */
	protected void setUp()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("setUp()") ;

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

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can create an Account object.
	 *
	 */
	public void testCreateAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCreateAccount()") ;

		//
		// Create the Account object.
		AccountData account = new AccountData() ;
		account.setIdent(TEST_ACCOUNT_IDENT) ;
		account.setDescription(TEST_ACCOUNT_DESC) ;

		//
		// Begin a new database transaction.
		database.begin();

		//
		// Try creating the Account in the database.
		database.create(account);

		//
		// Commit our transaction.
		database.commit() ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can load an Account object.
	 *
	 */
	public void testLoadAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLoadAccount()") ;

		//
		// Begin a new database transaction.
		database.begin();
		//
		// Load the Account from the database.
		AccountData account = (AccountData) database.load(AccountData.class, TEST_ACCOUNT_IDENT) ;
		assertNotNull("Null account data", account) ;
		//
		// Commit our transaction.
		database.commit() ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can modify an Account object.
	 *
	 */
	public void testModifyAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testModifyAccount()") ;

		//
		// Begin a new database transaction.
		database.begin();
		//
		// Load the Account from the database.
		AccountData data = (AccountData) database.load(AccountData.class, TEST_ACCOUNT_IDENT) ;
		//
		// Update the account data.
		data.setDescription("Modified description") ;
		//
		// Commit our transaction.
		database.commit() ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + data.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + data.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can delete an Account object.
	 *
	 */
	public void testDeleteAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDeleteAccount()") ;

		//
		// Begin a new database transaction.
		database.begin();
		//
		// Load the Account from the database.
		AccountData account = (AccountData) database.load(AccountData.class, TEST_ACCOUNT_IDENT) ;
		//
		// Delete the account.
		database.remove(account) ;
		//
		// Commit our transaction.
		database.commit() ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}






	/**
	 * Check we can find a permissions object.
	 *
	public void testFindPermission()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testFindPermission") ;

		//
		// Begin a new database transaction.
		database.begin();
		//
		// Create our OQL query.
		OQLQuery query = database.getOQLQuery(
			"SELECT permissions FROM org.astrogrid.community.policy.server.PolicyPermission permissions WHERE permissions.resource = $1 AND permissions.group = $2 AND permissions.action = $3"
			);
		//
		// Bind our query params.
		query.bind("joderell:database/table/field") ;
		query.bind("solar@mssl") ;
		query.bind("UPDATE") ;
		//
		// Execute our query.
		QueryResults results = query.execute();
		//
		// Iterate through our results.
		if (results.hasMore())
			{
			PolicyPermission result = (PolicyPermission) results.next();
			if (DEBUG_FLAG) System.out.println("  Got permission !!") ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;

		}
	 */

	/**
	 * Check we can load a permissions object.
	 *
	public void testLoadPermissionFail()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLoadPermissionFail") ;

		//
		// Begin a new database transaction.
		database.begin();
		//
		// Create the complex key.
		Complex key = new Complex(
			new Object[]
				{
				"joderell:database/table/field",
				"solar@mssl",
				"UPDATE"
				}
			) ;
		//
		// Try loading the target object.
		try {
			PolicyPermission result = (PolicyPermission) database.load(PolicyPermission.class, key);
			}
		catch (ObjectNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("Permission not found") ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	 */

	/**
	 * Check we can create a permissions object.
	 *
	public void testCreatePermission()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCreatePermission") ;

		//
		// Begin a new database transaction.
		database.begin();
		//
		// Create the Permission object.
		PolicyPermission permission = new PolicyPermission() ;
		permission.setResource("joderell:database/table/field") ;
		permission.setGroup("solar@mssl") ;
		permission.setAction("UPDATE") ;
		//
		// Try creating the permission in the database.
		database.create(permission);
		//
		// Commit our transaction.
		database.commit() ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	 */

	/**
	 * Check we can load a permissions object.
	 *
	public void testLoadPermissionPass()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLoadPermissionPass") ;

		//
		// Begin a new database transaction.
		database.begin();
		//
		// Create the complex key.
		Complex key = new Complex(
			new Object[]
				{
				"joderell:database/table",
				"solar@mssl",
				"UPDATE"
				}
			) ;
		//
		// Try loading the target object.
		PolicyPermission permission = (PolicyPermission) database.load(PolicyPermission.class, key);

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	 */


	}
