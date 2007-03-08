/*$Id: ImageLoadPlasticButton.java,v 1.9 2007/03/08 17:43:56 nw Exp $
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

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.votech.plastic.CommonMessageConstants;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;
/** button that loads an image over plastic */
public class ImageLoadPlasticButton extends PlasticScopeButton {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ImageLoadPlasticButton.class);

    public ImageLoadPlasticButton(PlasticApplicationDescription descr,  FocusSet selectedNodes, UIComponent ui,TupperwareInternal tupp) {
        super(descr,"View images in "  +  StringUtils.capitalize( descr.getName() )+ "", descr.getDescription(), selectedNodes, ui,tupp);
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
        (new BackgroundWorker(ui,super.getText()) {        
            protected Object construct() throws Exception {        
                for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
                    final TreeNode tn = (TreeNode)i.next();
                    // find each leaf node
                    if (tn.getChildCount() > 0 ) {
                        continue;
                    }
                        URL url = new URL(tn.getAttribute(SiapRetrieval.IMAGE_URL_ATTRIBUTE));
                        List args = new ArrayList();
                        args.add(url.toString()); // plastic expects a string, but we first construct a url to ensure it's valid.
                        tupperware.singleTargetPlasticMessage(
                                CommonMessageConstants.FITS_LOAD_FROM_URL
                                ,args,targetId);
                   }// end for each child node.
                return null;
            }
        }).start();
    }        
    

}


/* 
$Log: ImageLoadPlasticButton.java,v $
Revision 1.9  2007/03/08 17:43:56  nw
first draft of voexplorer

Revision 1.8  2006/09/14 13:52:59  nw
implemented plastic spectrum messaging.

Revision 1.7  2006/08/15 10:01:12  nw
migrated from old to new registry models.

Revision 1.6  2006/06/27 10:19:48  nw
reworked in tupperware

Revision 1.5  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.4  2006/03/31 15:20:56  nw
removed work-around, due to new version of plastic library

Revision 1.3  2006/03/16 18:12:56  jdt
Some bug fixes, and the next version of the plastic library.

Revision 1.2  2006/02/27 12:20:50  nw
improved plastic integration

Revision 1.1  2006/02/24 15:26:53  nw
build framework for dynamically adding buttons
 
*/