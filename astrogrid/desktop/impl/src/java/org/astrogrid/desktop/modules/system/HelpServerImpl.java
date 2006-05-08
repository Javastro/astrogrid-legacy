/*$Id: HelpServerImpl.java,v 1.8 2006/05/08 15:58:04 nw Exp $
 * Created on 17-Jun-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.awt.Component;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.help.InvalidHelpSetContextException;
import javax.help.Map;
import javax.swing.AbstractButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.contributions.HelpsetContribution;

/** Implementation of the help server.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2005
 *
 */
public class HelpServerImpl implements  HelpServerInternal{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(HelpServerImpl.class);

    /** Construct a new HelpServerImpl
     * 
     */
        
    public HelpServerImpl(List helpsets) {
    	super();
    	for (Iterator i = helpsets.iterator();i.hasNext(); ) {
    		try {
    			HelpsetContribution c = (HelpsetContribution)i.next();
    			HelpSet h= getHelpset(c);
    			if (hs == null) {
    				// use first valid helpset in the list as the 'root'
    				hs = h;
    				broker = hs.createHelpBroker();
    			} else {
    				// add rest as subsids
    				hs.add(h);
    			}
    		} catch (Exception e) {
    			logger.warn("Failed to load helpset",e);
    		}        	
    	}

    }
    
    private HelpSet getHelpset(HelpsetContribution c) throws HelpSetException {
		return  new HelpSet(
				c.getResourceAnchor().getClassLoader()
				,HelpSet.findHelpSet(c.getResourceAnchor().getClassLoader(),c.getPath())
				);
	}
 
    protected HelpBroker broker;
    protected HelpSet hs;


    
    public void showHelp() {
        Map.ID home = hs.getHomeID();
        try {
            broker.setCurrentID(home);
        } catch (InvalidHelpSetContextException e) {
            logger.warn("InvalidHelpSetContextException",e);
        }
        broker.setDisplayed(true);
    }
    
    public void showHelpForTarget(String target) {
        broker.setCurrentID(target);
        broker.setDisplayed(true);
    }

    public void enableHelpKey(Component comp, String defaultHelpId) {
        broker.enableHelpKey(comp,defaultHelpId,null);
    }
 
    public void enableHelp(Component comp, String helpId) {
        broker.enableHelp(comp,helpId,null);
    }

    public void enableHelp(MenuItem comp, String helpId) {
        broker.enableHelp(comp,helpId,null);        
    }
  
    public void enableHelpOnButton(AbstractButton b, String helpId) {
        broker.enableHelp(b,helpId,null);
    }
   
    public void enableHelpOnButton(MenuItem b, String helpId) {
        broker.enableHelp(b,helpId,null);        
    }

    public ActionListener createContextSensitiveHelpListener() {
        return new CSH.DisplayHelpAfterTracking(broker); // wonder whether we can reuse a single instance of this??
    }
    
    
    
}


/* 
$Log: HelpServerImpl.java,v $
Revision 1.8  2006/05/08 15:58:04  nw
removed dependency on javax.help from HelpsetContribution - javax.help now only required by the HelpServerImpl

Revision 1.7  2006/04/26 15:56:18  nw
made servers more configurable.added standalone browser launcher

Revision 1.6  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.5.10.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.5.10.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.5.10.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.5  2006/01/30 17:16:42  jdt
Make robust to help system failures - bug 1519.

Revision 1.4  2005/11/02 09:29:15  nw
fixed bug on windows

Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.2.10.2  2005/10/12 09:21:38  nw
added java help system

Revision 1.2.10.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/06/23 09:08:26  nw
changes for 1.0.3 release

Revision 1.1  2005/06/20 16:56:40  nw
fixes for 1.0.2-beta-2
 
*/