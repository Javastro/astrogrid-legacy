/*$Id: SqlCommands.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;
/** Interface for a component that provides sql statements to use in a job store
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 * @see org.astrogrid.jes.impl.workflow.DBJobFactoryImpl
 *
 */
public interface SqlCommands {
    /** 
     * @return prepared statement string to delete a job
     */
    public abstract String getDeleteSQL();
    /**
     * @return prepared statement string to insert a job
     */
    public abstract String getInsertSQL();
    /**
     * @return prepared statement string to list jobs
     */
    public abstract String getListSQL();
    /**
     * @return prepared statement string to retreive a job
     */
    public abstract String getRetrieveSQL();
    /**
     * @return prepared statement string to update a job.
     */
    public abstract String getUpdateSQL();

}
/* 
$Log: SqlCommands.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.3  2004/02/19 13:40:09  nw
updated to fit new interfaces

Revision 1.1.2.2  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.1  2004/02/12 12:54:47  nw
worked in inversion of control pattern - basically means that
components have to be assembled, rather than self-configuring
from properties in config files. so easier to test each component in isolation
 
*/