/*$Id: FolderImpl.java,v 1.6 2006/06/27 10:25:45 nw Exp $
 * Created on 07-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.recorder;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;

/**
 * implementation of a folder - little more than a bean.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Nov-2005
 *
 */
public class FolderImpl implements Serializable, Folder {
    static final long serialVersionUID = 12345689123456789L;
    public FolderImpl(ExecutionInformation info,URI parentKey) {
        this.info = info;
        this.parentKey = parentKey;
        childKeyList = new ArrayList();        
    }
    private final List childKeyList; // keys of children
    private final  URI parentKey; // key of parent (null for root)
    private ExecutionInformation info;
    private int unread;
    private boolean deleted;
  

    public ExecutionInformation getInformation() {
        return info;
    }

    /** if the new execution information has a different id, this is a program
     * error, and will be rejected with an IllegalArgumentException
     */
    public void setInformation(ExecutionInformation e) {
    	if (e == null) {
    		throw new IllegalArgumentException("null information");
    	}
    	if (! this.getKey().equals(e.getId())) {
    		throw new IllegalArgumentException("New execution information has different id");
    	}
        this.info = e;
    }

    
    public void setDeleted(boolean b) {
    	this.deleted = b;
    }
    public boolean isDeleted() {
    	return deleted;
    }
    public int getUnreadCount() {
        return unread;
    }
    public void setUnreadCount(int c) {
        this.unread = c;
    }
    public List getChildKeyList() {
        return childKeyList;
    }
    public URI getParentKey() {
        return parentKey;
    }
    
    public URI getKey() {
        return info.getId();
    }

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.childKeyList == null) ? 0 : this.childKeyList.hashCode());
		result = PRIME * result + (this.deleted ? 1231 : 1237);
		result = PRIME * result + ((this.info == null) ? 0 : this.info.hashCode());
		result = PRIME * result + ((this.parentKey == null) ? 0 : this.parentKey.hashCode());
		result = PRIME * result + this.unread;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FolderImpl other = (FolderImpl) obj;
		if (this.childKeyList == null) {
			if (other.childKeyList != null)
				return false;
		} else if (!this.childKeyList.equals(other.childKeyList))
			return false;
		if (this.deleted != other.deleted)
			return false;
		if (this.info == null) {
			if (other.info != null)
				return false;
		} else if (!this.info.equals(other.info))
			return false;
		if (this.parentKey == null) {
			if (other.parentKey != null)
				return false;
		} else if (!this.parentKey.equals(other.parentKey))
			return false;
		if (this.unread != other.unread)
			return false;
		return true;
	}


    

}

/* 
$Log: FolderImpl.java,v $
Revision 1.6  2006/06/27 10:25:45  nw
findbugs tweaks

Revision 1.5  2006/06/15 09:47:23  nw
improvements coming from unit testing

Revision 1.4  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.3.30.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.3  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.2.2.1  2005/11/23 18:09:37  nw
tuned up

Revision 1.2  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/