/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/java/org/astrogrid/portal/test/axis/doc/Attic/TestDocService.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/09 13:33:33 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: TestDocService.java,v $
 * Revision 1.1  2003/06/09 13:33:33  dave
 * Fixed bad directory structure
 *
 * Revision 1.1  2003/06/09 10:20:50  dave
 * Added Axis integration tests
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.test.axis.doc ;

import java.util.Date ;

/**
 * A test implementation of a DOC web service.
 *
 */
public class TestDocService
	{
	/**
	 * Public constructor.
	 *
	 */
	public TestDocService()
		{
		}

	/**
	 * Service method with no params.
	 * @returns void.
	 *
	 */
	public void doNothing()
		{
		}

	/**
	 * Service method with no params.
	 * @returns datestamp as a long.
	 *
	 */
	public long getDateAsLong()
		{
		Date date = new Date() ;
		return date.getTime() ;
		}

	/**
	 * Service method with no params.
	 * @returns datestamp as a Date object.
	 *
	 */
	public Date getDateAsDate()
		{
		Date date = new Date() ;
		return date ;
		}

	/**
	 * Service method with int param.
	 * @returns square of param
	 *
	 */
	public int getIntSquared(int param)
		{
		return (param * param) ;
		}

	}

