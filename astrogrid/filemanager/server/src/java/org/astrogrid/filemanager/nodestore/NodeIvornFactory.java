/*$Id: NodeIvornFactory.java,v 1.2 2005/03/11 13:37:05 clq2 Exp $
 * Created on 18-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.nodestore;

import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.ivorn.IvornFactory;
import org.astrogrid.filemanager.server.FileManagerConfig;
import org.astrogrid.store.Ivorn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Factory for new node identifiers
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2005
 *
 */
public class NodeIvornFactory {

    /** Construct a new NodeIvornFactory
     * @param nameGen unique id generator
     * @param config configuration object.
     * @throws FileManagerFault
     * 
     */
    public NodeIvornFactory(NameGen nameGen, FileManagerConfig config)  {
        super();
        this.nameGen = nameGen;
        
        String base1;
        try {
            base1 = config.getFileManagerIvorn().toString();
        } catch (FileManagerFault e) {
            throw new RuntimeException("Fault in configuration",e);
        }
        if (! base1.startsWith(Ivorn.SCHEME)) {
            base = Ivorn.SCHEME + "://" + base1;
        } else {
            base = base1;
        }
    }
    protected final NameGen nameGen;
    protected final String base;

    /**
     * Our debug logger.
     *  
     */
    private static Log log = LogFactory.getLog(IvornFactory.class);

    /** create a new node ivorn.
     * @return
     * @throws FileManagerFault
     */
    public NodeIvorn createNewNodeIvorn() throws FileManagerFault {        
        String ident = nameGen.newUnique();
        return new NodeIvorn(base + "#node-" + ident);
    }


    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[NodeIvornFactory:");
        buffer.append(" nameGen: ");
        buffer.append(nameGen);
        buffer.append(" base: ");
        buffer.append(base);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: NodeIvornFactory.java,v $
Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:35  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:27  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store
 
*/