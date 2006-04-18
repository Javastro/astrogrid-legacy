/*$Id: UITabContribution.java,v 1.2 2006/04/18 23:25:43 nw Exp $
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

import javax.swing.BorderFactory;
import javax.swing.Icon;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.UIImpl;

import com.l2fprod.common.swing.JButtonBar;
/**
 * ui contribution that is a tab in the pane.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2006
 *
 */
public class UITabContribution extends JButtonBar implements UIStructureContribution {

    public UITabContribution() {
        super(JButtonBar.HORIZONTAL);
        setBorder(BorderFactory.createEmptyBorder());
    }
   
    private String before;
    private String after;
    private String text;
    private String toolTipText;
    private Icon icon;
    

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
Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.3  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1.2.2  2006/04/04 10:31:27  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development
 
*/