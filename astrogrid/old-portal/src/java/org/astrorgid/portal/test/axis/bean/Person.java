/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/java/org/astrorgid/portal/test/axis/bean/Attic/Person.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/09 10:20:50 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: Person.java,v $
 * Revision 1.1  2003/06/09 10:20:50  dave
 * Added Axis integration tests
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.test.axis.bean ;

import java.util.Date ;

/**
 * A Bean class to encapsulate data about a person.
 *
 */
public class Person
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

