/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/junit/org/astrogrid/filestore/common/ivorn/FileStoreIvornFactoryTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/23 09:11:16 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreIvornFactoryTestCase.java,v $
 *   Revision 1.2  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/23 02:10:58  dave
 *   Added IvornFactory and IvornParser
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.ivorn ;

import junit.framework.TestCase ;

import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

/**
 * A JUnit test case for the ivorn factory.
 *
 */
public class FileStoreIvornFactoryTestCase
	extends TestCase
	{

	/**
	 * Check we get the right Exception for a null service.
	 *
	 */
	public void testNullService()
		throws Exception
		{
		try {
			FileStoreIvornFactory.createIdent(
				null,
				"anything"
				) ;
			}
		catch (FileStoreIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileStoreIdentifierException") ;
		}

	/**
	 * Check we get the right Exception for a null resource.
	 *
	 */
	public void testNullResource()
		throws Exception
		{
		try {
			FileStoreIvornFactory.createIdent(
				"anything",
				null
				) ;
			}
		catch (FileStoreIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileStoreIdentifierException") ;
		}

	/**
	 * Check we can cope with a service name.
	 *
	 */
	public void testServiceName()
		throws Exception
		{
		assertEquals(
			"ivo://one.two/three#four",
			FileStoreIvornFactory.createIdent(
				"one.two/three",
				"four"
				)
			) ;
		}

	/**
	 * Check we can cope with a service ivorn.
	 *
	 */
	public void testServiceIvorn()
		throws Exception
		{
		assertEquals(
			"ivo://one.two/three#four",
			FileStoreIvornFactory.createIdent(
				"ivo://one.two/three",
				"four"
				)
			) ;
		}
	}
