/*$Id: UIStructureContribution.java,v 1.2 2006/04/18 23:25:43 nw Exp $
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

    public String getAfter();

    public String getBefore();

    public String getParentName();
    
    public String getName();
    
    public String getText();
    
    public Icon getIcon();

}

/* 
 $Log: UIStructureContribution.java,v $
 Revision 1.2  2006/04/18 23:25:43  nw
 merged asr development.

 Revision 1.1.2.1  2006/03/22 18:01:31  nw
 merges from head, and snapshot of development
 
 */