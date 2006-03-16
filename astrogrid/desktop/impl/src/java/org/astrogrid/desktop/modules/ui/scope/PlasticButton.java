/*$Id: PlasticButton.java,v 1.4 2006/03/16 18:12:56 jdt Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.PlasticWrapper;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.io.Piper;

import org.apache.commons.collections.ListUtils;
import org.votech.plastic.PlasticHubListener;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.focus.FocusSet;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public PlasticButton( URI plasticID, String name, String description, URL iconURL,FocusSet selectedNodes, UIComponent ui, PlasticWrapper wrapper) {
        super(name,description!=null? description: "Display data using PLASTIC",  selectedNodes);
        if (iconURL != null) {
            try { //need to do this the long way, rather than just passing the url to ImageIcon, because that seems to 
                // throw security exceptions when runnning under webstart.
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                InputStream is = iconURL.openStream();
                Piper.pipe(is,bos);
                is.close();
                ImageIcon orig = new ImageIcon(bos.toByteArray());
                if (orig != null) {
                    ImageIcon scaled = new ImageIcon(orig.getImage().getScaledInstance(-1,50,Image.SCALE_SMOOTH));
                    setIcon(scaled);
                }
            } catch (IOException e ) {
                logger.warn("Failed to download icon " + iconURL);
                
            }
            // run rule to enable / disable this button.
            this.focusChanged(null);
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
  
    
    


}


/* 
$Log: PlasticButton.java,v $
Revision 1.4  2006/03/16 18:12:56  jdt
Some bug fixes, and the next version of the plastic library.

Revision 1.3  2006/02/27 12:20:50  nw
improved plastic integration

Revision 1.2  2006/02/24 16:27:40  nw
fix for loading rmeote icons - otherwise get a security exception in webstart

Revision 1.1  2006/02/24 15:26:53  nw
build framework for dynamically adding buttons
 
*/