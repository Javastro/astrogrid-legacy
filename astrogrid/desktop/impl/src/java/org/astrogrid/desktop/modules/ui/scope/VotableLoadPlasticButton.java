/*$Id: VotableLoadPlasticButton.java,v 1.7 2007/01/29 10:43:49 nw Exp $
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

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

/** button that loads a votable over plastic.
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 22-Feb-2006
 *
 */
public class VotableLoadPlasticButton extends PlasticButton {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(VotableLoadPlasticButton.class);

  
    public VotableLoadPlasticButton(PlasticApplicationDescription descr,  FocusSet selectedNodes, UIComponent ui, TupperwareInternal tupp) {
        super(descr, "View tables in " + StringUtils.capitalize( descr.getName() ), descr.getDescription(), selectedNodes, ui, tupp);        
    }
    
    public void focusChanged(FocusEvent arg0) {
        setEnabled(selectedNodes.size() > 0); // reckon should be able to handle anything.
    }

    public void actionPerformed(ActionEvent e) {
        (new BackgroundWorker(ui,super.getText()) {
            private Set processedServices = new HashSet();
            protected Object construct() throws Exception {
                for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
                    final TreeNode tn = (TreeNode)i.next();
                    // find each leaf node
                    if (tn.getChildCount() > 0 ) {
                        continue;
                    }
                    TreeNode service = tn.getParent().getParent().getParent();
                    if (! processedServices.contains(service)) {
                        processedServices.add(service);                       
                        URL url = new URL(service.getAttribute(Retriever.SERVICE_URL_ATTRIBUTE));
                        List args = new ArrayList();  
                        args.add(url.toString()); // plastic spec expects parameter types that are strings - but still parse into a url first, to check it's valid.
                        args.add(url.toString()); // identifier.
                        tupperware.singleTargetPlasticMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL,args,targetId);
                    }// end if new catalog
                    //next send plastic messages to highlight selected rows.
                    // hard, as we're not maintining row ids at the moment - might be easier with prefuse beta.
                }// end for each child node
                return null;
            }
        }).start();
    }

}


/* 
$Log: VotableLoadPlasticButton.java,v $
Revision 1.7  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.6  2006/09/14 13:52:59  nw
implemented plastic spectrum messaging.

Revision 1.5  2006/08/15 09:59:58  nw
migrated from old to new registry models.

Revision 1.4  2006/06/27 10:19:33  nw
reworked in tupperware

Revision 1.3  2006/03/16 18:12:56  jdt
Some bug fixes, and the next version of the plastic library.

Revision 1.2  2006/02/27 12:20:50  nw
improved plastic integration

Revision 1.1  2006/02/24 15:26:53  nw
build framework for dynamically adding buttons
 
*/