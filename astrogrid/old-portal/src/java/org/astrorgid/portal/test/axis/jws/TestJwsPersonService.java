/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/java/org/astrorgid/portal/test/axis/jws/Attic/TestJwsPersonService.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/05 09:05:56 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: TestJwsPersonService.java,v $
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
public class TestJwsPersonService
	{
	/**
	 * Public constructor.
	 *
	 */
	public TestJwsPersonService() {}

	/**
	 * A Bean class to encapsulate data about a person.
	 *
	 */
	public static class Person
		{
		/**
		 * Public constructor.
		 *
		 */
		public Person() {}

		/**
		 * Our ident.
		 *
		 */
		private int ident ;

		/**
		 * Access to our ident.
		 *
		 */
		public int getIdent()
			{
			return this.ident ;
			}

		/**
		 * Access to our ident.
		 *
		 */
		public void setIdent(int ident)
			{
			this.ident = ident ;
			}

		/**
		 * Our name.
		 *
		 */
		private String name ;

		/**
		 * Access to our name.
		 *
		 */
		public String getName()
			{
			return this.name ;
			}

		/**
		 * Access to our name.
		 *
		 */
		public void setName(String name)
			{
			this.name = name ;
			}

		}

	/**
	 * Service method with Person param.
	 * @returns ident of person
	 *
	 */
	public int getPersonIdent(Person person)
		{
		return person.getIdent() ;
		}

	}

