/*$Id: RegistryBrowser.java,v 1.1 2005/08/11 10:15:01 nw Exp $
 * Created on 30-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.ui;

/** A rudimentary registry browser
 * @todo improve browsing and linking methods - maybe easier once integrate JDIC.
 * @todo add 'show resource' methods.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Mar-2005
 *
 */
public interface RegistryBrowser {
    /**show the browser */
    public void show();
    /** hide the browser */
    public void hide();
}

/* 
 $Log: RegistryBrowser.java,v $
 Revision 1.1  2005/08/11 10:15:01  nw
 finished split

 Revision 1.4  2005/05/12 15:59:10  clq2
 nww 1111 again

 Revision 1.2.8.1  2005/05/11 11:55:19  nw
 javadoc

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:18  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.1  2005/04/01 19:03:10  nw
 beta of job monitor
 
 */