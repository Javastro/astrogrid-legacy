/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerStoreNodeMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerStoreNodeMock.java,v $
 *   Revision 1.2  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.1.2.2  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.1.2.1  2005/01/10 21:27:47  dave
 *   Refactores NodeMock as FileManagerStoreNode ...
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
public class FileManagerStoreNodeMock
    implements FileManagerStoreNode
    {

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
    public Collection getChildren()
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
    public void addNode(FileManagerStoreNode node)
        throws DuplicateNodeException
        {
        log.debug("");
        log.debug("FileManagerStoreNode.addNode(Node)");
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
    public void delNode(String name)
        throws NodeNotFoundException
        {
        log.debug("");
        log.debug("FileManagerStoreNode.delNode(String)");
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
    public FileManagerStoreNode getChild(String path)
        throws NodeNotFoundException
        {
        log.debug("");
        log.debug("FileManagerStoreNode.getChild()");
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
    public FileManagerStoreNode getChild(StringTokenizer tokens)
        throws NodeNotFoundException
        {
        log.debug("");
        log.debug("FileManagerStoreNode.getChild(StringTokenizer)");
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
     * Access to our properties.
     *
     */
    public void setProperties(FileProperties properties)
        {
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
