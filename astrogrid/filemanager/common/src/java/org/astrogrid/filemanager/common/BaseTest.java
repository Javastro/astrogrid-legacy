/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/BaseTest.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: BaseTest.java,v $
 *   Revision 1.2  2004/11/25 00:20:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.3  2004/11/16 03:25:37  dave
 *   Updated API to use full ivorn rather than ident ...
 *
 *   Revision 1.1.2.2  2004/11/13 01:39:03  dave
 *   Modifications to support the new client API ...
 *
 *   Revision 1.1.2.1  2004/10/13 06:33:17  dave
 *   Refactored exceptions ...
 *   Refactored the container API
 *   Added placeholder file interface ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import junit.framework.TestCase ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

/**
 * A common utility base class for file manager tests.
 *
 */
public class BaseTest
	extends TestCase
	{
    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(BaseTest.class);

	/**
	 * Test properties prefix.
	 *
	 */
	public static final String TEST_PROPERTY_PREFIX = "org.astrogrid.filemanager.test" ;

	/**
	 * Helper method to get a local property.
	 *
	 */
	public String getTestProperty(String name)
		{
		return System.getProperty(TEST_PROPERTY_PREFIX + "." + name) ;
		}

	/**
	 * A test string.
	 * "A short test string ...."
	 *
	 */
	public static final String TEST_STRING = "A short test string ...." ;

	/**
	 * A test string.
	 * " plus a bit more ...."
	 *
	 */
	public static final String EXTRA_STRING = " plus a bit more ...." ;

	/**
	 * A test byte array.
	 * "A short byte array ...."
	 *
	 */
	public static final byte[] TEST_BYTES = {
		0x41,
		0x20,
		0x73,
		0x68,
		0x6f,
		0x72,
		0x74,
		0x20,
		0x62,
		0x79,
		0x74,
		0x65,
		0x20,
		0x61,
		0x72,
		0x72,
		0x61,
		0x79,
		0x20,
		0x2e,
		0x2e,
		0x2e,
		0x2e
		} ;

	/**
	 * A test byte array.
	 * " plus a few more ...."
	 *
	 */
	public static final byte[] EXTRA_BYTES = {
		0x20,
		0x70,
		0x6c,
		0x75,
		0x73,
		0x20,
		0x61,
		0x20,
		0x66,
		0x65,
		0x77,
		0x20,
		0x6d,
		0x6f,
		0x72,
		0x65,
		0x20,
		0x2e,
		0x2e,
		0x2e,
		0x2e
		} ;

	/**
	 * Debug utility to log an array of bytes.
	 *
	 */
	public static void printBytes(byte[] data)
		{
		log.debug("--------") ;
		for (int i = 0 ; i < data.length ; i++)
			{
			log.debug(
				"[" + i + "] '" + Integer.toHexString(data[i]) + "'"
				) ;
			}
		log.debug("--------") ;
		}

	/**
	 * Test utility to compare two arrays of bytes.
	 *
	 */
	public static void assertEquals(byte[] left, byte[] right)
		{
		log.debug("--------") ;
		assertEquals(
			"Different array length",
			left.length,
			right.length
			) ;
		for (int i = 0 ; i < left.length ; i++)
			{
			log.debug(
				"[" + i + "] " + Integer.toHexString(left[i]) + ":" + Integer.toHexString(right[i])
				) ;
			assertEquals(
				"Wrong value for byte[" + i + "]",
				left[i],
				right[i]
				) ;
			}
		log.debug("--------") ;
		}

	/**
	 * Our test account ivorn.
	 *
	 */
	protected Ivorn accountIvorn ;

	/**
	 * Our test account name.
	 *
	 */
	protected String accountName ;

	/**
	 * Our test account ivorn.
	 *
	 */
	protected Ivorn unknownIvorn ;

	/**
	 * Our test account name.
	 *
	 */
	protected String unknownName ;

	/**
	 * Setup our test.
	 * This may need to be changed for the integration tests.
	 *
	 */
	public void setUp()
		throws Exception
		{
		log.debug("");
		log.debug("BaseTest.setUp()");
		//
		// Create our test accounts.
		accountIvorn = CommunityAccountIvornFactory.createLocal(
			"test-" +
			String.valueOf(
				System.currentTimeMillis()
				)
			) ;
		unknownIvorn = CommunityAccountIvornFactory.createLocal(
			"unknown-" +
			String.valueOf(
				System.currentTimeMillis()
				)
			) ;
		//
		// Create our test account names.
		accountName = accountIvorn.toString() ;
		unknownName = unknownIvorn.toString() ;
		}
	}
