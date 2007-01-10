/*$Id: UIMenuContribution.java,v 1.5 2007/01/10 14:55:30 nw Exp $
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenu;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.Preference;

/** bean modelling a menu in the user interface.*/
public class UIMenuContribution extends JMenu implements UIStructureContribution, PropertyChangeListener {

    public UIMenuContribution() {
        super();
    }
    

    private String after;
    private String before;
    private String parentName;
    
    
    private Preference visiblePreference;
    
    /** provide an optional preference object which is 'watched'.
     * when this preference goes 'true', the 'visible' property of this object
     * goes 'true', and a ComponentEvent is fired.. When the preference goes 'false'
     * this visible property of this object goes 'false'
     * @param p
     */    
    public void setVisibleCondition(Preference p) {
    	// remove any previous listeners.
    	if (visiblePreference != null) {
    		visiblePreference.removePropertyChangeListener(this);
    	}
    	visiblePreference = p;
    	visiblePreference.addPropertyChangeListener(this);
    	boolean b = Boolean.parseBoolean(visiblePreference.getValue());
    	setVisible(b);
    }
    
    // package-private method, for testing only.
    Preference getVisibleCondition() {
    	return visiblePreference;
    }


    /** used internally to watch for preference changes */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == visiblePreference) {
	    	boolean b = Boolean.parseBoolean(visiblePreference.getValue());
	    	setVisible(b);			
		}
	}
 
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
Revision 1.5  2007/01/10 14:55:30  nw
integrated with preference system.

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