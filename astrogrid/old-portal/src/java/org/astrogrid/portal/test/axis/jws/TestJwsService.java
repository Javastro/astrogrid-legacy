/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/java/org/astrogrid/portal/test/axis/jws/Attic/TestJwsService.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/09 13:33:33 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: TestJwsService.java,v $
 * Revision 1.1  2003/06/09 13:33:33  dave
 * Fixed bad directory structure
 *
 * Revision 1.1  2003/06/05 09:05:56  dave
 * Added JWS and RPC WebService tests.
 *
 * <cvs:log>
 *
 *
 */
//
// Putting our class in a package breaks the JWS mechanism.
// package org.astrogrid.portal.test.axis.jws ;

import java.util.Date ;

/**
 * A test implementation of a RPC web service.
 * This service is intended to be deployed as a JWS file.
 *
 */
public class TestJwsService
	{
	/**
	 * Public constructor.
	 *
	 */
	public TestJwsService()
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

