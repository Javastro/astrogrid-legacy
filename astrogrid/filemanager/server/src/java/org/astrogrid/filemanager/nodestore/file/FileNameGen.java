/*$Id: FileNameGen.java,v 1.4 2005/04/28 21:38:06 clq2 Exp $
 * Created on 24-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.nodestore.file;

import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.nodestore.NameGen;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.file.FileSequence;
import org.apache.commons.transaction.file.ResourceManagerException;

import java.io.File;

/** Implementation of name generator using the FileSequence from commons-transactions - 
 * provides a stream of unique identifiers, in a thread-safe, persistent manner.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2005
 */
public class FileNameGen implements NameGen {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(FileNameGen.class);
    private static final String NODE_IVORN_SEQUENCE = "node-ivorn-sequence";
    /** Construct a new FileNameGen
     * @throws ResourceManagerException
     * 
     */
    public FileNameGen(File workingDir) throws ResourceManagerException {
        super();
        FileNodeStore.checkStorage("id generator",workingDir);
        seq = new FileSequence(workingDir.toString(),new CommonsLoggingFacade(logger));
        //create sequence, if not already there..
        if (!seq.exists(NODE_IVORN_SEQUENCE)) {
            seq.create(NODE_IVORN_SEQUENCE,100L);
        }
    }
    protected final FileSequence seq;
    

    /**
     * @throws FileManagerFault
     * @throws ResourceManagerException
     * @see org.astrogrid.filemanager.nodestore.NameGen#newUnique()
     */
    public String newUnique() throws FileManagerFault {
        try {
            return Long.toString(seq.nextSequenceValueBottom(NODE_IVORN_SEQUENCE,1L));
        } catch (ResourceManagerException e) {
            logger.fatal("Sequence failure",e);
            FileManagerFault f = new FileManagerFault("Sequence failure");
            f.setFaultReason(e.getMessage());
            throw f;
        }
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FileNameGen:");
        buffer.append(" NODE_IVORN_SEQUENCE: ");
        buffer.append(NODE_IVORN_SEQUENCE);
        buffer.append(" seq: ");
        buffer.append(seq);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: FileNameGen.java,v $
Revision 1.4  2005/04/28 21:38:06  clq2
roll back before 1035

Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:38  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:27  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store
 
*/