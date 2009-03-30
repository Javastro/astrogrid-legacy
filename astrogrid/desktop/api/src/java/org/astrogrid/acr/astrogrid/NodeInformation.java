/*$Id: NodeInformation.java,v 1.10 2009/03/30 15:02:54 nw Exp $
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

import java.net.URI;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Metadata for a myspace 'node' - a file or folder.
 * <p/>
 * {@link #getId()} will return a myspace resouce locator - an ivorn of form 
 * <tt>ivo://<i>Community-Id</i>/<i>User-Id</i>#<i>File-Path</i></tt>
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 02-Aug-2005
 * @bean
  * @see Myspace
 *
 */
public class NodeInformation extends AbstractInformation {

    /** @exclude */
    @Override
    public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.attributes == null) ? 0 : this.attributes.hashCode());
		result = PRIME * result + ((this.contentLocation == null) ? 0 : this.contentLocation.hashCode());
		result = PRIME * result + ((this.createDate == null) ? 0 : this.createDate.hashCode());
		result = PRIME * result + (this.file ? 1231 : 1237);
		result = PRIME * result + ((this.modifyDate == null) ? 0 : this.modifyDate.hashCode());
		result = PRIME * result + (int) (this.size ^ (this.size >>> 32));
		return result;
	}
    /**@exclude */
	@Override
    public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (!super.equals(obj)) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		final NodeInformation other = (NodeInformation) obj;
		if (this.attributes == null) {
			if (other.attributes != null) {
                return false;
            }
		} else if (!this.attributes.equals(other.attributes)) {
            return false;
        }
		if (this.contentLocation == null) {
			if (other.contentLocation != null) {
                return false;
            }
		} else if (!this.contentLocation.equals(other.contentLocation)) {
            return false;
        }
		if (this.createDate == null) {
			if (other.createDate != null) {
                return false;
            }
		} else if (!this.createDate.equals(other.createDate)) {
            return false;
        }
		if (this.file != other.file) {
            return false;
        }
		if (this.modifyDate == null) {
			if (other.modifyDate != null) {
                return false;
            }
		} else if (!this.modifyDate.equals(other.modifyDate)) {
            return false;
        }
		if (this.size != other.size) {
            return false;
        }
		return true;
	}
	/** @exclude */
	public NodeInformation(final String name,final URI node,final Long size,final Calendar createDate,final Calendar modifyDate, final Map attributes, final boolean file, final URI contentLocation) {
        super(name,node);
        this.size = size == null ? 0 : size.longValue();
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.attributes = Collections.unmodifiableMap(new HashMap(attributes));
        this.file = file;
        this.contentLocation = contentLocation;
    }
    
    static final long serialVersionUID = 8310686463381671881L;
    private final boolean file;
    private final long size;
    private final Calendar createDate;
    private final Calendar modifyDate;
    private final Map attributes;
    private final URI contentLocation;

    /** Access any other metadata attributes of this node 
     * @return a map containing key-value pairs of other metadata about this resource
     * @xmlrpc key will be 'attributes', type will be 'struct'*/
    public Map getAttributes() {
        return this.attributes;
    }
    /** the creation date for this node 
     * @return object representing creation date
     * @xmlrpc key will be <tt>createDate</tt>, type will be 'date'*/
    public Calendar getCreateDate() {
        return this.createDate;
    }
    /** the modification date for this node 
     * @return object representing modification date
     * @xmlrpc key will be <tt>modifyDate</tt>, type will be 'date'*/
    public Calendar getModifyDate() {
        return this.modifyDate;
    }
 
    /** the size of this resource 
     * @return size in bytes of this resource
     * @xmlrpc key will be <tt>size</tt>, type will be 'int' - beware of exceeding max size.*/
    public long getSize() {
        return this.size;
    }
    
    /**  determines whether this resource is a file
     * @return true if this resource is a file 
     * @xmlrpc key will be <tt>file</tt>, type will be 'boolean'
     * @equivalence ! isFolder()
     * */
    public boolean isFile() {
        return file;
    }
    
    /** determines whether this resource is a folder
     * @return true if this resource is a folder 
     * @xmlrpc key will be <tt>folder</tt>, type will be 'boolean'
     * @equivalence ! isFile()
     * */     
    public boolean isFolder() {
        return !file;
    }
    
    /** the ID of the filestore that holds the data of this resource.
     * 
     * @return a registy identifier - may be null in case of a folder, or a file with no content.
     * @xmlrpc key will be <tt>contentLocation</tt>, type will be 'string'
     */
    public URI getContentLocation() {
        return contentLocation;
    }
    /**@exclude */
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
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
Revision 1.10  2009/03/30 15:02:54  nw
Added override annotations.

Revision 1.9  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.8  2007/03/08 17:46:56  nw
removed deprecated interfaces.

Revision 1.7  2007/01/24 14:04:44  nw
updated my email address

Revision 1.6  2006/06/15 09:01:27  nw
provided implementations of equals()

Revision 1.5  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.4.6.1  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.4  2006/02/02 14:19:48  nw
fixed up documentation.

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