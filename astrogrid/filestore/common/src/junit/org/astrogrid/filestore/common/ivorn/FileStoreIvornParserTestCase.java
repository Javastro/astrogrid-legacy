/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/junit/org/astrogrid/filestore/common/ivorn/FileStoreIvornParserTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:20 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreIvornParserTestCase.java,v $
 *   Revision 1.3  2004/11/25 00:19:20  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.2.72.1  2004/11/06 12:17:36  dave
 *   Modified getServiceIdent() to return full ivorn string.
 *
 *   Revision 1.2  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.1.2.4  2004/07/23 03:55:38  dave
 *   Added getServiceIvorn to parser
 *
 *   Revision 1.1.2.3  2004/07/23 03:37:06  dave
 *   Debugged tests and parser bugs
 *
 *   Revision 1.1.2.2  2004/07/23 03:08:37  dave
 *   Updated parser tests
 *
 *   Revision 1.1.2.1  2004/07/23 02:10:58  dave
 *   Added IvornFactory and IvornParser
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.ivorn ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

/**
 * A JUnit test case for the ivorn parser.
 *
 */
public class FileStoreIvornParserTestCase
	extends TestCase
	{

	/**
	 * Check we get the right Exception for a null ivorn.
	 *
	 */
	public void testNullIvorn()
		throws Exception
		{
		try {
			new FileStoreIvornParser(
				(Ivorn) null
				) ;
			}
		catch (FileStoreIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileStoreIdentifierException") ;
		}

	/**
	 * Check we get the right Exception for a null ident.
	 *
	 */
	public void testNullIdent()
		throws Exception
		{
		try {
			new FileStoreIvornParser(
				(String) null
				) ;
			}
		catch (FileStoreIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileStoreIdentifierException") ;
		}

	/**
	 * Check that we get the right service ident.
	 *
	 */
	public void testServiceIdent()
		throws Exception
		{
		FileStoreIvornParser parser = 
			new FileStoreIvornParser(
				"ivo://one.two/three#four"
				) ;
		assertEquals(
			"ivo://one.two/three",
			parser.getServiceIdent()
			) ;
		}

	/**
	 * Check that we get the right service ivorn.
	 *
	 */
	public void testServiceIvorn()
		throws Exception
		{
		FileStoreIvornParser parser = 
			new FileStoreIvornParser(
				"ivo://one.two/three#four"
				) ;
		assertEquals(
			"ivo://one.two/three",
			parser.getServiceIvorn().toString()
			) ;
		}

	/**
	 * Check that we get the right resource ident.
	 *
	 */
	public void testResourceIdent()
		throws Exception
		{
		FileStoreIvornParser parser = 
			new FileStoreIvornParser(
				"ivo://one.two/three#four"
				) ;
		assertEquals(
			"four",
			parser.getResourceIdent()
			) ;
		}

	/**
	 * Check that we can handle no path
	 *
	 */
	public void testNoPath()
		throws Exception
		{
		FileStoreIvornParser parser = 
			new FileStoreIvornParser(
				"ivo://one.two#four"
				) ;
		assertEquals(
			"ivo://one.two",
			parser.getServiceIdent()
			) ;
		assertEquals(
			"four",
			parser.getResourceIdent()
			) ;
		}

	/**
	 * Check that we can handle an empty path
	 *
	 */
	public void testEmptyPath()
		throws Exception
		{
		FileStoreIvornParser parser = 
			new FileStoreIvornParser(
				"ivo://one.two/#four"
				) ;
		assertEquals(
			"ivo://one.two",
			parser.getServiceIdent()
			) ;
		assertEquals(
			"four",
			parser.getResourceIdent()
			) ;
		}


	}
