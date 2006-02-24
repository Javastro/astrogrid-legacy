/*$Id: ImageLoadPlasticButton.java,v 1.1 2006/02/24 15:26:53 nw Exp $
 * Created on 23-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.desktop.modules.ui.PlasticWrapper;
import org.astrogrid.desktop.modules.ui.UIComponent;

import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.PlasticHubListener;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;

import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
/** button that loads an image over plastic */
public class ImageLoadPlasticButton extends PlasticButton {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ImageLoadPlasticButton.class);

    public ImageLoadPlasticButton(URI plasticID, String name, URL iconURL, FocusSet selectedNodes, UIComponent ui, PlasticWrapper wrapper) {
        super(plasticID,"View images in "  +  StringUtils.capitalize( name )+ "", iconURL, selectedNodes, ui, wrapper);
    }

    public void focusChanged(FocusEvent arg0) {
        for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
            TreeNode t= (TreeNode)i.next();
            if (t.getAttribute(SiapRetrieval.IMAGE_URL_ATTRIBUTE) != null) {
                setEnabled(true);
                return;
            }
        }
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        final Set processedServices = new HashSet();
        for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
            final TreeNode tn = (TreeNode)i.next();
            // find each leaf node
            if (tn.getChildCount() > 0 ) {
                continue;
            }
            TreeNode catalog = tn.getParent().getParent().getParent();
            if (! processedServices.contains(catalog)) {
                processedServices.add(catalog);
                try {
                    URL url = new URL(catalog.getAttribute(Retriever.SERVICE_URL_ATTRIBUTE));
                    List args = new ArrayList();
                    args.add(url);
                    wrapper.getHub().requestToSubset(wrapper.getPlasticId(),CommonMessageConstants.VOTABLE_LOAD_FROM_URL,args,target);
                } catch (MalformedURLException ex) {
                    logger.warn("Failed to plasticize",ex);
                    //@todo report errors better
                }
                //@todo next send plastic messages to select correct position in image.
            }
                
        }        
    }

}


/* 
$Log: ImageLoadPlasticButton.java,v $
Revision 1.1  2006/02/24 15:26:53  nw
build framework for dynamically adding buttons
 
*/