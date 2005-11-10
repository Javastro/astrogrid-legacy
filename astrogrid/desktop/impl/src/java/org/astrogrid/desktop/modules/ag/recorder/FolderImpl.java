/*$Id: FolderImpl.java,v 1.1 2005/11/10 12:05:43 nw Exp $
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

import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * implementation of a folder - little more than a bean.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Nov-2005
 *
 */
public class FolderImpl implements Serializable, Folder {
    public FolderImpl(ExecutionInformation info,URI parentKey) {
        this.info = info;
        this.parentKey = parentKey;
        childKeyList = new ArrayList();        
    }
    private final List childKeyList; // keys of children
    private final  URI parentKey; // key of parent (null for root)
    private ExecutionInformation info;
    private int unread;
  

    public ExecutionInformation getInformation() {
        return info;
    }

    public void setInformation(ExecutionInformation e) {
        this.info = e;
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
    
    /**
     *     public String toString() {
        StringBuffer sb = new StringBuffer("<html>");
        if (phase == null || phase.equals(ExecutionInformation.UNKNOWN)) {
            sb.append("<font style='color:blue'>");
        }else if (ExecutionInformation.ERROR.equals(phase)) {
                sb.append("<font style='color:red'>");
        } else if(ExecutionInformation.INITIALIZING.equals(phase)) {              
                sb.append("<font style='color:blue'>"); 
        } else if (ExecutionInformation.PENDING.equals(phase)) {              
            sb.append("<font style='color:blue'>"); 
        } else if (ExecutionInformation.RUNNING.equals(phase)) {               
                sb.append("<font style='color:green'>"); 
        } else {
                sb.append("<font>"); // nothing
        }
        sb.append(StringUtils.abbreviate(label,label.length()-1,20));            
        sb.append("</font></html>");
        return sb.toString();
    }
    */


}

/* 
$Log: FolderImpl.java,v $
Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/