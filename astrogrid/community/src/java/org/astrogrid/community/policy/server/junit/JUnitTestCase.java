/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/junit/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/03 06:39:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitTestCase.java,v $
 *   Revision 1.2  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
 *   Revision 1.1  2003/08/28 17:33:56  dave
 *   Initial policy prototype
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server.junit ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.PolicyPermission  ;
import org.astrogrid.community.policy.data.PolicyCredentials ;

import junit.framework.TestCase ;

import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.ObjectNotFoundException ;

import org.exolab.castor.persist.spi.Complex ;


import org.exolab.castor.mapping.Mapping;

import org.exolab.castor.util.Logger;

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
		if (DEBUG_FLAG) System.out.println("setUp") ;

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
	 * Check we can find a permissions object.
	 *
	 */
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

	/**
	 * Check we can load a permissions object.
	 *
	 */
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

	/**
	 * Check we can create a permissions object.
	 *
	 */
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

	/**
	 * Check we can load a permissions object.
	 *
	 */
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
	}
