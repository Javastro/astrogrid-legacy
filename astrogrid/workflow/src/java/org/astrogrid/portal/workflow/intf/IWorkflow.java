/*$Id: IWorkflow.java,v 1.2 2004/02/25 10:57:43 nw Exp $
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
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public interface IWorkflow extends IActivity {
    /**
      * <p> 
      * A navigational aid. Navigates straight to the Activity 
      * given a key without having to know how to traverse the Worlflow.
      * In effect, this gets you straight into context.
      * <p>
      * 
      * @param key - the key of the activity
      * @deprecated - don't want to support this - it'd be hard to reimplement.
      **/
    public abstract IActivity getActivity(String key);
    /**
     * Sets workflow name. Sets the dirty flag.
     */
    public abstract void setName(String name);
    /**
     * Gets workflow name.
     */
    public abstract String getName();
    /**
     * Sets the userid (synonym for account). Sets the dirty flag.
     */
    public abstract void setUserid(String userid);
    /**
     * Gets the userid.
     */
    public abstract String getUserid();
    /** @deprecated - hard to replicate. better maintained in the controller logic */
    public abstract boolean isDirty();
    public abstract void setDescription(String description);
    public abstract String getDescription();
    public abstract void setChild(IActivity child);
    public abstract IActivity getChild();
}
/* 
$Log: IWorkflow.java,v $
Revision 1.2  2004/02/25 10:57:43  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.1  2004/02/24 15:35:46  nw
extracted public interface from each implementation class.
altered types to reference interface rather than implementation whever possible.
added factory and manager facade at the front
 
*/