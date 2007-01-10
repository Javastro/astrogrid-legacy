/*$Id: UIStructureContribution.java,v 1.4 2007/01/10 14:55:30 nw Exp $
 * Created on 21-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.system.contributions;

import javax.swing.Icon;
/** interface common to all ui structure contributions *
 * defines the minimum required to assemble the contributions into the ui
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2006
 *
 */ 
public interface UIStructureContribution {
	/** returns comma separated list of names of ui components that
	 * this component occurs 'after'
	 * @return
	 */
    public String getAfter();

    /** returns a comma separated list of names of ui components that
     * this component occurs 'before'
     * @return
     */
    public String getBefore();

    /** retunrs the name of the parent / enclosing ui component */
    public String getParentName();
    
    /** reutnrz the name of this component */
    public String getName();
    
    /** display text for this component */
    public String getText();
    
    /** display icon fopr this component */
    public Icon getIcon();

    
}

/* 
 $Log: UIStructureContribution.java,v $
 Revision 1.4  2007/01/10 14:55:30  nw
 integrated with preference system.

 Revision 1.3  2006/06/15 09:57:05  nw
 doc fix

 Revision 1.2  2006/04/18 23:25:43  nw
 merged asr development.

 Revision 1.1.2.1  2006/03/22 18:01:31  nw
 merges from head, and snapshot of development
 
 */