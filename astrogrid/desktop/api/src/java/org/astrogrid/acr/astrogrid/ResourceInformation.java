/*$Id: ResourceInformation.java,v 1.3 2005/08/16 13:14:42 nw Exp $
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

/** Information Bean that summarizes the most useful bits of a registry entry
 * <p>
 * <tt>getId()</tt> will return a registry identifier - an ivorn of form <tt>ivo://<i>Authority-Id</i>/<i>Resource-Id</i></tt>
 * @xmlrpc returned as a struct, with keys corresponding to bean names
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Aug-2005
 *
 */
public class ResourceInformation extends AbstractInformation {
    
    public ResourceInformation(URI ivorn,String title,String description, URL url) {
        super(title,ivorn);
        this.description= description;
        this.url = url;
    }
    
    private final String description;

    private final URL url;
    
    
    /** access the description of this entry 
     * @return contents of the 'description' element from the registry entry
     * @xmlrpc key is <tt>description</tt>*/
    public String getDescription() {
        return description;
    }
/** access the title of this entry - synonym for {@link #getName}
 * @return contents of 'title' field of this registry entry
 * @xmlrpc key is <tt>title</tt>
 * */
    public String getTitle() {
        return getName();
    }
    /** access the endpoint URL for this entry 
     * @return the contents of the 'accessURL' element in the registry entry - the URL where this service
     * may be accessed. Will be empty for registry resouces that aren't services (e.g. catalogues, people)
     * @xmlrpc key will be <tt>accessURL</tt>. May not be present in cases where registry entry has not endpoint*/
    public URL getAccessURL() {
        return url;
    }
    // also getInterface() - but don't know what this is, so will leave for now.
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ResourceInformation:");
        buffer.append(" id: ");
        buffer.append(id);        
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