/*$Id: ResourceInformation.java,v 1.7 2006/02/02 14:19:48 nw Exp $
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

/** summarizes the most useful fields of a registry record.
 * 
 * <tt>getId()</tt> will return a registry identifier - an ivorn of form <tt>ivo://<i>Authority-Id</i>/<i>Resource-Id</i></tt>
 * @xmlrpc returned as a struct, with keys corresponding to bean names
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Aug-2005
 *
 */
public class ResourceInformation extends AbstractInformation {
    
    public ResourceInformation(URI ivorn,String title,String description, URL url, URL logo) {
        super(title,ivorn);
        this.description= description;
        this.url = url;
        this.logo = logo;
    }
    
    protected final String description;

    protected final URL url;
    //@since 1.3
    protected final URL logo;
    
    /** the description of this entry 
     * @return contents of the 'description' element from the registry entry
     * @xmlrpc key is <tt>description</tt>*/
    public String getDescription() {
        return description;
    }
/** the title of this entry - synonym for {@link #getName}
 * @return contents of 'title' field of this registry entry
 * @xmlrpc key is <tt>title</tt>
 * */
    public String getTitle() {
        return getName();
    }
    /** the endpoint URL for this entry 
     * @return the contents of the 'accessURL' element in the registry entry - the URL where this service
     * may be accessed. Will be empty for registry resouces that aren't services (e.g. catalogues, people)
     * @xmlrpc key will be <tt>accessURL</tt>. May not be present in cases where registry entry has not endpoint*/
    public URL getAccessURL() {
        return url;
    }
    
    /** the url for the logo for this entry
     * @return url for the logo for this resource, if defined.
     * @xmlrpc key will be <tt>logoURL</tt>. May not be present.
     * @since 1.3
     */
    public URL getLogoURL() {
        return logo;
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
        buffer.append(" logo: ");
        buffer.append(logo);        
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: ResourceInformation.java,v $
Revision 1.7  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.6  2005/11/11 10:09:01  nw
improved javadoc

Revision 1.5  2005/11/04 14:38:58  nw
added logo field

Revision 1.4  2005/09/12 15:21:43  nw
added stuff for adql.

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