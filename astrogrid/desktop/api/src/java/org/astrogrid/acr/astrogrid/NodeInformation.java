/*$Id: NodeInformation.java,v 1.3 2005/08/16 13:14:42 nw Exp $
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

/** Bean that summarizes the properties of a myspace resource (a file or folder)
 * <p>
 * <tt>getId()</tt> will return a myspace resouce locator - an ivorn of form 
 * <tt>ivo://<i>Community-Id</i>/<i>User-Id</i>#<i>File-Path</i></tt>
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Aug-2005
  * @xmlrpc returned as a struct, with keys corresponding to bean names
  * @see org.astrogrid.acr.astrogrid.Myspace
 *
 */
public class NodeInformation extends AbstractInformation {


    public NodeInformation(String name,URI node,Long size,Calendar createDate,Calendar modifyDate, Map attributes, boolean file, URI contentLocation) {
        super(name,node);
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

    /** access other metadata attributes of this node 
     * @return a map containing key-value pairs of other metadata about this resource
     * @xmlrpc key will be 'attributes', type will be 'struct'*/
    public Map getAttributes() {
        return this.attributes;
    }
    /** access the creation date for this node 
     * @return object representing creation date
     * @xmlrpc key will be <tt>createDate</tt>, type will be 'date'*/
    public Calendar getCreateDate() {
        return this.createDate;
    }
    /** access the modification date for this node 
     * @return object representing modification date
     * @xmlrpc key will be <tt>modifyDate</tt>, type will be 'date'*/
    public Calendar getModifyDate() {
        return this.modifyDate;
    }
 
    /** access the size of this resource 
     * @return size in bytes of this resource
     * @xmlrpc key will be <tt>size</tt>, type will be 'int' - beware of exceeding max size.*/
    public long getSize() {
        return this.size;
    }
    
    /**  Check whether this resource is a file
     * @return true if this resource is a file 
     * @xmlrpc key will be <tt>file</tt>, type will be 'boolean'*/
    public boolean isFile() {
        return file;
    }
    
    /** Check whether this resource is a folder
     * @return true if this resource is a folder 
     * @xmlrpc key will be <tt>folder</tt>, type will be 'boolean'*/
    public boolean isFolder() {
        return !file;
    }
    
    /** access the name of the filestore that holds the data of this resource.
     * 
     * @return a registy identifier - ivorn - may be null in case of a folder, or a file with no content.
     * @xmlrpc key will be <tt>contentLocation</tt>, type will be 'string'
     */
    public URI getContentLocation() {
        return contentLocation;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[NodeInformation:");
        buffer.append(" id: ");
        buffer.append(id);        
        buffer.append(" file: ");
        buffer.append(file);
        buffer.append(" size: ");
        buffer.append(size);
        buffer.append(" createDate: ");
        buffer.append(createDate);
        buffer.append(" modifyDate: ");
        buffer.append(modifyDate);
        buffer.append(" attributes: ");
        buffer.append(attributes);
        buffer.append(" contentLocation: ");
        buffer.append(contentLocation);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: NodeInformation.java,v $
Revision 1.3  2005/08/16 13:14:42  nw
added 'name' as a common field for all information objects

Revision 1.2  2005/08/12 08:45:16  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/