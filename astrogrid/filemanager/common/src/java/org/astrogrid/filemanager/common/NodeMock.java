/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/NodeMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/12/16 17:25:49 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: NodeMock.java,v $
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.21  2004/12/14 15:48:54  dave
 *   Added delete from filemanager ....
 *
 *   Revision 1.1.2.20  2004/12/06 13:29:02  dave
 *   Added initial code for move location ....
 *
 *   Revision 1.1.2.19  2004/12/02 19:11:54  dave
 *   Added move name and parent to manager ...
 *
 *   Revision 1.1.2.18  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
 *
 *   Revision 1.1.2.17  2004/11/11 17:53:10  dave
 *   Removed Node interface from the server side ....
 *
 *   Revision 1.1.2.16  2004/11/11 16:36:19  dave
 *   Changed getChildren to retunr array of names ...
 *
 *   Revision 1.1.2.15  2004/11/10 17:00:11  dave
 *   Moving the manager API towards property based rather than node based ...
 *
 *   Revision 1.1.2.14  2004/11/06 20:03:17  dave
 *   Implemented ImportInit and ExportInit using properties
 *
 *   Revision 1.1.2.13  2004/11/05 05:58:31  dave
 *   Refactored the properties handling in importInitEx() ..
 *
 *   Revision 1.1.2.12  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.11  2004/11/04 04:15:34  dave
 *   Sketched out the new business logic for import init ....
 *
 *   Revision 1.1.2.10  2004/11/04 02:33:38  dave
 *   Refactored test to include multiple filestores ...
 *
 *   Revision 1.1.2.9  2004/11/02 23:40:08  dave
 *   Fixed typos and bugs ...
 *
 *   Revision 1.1.2.8  2004/11/02 23:21:22  dave
 *   Added FileManagerProperties and filter ...
 *
 *   Revision 1.1.2.7  2004/11/02 15:05:13  dave
 *   Added old path tests back in ...
 *
 *   Revision 1.1.2.6  2004/11/01 16:23:22  dave
 *   Started integrating import and export with FileStore ...
 *
 *   Revision 1.1.2.5  2004/10/21 21:08:52  dave
 *   Added config interface and mock implementation.
 *   Partial implementation of data import into FileStore via direct URL transfer.
 *
 *   Revision 1.1.2.4  2004/10/19 14:52:36  dave
 *   Refactored container and file into just node.
 *
 *   Revision 1.1.2.3  2004/10/13 06:33:17  dave
 *   Refactored exceptions ...
 *   Refactored the container API
 *   Added placeholder file interface ...
 *
 *   Revision 1.1.2.2  2004/10/07 16:04:38  dave
 *   Added exception to addAccount
 *
 *   Revision 1.1.2.1  2004/10/07 14:29:08  dave
 *   Added initial interface and implementations ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import java.util.Map ;
import java.util.HashMap ;
import java.util.Collection ;
import java.util.StringTokenizer ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;

import org.astrogrid.filemanager.common.ivorn.FileManagerIvornParser;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;

/**
 * A mock implementation of the node interface.
 *
 */
