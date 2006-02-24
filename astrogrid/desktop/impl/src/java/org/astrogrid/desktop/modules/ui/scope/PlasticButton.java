/*$Id: PlasticButton.java,v 1.1 2006/02/24 15:26:53 nw Exp $
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

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.PlasticWrapper;
import org.astrogrid.desktop.modules.ui.UIComponent;

import org.apache.commons.collections.ListUtils;
import org.votech.plastic.PlasticHubListener;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.focus.FocusSet;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/** abstract node consumer for passing data onto plastic apps
 * 
 * sets up all the machinery - leaves it to subclasses to implement what happens when the button is pressed
 * */
public abstract class PlasticButton extends NodeConsumerButton{
    /**
     *  Construct a new PlasticButton
     * @param plasticID id of the remote plastic application to message
     * @param name name of this application
     * @param iconURL url for icon of this application (may be null);
     * @param selectedNodes astroscope node selection set
     * @param ui userinterface component
     * @param wrapper wrapper around plasticized UI application
     */
    public PlasticButton( URI plasticID, String name, URL iconURL,FocusSet selectedNodes, UIComponent ui, PlasticWrapper wrapper) {
        super(name,"Display data in a PLASTIC application",  selectedNodes);
        if (iconURL != null) {
            ImageIcon orig = new ImageIcon(iconURL);
            if (orig != null) {
                ImageIcon scaled = new ImageIcon(orig.getImage().getScaledInstance(-1,50,Image.SCALE_SMOOTH));
                setIcon(scaled);
            }
        }
        this.ui = ui;
        this.plasticId = plasticID;
        target  = new ArrayList();
        target.add(plasticId);
        this.wrapper = wrapper;
    }
    protected final UIComponent ui;
    protected final PlasticWrapper wrapper;
    /** singleton list containing only {@link #plasticId} */
    protected final List target;
    /** id of the plastic application to message */
    protected final URI plasticId;
    
    /** loose implementation of equals - will accept  another PlasticButton, or a URI.
     * matches on value of {@link #plasticId} */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof PlasticButton) {      
            return ((PlasticButton)obj).plasticId.equals(this.plasticId);
        } 
        if (obj instanceof URI) {
            return ((URI)obj).equals(this.plasticId);
        }
        return false;
    }
    
    


}


/* 
$Log: PlasticButton.java,v $
Revision 1.1  2006/02/24 15:26:53  nw
build framework for dynamically adding buttons
 
*/