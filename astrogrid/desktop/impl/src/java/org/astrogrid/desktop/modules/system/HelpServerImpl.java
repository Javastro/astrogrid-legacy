/*$Id: HelpServerImpl.java,v 1.20 2009/03/26 18:04:11 nw Exp $
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;
import org.astrogrid.desktop.modules.system.contributions.HelpItemContribution;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.joda.time.Duration;

/** Implementation of the help server.
 * 
 * Previusly was implemented over javahelp. However, this is quite heavyweight, and
 * does a poor job of displaying help documentation. (html 3.2, no decent media).
 * <p>
 * So rewritten to control external browser to point to astrogrid plone site.
 * Interface remains the same - and implmeentation of the methods now taken from the 
 * source of javahelp's DefaultHelpBroker and CSH, but simplified 
 * <ul>
 * <li>removed concept of presentation
 * <li> no multiple help sets, 
 * <li> no support for awt, only swing
 * <li>display help in external browser.
 * <p>
 * changed once again to externalize the help map - this can then be modified and improved after release.
 * </ul> 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Jun-2005
 *
 */
public class HelpServerImpl implements  HelpServerInternal, KeyListener{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(HelpServerImpl.class);
    private final UIContext context;
    private final URL helpMapURL;
    /** construct a new helpserverImpl by providing a URL to a help mapping file 
     * @throws MalformedURLException */
    public HelpServerImpl(BrowserControl browser, String mapURL, UIContext context) throws MalformedURLException {
        this.browser = browser;
        this.context = context;        
        helpMapURL = new URL(mapURL);
       
    }
    
    private Map helpMapping = new HashMap();
    private final BrowserControl browser;

    
    public void showHelp() {
    	showHelpForTarget(HelpItemContribution.HOME_ID);
    }
    
    public void showHelpForTarget(String target) {
    	if (target == null) {
    		logger.warn("Null help target - ignoring");
    		return;
    	}
    	HelpItemContribution item = (HelpItemContribution) helpMapping.get(target);
    	if (item != null) {
    		try {
				browser.openURL(item.getUrlObject());
			} catch (ACRException x) {
				logger.error("Failed to display browser for help: " + item);
			}
    	} else {
    		logger.warn("Unknown help target: " + target);
    		if (! HelpItemContribution.HOME_ID.equals(target)) { // show the default, if that hasn't already failed
    			showHelpForTarget(HelpItemContribution.HOME_ID);
    		} 
    	}
    }

    public void enableHelpKey(Component comp, String id) {
    	if (id == null) {
    	    throw new IllegalArgumentException("id");
    	}
    	if (comp == null) {
    	    throw new IllegalArgumentException("comp");
    	}
    	CSH.setHelpIDString(comp, id);
    	if (comp instanceof JComponent) {
    	    JComponent root = (JComponent) comp;
    	    ActionListener al = getDisplayHelpFromFocus();
    	  
    	    root.registerKeyboardAction(al,
    				   KeyStroke.getKeyStroke(KeyEvent.VK_HELP, 0),
    				   JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    	    root.registerKeyboardAction(al,
    				    KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),
    				    JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    	} else {
    	    comp.addKeyListener(this);
    	}
    }
 
    public void enableHelp(Component comp, String id) {
    	if (id == null) {
    	    throw new IllegalArgumentException("id");
    	}
    	CSH.setHelpIDString(comp, id);
    }

    // unused at present
    public void enableHelp(MenuItem comp, String id) {
    	if (id == null) {
    	    throw new IllegalArgumentException("id");
    	}
    	CSH.setHelpIDString(comp, id);  
    }
  
    // unused at present
    public void enableHelpOnButton(AbstractButton comp, String id) {
    	if (id == null) {
    	    throw new IllegalArgumentException("id");
    	}
    	CSH.setHelpIDString(comp, id);
    	    comp.addActionListener(getDisplayHelpFromSource());
    }
   // unused at present.
    public void enableHelpOnButton(MenuItem comp, String id) {
    	if (comp == null) {
    	    throw new IllegalArgumentException("Invalid Component");
    	}
    	if (id == null) {
    	    throw new IllegalArgumentException("id");
    	}
    	CSH.setHelpIDString(comp, id);
    	comp.addActionListener(getDisplayHelpFromSource());     
    }

    
    public ActionListener createContextSensitiveHelpListener() {
        return new CSH.DisplayHelpAfterTracking(this); // wonder whether we can reuse a single instance of this??
    }
    
    // lazy-initialzation for listeners
    protected ActionListener displayHelpFromFocus;
    protected ActionListener displayHelpFromSource;
    /**
     * Returns the default DisplayHelpFromFocus listener.
     *
     * @see #enableHelpKey(Component, String)
     */
    
    protected ActionListener getDisplayHelpFromFocus() {
	if (displayHelpFromFocus == null) {
	    displayHelpFromFocus = new CSH.DisplayHelpFromFocus(this);
	}
	return displayHelpFromFocus;
    }

    /**
     * Returns the default DisplayHelpFromSource listener.
     *
     * @see #enableHelp(Component, String)
     */
    protected ActionListener getDisplayHelpFromSource() {
	if (displayHelpFromSource==null) {
	    displayHelpFromSource = new CSH.DisplayHelpFromSource(this);
	}
	return displayHelpFromSource;
    }
    
    // key listerner interface.

	public void keyPressed(KeyEvent e) {
		// ignore
	}

	public void keyReleased(KeyEvent e) {
		// simulate what is done in JComponents registerKeyboardActions.
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_F1 || code == KeyEvent.VK_HELP) {
		    ActionListener al= getDisplayHelpFromFocus();
		    al.actionPerformed(new ActionEvent(e.getComponent(),
						       ActionEvent.ACTION_PERFORMED,
						       null));
		}		
	}

