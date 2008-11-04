/*$Id: ServletsContribution.java,v 1.6 2008/11/04 14:35:53 nw Exp $
 * Created on 16-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.contributions;
 /** contributes a servlet to be deployed in the servlet container. 
 *
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 16-Mar-2006
 *
 */
public class ServletsContribution {

    public ServletsContribution() {
        super();
    }
    
    private String name;
    private String path;
    private Class servletClass;
    /** the name of the servlet */
    public String getName() {
        return this.name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    /** path to deploy this servlet to */
    public String getPath() {
        return this.path;
    }
    public void setPath(final String path) {
        this.path = path;
    }
    /** implementation class for this servlet */
    public Class getServletClass() {
        return this.servletClass;
    }
    public void setServletClass(final Class servletClass) {
        this.servletClass = servletClass;
    }
    

}


/* 
$Log: ServletsContribution.java,v $
Revision 1.6  2008/11/04 14:35:53  nw
javadoc polishing

Revision 1.5  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.4  2006/06/27 19:13:37  nw
minor tweaks

Revision 1.3  2006/06/15 09:56:22  nw
doc fix

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development
 
*/