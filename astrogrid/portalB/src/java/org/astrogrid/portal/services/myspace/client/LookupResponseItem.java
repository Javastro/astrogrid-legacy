/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/Attic/LookupResponseItem.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/18 23:28:23 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: LookupResponseItem.java,v $
 * Revision 1.1  2003/06/18 23:28:23  dave
 * Added LookupResponseItem to LookupResponseParser
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client ;

import java.util.Map ;
import java.util.Iterator ;
import java.util.Hashtable ;
import java.util.TreeMap ;

/**
 * Class to encapsulate a LookupResponseItem from MySpace.
 *
 */
public class LookupResponseItem
	{
	/**
	 * Public constructor.
	 *
	 */
	public LookupResponseItem(String name)
		{
		this.name = name ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public LookupResponseItem(String name, DataItemRecord data)
		{
		this.name = name ;
		this.data = data ;
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
	 * Our data item.
	 *
	 */
	private DataItemRecord data ;

	/**
	 * Access to our item.
	 *
	 */
	public DataItemRecord getData()
		{
		return this.data ;
		}

	/**
	 * Our parent.
	 *
	 */
	protected LookupResponseItem parent ;

	/**
	 * Access to our parent.
	 *
	 */
	public LookupResponseItem getParent()
		{
		return this.parent ;
		}

	/**
	 * Access to our parent.
	 *
	 */
	protected void setParent(LookupResponseItem parent)
		{
		this.parent = parent ;
		}

	/**
	 * Our Map of child elements.
	 *
	 */
	//protected Map map = new Hashtable() ;
	protected Map map = new TreeMap() ;
	/**
	 * Find a child.
	 *
	 */
	public LookupResponseItem getChild(String name)
		{
		return (LookupResponseItem) map.get(name) ;
		}

	/**
	 * Add a child.
	 *
	 */
	public void addChild(String name, LookupResponseItem child)
		{
		map.put(name, child) ;
		}

	/**
	 * Acces to our child items.
	 *
	 */
	public Iterator iterator()
		{
		return map.values().iterator() ;
		}
	}
