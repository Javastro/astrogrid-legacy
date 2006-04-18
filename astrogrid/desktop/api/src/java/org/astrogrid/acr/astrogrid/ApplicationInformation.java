/*$Id: ApplicationInformation.java,v 1.8 2006/04/18 23:25:45 nw Exp $
 * Created on 04-Aug-2005
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
import java.util.Map;

/** Information Bean describing a registered remote  application.
 * 
 * <tt>getId()</tt> will contain a registry identifier - see {@link org.astrogrid.acr.astrogrid.ResourceInformation} for
 * definition
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Aug-2005
  * @xmlrpc returned as a struct, with keys corresponding to bean names
 * @todo should this contain description too?
 * @see org.astrogrid.acr.astrogrid.Applications
 * @since 1.2 extends ResourceInformation
 */
public class ApplicationInformation extends ResourceInformation{

    
    /** construct a new application information, specifying an endpoint for it (i.e. a non-cea application) */
    public ApplicationInformation(URI id,String name,String description,Map parameters,InterfaceBean[] interfaces,URL endpoint, URL logo) {
        super(id,name,description,endpoint,logo);
        this.interfaces = interfaces;
        this.parameters = parameters;
    }
    
    static final long serialVersionUID = 9164345724780990915L;
    private final InterfaceBean[] interfaces;
    private final Map parameters;

    /** The names of the interfaces provided by this application.
     * @return an array of interface names
     * @xmlrpc key will be <tt>interfaces</tt>, type will be array.
     */
    public InterfaceBean[] getInterfaces() {
        return this.interfaces;
    }

    /** The Parameters used in this application.
     * 
     * @return a map of <tt>parameter name</tt> - {@link ParameterBean} pairs
     */
    public Map getParameters() {
        return this.parameters;
    }


    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ApplicationInformation:");
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append(" id: ");
        buffer.append(id);
        buffer.append(" { ");      
        for (int i0 = 0; interfaces != null && i0 < interfaces.length; i0++) {
            buffer.append(" interfaces[" + i0 + "]: ");
            buffer.append(interfaces[i0]);
        }
        buffer.append(" } ");
        buffer.append(" description: ");
        buffer.append(description);
        buffer.append(" parameters: ");
        buffer.append(parameters);
        buffer.append(" url :");
        buffer.append(url);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: ApplicationInformation.java,v $
Revision 1.8  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.7.6.1  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.7  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.6  2005/11/04 14:38:58  nw
added logo field

Revision 1.5  2005/09/12 15:21:43  nw
added stuff for adql.

Revision 1.4  2005/08/25 16:59:44  nw
1.1-beta-3

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