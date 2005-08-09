/*$Id: ResourceInformation.java,v 1.2 2005/08/09 17:33:07 nw Exp $
 * Created on 01-Aug-2005
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
import java.net.URL;

/** Bean that summarizes the most useful bits of a registry entry
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Aug-2005
 *
 */
public class ResourceInformation extends AbstractInformation {
    
    public ResourceInformation(URI ivorn,String title,String description, URL url) {
        super(ivorn);
        this.title  = title;
        this.description= description;
        this.url = url;
    }
    
    private final String description;

    private final String title;
    private final URL url;
    
    
    /** access the description of this entry */
    public String getDescription() {
        return description;
    }
/** access the title of this entry */
    public String getTitle() {
        return title;
    }
    /** access the endpoint URL for this entyr */
    public URL getAccessURL() {
        return url;
    }
    // also getInterface() - but don't know what this is, so will leave for now.
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ResourceInformation:");
        buffer.append(" id: ");
        buffer.append(id);        
        buffer.append(" title: ");
        buffer.append(title);
        buffer.append(" description: ");
        buffer.append(description);
        buffer.append(" url: ");
        buffer.append(url);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: ResourceInformation.java,v $
Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/