/*$Id: ServiceDoc.java,v 1.1 2005/02/22 01:10:31 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service.annotation;
import org.apache.commons.attributes.Target;
/** Documentation annotation for a service..
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *@@Target(Target.CLASS)
 */
public class ServiceDoc {

    /** Construct a new Service
     * 
     */
    public ServiceDoc(String name,String description) {
       this.name = name;
       this.description = description;
    }
   protected String name;
   protected String description;

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<br /><b><a href='./");
        buffer.append(name);
        buffer.append("/'>") ;
        buffer.append(name);
        buffer.append("</a></b> - ");
        buffer.append(description);
        return buffer.toString();
    }
     
}


/* 
$Log: ServiceDoc.java,v $
Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/