/*$Id: NodeMetadata.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
 * Created on 11-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.client;

import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filestore.common.file.FileProperties;

import java.net.URI;
import java.util.Calendar;
import java.util.Map;

/** Meta-information about a node.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2005
 *
 */
public interface NodeMetadata {
    
    /**Get a URI that points to the storage service that holds this node's data.
     * 
     * In the case of a node stored in a filestore, will return the  ivorn identifier of that FileStore.
     * 
     * @return A URI for the storage service.
     * @throws UnsupportedOperationException
     *             If the node represents a container (although future
     *             extensions may allow this).
     * @throws FileManagerIdentifierException
     *             If the location Ivorn is not valid.
     *  
     */
    public URI getContentLocation() throws UnsupportedOperationException;

    
    
    /** access the ID for this content, (unique to the {@link #getContentLocation()} for this node.) 
     * @return a unique id for this nodes' data, which may be used to retreive it in some way.
     * @throws UnsupportedOperationException if this node is a folder*/
    public String getContentId() throws UnsupportedOperationException;
    /**
     * Get the content size for a data node.
     * 
     * @return The size of the stored data for a data node, or -1 for a
     *         container node. Will return null if this information is not available.
     *  
     */
    public Long getSize();

    public Calendar getCreateDate();


    public Calendar getModifyDate();

    /** access the short-form ivorn for this node 
     *<p>
     *this is an ivorn of the form <code>ivo://<i>filemanager</i>#<i>node-id</i></code>
     *It may be used to reference this node uniquely, irrespective of its path / position in the tree.  
     * @return a nodeIvorn (different concrete class, but still an ivorn formatted string).
     * @see FileManagerNode#getIvorn() for a human readable ivorn 
     *
     * */
    public NodeIvorn getNodeIvorn();
    
    /** access a collection of further key-value pairs associated with this node. */
    public Map getAttributes();
    
    // keys to index into metadata with.
    /** attributes key that may contain information about the encoding of the data */
    public static final String MIME_ENCODING = FileProperties.MIME_ENCODING_PROPERTY;
    /** attributes key that may contain information about the mime type of the data */
    public static final String MIME_TYPE = FileProperties.MIME_TYPE_PROPERTY;
    /** attributes key that may contain information about the homespace related to this node */
    public static final String HOME_SPACE = "community.home.space";
}


/* 
$Log: NodeMetadata.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.4  2005/03/01 15:07:31  nw
close to finished now.

Revision 1.1.2.3  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.2  2005/02/18 15:50:15  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)

Revision 1.1.2.1  2005/02/11 14:27:52  nw
refactored, split out candidate classes.
 
*/