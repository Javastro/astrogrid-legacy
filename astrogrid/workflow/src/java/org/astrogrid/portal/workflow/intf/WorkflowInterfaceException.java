/*$Id: WorkflowInterfaceException.java,v 1.3 2004/03/03 11:15:23 nw Exp $
 * Created on 24-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

/** Catch all exception type for things that go wrong with the workflow interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public class WorkflowInterfaceException extends Exception {
    /** Construct a new WorkflowInterfaceException
     * 
     */
    public WorkflowInterfaceException() {
        super();
    }
    /** Construct a new WorkflowInterfaceException
     * @param message
     */
    public WorkflowInterfaceException(String message) {
        super(message);
    }
    /** Construct a new WorkflowInterfaceException
     * @param cause
     */
    public WorkflowInterfaceException(Throwable cause) {
        super(cause);
    }
    /** Construct a new WorkflowInterfaceException
     * @param message
     * @param cause
     */
    public WorkflowInterfaceException(String message, Throwable cause) {
        super(message, cause);
    }
}


/* 
$Log: WorkflowInterfaceException.java,v $
Revision 1.3  2004/03/03 11:15:23  nw
tarted up javadocs, reviewed types

Revision 1.2  2004/02/25 10:57:43  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.1  2004/02/24 21:56:46  nw
added an exception type for the interface
 
*/