/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/data/Attic/DataNode.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 04:03:41 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: DataNode.java,v $
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.data ;

import java.util.Map ;
import java.util.TreeMap ;
import java.util.Iterator ;

/**
 * Class to encapsulate a node in the data tree.
 *
 */
public class DataNode
	{
	/**
	 * Public constructor.
	 *
	 */
	public DataNode()
		{
		this("", "") ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public DataNode(String name)
		{
		this("", name) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public DataNode(String path, String name)
		{
		this.path = path ;
		this.name = name ;
		}

	/**
	 * Our node name.
	 *
	 */
	private String name = "" ;

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

	/**
	 * Our ident.
	 *
	 */
	private String ident = "" ;

	/**
	 * Access to our ident.
	 *
	 */
	public String getIdent()
		{
		return this.ident ;
		}

	/**
	 * Access to our ident.
	 *
	 */
	public void setIdent(String ident)
		{
		this.ident = ident ;
		}

	/**
	 * Our node path.
	 *
	 */
	private String path = "" ;

	/**
	 * Access to our path.
	 *
	 */
	public String getPath()
		{
		return this.path ;
		}

	/**
	 * Access to our path.
	 *
	 */
	public void setPath(String path)
		{
		this.path = path ;
		}

	/**
	 * Our node type.
	 * Defaults to "1" for a container.
	 *
	 */
	private String type = "1" ;

	/**
	 * Access to our type.
	 *
	 */
	public String getType()
		{
		return this.type ;
		}

	/**
	 * Access to our type.
	 *
	 */
	public void setType(String type)
		{
		this.type = type ;
		}

	/**
	 * Our Map of child elements.
	 *
	 */
	protected Map map = new TreeMap() ;

	/**
	 * Find a child.
	 *
	 */
	public DataNode getChild(String name)
		{
		return (DataNode) map.get(name) ;
		}

	/**
	 * Add a child.
	 *
	 */
	public void addChild(DataNode child)
		{
		map.put(child.getName(), child) ;
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
