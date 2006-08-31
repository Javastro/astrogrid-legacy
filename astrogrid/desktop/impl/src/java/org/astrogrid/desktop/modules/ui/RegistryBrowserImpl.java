/*$Id: RegistryBrowserImpl.java,v 1.9 2006/08/31 21:31:37 nw Exp $
 * Created on 30-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import java.awt.BorderLayout;
import java.net.URI;

import javax.swing.JPanel;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel.ResourceTableModel;
import org.astrogrid.desktop.modules.ivoa.CacheFactory;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
/** Implementation of the registry browser component.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Mar-2005
 *
 */
public class RegistryBrowserImpl extends UIComponentImpl implements  RegistryBrowser
{
   

    public RegistryBrowserImpl(RegistryInternal reg, HelpServerInternal hs,UIInternal ui,Configuration conf, BrowserControl browser, RegistryBrowser factory, CacheFactory cache) {
        super(conf,hs,ui);
        this.reg=reg;
        this.browser =browser;
        this.factory = factory;
        this.cache = cache;
        initialize();        
    }
    protected final RegistryInternal reg;
    protected final BrowserControl browser;
    protected final RegistryBrowser factory;
    protected final CacheFactory cache;
	private void initialize() {
		this.setSize(425, 600); // same proportions as A4, etc., and 600 high.   
        getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.registryBrowser");        
        JPanel pane = getMainPanel();
        pane.add(getRegistryChooser(),BorderLayout.CENTER); 
		this.setContentPane(pane);
		this.setTitle("Registry Browser");
        setIconImage(IconHelper.loadIcon("java_lib_obj.gif").getImage());        
	}
    
    private RegistryGooglePanel regChooser;
    private RegistryGooglePanel getRegistryChooser() {
        if (regChooser == null) {
            regChooser = new RegistryGooglePanel(this,reg,browser,factory,cache);
        }
        return regChooser;
    }
	public void search(String arg0) {
		RegistryGooglePanel panel = getRegistryChooser();
		panel.doSearch(arg0);
	}
	
	public void open(final URI ivorn) {
		RegistryGooglePanel panel = getRegistryChooser();
		panel.doOpen(ivorn);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"


/* 
$Log: RegistryBrowserImpl.java,v $
Revision 1.9  2006/08/31 21:31:37  nw
minor tweaks and doc fixes.

Revision 1.8  2006/08/15 10:04:11  nw
migrated from old to new registry models.added methods to do a specific search / display a particular record

Revision 1.7  2006/06/27 10:36:41  nw
tweaks

Revision 1.6  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.5.34.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.5  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.4  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.3.10.1  2005/10/12 09:21:38  nw
added java help system

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.8  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.7  2005/07/10 18:07:38  nw
files for 1.0.5

Revision 1.6  2005/06/20 16:56:40  nw
fixes for 1.0.2-beta-2

Revision 1.5  2005/05/12 15:59:08  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 14:25:22  nw
javadoc, improved result transformers for xml

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:50  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.6  2005/04/13 12:23:28  nw
refactored a common base class for ui components

Revision 1.1.2.5  2005/04/06 16:18:50  nw
finished icon set

Revision 1.1.2.4  2005/04/05 11:42:40  nw
tidied imports

Revision 1.1.2.3  2005/04/04 16:43:48  nw
made frames remember their previous positions.
synchronized guiLogin, so only one login box ever comes up.
made refresh action in jobmonitor more robust

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/04/01 19:03:10  nw
beta of job monitor
 
*/