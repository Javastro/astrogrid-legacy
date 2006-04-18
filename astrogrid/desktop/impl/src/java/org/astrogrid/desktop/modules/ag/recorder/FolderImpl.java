/*$Id: FolderImpl.java,v 1.4 2006/04/18 23:25:46 nw Exp $
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

    public void setInformation(ExecutionInformation e) {
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
    

}

/* 
$Log: FolderImpl.java,v $
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