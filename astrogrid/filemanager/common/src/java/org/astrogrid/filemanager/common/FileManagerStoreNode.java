/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerStoreNode.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerStoreNode.java,v $
 *   Revision 1.3  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.2.2.1  2005/01/15 08:25:20  dave
 *   Added file create and modify dates to manager and client API ...
 *
 *   Revision 1.2  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.1.2.2  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.1.2.1  2005/01/10 21:27:47  dave
 *   Refactores NodeMock as FileManagerStoreNode ...
 *
 *   Revision 1.1.2.1  2005/01/10 15:36:28  dave
 *   Refactored store into a separate interface and mock impl ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import java.util.Collection;
import java.util.StringTokenizer ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException ;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;

/*
 * Public interface for a FileManager store node.
 *
 */
public interface FileManagerStoreNode
    {

    /**
     * Get a Collection of the child nodes.
     *
     */
    public Collection getChildren();

    /**
     * Add a child node.
     * @param node The child node to add.
     * @throws DuplicateNodeException If a node with the same name already exists.
     *
     */
    public void addNode(FileManagerStoreNode node)
        throws DuplicateNodeException ;

    /**
     * Remove a child node.
     * @param name The name of the child node to remove.
     * @throws NodeNotFoundException If the node does not exist.
     *
     */
    public void delNode(String name)
        throws NodeNotFoundException ;

    /**
     * Get a child node, indexed by path/name.
     * @param path The target node path.
     * @return The target node.
     * @throws NodeNotFoundException if the child node does not exist.
     *
     */
    public FileManagerStoreNode getChild(String path)
        throws NodeNotFoundException ;

    /**
     * Get a child node, indexed by path/name.
     * @param tokens A StringTokenizer of the path.
     * @return The target node.
     * @throws NodeNotFoundException if the child node does not exist.
     *
     */
    public FileManagerStoreNode getChild(StringTokenizer tokens)
        throws NodeNotFoundException;

    /**
     * Public access to the properties.
     * @return The array of file properties.
     * @todo Need to make this a clone to prevent changes.
     *
     */
    public FileManagerProperties getProperties();

    /**
     * Access to our properties.
     *
     */
    public void setProperties(FileProperties properties);

    /**
     * Access to our properties.
     *
     */
    public void setProperties(FileProperty[] properties);

    /**
     * Get the node ivorn.
     *
     */
    public Ivorn getIvorn()
        throws FileManagerIdentifierException;

    /**
     * Get the node ident.
     *
     */
    public String getIdent();

    /**
     * Get the parent ivorn.
     *
     */
    public Ivorn getParentIvorn()
        throws FileManagerIdentifierException;

    /**
     * Set the parent ivorn.
     *
     */
    public void setParentIvorn(Ivorn ivorn);

    /**
     * Get the node name.
     *
     */
    public String getName();

    /**
     * Set the node name.
     *
     */
    public void setName(String name);

    /**
     * Get the node type.
     *
     */
    public String getType();

    /**
     * Set the node type.
     *
     */
    public void setType(String type);

    /**
     * Check if this node is a container.
     *
     */
    public boolean isContainer();

    /**
     * Check if this node is a data node.
     *
     */
    public boolean isDataNode();

    }
