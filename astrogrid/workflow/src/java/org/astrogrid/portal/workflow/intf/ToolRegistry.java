/*$Id: ToolRegistry.java,v 1.3 2004/03/03 11:15:23 nw Exp $
 * Created on 01-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

import org.astrogrid.workflow.beans.v1.Tool;

import java.util.Iterator;

/** A component that queries a registry for information about tools.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Mar-2004
 *
 */
public interface ToolRegistry {
    /** return list of names of known tools  
     * @deprecated - use {@link listTools} which returns a strongly-typed result */ 
    Iterator readToolList( ) throws WorkflowInterfaceException;
    
    /** list names of known tools 
     * @todo - do we want to return a little more here - a summary of info for each tool maybe*/
    String[] listTools() throws WorkflowInterfaceException;

    /**
     * Create a new tool object, populated from tool registry 
     * @param name the name of the tool in the registry
     * @return initialized tool object. never null
     * @todo don't know what is required in here yet. 
     **/   
    public Tool createTool( String name) throws WorkflowInterfaceException;
      
}



/* 
$Log: ToolRegistry.java,v $
Revision 1.3  2004/03/03 11:15:23  nw
tarted up javadocs, reviewed types

Revision 1.2  2004/03/03 01:36:38  nw
merged interfaces in from branch nww-int05-bz#146

Revision 1.1.2.2  2004/03/03 01:18:00  nw
commited first draft of interface design

Revision 1.1.2.1  2004/03/01 19:02:57  nw
refined interfaces. almost ready to publish
 
*/