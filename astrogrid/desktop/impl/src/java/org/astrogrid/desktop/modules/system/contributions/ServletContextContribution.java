/*$Id: ServletContextContribution.java,v 1.4 2008/11/04 14:35:53 nw Exp $
 * Created on 20-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.contributions;
/** contribution of an object to add into the context of the servlet container */
public class ServletContextContribution {

    public ServletContextContribution() {
        super();
    }

    
    private String name;
    private Object object;
    public String getName() {
        return this.name;
    }
    /** the name to store {@link #getObject} under in the context */
    public void setName(final String name) {
        this.name = name;
    }
    public Object getObject() {
        return this.object;
    }
    /** the object to store in the context */
    public void setObject(final Object object) {
        this.object = object;
    }

}


/* 
$Log: ServletContextContribution.java,v $
Revision 1.4  2008/11/04 14:35:53  nw
javadoc polishing

Revision 1.3  2006/06/15 09:56:09  nw
doc fix

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.2  2006/04/04 10:31:27  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development
 
*/