/*$Id: TreeClientFactory.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
 * Created on 05-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.store.tree;

/** Factory class, to isolate aladin code from the details of which implementation is being used for the Aladin Adapter.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2004
 *
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.FileManagerClientFactory} instead.
 */
public class TreeClientFactory {

    /** Construct a new AladinAdapterFactory
     * 
     */
    public TreeClientFactory() {
        super();
    }
    
    /** create a new instance of an aladin adapter 
     * @todo add seleciton of implementaiton based on config key*/
    public TreeClient createClient() {
        return new FileManagerTreeClient();
    }

}


/* 
$Log: TreeClientFactory.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.2  2004/11/17 16:22:53  clq2
nww-itn07-704

Revision 1.1.2.1  2004/11/16 16:47:28  nw
copied aladinAdapter interfaces into a neutrally-named package.
deprecated original interfaces.
javadoc

Revision 1.2  2004/11/11 17:50:42  clq2
Noel's aladin stuff

Revision 1.1.2.1  2004/11/09 13:56:38  nw
first stab at an iteration6 implementation

Revision 1.1  2004/11/05 17:10:41  nw
created factory class to return instance of aladin adapter.
returns the mock for now.
 
*/