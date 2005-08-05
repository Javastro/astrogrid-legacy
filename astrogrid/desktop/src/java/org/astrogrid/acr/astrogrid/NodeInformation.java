/*$Id: NodeInformation.java,v 1.1 2005/08/05 11:46:55 nw Exp $
 * Created on 02-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.io.Serializable;
import java.net.URI;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Bean that summarizes the properties of a myspace resouce (a file or folder)
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Aug-2005
 *
 */
public class NodeInformation extends AbstractInformation {

    /** Construct a new NodeMetadata
     * @todo also got 'contentLocation' and 'contentId' - do I want to expose these? 
     */
    public NodeInformation(URI node,Long size,Calendar createDate,Calendar modifyDate, Map attributes, boolean file, URI contentLocation) {
        super(node);
        this.size = size == null ? 0 : size.longValue();
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.attributes = Collections.unmodifiableMap(new HashMap(attributes));
        this.file = file;
        this.contentLocation = contentLocation;
    }
    
    private final boolean file;
    private final long size;
    private final Calendar createDate;
    private final Calendar modifyDate;
    private final Map attributes;
    private final URI contentLocation;

    /** access other metadata attributes of this node */
    public Map getAttributes() {
        return this.attributes;
    }
    /** access the creation date for this node */
    public Calendar getCreateDate() {
        return this.createDate;
    }
    /** access the modification date for this node */
    public Calendar getModifyDate() {
        return this.modifyDate;
    }
 
    /** access the size of this resource */
    public long getSize() {
        return this.size;
    }
    
    /** true if this resource is a file */
    public boolean isFile() {
        return file;
    }
    
    /** true if this resource is a folder */
    public boolean isFolder() {
        return !file;
    }
    
    /** access the identifier of the filestore that holds the content of this resource.
     * 
     * @return registy identifier - may be null in case of a folder, or a file with no content.
     */
    public URI getContentLocation() {
        return contentLocation;
    }
}


/* 
$Log: NodeInformation.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/