/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/mock/myspace/service/Attic/MySpaceService.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/12 18:19:38 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: MySpaceService.java,v $
 * Revision 1.1  2003/06/12 18:19:38  dave
 * Initial import into cvs ...
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.mock.myspace.service ;

import java.util.Map ;
import java.util.Vector ;
import java.util.Iterator ;
import java.util.Hashtable ;
import java.util.Collection ;

/**
 * A mock service to emulate the MySpace server.
 * Based on the current MySpace code in CVS.
 *
 */
public class MySpaceService
	{
	/**
	 * Public constructor.
	 *
	 */
	public MySpaceService()
		{
		//
		// Initialise with fake data.
		init() ;
		}

	/**
	 * WebService test method.
	 *
	 */
	public void ping()
		{
		System.out.println("MySpaceService : ping()") ;
		}

	/**
	 * Our internal map of items.
	 *
	 */
	protected Map items = new Hashtable() ;

	/**
	 * Add a data item to our list.
	 *
	 */
	public void addItem(MySpaceItem item)
		{
		items.put(item.getIdent(), item) ;
		}

	/**
	 * Find a specific item in our list.
	 *
	 */
	public MySpaceItem getItem(String ident)
		{
		return (MySpaceItem) items.get(ident) ;
		}

	/**
	 * Find items matching a query.
	 *
	 */
	public Collection findItems(String path)
		{
		Vector results = new Vector() ;
		Iterator iter = items.values().iterator() ;
		while (iter.hasNext())
			{
			MySpaceItem item = (MySpaceItem) iter.next() ;
			if (item.getPath().startsWith(path))
				{
				results.add(item) ;
				}
			}
		return results ;
		}

	/**
	 * Initialise with fake data.
	 *
	 */
	public void init()
		{
		addItem(
			new MySpaceItem(
				"0000",
				"zero",
				"/var/data/2003/04/"
				)
			) ;

		addItem(
			new MySpaceItem(
				"0001",
				"one",
				"/var/data/2003/05/"
				)
			) ;

		addItem(
			new MySpaceItem(
				"0002",
				"two",
				"/var/data/2004/04/"
				)
			) ;

		addItem(
			new MySpaceItem(
				"0003",
				"three",
				"/var/data/2004/05/"
				)
			) ;
		}
	}