public class NodeMock
	{

    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(NodeMock.class);

	/**
	 * Public constructor.
	 * @param parent  The parent node.
	 * @param ivorn   The node ivorn.
	 * @param name    The node name.
	 * @param name    The node type.
	 *
	 */
	public NodeMock(NodeMock parent, String name, String type)
		throws FileManagerIdentifierException
		{
		if (null == name)
			{
			throw new IllegalArgumentException(
				"Null name"
				) ;
			}
		if (null == type)
			{
			throw new IllegalArgumentException(
				"Null type"
				) ;
			}
		//
		// Set our parent ivorn.
		if (null != parent)
			{
			this.setParentIvorn(
				parent.getIvorn()
				);
			}
		//
		// Set the rest of our properties.
		this.setName(name)   ;
		this.setType(type)   ;
		}

	/**
	 * Our internal map of child nodes.
	 *
	 */
	private Map children = new HashMap() ;

	/**
	 * Get an array of the child nodes.
	 *
	 */
	protected Collection getChildren()
		{
		//
		// Convert the map values into an array.
		return children.values() ;
		}

	/**
	 * Add a child node.
	 * @param node The child node to add.
	 * @throws DuplicateNodeException If a node with the same name already exists.
	 *
	 */
	protected void addNode(NodeMock node)
		throws DuplicateNodeException
		{
		log.debug("");
		log.debug("NodeMock.addNode(Node)");
		log.debug("  This : " + this.getName());
		log.debug("  This : " + this.getIdent());
		if (null == node)
			{
			throw new IllegalArgumentException(
				"Null node"
				) ;
			}
		log.debug("  Node : " + node.getName());
		log.debug("  Node : " + node.getIdent());
		//
		// Check if a node already exists.
		if (children.containsKey(node.getName()))
			{
			throw new DuplicateNodeException() ;
			}
		//
		// If the node does not exist yet.
		else {
			//
			// Add the node to our list.
			children.put(
				node.getName(),
				node
				);
			//
			// Set the parent ivorn.
			try {
				node.setParentIvorn(
					this.getIvorn()
					);
				}
			catch (FileManagerIdentifierException ouch)
				{
				log.warn("");
				log.warn("Exception parsing parent ivorn");
				log.warn(ouch);
				}
			}
		}

	/**
	 * Remove a child node.
	 * @param name The name of the child node to remove.
	 * @throws NodeNotFoundException If the node does not exist.
	 *
	 */
	protected void delNode(String name)
		throws NodeNotFoundException
		{
		log.debug("");
		log.debug("NodeMock.delNode(String)");
		log.debug("  This : " + this.getName());
		log.debug("  This : " + this.getIdent());
		log.debug("  Name : " + name);
		if (null == name)
			{
			throw new IllegalArgumentException(
				"Null name"
				) ;
			}
		//
		// Remove the node if it exists.
		if (children.containsKey(name))
			{
			children.remove(
				name
				);
			}
		else {
			throw new NodeNotFoundException() ;
			}
		}

	/**
	 * Get a child node, indexed by path/name.
	 * @param path The target node path.
	 * @return The target node.
	 * @throws NodeNotFoundException if the child node does not exist.
	 *
	 */
	protected NodeMock getChild(String path)
		throws NodeNotFoundException
		{
		log.debug("");
		log.debug("NodeMock.getChild()");
		log.debug("  This : " + this.getName());
		log.debug("  This : " + this.getIdent());
		log.debug("  Path : " + path);
		if (null == path)
			{
			throw new IllegalArgumentException(
				"Null path"
				) ;
			}
		//
		// Split the path on '/'
		StringTokenizer tokens = new StringTokenizer(path, "/") ;
		//
		// Use the StringTokenizer to find the child node(s).
		return this.getChild(
			tokens
			);
		}

	/**
	 * Get a child node, indexed by path/name.
	 * @param tokens A StringTokenizer of the path.
	 * @return The target node.
	 * @throws NodeNotFoundException if the child node does not exist.
	 *
	 */
	protected NodeMock getChild(StringTokenizer tokens)
		throws NodeNotFoundException
		{
		log.debug("");
		log.debug("NodeMock.getChild(StringTokenizer)");
		log.debug("  This  : " + this.getName());
		log.debug("  This  : " + this.getIdent());
		if (null == tokens)
			{
			throw new IllegalArgumentException(
				"Null tokens"
				) ;
			}
		//
		// If we have an initial token.
		if (tokens.hasMoreTokens())
			{
			String name = tokens.nextToken();
			log.debug("  Name : " + name);
			//
			// If we have a node for the first step.
			if (children.containsKey(name))
				{
				NodeMock node = (NodeMock) children.get(name) ;
				//
				// If we have more steps.
				if (tokens.hasMoreTokens())
					{
					return node.getChild(
						tokens
						) ;
					}
				//
				// If we don't have more steps.
				else {
					return node ;
					}
				}
			//
			// If we don't have a matching node.
			else {
				throw new NodeNotFoundException() ;
				}
			}
		//
		// If we don't have an initial token.
		else {
			throw new NodeNotFoundException() ;
			}
		}

	/**
	 * The file properties.
	 *
	 */
	private FileManagerProperties properties = new FileManagerProperties() ;

	/**
	 * Public access to the properties.
	 * @return The array of file properties.
	 * @todo Need to make this a clone to prevent changes.
	 *
	 */
	public FileManagerProperties getProperties()
		{
		return this.properties ;
		}

	/**
	 * Protected access to our properties.
	 *
	 */
	protected void setProperties(FileProperties properties)
		{
		this.setProperties(
			properties.toArray()
			);
		}

	/**
	 * Protected access to our properties.
	 *
	 */
	protected void setProperties(FileProperty[] properties)
		{
		this.properties.merge(
			properties,
			new FileManagerPropertyFilter()
			) ;
		}

	/**
	 * Get the node ivorn.
	 *
	 */
	public Ivorn getIvorn()
		throws FileManagerIdentifierException
		{
		return properties.getManagerResourceIvorn() ;
		}

	/**
	 * Get the node ident.
	 *
	 */
	public String getIdent()
		{
		try {
			return properties.getManagerResourceIdent() ;
			}
		catch (FileManagerIdentifierException ouch)
			{
			log.warn("");
			log.warn("Exception parsing node ivorn");
			log.warn(ouch);
			return null ;
			}
		}

	/**
	 * Set the node ivorn.
	 *
	 */
	protected void setIvorn(Ivorn ivorn)
		{
		this.properties.setManagerResourceIvorn(
			ivorn
			) ;
		}

	/**
	 * Get the parent ivorn.
	 *
	 */
	public Ivorn getParentIvorn()
		{
		try {
			return properties.getManagerParentIvorn();
			}
		catch (FileManagerIdentifierException ouch)
			{
			log.warn("");
			log.warn("Exception parsing parent ivorn");
			log.warn(ouch);
			return null ;
			}
		}

	/**
	 * Set the parent ivorn.
	 *
	 */
	protected void setParentIvorn(Ivorn ivorn)
		{
		this.properties.setManagerParentIvorn(
			ivorn
			) ;
		}

	/**
	 * Get the node name.
	 *
	 */
	public String getName()
		{
		return this.properties.getManagerResourceName() ;
		}

	/**
	 * Set the node name.
	 *
	 */
	protected void setName(String name)
		{
		this.properties.setManagerResourceName(
			name
			) ;
		}

	/**
	 * Get the node type.
	 *
	 */
	public String getType()
		{
		return this.properties.getManagerResourceType() ;
		}

	/**
	 * Set the node type.
	 *
	 */
	protected void setType(String type)
		{
		this.properties.setManagerResourceType(
			type
			) ;
		}

	/**
	 * Check if this node is a container.
	 *
	 */
	public boolean isContainer()
		{
		if (null != this.getType())
			{
			return FileManagerProperties.CONTAINER_NODE_TYPE.equals(
				this.getType()
				);
			}
		else {
			return false ;
			}
		}

	/**
	 * Check if this node is a data node.
	 *
	 */
	public boolean isDataNode()
		{
		if (null != this.getType())
			{
			return FileManagerProperties.DATA_NODE_TYPE.equals(
				this.getType()
				);
			}
		else {
			return false ;
			}
		}

	}
