/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerStoreNodeMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/10 14:17:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
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
@modified nww added a clone method, which provides a deep copy.
 */
public class FileManagerStoreNodeMock
    implements FileManagerStoreNode, Cloneable
    {

    /** want to make a deep copy of this object - ensure total independence 
     * found its sufficient to clone the current node, and its collections.
     * the objects contained by these collections can be shared - 
     * this reproduces the errors found in persistent implementation.*/
    public  Object clone() throws CloneNotSupportedException {
        FileManagerStoreNodeMock other = (FileManagerStoreNodeMock)super.clone();
        other.children = new HashMap(this.children);
        other.properties = new FileManagerProperties(this.properties);
        return other;
    }

    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerStoreNodeMock.class);

    /**
     * Public constructor.
     * @param parent  The parent node identifier.
     * @param ivorn   The full ivorn identifier.
     * @param name    The node name.
     * @param name    The node type.
     *
     */
    public FileManagerStoreNodeMock(Ivorn parent, Ivorn ivorn, String name, String type)
        {
        if (null == ivorn)
            {
            throw new IllegalArgumentException(
                "Null identifier"
                ) ;
            }
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
                parent
                );
            }
        //
        // Set the rest of our properties.
        this.setIvorn(ivorn) ;
        this.setName(name)   ;
        this.setType(type)   ;
		//
		// Set our node dates.
		properties.created() ;
        }

	/**
	 * Update our modify date.
	 *
	 */
	protected void modified()
		{
		properties.modified();
		}

    /**
     * Our internal map of child nodes.
     *@todo replace this with a list of references back into store.
     */
    private Map children = new HashMap() ;

    /**
     * Get an array of the child nodes.
     * @todo not so good - we lose control here of the contents. Would like to make this unmodifiable. 
     */
    public Collection getChildren()
        {
        //
        // Convert the map values into an array.
        log.debug("getting children");
        return children.values() ;
        }

    /**
     * Add a child node.
     * @param node The child node to add.
     * @throws DuplicateNodeException If a node with the same name already exists.
     *
     */
    public void addNode(FileManagerStoreNode node)
        throws DuplicateNodeException
        {
        log.debug("FileManagerStoreNode.addNode" + node);
        log.debug("  This : " + this);
        if (null == node)
            {
            throw new IllegalArgumentException(
                "Null node"
                ) ;
            }

        // Check if a node already exists.
        if (children.containsKey(node.getName()))
            {
            throw new DuplicateNodeException(node.getName()) ;
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
                log.warn("Exception parsing parent ivorn",ouch);
                }
            }
		//
		// Update the modified date.
		this.modified();
        }

    /**
     * Remove a child node.
     * @param name The name of the child node to remove.
     * @throws NodeNotFoundException If the node does not exist.
     *
     */
    public void delNode(String name)
        throws NodeNotFoundException
        {
        log.debug("FileManagerStoreNode.delNode(" + name +")");
        log.debug("  This : " + this);
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
            throw new NodeNotFoundException(name) ;
            }
		//
		// Update the modified date.
		this.modified();
        }

    /**
     * Get a child node, indexed by path/name.
     * @param path The target node path.
     * @return The target node.
     * @throws NodeNotFoundException if the child node does not exist.
     *
     */
    public FileManagerStoreNode getChild(String path)
        throws NodeNotFoundException
        {
        log.debug("FileManagerStoreNode.getChild(" + path +")");
        log.debug("  This : " + this);
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
    public FileManagerStoreNode getChild(StringTokenizer tokens)
        throws NodeNotFoundException
        {
        log.debug("FileManagerStoreNode.getChild(StringTokenizer)");
        log.debug("  This  : " + this);
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
                FileManagerStoreNode node = (FileManagerStoreNode) children.get(name) ;
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
                log.debug("Not found " + name);
                throw new NodeNotFoundException(name) ;
                }
            }
        //
        // If we don't have an initial token.
        else {
            throw new NodeNotFoundException("no initial token") ;
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
     * Access to our properties.
     *
     */
    public void setProperties(FileProperties properties)
        {
        log.debug("Calling set properties");
        this.setProperties(
            properties.toArray()
            );
        }

    /**
     * Access to our properties.
     *
     */
    public void setProperties(FileProperty[] properties)
        {
        log.debug("Calling set properties");
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
            log.warn("Exception parsing node ivorn",ouch);
            return null ;
            }
        }

    /**
     * Set the node ivorn.
     *
     */
    protected void setIvorn(Ivorn ivorn)
        {
        log.debug("setting ivorn");
        this.properties.setManagerResourceIvorn(
            ivorn
            ) ;
        }

    /**
     * Get the parent ivorn.
     *
     */
    public Ivorn getParentIvorn()
        throws FileManagerIdentifierException
        {
        return properties.getManagerParentIvorn();
        }

    /**
     * Set the parent ivorn.
     *
     */
    public void setParentIvorn(Ivorn ivorn)
        {
        log.debug("setting parent ivorn");
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
    public void setName(String name)
        {
        log.debug("setting name");
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
    public void setType(String type)
        {
        log.debug("setting type" );
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


    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[name: ");
        buffer.append(getName());
        buffer.append(" ident :");
        buffer.append(getIdent());
        buffer.append(" children: ");
        buffer.append(children.size());
        buffer.append("]");
        return buffer.toString();
    }
    }
