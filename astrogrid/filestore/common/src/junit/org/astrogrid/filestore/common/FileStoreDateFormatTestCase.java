/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/junit/org/astrogrid/filestore/common/FileStoreDateFormatTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreDateFormatTestCase.java,v $
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.1  2005/01/14 21:01:33  dave
 *   Added FileStoreDateFormat utility to handle dates in ISO format ....
 *   Fixed the file:/// problem on Unix ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common ;

import java.util.Date ;

import junit.framework.TestCase ;

/**
 * Junit test case for the ISO date formatter.
 *
 */
public class FileStoreDateFormatTestCase
	extends TestCase
	{
	/**
	 * Test that we can format a date.
	 *
	 */
	public void testFormatDate()
		throws Exception
		{
		Date date = new Date();
		FileStoreDateFormat formatter = new FileStoreDateFormat();
		String string = formatter.format(
			date
			);
		assertNotNull(
			string
			);
System.out.println("");
System.out.println("String : " + string);
System.out.println("");
		}

	/**
	 * Test that we can format a date and parse the result..
	 *
	 */
	public void testParseDate()
		throws Exception
		{
		Date original = new Date();
		FileStoreDateFormat formatter = new FileStoreDateFormat();
		String string = formatter.format(
			original
			);
		assertNotNull(
			string
			);
		Date result = formatter.parse(
			string
			);
System.out.println("");
System.out.println("Date   : " + original.toString());
System.out.println("String : " + string);
System.out.println("Result : " + result.toString());
System.out.println("");
		assertEquals(
			original,
			result
			);
		}

	}
