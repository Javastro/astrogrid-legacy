/*$Id: UIMenuContribution.java,v 1.4 2006/06/15 09:57:05 nw Exp $
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

import javax.swing.JMenu;

import org.astrogrid.desktop.icons.IconHelper;

/** bean modelling a menu in the user interface.*/
public class UIMenuContribution extends JMenu implements UIStructureContribution {

    public UIMenuContribution() {
        super();
    }
    

    private String after;
    private String before;
    private String parentName;
    
    /** sets the icon to use */
    public void setIconName(String icon) {
        super.setIcon(IconHelper.loadIcon(icon));
    }

    /** hivemind ordering constraint
     * @see org.astrogrid.desktop.modules.system.contributions.UIStructureContribution#getAfter()
     */
    public String getAfter() {
        return this.after;
    }
    /**
     * @see org.astrogrid.desktop.modules.system.contributions.UIStructureContribution#setAfter(java.lang.String)
     */
    public void setAfter(String followingNames) {
        this.after = followingNames;
    }
    /** hivemind ordering constraint */
    public String getBefore() {
        return this.before;
    }
    public void setBefore(String precedingNames) {
        this.before = precedingNames;
    }

    /** parent ui component for this component 
     * @see org.astrogrid.desktop.modules.system.contributions.UIStructureContribution#getParentName()
     */
    public String getParentName() {
        return this.parentName;
    }

    public void setParentName(String parent) {
        this.parentName = parent;
    }

    

}


/* 
$Log: UIMenuContribution.java,v $
Revision 1.4  2006/06/15 09:57:05  nw
doc fix

Revision 1.3  2006/05/17 23:57:46  nw
documentation improvements.

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development
 
*/