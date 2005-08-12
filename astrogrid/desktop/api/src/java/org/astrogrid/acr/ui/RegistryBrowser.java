/*$Id: RegistryBrowser.java,v 1.2 2005/08/12 08:45:15 nw Exp $
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

/** Interface to the registry browser UI
 * <p>
 * <img src="doc-files/registry.png">
 * @todo UI is pretty simple at present. needs a lot of improvements
 * @todo improve browsing and linking methods - maybe easier once integrate JDIC.
 * @todo add 'show resource' methods.
 * @service userInterface.registryBrowser
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Mar-2005
 *@see org.astrogrid.acr.astrogrid.Registry
 */
public interface RegistryBrowser {
    /**show the registry browser ui*/
    public void show();
    /** hide the registry browser ui*/
    public void hide();
}

/* 
 $Log: RegistryBrowser.java,v $
 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

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