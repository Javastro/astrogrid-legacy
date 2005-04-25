/*$Id: FileNameGen.java,v 1.2 2005/04/25 12:13:30 clq2 Exp $
 * Created on 24-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.common.namegen;


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

    protected final FileSequence seq;
    protected final String sequenceName;
    /** Construct a new FileNameGen
     * @throws ResourceManagerException
     * 
     */
    public FileNameGen(File workingDir, String sequenceName) throws ResourceManagerException, IllegalArgumentException {
        super();
        this.sequenceName = sequenceName;
        verifyWorkingDir(workingDir);
        seq = new FileSequence(workingDir.toString(),new CommonsLoggingFacade(logger));
        //create sequence, if not already there, starting from 100.
        if (!seq.exists(sequenceName)) {
            seq.create(sequenceName,100L);
        }
    }
    /** Verify that working directory exists, is readable, etc.
     * will create working directory if it is missing, and is able to.
     * @param workingDir
     * @throws IllegalArgumentException
     */
    private final void verifyWorkingDir(File workingDir) throws IllegalArgumentException {
        if (! workingDir.exists()) {
            logger.info("creating storage for id generator");
            workingDir.mkdirs();            
        }
        if (!workingDir.exists()) {
            String msg = "Base directory for id generator does not exist, and cannot be created";
            logger.fatal(msg);
            throw new IllegalArgumentException(msg);            
        }
        if (!workingDir.isDirectory()) {
            String msg = "Base for id generator must be a directory";
            logger.fatal(msg);
            throw new IllegalArgumentException(msg);
        }
        if (! (workingDir.canRead() && workingDir.canWrite())) {
            String msg = "Must be able to read and write to base directory for id generator";
            logger.fatal(msg);
            throw new IllegalArgumentException(msg);
        }
        // now set up node and account dir;
        
        logger.info("storage locations seem ok");
    }

    

    /**
     * @throws FileManagerFault
     * @throws ResourceManagerException
     * @throws ResourceManagerException
     * @see org.astrogrid.common.namegen.NameGen#next()
     */
    public String next() throws  ResourceManagerException {
            return Long.toString(seq.nextSequenceValueBottom(sequenceName,1L));

    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FileNameGen:");
        buffer.append(" seq: ");
        buffer.append(seq);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: FileNameGen.java,v $
Revision 1.2  2005/04/25 12:13:30  clq2
common-nww-1035

Revision 1.1.2.1  2005/04/11 11:03:21  nw
moved name generator into common from filemanager - can then
reuse in jes too.

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