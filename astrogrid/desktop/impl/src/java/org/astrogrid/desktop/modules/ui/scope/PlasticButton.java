/*$Id: PlasticButton.java,v 1.9 2006/08/15 10:01:12 nw Exp $
 * Created on 22-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Image;
import java.net.URI;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.UIComponent;

import edu.berkeley.guir.prefuse.focus.FocusSet;

/** abstract node consumer for passing data onto plastic apps
 * 
 * sets up all the machinery - leaves it to subclasses to implement what happens when the button is pressed
 * */
public abstract class PlasticButton extends NodeConsumerButton{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(PlasticButton.class);

    /**
     *  Construct a new PlasticButton
     * @param plasticID id of the remote plastic application to message
     * @param name name of this application
     * @param description optional description of the application
     * @param iconURL url for icon of this application (may be null);
     * @param selectedNodes astroscope node selection set
     * @param ui userinterface component
     * @param wrapper wrapper around plasticized UI application
     */
    public PlasticButton( PlasticApplicationDescription descr, String name, String description, FocusSet selectedNodes, UIComponent ui, TupperwareInternal tupp) {
        super(name,description!=null? description: "Display data using PLASTIC",  selectedNodes);
        if (descr.getIcon() != null) {
            ImageIcon scaled = new ImageIcon((descr.getIcon()).getImage().getScaledInstance(-1,32,Image.SCALE_SMOOTH));
			setIcon(scaled);
            // run rule to enable / disable this button.
            this.focusChanged(null);
        }
        this.ui = ui;
        this.targetId = descr.getId();
        this.tupperware = tupp;

    }
    protected final UIComponent ui;
    /** id of the plastic application to message */
    protected final URI targetId;
  /** the plastic wrapper *
   */
    protected final TupperwareInternal tupperware;
    
    


}


/* 
$Log: PlasticButton.java,v $
Revision 1.9  2006/08/15 10:01:12  nw
migrated from old to new registry models.

Revision 1.8  2006/07/20 12:31:36  nw
changed to use image fetching library.

Revision 1.7  2006/06/27 19:16:05  nw
adjusted todo tags.

Revision 1.6  2006/06/27 10:20:57  nw
reworked in tupperware

Revision 1.5  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.4  2006/03/16 18:12:56  jdt
Some bug fixes, and the next version of the plastic library.

Revision 1.3  2006/02/27 12:20:50  nw
improved plastic integration

Revision 1.2  2006/02/24 16:27:40  nw
fix for loading rmeote icons - otherwise get a security exception in webstart

Revision 1.1  2006/02/24 15:26:53  nw
build framework for dynamically adding buttons
 
*/