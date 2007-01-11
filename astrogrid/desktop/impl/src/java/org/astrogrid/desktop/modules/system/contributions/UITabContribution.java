/*$Id: UITabContribution.java,v 1.6 2007/01/11 18:15:50 nw Exp $
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

import javax.swing.BorderFactory;
import javax.swing.Icon;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.system.UIImpl;

import com.l2fprod.common.swing.JButtonBar;
/**
 * ui contribution that is a tab in the main window
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2006
 *
 */
public class UITabContribution extends JButtonBar implements UIStructureContribution, PropertyChangeListener {

    public UITabContribution() {
        super();
        setBorder(BorderFactory.createEmptyBorder());
    }
   
    private String before;
    private String after;
    private String text;
    private String toolTipText;
    private Icon icon;
    
    public void setHelpId(String s) {
    	CSH.setHelpIDString(this, s);
    }
    
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
    	setVisible(visiblePreference.asBoolean());
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



	public String getAfter() {
        return this.after;
    }


    public void setAfter(String after) {
        this.after = after;
    }


    public String getBefore() {
        return this.before;
    }


    public void setBefore(String before) {
        this.before = before;
    }


    public Icon getIcon() {
        return this.icon;
    }


    public void setIconName(String i) {
        this.icon = IconHelper.loadIcon(i);
    }



    public String getText() {
        return this.text;
    }


    public void setText(String text) {
        this.text = text;
    }


    public String getToolTipText() {
        return this.toolTipText;
    }


    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }


    public String getParentName() {
        return UIImpl.TABS_NAME;
    }



}


/* 
$Log: UITabContribution.java,v $
Revision 1.6  2007/01/11 18:15:50  nw
fixed help system to point to ag site.

Revision 1.5  2007/01/10 19:12:16  nw
integrated with preferences.

Revision 1.4  2007/01/10 14:55:30  nw
integrated with preference system.

Revision 1.3  2006/08/31 21:31:37  nw
minor tweaks and doc fixes.

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.3  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1.2.2  2006/04/04 10:31:27  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development
 
*/