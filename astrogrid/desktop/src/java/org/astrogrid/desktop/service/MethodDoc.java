/*$Id: MethodDoc.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service;

/** Documentation annotation for a method
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class MethodDoc {

    /** Construct a new MethodDoc
     * 
     */
    public MethodDoc(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }
    
    protected String description;
    protected String name;

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<b><a href='./");
        buffer.append(name);
        buffer.append("/'>") ;
        buffer.append(name);
        buffer.append("</a></b> - ");
        buffer.append(description);
        buffer.append("<br />");
        return buffer.toString();
    }
}


/* 
$Log: MethodDoc.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/