	public void keyTyped(KeyEvent e) {
		// ignore
	}

	public JButton createHelpButton(String id) {
		JButton b = new JButton(IconHelper.loadIcon("help16.png"));
		b.setToolTipText("Show help");
		this.enableHelpOnButton(b, id);
		return b;
	}
	
	
	// the delayedContinuation interface - used to fetch the helpmap, avoiding a race condition.
    public DelayedContinuation execute() {
                final XMLInputFactory fac = XMLInputFactory.newInstance();
                XMLStreamReader in = null;
                try {
                    in = fac.createXMLStreamReader(helpMapURL.openStream());
                    while(in.hasNext()) {
                        in.next();
                        if (in.isStartElement()) {
                            final String localName = in.getLocalName();
                            if ("item".equals(localName)) {
                                HelpItemContribution nu = new HelpItemContribution();
                                nu.setId(in.getAttributeValue(null,"id"));
                                try {
                                nu.setUrl(in.getAttributeValue(null,"url"));
                                helpMapping.put(nu.getId(),nu);
                                } catch (MalformedURLException e) { // discard this one, but continue.
                                    logger.info("Malformed url for HelpID '" + nu.getId() + "' discarding");
                                }
                            }
                        }
                    } // end while.
                } catch (XMLStreamException x) {
                    logger.error("XMLStreamException",x);
                } catch (IOException x) {
                    logger.error("IOException",x);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (XMLStreamException e) {
                            // ignored
                        }
                    }
                }                
        return null; // no more repititions for this task.
    }
    public Duration getDelay() {
        return Duration.ZERO; // as soon as possible
    }
    public Principal getPrincipal() {
        return null;
    }
    public String getTitle() {
        return "Fetching Helpmap";
    }
    
    
    
}


/* 
$Log: HelpServerImpl.java,v $
Revision 1.20  2009/03/26 18:04:11  nw
source code improvements - cleaned imports, @override, etc.

Revision 1.19  2008/06/06 13:44:52  nw
fix to threading implementation.

Revision 1.18  2008/05/09 11:32:34  nw
Incomplete - task 392: joda time

Revision 1.17  2008/04/23 10:57:34  nw
changes cominfg from unit testing.

Revision 1.16  2008/03/30 14:44:28  nw
Complete - task 363: race condition at startup.

Revision 1.15  2008/03/28 13:09:01  nw
help-tagging

Revision 1.14  2007/06/18 17:00:13  nw
javadoc fixes.

Revision 1.13  2007/03/08 17:44:02  nw
first draft of voexplorer

Revision 1.12  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.11  2007/01/23 11:49:27  nw
ui improvmenet.

Revision 1.10  2007/01/12 13:20:05  nw
made sure every ui app has a help menu.

Revision 1.9  2007/01/11 18:15:49  nw
fixed help system to point to ag site.

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