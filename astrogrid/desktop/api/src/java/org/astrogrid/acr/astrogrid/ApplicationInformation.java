/*$Id: ApplicationInformation.java,v 1.2 2005/08/12 08:45:16 nw Exp $
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

/** Information Bean that contains information about an registered application
 * <p>
 * <tt>getId()</tt> will contain a registry identifier - see {@link org.astrogrid.acr.astrogrid.ResourceInformation} for
 * definition
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Aug-2005
  * @xmlrpc returned as a struct, with keys corresponding to bean names
 * @todo should this contain description too?
 * @see org.astrogrid.acr.astrogrid.Applications
 */
public class ApplicationInformation extends AbstractInformation{

    /** Construct a new ApplicationInfomation
     * 
     */
    public ApplicationInformation(URI id,String name,String[] interfaces) {
        super(id);
        this.name = name;
        this.interfaces = interfaces;
    }
    
    private final String name;
    private final String[] interfaces;

    /** access the human-friendly name of the application
     * @return a string
     * @xmlrpc key will be <tt>name</tt>
     */
    public String getName() {
        return this.name;
    }
    /** access the names of the interfaces this application supports
     * @return an array of interface names
     * @xmlrpc key will be <tt>interfaces</tt>, type will be array.
     */
    public String[] getInterfaces() {
        return this.interfaces;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ApplicationInformation:");
        buffer.append(" id: ");
        buffer.append(id);        
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append(" { ");
        for (int i0 = 0; interfaces != null && i0 < interfaces.length; i0++) {
            buffer.append(" interfaces[" + i0 + "]: ");
            buffer.append(interfaces[i0]);
        }
        buffer.append(" } ");
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: ApplicationInformation.java,v $
Revision 1.2  2005/08/12 08:45:16  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/