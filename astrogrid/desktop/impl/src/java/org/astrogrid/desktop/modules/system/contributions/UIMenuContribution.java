/*$Id: UIMenuContribution.java,v 1.10 2007/06/18 16:55:51 nw Exp $
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
import org.astrogrid.desktop.modules.system.pref.Preference;

/** bean modelling a menu in the user interface.*/
public class UIMenuContribution extends JMenu implements UIStructureContribution, PropertyChangeListener{

    public UIMenuContribution() {
        super();
    }
	public UIStructureContribution cloneStructure() {
// clone doesn't work well with menus, it seems. dunno why. 
		// use a copy constructor instead.
		return new UIMenuContribution(this);
	}

	
	protected UIMenuContribution(UIMenuContribution copy) {
		this.after = copy.after;
		this.before = copy.before;
		this.parentName= copy.parentName;
		this.setVisibleCondition(copy.visiblePreference);
		// now copy some fields from JMenu too - the ones accessible through the contribution.
		this.setText(copy.getText());
		this.setName(copy.getName());
		this.setToolTipText(copy.getToolTipText());
		this.setIcon(copy.getIcon());
		
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
    	if (p == null) {
    		return;
    	}
    	// remove any previous listeners.
    	if (visiblePreference != null) {
    		visiblePreference.removePropertyChangeListener(this);
    	}
    	visiblePreference = p;
    	visiblePreference.addPropertyChangeListener(this);
    	visiblePreference.initializeThroughListener(this);
    	
    }
    
    // package-private method, for testing only.
    Preference getVisibleCondition() {
    	return visiblePreference;
    }


    /** used internally to watch for preference changes */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == visiblePreference) {
	    	setVisible(visiblePreference.asBoolean());			
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
Revision 1.10  2007/06/18 16:55:51  nw
javadoc fixes.

Revision 1.9  2007/05/18 06:13:13  nw
fixed menu visibility bug.

Revision 1.8  2007/04/18 15:47:09  nw
tidied up voexplorer, removed front pane.

Revision 1.7  2007/01/23 11:50:49  nw
preferences integration.

Revision 1.6  2007/01/10 19:12:16  nw
integrated with preferences.

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